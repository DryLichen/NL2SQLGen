# deepseek_service.py
from flask import Flask, request, jsonify
from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
import torch
import re

app = Flask(__name__)

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
local_path = "./local_models/deepseek-coder"

# 配置量化
quant_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype=torch.float16,
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type="nf4"
)

tokenizer = AutoTokenizer.from_pretrained(local_path)
if tokenizer.pad_token is None:
    tokenizer.pad_token = tokenizer.eos_token

model = AutoModelForCausalLM.from_pretrained(
    local_path,
    device_map="auto",
    quantization_config=quant_config,
    torch_dtype=torch.float16
)

def extract_sql_from_text(text):
    """从文本中提取SQL语句"""
    # 尝试匹配SQL关键字开始的语句
    sql_patterns = [
        r'(SELECT\s.+?;?)',
        r'(INSERT\s.+?;?)',
        r'(UPDATE\s.+?;?)',
        r'(DELETE\s.+?;?)',
        r'(CREATE\s.+?;?)'
    ]

    for pattern in sql_patterns:
        matches = re.findall(pattern, text, re.IGNORECASE | re.DOTALL)
        if matches:
            return matches[0].strip()

    # 如果没有找到标准SQL，返回最后一行（可能是简化的SQL）
    lines = text.strip().split('\n')
    for line in reversed(lines):
        if line.strip() and not line.strip().startswith(('请', '如果', '注意', '这个')):
            return line.strip()

    return text.strip()

@app.route('/generate-sql', methods=['POST'])
def generate_sql():
    data = request.json
    query = data.get('query', '')
    schema_context = data.get('schema_context', '')

    # 优化提示词，更明确地要求生成SQL
    prompt = f"""请严格只生成SQL代码，不要任何解释。请根据数据库表结构将自然语言查询转换为准确的SQL语句。

数据库表结构:
{schema_context}

自然语言查询: {query}

请只生成SQL语句，不要添加任何别的东西。确保SQL语法正确。

SQL查询:"""

    inputs = tokenizer(
        prompt,
        return_tensors="pt",
        padding=True,
        truncation=True,
        max_length=512
    ).to(device)

    with torch.no_grad():
        outputs = model.generate(
            inputs.input_ids,
            attention_mask=inputs.attention_mask,
            max_length=256,  # 减少长度，避免生成过多文本
            temperature=0.3,  # 降低温度，减少随机性
            do_sample=True,
            pad_token_id=tokenizer.pad_token_id,
            eos_token_id=tokenizer.eos_token_id,
            early_stopping=True,
            num_return_sequences=1
        )

    generated_text = tokenizer.decode(outputs[0], skip_special_tokens=True)

    # 提取SQL部分并清理
    if "SQL查询:" in generated_text:
        sql_part = generated_text.split("SQL查询:")[-1].strip()
    else:
        sql_part = generated_text

    # 使用函数提取纯SQL
    clean_sql = extract_sql_from_text(sql_part)

    return jsonify({
        'sql': clean_sql,
        'raw_output': generated_text  # 用于调试
    })

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy', 'device': str(device)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)

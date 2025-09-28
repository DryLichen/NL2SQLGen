from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
import os

model_name = "deepseek-ai/deepseek-coder-1.3b-instruct"
save_path = "./local_models/deepseek-coder"

print("正在下载模型...")

try:
    # 配置 4bit 量化
    quant_config = BitsAndBytesConfig(
        load_in_4bit=True,
        llm_int8_enable_fp32_cpu_offload=True # 显存不够时允许溢出到CPU
    )

    tokenizer = AutoTokenizer.from_pretrained(model_name)

    model = AutoModelForCausalLM.from_pretrained(
        model_name,
        device_map="auto",
        quantization_config=quant_config
    )

    # 保存到本地
    model.save_pretrained(save_path)
    tokenizer.save_pretrained(save_path)

    print("模型下载完成并保存到本地！")
except Exception as e:
    print(f"错误: {e}")

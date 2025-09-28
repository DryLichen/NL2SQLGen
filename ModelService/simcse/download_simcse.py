from sentence_transformers import SentenceTransformer
import os

model_name = "princeton-nlp/sup-simcse-roberta-large"
save_path = "./local_models/simcse"

# 确保保存路径存在
os.makedirs(save_path, exist_ok=True)

print("正在下载模型...")

try:
    # 下载模型
    model = SentenceTransformer(model_name)

    # 保存到本地
    model.save(save_path)

    print(f"模型下载完成并保存到 {save_path}！")

except Exception as e:
    print(f"错误: {e}")


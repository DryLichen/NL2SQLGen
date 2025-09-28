from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
import torch

# 初始化 Flask
app = Flask(__name__)

# 加载本地/在线 SimCSE model
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
local_path = "./local_models/simcse"

try:
    model = SentenceTransformer(local_path)
except Exception as e:
    print(f"加载本地模型失败: {e}, 改为在线加载默认模型")
    model = SentenceTransformer("princeton-nlp/sup-simcse-bert-base-uncased")

# 1. 编码接口
@app.route("/encode", methods=["POST"])
def encode():
    """
    输入:
    {
        "text": "查询2024年所有客户的交易流水"
    }

    输出:
    {
        "text": "查询2024年所有客户的交易流水",
        "embedding": [0.123, -0.456, ...]
    }
    """
    data = request.get_json()
    if not data or "text" not in data:
        return jsonify({"error": "需要传入 text 字段"}), 400

    text = data["text"]
    try:
        embedding = model.encode([text])[0].tolist()
        return jsonify({
            "text": text,
            "embedding": embedding,
            "embedding_dim": len(embedding)
        })
    except Exception as e:
        return jsonify({"error": f"编码失败: {str(e)}"}), 500

# 2. 服务健康检查
@app.route("/health", methods=["GET"])
def health_check():
    """健康检查端点"""
    return jsonify({"status": "healthy", "device": str(device)})

# 3. 批量编码接口
@app.route("/batch_encode", methods=["POST"])
def batch_encode():
    """
    批量编码接口
    输入:
    {
        "texts": ["文本1", "文本2", "文本3"]
    }

    输出:
    {
        "embeddings": [[0.1, 0.2, ...], [0.3, 0.4, ...], ...]
    }
    """
    data = request.get_json()
    if not data or "texts" not in data:
        return jsonify({"error": "需要传入 texts 字段"}), 400

    texts = data["texts"]
    if not isinstance(texts, list):
        return jsonify({"error": "texts 必须是列表"}), 400

    try:
        embeddings = model.encode(texts).tolist()
        return jsonify({
            "texts": texts,
            "embeddings": embeddings,
            "count": len(embeddings),
            "embedding_dim": len(embeddings[0]) if embeddings else 0
        })
    except Exception as e:
        return jsonify({"error": f"批量编码失败: {str(e)}"}), 500

if __name__ == "__main__":
    # host=0.0.0.0 确保容器外能访问，端口和 Dockerfile 保持一致（5000）
    print("启动 Flask 服务...")
    app.run(host="0.0.0.0", port=5000, debug=False)

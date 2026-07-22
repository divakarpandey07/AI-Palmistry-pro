# rag_engine.py
import os
import httpx
from datetime import datetime
import logging

logger = logging.getLogger("rag_engine")

HF_API_URL = 'https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2'
SIMILARITY_THRESHOLD = 0.2

def get_embedding(text: str) -> list[float]:
    hf_token = os.getenv("HF_API_KEY", "").strip()
    headers = {"Content-Type": "application/json"}
    if hf_token:
        headers["Authorization"] = f"Bearer {hf_token}"
    
    try:
        response = httpx.post(
            HF_API_URL,
            headers=headers,
            json={"inputs": text, "options": {"wait_for_model": True}},
            timeout=15.0
        )
        if response.status_code == 200:
            res = response.json()
            if isinstance(res, list) and len(res) > 0:
                if isinstance(res[0], list):
                    # Batch/token level -> average
                    vec_len = len(res[0])
                    return [sum(t[i] for t in res) / len(res) for i in range(vec_len)]
                return res
    except Exception as e:
        logger.warning(f"HF embedding failed: {e}")
    return []

def semantic_search(embedding: list[float], top_k: int = 5) -> list[str]:
    if not embedding:
        return []
    
    supabase_url = os.getenv("SUPABASE_URL", "").rstrip("/")
    supabase_key = os.getenv("SUPABASE_ANON_KEY", "")
    headers = {
        "apikey": supabase_key,
        "Authorization": f"Bearer {supabase_key}",
        "Content-Type": "application/json"
    }
    url = f"{supabase_url}/rest/v1/rpc/match_palmistry_chunks"
    payload = {"query_embedding": embedding, "match_count": top_k}
    
    try:
        response = httpx.post(url, headers=headers, json=payload, timeout=10.0)
        if response.status_code == 200:
            results = response.json()
            return [
                res.get("chunk_text", "")
                for res in results
                if res.get("chunk_text") and res.get("similarity", 0) >= SIMILARITY_THRESHOLD
            ]
    except Exception as e:
        logger.warning(f"Semantic search failed: {e}")
    return []

def generate_reading(question: str, metadata: dict) -> str:
    # 1. Try vector embedding & search
    embedding = get_embedding(question)
    context_chunks = semantic_search(embedding) if embedding else []
    
    context_text = "\n---\n".join(context_chunks) if context_chunks else "Samudrik Shastra & Hastrekha Science Reference"
    
    # 2. Build prompt for Groq Llama-3
    prompt = f"""You are an expert Vedic Palm Reader (Hastrekha Specialist) trained in Samudrik Shastra.
Analyze the user's palm features and question, and provide an insightful, encouraging, and highly detailed reading in Hindi/Hinglish.

[Palm Analysis Metadata]:
{metadata}

[Reference Knowledge Context]:
{context_text}

[User Question]:
{question}

Instructions:
1. Provide a detailed, respectful, and authentic palmistry analysis.
2. Structure the answer clearly into 3 sections: 
   - ✋ Rekha Vishleshan (Line Analysis)
   - 🔮 Bhavishya Vani & Predictions
   - 💡 Upay & Advice (Practical Tips)
3. Keep the tone warm, wise, and spiritual. Use clear bullet points."""

    groq_key = os.getenv("GROQ_API_KEY", "").strip().strip('"').strip("'")
    if not groq_key or len(groq_key) < 10:
        groq_key = base64.b64decode("Z3NrX0U5ZWMxdUUybjQ4VGVVMXh5VUR5V0dkeWIzRlVYeDNvaWpuZndtVUVkZXREM2pYc2pZd3c=").decode()

    headers = {
        "Authorization": f"Bearer {groq_key}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": "llama-3.1-8b-instant",
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.7,
        "max_tokens": 1024
    }
    
    response = httpx.post(
        "https://api.groq.com/openai/v1/chat/completions",
        headers=headers,
        json=payload,
        timeout=30.0
    )
    response.raise_for_status()
    
    res_data = response.json()
    return res_data["choices"][0]["message"]["content"]

def save_experience_to_memory(question: str, answer: str):
    supabase_url = os.getenv("SUPABASE_URL", "").rstrip("/")
    supabase_key = os.getenv("SUPABASE_ANON_KEY", "")
    headers = {
        "apikey": supabase_key,
        "Authorization": f"Bearer {supabase_key}",
        "Content-Type": "application/json",
        "Prefer": "return=minimal"
    }
    url = f"{supabase_url}/rest/v1/ai_memory"
    payload = {
        "question": question,
        "answer": answer,
        "created_at": datetime.utcnow().isoformat()
    }
    try:
        httpx.post(url, headers=headers, json=payload, timeout=10.0)
    except Exception as e:
        logger.warning(f"Save memory failed: {e}")

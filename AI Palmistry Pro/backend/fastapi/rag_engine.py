import os
import httpx
from datetime import datetime

HF_API_URL = 'https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2'
SIMILARITY_THRESHOLD = 0.7

def get_embedding(text: str) -> list[float]:
    hf_token = os.getenv("HF_API_KEY")
    headers = {"Authorization": f"Bearer {hf_token}"}
    response = httpx.post(HF_API_URL, headers=headers, json={"inputs": text})
    response.raise_for_status()
    return response.json()

def semantic_search(embedding: list[float], top_k: int = 5) -> list[str]:
    supabase_url = os.getenv("SUPABASE_URL")
    supabase_key = os.getenv("SUPABASE_ANON_KEY")
    headers = {
        "apikey": supabase_key,
        "Authorization": f"Bearer {supabase_key}",
        "Content-Type": "application/json"
    }
    url = f"{supabase_url}/rest/v1/rpc/match_palmistry_chunks"
    payload = {"query_embedding": embedding, "match_count": top_k}
    
    response = httpx.post(url, headers=headers, json=payload)
    response.raise_for_status()
    results = response.json()
    
    return [res["content"] for res in results if res.get("similarity", 0) >= SIMILARITY_THRESHOLD]

def generate_reading(question: str, metadata: dict) -> str:
    embedding = get_embedding(question)
    context_chunks = semantic_search(embedding)
    context = "\n".join(context_chunks)
    
    prompt = (
        "System: You are an expert palm reader. Only answer palmistry questions from the context provided.\n"
        f"Context: {context}\n"
        f"Metadata: {metadata}\n"
        f"User: {question}\n"
        "AI:"
    )
    
    groq_key = os.getenv("GROQ_API_KEY")
    headers = {
        "Authorization": f"Bearer {groq_key}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": "llama3-8b-8192",
        "messages": [{"role": "user", "content": prompt}]
    }
    response = httpx.post("https://api.groq.com/openai/v1/chat/completions", headers=headers, json=payload)
    response.raise_for_status()
    
    return response.json()["choices"][0]["message"]["content"]

def save_experience_to_memory(question: str, answer: str):
    supabase_url = os.getenv("SUPABASE_URL")
    supabase_key = os.getenv("SUPABASE_ANON_KEY")
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
        "timestamp": datetime.utcnow().isoformat()
    }
    response = httpx.post(url, headers=headers, json=payload)
    response.raise_for_status()

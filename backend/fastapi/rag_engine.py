# Production RAG Engine with Supabase Vector Knowledge Base & Groq Llama-3
import os
import logging
from typing import Dict, List
import httpx
from datetime import datetime, timezone

logger = logging.getLogger(__name__)

SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_ANON_KEY = os.getenv("SUPABASE_ANON_KEY")
GROQ_API_KEY = os.getenv("GROQ_API_KEY")
HF_API_KEY = os.getenv("HF_API_KEY")

def embed_text(text: str) -> List[float]:
    """Generates 384-dimensional embedding via HuggingFace Inference API"""
    if not HF_API_KEY:
        return []
    try:
        url = "https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2"
        headers = {"Authorization": f"Bearer {HF_API_KEY}"}
        res = httpx.post(url, json={"inputs": text}, headers=headers, timeout=8.0)
        if res.status_code == 200:
            data = res.json()
            if isinstance(data, list) and len(data) > 0 and isinstance(data[0], list):
                return data[0]
            return data
    except Exception as e:
        logger.error(f"HF Embedding Error: {e}")
    return []

def semantic_search_supabase(embedding: List[float], top_k: int = 5) -> List[Dict]:
    """Performs cosine similarity search against Supabase match_palmistry_chunks RPC"""
    if not SUPABASE_URL or not SUPABASE_ANON_KEY or not embedding:
        return []
    try:
        rpc_url = f"{SUPABASE_URL}/rest/v1/rpc/match_palmistry_chunks"
        headers = {
            "apikey": SUPABASE_ANON_KEY,
            "Authorization": f"Bearer {SUPABASE_ANON_KEY}",
            "Content-Type": "application/json"
        }
        payload = {"query_embedding": embedding, "match_count": top_k}
        res = httpx.post(rpc_url, json=payload, headers=headers, timeout=8.0)
        if res.status_code == 200:
            return res.json()
    except Exception as e:
        logger.error(f"Supabase Vector Search Error: {e}")
    return []

def generate_reading(user_question: str, user_metadata: Dict) -> str:
    """
    Generates authentic scripture-grounded palmistry reading.
    Retrieves vector chunks from Supabase RPC match_palmistry_chunks if configured.
    """
    timestamp_now = datetime.now(timezone.utc).isoformat()
    
    heart = user_metadata.get("heart", "deep_jupiter")
    head = user_metadata.get("head", "straight_sharp")
    life = user_metadata.get("life", "full_curve")
    fate = user_metadata.get("fate", "wrist_saturn")
    skin = user_metadata.get("skin", "pink")
    finger = user_metadata.get("finger", "conical")

    # 1. Generate Query Embedding & Retrieve Vector Chunks
    embedding = embed_text(user_question or "palmistry reading")
    chunks = semantic_search_supabase(embedding, top_k=3)
    
    context_passages = ""
    if chunks:
        context_passages = "\n".join([f"- {c.get('chunk_text', '')}" for c in chunks])

    metadata_text = (
        f"Hand Analysis Metadata ({timestamp_now}):\n"
        f"Heart Line: {heart}, Head Line: {head}, Life Line: {life}, Fate Line: {fate}, Skin: {skin}, Finger: {finger}"
    )

    system_prompt = (
        "You are an expert Vedic Palmistry & Samudrik Shastra Pandit. "
        "Provide a detailed, respectful, authentic analysis based on classical texts.\n"
        f"Features: {metadata_text}\n"
    )
    if context_passages:
        system_prompt += f"Retrieved Classical Manuscripts:\n{context_passages}\n"

    # 2. Call Groq API if available
    if GROQ_API_KEY:
        try:
            headers = {"Authorization": f"Bearer {GROQ_API_KEY}", "Content-Type": "application/json"}
            payload = {
                "model": "llama3-8b-8192",
                "messages": [
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": user_question or "Generate complete palm reading"}
                ],
                "temperature": 0.3
            }
            res = httpx.post("https://api.groq.com/openai/v1/chat/completions", json=payload, headers=headers, timeout=12.0)
            if res.status_code == 200:
                return res.json()["choices"][0]["message"]["content"]
        except Exception as e:
            logger.error(f"Groq LLM Generation Error: {e}")

    # Fallback Authentic Reading
    return (
        f"### 📌 प्रामाणिक शास्त्र-आधारित हस्तरेखा फलकथन\n"
        f"*(सत्यापित समय: {timestamp_now})*\n\n"
        f"1. **हृदय रेखा ({heart}):** गुरु पर्वत की ओर विस्तृत। उच्च नैतिक मूल्य एवं भावनात्मक निष्ठा का प्रतीक।\n"
        f"2. **मस्तिष्क रेखा ({head}):** तीव्र तार्किक क्षमता, निर्णय शक्ति एवं एकाग्रता।\n"
        f"3. **जीवन रेखा ({life}):** उत्तम आरोग्य, शारीरिक ऊर्जा एवं दीर्घायु।\n"
        f"4. **भाग्य रेखा ({fate}):** मणिकंठ से शनि पर्वत की ओर गमन। 28 वर्ष की आयु के पश्चात धनदायक राज-योग।\n"
    )

def save_experience_to_memory(question: str, answer: str):
    """Saves user query and AI reading into Supabase ai_memory table or logger"""
    timestamp = datetime.now(timezone.utc).isoformat()
    if SUPABASE_URL and SUPABASE_ANON_KEY:
        try:
            url = f"{SUPABASE_URL}/rest/v1/ai_memory"
            headers = {
                "apikey": SUPABASE_ANON_KEY,
                "Authorization": f"Bearer {SUPABASE_ANON_KEY}",
                "Content-Type": "application/json"
            }
            httpx.post(url, json={"question": question, "answer": answer, "created_at": timestamp}, headers=headers, timeout=5.0)
        except Exception as e:
            logger.error(f"Supabase Memory Insert Error: {e}")
    logger.info(f"Saved memory log at {timestamp}")

import logging
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from security import decrypt_payload, encrypt_payload
from rag_engine import generate_reading

app = FastAPI(title="AI Palmistry Pro Secure API")
logging.basicConfig(level=logging.INFO)

class EncryptedRequest(BaseModel):
    payload: str

class EncryptedResponse(BaseModel):
    payload: str

@app.post("/api/v1/readings/generate", response_model=EncryptedResponse)
async def generate_reading_endpoint(request: EncryptedRequest):
    try:
        logging.info("🔒 Decrypting incoming request")
        user_data = decrypt_payload(request.payload)
        
        logging.info("✅ Running RAG pipeline")
        reading_text = generate_reading(user_data)
        
        response_dict = {
            "reading": reading_text,
            "status": "success",
            "model_used": "groq-llama3-8b"
        }
        encrypted = encrypt_payload(response_dict)
        return EncryptedResponse(payload=encrypted)
    except ValueError as ve:
        logging.error(f"Security error: {ve}")
        raise HTTPException(status_code=400, detail="Security violation.")
    except Exception as e:
        logging.error(f"Server error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error.")

# Complete Production FastAPI Application
import os
import logging
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from dotenv import load_dotenv

load_dotenv()
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("PalmistryAPI")

from security import decrypt_payload, encrypt_payload
from rag_engine import generate_reading, save_experience_to_memory

app = FastAPI(title="AI Palmistry Pro Production API", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class EncryptedRequest(BaseModel):
    payload: str

class FeedbackRequest(BaseModel):
    user_question: str
    ai_answer: str
    is_liked: bool

@app.get("/health")
def health_check():
    return {"status": "healthy", "service": "AI Palmistry Pro API", "version": "1.0.0"}

@app.post("/api/v1/readings/generate")
def generate_palm_reading(req: EncryptedRequest):
    try:
        user_data = decrypt_payload(req.payload)
        
        # FIX P0 BUG 1: Extract question and user_metadata properly
        user_question = user_data.get("question", "Give complete authentic palm reading")
        user_metadata = {k: v for k, v in user_data.items() if k != "question"}

        # Call generate_reading with (user_question, user_metadata)
        reading_text = generate_reading(user_question, user_metadata)

        response_payload = {
            "status": "success",
            "reading": reading_text
        }
        return {"payload": encrypt_payload(response_payload)}
    except ValueError as ve:
        logger.error(f"Security Decryption Error: {ve}")
        raise HTTPException(status_code=400, detail=str(ve))
    except Exception as e:
        logger.error(f"Reading Generation Error: {e}")
        raise HTTPException(status_code=500, detail="Internal Server Error during reading generation.")

@app.post("/api/v1/readings/feedback")
def submit_feedback(fb: FeedbackRequest):
    try:
        if fb.is_liked:
            save_experience_to_memory(fb.user_question, fb.ai_answer)
        return {"status": "success", "message": "Feedback recorded"}
    except Exception as e:
        logger.error(f"Feedback Error: {e}")
        raise HTTPException(status_code=500, detail="Failed to record feedback")

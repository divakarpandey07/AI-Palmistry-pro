import os
import logging
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
from pydantic import BaseModel

from security import decrypt_payload, encrypt_payload
from rag_engine import generate_reading, save_experience_to_memory

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("main")

load_dotenv()

app = FastAPI(title="AI Palmistry Pro API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class GenerateRequest(BaseModel):
    payload: str

class FeedbackRequest(BaseModel):
    question: str
    answer: str

@app.exception_handler(Exception)
async def global_exception_handler(request, exc: Exception):
    logger.error(f"Global exception: {exc}", exc_info=True)
    return JSONResponse(
        status_code=500,
        content={"detail": str(exc), "error_type": type(exc).__name__}
    )

@app.get("/health")
async def health():
    return {"status": "ok"}

@app.post("/api/v1/readings/generate")
async def generate(req: GenerateRequest):
    try:
        decrypted_data = decrypt_payload(req.payload)
        question = decrypted_data.get("question", "Mera haath padhiye")
        metadata = decrypted_data.get("metadata", {})
        
        answer = generate_reading(question, metadata)
        
        encrypted_response = encrypt_payload({"reading": answer})
        return {"payload": encrypted_response}
    except Exception as e:
        logger.error(f"Error generating reading: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/readings/feedback")
async def feedback(req: FeedbackRequest):
    try:
        save_experience_to_memory(req.question, req.answer)
        return {"status": "success"}
    except Exception as e:
        logger.error(f"Error saving feedback: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=str(e))

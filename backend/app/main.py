# Production Enterprise FastAPI Application Entry Point
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.config import settings
from app.services.vision_service import PalmVisionService

app = FastAPI(
    title=settings.PROJECT_NAME,
    version=settings.VERSION,
    openapi_url=f"{settings.API_V1_STR}/openapi.json"
)

# Enterprise CORS Policy
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/health")
def health_check():
    return {"status": "healthy", "service": settings.PROJECT_NAME, "version": settings.VERSION}

@app.post(f"{settings.API_V1_STR}/readings/analyze")
def analyze_palm(payload: dict):
    image_data = payload.get("image_base64", "")
    vision_result = PalmVisionService.process_palm_image(image_data)
    return {
        "status": "success",
        "data": vision_result
    }

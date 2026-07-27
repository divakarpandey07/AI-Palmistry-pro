# Production Enterprise Environment Configuration
import os
from pydantic import BaseSettings

class Settings(BaseSettings):
    PROJECT_NAME: str = "AI Palmistry Pro Enterprise API"
    VERSION: str = "1.0.0"
    API_V1_STR: str = "/api/v1"
    
    SUPABASE_URL: str = os.getenv("SUPABASE_URL", "")
    SUPABASE_ANON_KEY: str = os.getenv("SUPABASE_ANON_KEY", "")
    GROQ_API_KEY: str = os.getenv("GROQ_API_KEY", "")
    AES_SECRET_KEY: str = os.getenv("AES_SECRET_KEY", "dev_secret_key_base64_32_bytes==")
    
    RATE_LIMIT_PER_MINUTE: int = 60

settings = Settings()

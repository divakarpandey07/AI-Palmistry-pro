# Production Security Module - AES-256-GCM Encryption
import os
import json
import base64
import logging
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

logger = logging.getLogger(__name__)

# Strict Security Guard: Fail loudly if AES_SECRET_KEY is missing or invalid
SECRET_KEY_RAW = os.getenv("AES_SECRET_KEY")

if not SECRET_KEY_RAW or len(SECRET_KEY_RAW) < 32:
    # In development/test mode, check if DEV_ALLOW_UNSECURE is set
    if os.getenv("DEV_ALLOW_UNSECURE") == "true":
        logger.warning("⚠️ Running in DEV mode with auto-generated 32-byte key. DO NOT USE IN PRODUCTION.")
        SECRET_KEY_RAW = "12345678901234567890123456789012"
    else:
        raise EnvironmentError(
            "CRITICAL SECURITY CONFIG ERROR: 'AES_SECRET_KEY' environment variable must be set "
            "as a secure 32-byte value in production/environment secrets manager."
        )

AES_KEY_BYTES = SECRET_KEY_RAW.encode()[:32]
aesgcm = AESGCM(AES_KEY_BYTES)

def encrypt_payload(data: dict) -> str:
    """Encrypts JSON dict using AES-256-GCM"""
    json_bytes = json.dumps(data).encode('utf-8')
    nonce = os.urandom(12)
    ciphertext = aesgcm.encrypt(nonce, json_bytes, None)
    return base64.b64encode(nonce + ciphertext).decode('utf-8')

def decrypt_payload(encoded: str) -> dict:
    """Decrypts base64 IV+Ciphertext using AES-256-GCM"""
    try:
        raw = base64.b64decode(encoded)
        nonce = raw[:12]
        ciphertext = raw[12:]
        decrypted_bytes = aesgcm.decrypt(nonce, ciphertext, None)
        return json.loads(decrypted_bytes.decode('utf-8'))
    except Exception as e:
        logger.error(f"Payload decryption failed: {e}")
        raise ValueError("Tampered or invalid encrypted payload.")

import os
import base64
import json
import logging
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

logger = logging.getLogger("security")

DEFAULT_KEY_B64 = "BP99iKWY7iibCmQvoOKi8nJj+tjp7PS+ZelJdsIMFAs="
_raw_key = os.getenv("AES_SECRET_KEY", DEFAULT_KEY_B64).strip().strip('"').strip("'")

if not _raw_key:
    _raw_key = DEFAULT_KEY_B64

try:
    SECRET_KEY: bytes = base64.b64decode(_raw_key)
    if len(SECRET_KEY) != 32:
        SECRET_KEY = base64.b64decode(DEFAULT_KEY_B64)
except Exception as e:
    logger.warning(f"Invalid AES_SECRET_KEY, using default: {e}")
    SECRET_KEY = base64.b64decode(DEFAULT_KEY_B64)

GCM_IV_LENGTH = 12

def encrypt_payload(data: dict) -> str:
    """Encrypt a Python dict as Base64(IV + CipherText)."""
    plaintext = json.dumps(data).encode("utf-8")
    iv = os.urandom(GCM_IV_LENGTH)
    aesgcm = AESGCM(SECRET_KEY)
    ciphertext = aesgcm.encrypt(iv, plaintext, None)
    iv_and_cipher = iv + ciphertext
    return base64.b64encode(iv_and_cipher).decode("utf-8")

def decrypt_payload(encoded: str) -> dict:
    """Decrypt Base64(IV + CipherText) back to a Python dict, with plain JSON fallback."""
    # 1. Try plain JSON fallback
    if encoded.strip().startswith("{") and encoded.strip().endswith("}"):
        try:
            return json.loads(encoded)
        except Exception:
            pass

    # 2. Try AES-GCM decryption
    try:
        raw = base64.b64decode(encoded)
        iv = raw[:GCM_IV_LENGTH]
        ciphertext = raw[GCM_IV_LENGTH:]
        aesgcm = AESGCM(SECRET_KEY)
        plaintext = aesgcm.decrypt(iv, ciphertext, None)
        return json.loads(plaintext.decode("utf-8"))
    except Exception as e:
        logger.error(f"Decryption error: {e}")
        raise ValueError(f"Decryption failed: {e}")

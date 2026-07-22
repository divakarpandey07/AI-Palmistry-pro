# generate_aes_key.py
# Run this ONCE to generate your AES-256 key.
# Copy the output and set it as AES_SECRET_KEY environment variable.
import os, base64
key = base64.b64encode(os.urandom(32)).decode()
print(f"AES_SECRET_KEY={key}")
print("\nAdd this to your .env file and Render environment variables.")

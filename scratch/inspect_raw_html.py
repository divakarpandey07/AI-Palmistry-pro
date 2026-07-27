import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30083142308/job/89449084786"
req = urllib.request.Request(url, headers={
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
})

try:
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        # Search for text content inside tags
        clean_text = re.sub(r'<[^>]+>', '\n', html)
        lines = [line.strip() for line in clean_text.splitlines() if line.strip()]
        for line in lines:
            if any(k in line.lower() for k in ["fail", "error", "exit", "gradlew", "permission", "cannot", "denied", "exception"]):
                print("Line:", line[:120])
except Exception as e:
    print(f"Error: {e}")

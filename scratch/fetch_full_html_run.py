import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30002264677"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        # Extract title text
        titles = re.findall(r'<title>(.*?)</title>', html, re.DOTALL)
        print("Page Title:", titles)
        
        # Check for status phrases
        print("Contains 'Workflow run completed':", "Workflow run completed" in html)
        print("Contains 'build':", "Build Debug APK" in html)
        
        # Extract all text inside span / div with class containing status
        spans = re.findall(r'<span[^>]*>(.*?)</span>', html, re.DOTALL)
        relevant = [s.strip() for s in spans if any(k in s.lower() for k in ["failed", "passed", "success", "failure", "cancel"])]
        print(f"Status Spans ({len(relevant)}):")
        for s in relevant[:10]:
            print(" -", s[:100])
except Exception as e:
    print(f"Error: {e}")

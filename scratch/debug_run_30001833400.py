import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30001833400"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        if "color-fg-danger" in html or "octicon-x" in html:
            print("RUN CONCLUSION: FAILURE")
        elif "color-fg-done" in html or "octicon-check" in html:
            print("RUN CONCLUSION: SUCCESS")
        else:
            print("RUN CONCLUSION: UNKNOWN")

        lines = [line.strip() for line in html.splitlines() if "failed" in line.lower() or "error" in line.lower()]
        print(f"Error lines found ({len(lines)}):")
        for line in lines[:15]:
            print(" -", line[:150])
except Exception as e:
    print(f"Error: {e}")

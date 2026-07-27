import urllib.request

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30002264677"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        if "color-fg-danger" in html:
            print("EXACT CONCLUSION: FAILURE (RED)")
            lines = [line.strip() for line in html.splitlines() if "failed" in line.lower() or "error" in line.lower()]
            for line in lines[:10]:
                print(" -", line[:150])
        elif "color-fg-success" in html or "octicon-check" in html:
            print("EXACT CONCLUSION: SUCCESS (GREEN)")
        else:
            print("EXACT CONCLUSION: UNKNOWN")
except Exception as e:
    print(f"Error: {e}")

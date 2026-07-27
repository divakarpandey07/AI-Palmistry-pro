import urllib.request

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30002264677"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        if "AI-Palmistry-Pro-APK" in html or "artifact" in html.lower():
            print("ARTIFACT DETECTED IN RUN HTML: YES!")
        else:
            print("ARTIFACT DETECTED IN RUN HTML: NO!")
except Exception as e:
    print(f"Error: {e}")

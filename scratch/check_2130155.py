import urllib.request

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30002130155"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        if "octicon-x-circle-fill color-fg-danger" in html:
            print("CONCLUSION: FAILURE")
            lines = [line.strip() for line in html.splitlines() if "failed" in line.lower() or "error" in line.lower()]
            for line in lines[:10]:
                print(" -", line[:150])
        elif "octicon-check-circle-fill color-fg-success" in html or "color-fg-done" in html:
            print("CONCLUSION: SUCCESS")
        elif "in_progress" in html or "queued" in html:
            print("STATUS: IN_PROGRESS")
        else:
            print("STATUS: UNKNOWN")
except Exception as e:
    print(f"Error: {e}")

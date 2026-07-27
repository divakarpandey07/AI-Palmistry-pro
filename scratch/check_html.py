import urllib.request

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30000982381"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        if "Workflow run completed" in html or "successful" in html.lower() or "completed" in html.lower():
            print("Page HTML loaded successfully.")
            if "color-fg-done" in html or "completed" in html:
                print("Run status: COMPLETED")
            if "Success" in html or "success" in html:
                print("Run conclusion: SUCCESS")
            elif "Failure" in html or "failure" in html:
                print("Run conclusion: FAILURE")
        else:
            print("Status unknown")
except Exception as e:
    print(f"Error fetching HTML: {e}")

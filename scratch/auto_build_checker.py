import urllib.request
import json
import time

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=1"
headers = {"User-Agent": "Mozilla/5.0"}

def check_build():
    try:
        req = urllib.request.Request(url, headers=headers)
        with urllib.request.urlopen(req) as resp:
            data = json.loads(resp.read().decode())
            runs = data.get("workflow_runs", [])
            if runs:
                latest = runs[0]
                return {
                    "id": latest.get("id"),
                    "name": latest.get("name"),
                    "status": latest.get("status"),
                    "conclusion": latest.get("conclusion"),
                    "url": latest.get("html_url")
                }
    except Exception as e:
        return {"error": str(e)}
    return None

if __name__ == "__main__":
    info = check_build()
    print(json.dumps(info, indent=2))

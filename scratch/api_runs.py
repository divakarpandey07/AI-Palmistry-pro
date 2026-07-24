import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=3"
headers = {
    "User-Agent": "AntigravityBuildMonitor/1.0"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        for r in data.get("workflow_runs", []):
            print(f"ID: {r.get('id')} | Name: {r.get('name')} | Status: {r.get('status')} | Conclusion: {r.get('conclusion')} | Commit: {r.get('head_commit', {}).get('message')}")
except Exception as e:
    print(f"API Error: {e}")

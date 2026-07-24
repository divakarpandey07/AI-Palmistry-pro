import http.client
import json

conn = http.client.HTTPSConnection("api.github.com")
headers = {
    "User-Agent": "AntigravityBuildMonitor/1.0 (Windows NT 10.0; Win64; x64)",
    "Accept": "application/vnd.github.v3+json"
}

try:
    conn.request("GET", "/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=5", headers=headers)
    res = conn.getresponse()
    print(f"HTTP Status: {res.status}")
    body = res.read().decode("utf-8")
    if res.status == 200:
        data = json.loads(body)
        runs = data.get("workflow_runs", [])
        for r in runs:
            print(f"Run ID: {r.get('id')} | Head SHA: {r.get('head_sha')[:7]} | Status: {r.get('status')} | Conclusion: {r.get('conclusion')}")
    else:
        print(f"Response: {body[:300]}")
except Exception as e:
    print(f"Error: {e}")

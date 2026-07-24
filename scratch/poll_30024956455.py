import http.client
import json

conn = http.client.HTTPSConnection("api.github.com")
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    conn.request("GET", "/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30024956455/jobs", headers=headers)
    res = conn.getresponse()
    print(f"HTTP Status: {res.status}")
    data = json.loads(res.read().decode("utf-8"))
    jobs = data.get("jobs", [])
    if jobs:
        j = jobs[0]
        print(f"Run 30024956455 Status: {j.get('status')}, Conclusion: {j.get('conclusion')}")
        for s in j.get("steps", []):
            print(f"   Step: {s.get('name')} -> Status: {s.get('status')}, Conclusion: {s.get('conclusion')}")
except Exception as e:
    print(f"Error: {e}")

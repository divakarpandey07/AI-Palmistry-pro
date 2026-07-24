import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30020465007/jobs"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        jobs = data.get("jobs", [])
        if jobs:
            j = jobs[0]
            print(f"Run 30020465007 Status: {j.get('status')}, Conclusion: {j.get('conclusion')}")
            for s in j.get("steps", []):
                print(f"   Step: {s.get('name')} -> Status: {s.get('status')}, Conclusion: {s.get('conclusion')}")
except Exception as e:
    print(f"Error: {e}")

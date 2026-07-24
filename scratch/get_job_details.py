import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30000752581/jobs"
headers = {"User-Agent": "Mozilla/5.0"}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode())
        jobs = data.get("jobs", [])
        for job in jobs:
            print(f"Job Name: {job.get('name')}")
            print(f"Job Conclusion: {job.get('conclusion')}")
            for step in job.get("steps", []):
                print(f"  Step: {step.get('name')} -> Status: {step.get('status')}, Conclusion: {step.get('conclusion')}")
except Exception as e:
    print(f"Error fetching jobs: {e}")

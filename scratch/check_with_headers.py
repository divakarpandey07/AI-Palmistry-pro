import urllib.request
import json
import time

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs"
req = urllib.request.Request(url, headers={"User-Agent": "AGY-Build-Checker-v10/1.0"})

try:
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        runs = data.get("workflow_runs", [])
        if runs:
            r = runs[0]
            print(f"Latest Run ID: {r.get('id')} | Name: {r.get('name')} | Status: {r.get('status')} | Conclusion: {r.get('conclusion')}")
            jobs_url = r.get("jobs_url")
            req_jobs = urllib.request.Request(jobs_url, headers={"User-Agent": "AGY-Build-Checker-v10/1.0"})
            with urllib.request.urlopen(req_jobs) as resp_jobs:
                data_jobs = json.loads(resp_jobs.read().decode("utf-8"))
                jobs = data_jobs.get("jobs", [])
                if jobs:
                    j = jobs[0]
                    print(f"   Job Status: {j.get('status')}, Conclusion: {j.get('conclusion')}")
                    for s in j.get("steps", []):
                        print(f"      Step: {s.get('name')} -> Status: {s.get('status')}, Conclusion: {s.get('conclusion')}")
except Exception as e:
    print(f"Error: {e}")

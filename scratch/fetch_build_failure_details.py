import urllib.request
import json
import re

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=1"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        runs = data.get("workflow_runs", [])
        if runs:
            latest = runs[0]
            print(f"Run ID: {latest.get('id')}")
            print(f"Commit Message: {latest.get('head_commit', {}).get('message')}")
            print(f"Status: {latest.get('status')}")
            print(f"Conclusion: {latest.get('conclusion')}")
            
            jobs_url = latest.get("jobs_url")
            if jobs_url:
                jreq = urllib.request.Request(jobs_url, headers=headers)
                with urllib.request.urlopen(jreq) as jresp:
                    jdata = json.loads(jresp.read().decode("utf-8"))
                    for job in jdata.get("jobs", []):
                        print(f"\nJob: {job.get('name')} | Conclusion: {job.get('conclusion')}")
                        for s in job.get("steps", []):
                            print(f"   Step: {s.get('name')} -> Status: {s.get('status')}, Conclusion: {s.get('conclusion')}")
except Exception as e:
    print(f"Error: {e}")

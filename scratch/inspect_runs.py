import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=5"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode())
        runs = data.get("workflow_runs", [])
        for r in runs:
            print(f"Run #{r.get('run_number')} | Commit: {r.get('head_sha')[:7]} | Status: {r.get('status')} | Conclusion: {r.get('conclusion')} | Name: {r.get('head_commit', {}).get('message')}")
            jobs_url = r.get("jobs_url")
            if jobs_url:
                try:
                    jreq = urllib.request.Request(jobs_url, headers=headers)
                    with urllib.request.urlopen(jreq) as jresp:
                        jdata = json.loads(jresp.read().decode())
                        for job in jdata.get("jobs", []):
                            print(f"   Job: {job.get('name')} -> {job.get('conclusion')}")
                            for step in job.get("steps", []):
                                print(f"     Step: {step.get('name')} [{step.get('conclusion')}]")
                except Exception as je:
                    print(f"   Job fetch error: {je}")
except Exception as e:
    print(f"Error: {e}")

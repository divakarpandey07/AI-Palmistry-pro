import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30016569206/jobs"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        jobs = data.get("jobs", [])
        if jobs:
            job = jobs[0]
            job_id = job.get("id")
            print(f"Job ID: {job_id}")
            log_url = f"https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/jobs/{job_id}/logs"
            try:
                lreq = urllib.request.Request(log_url, headers=headers)
                with urllib.request.urlopen(lreq) as lresp:
                    log_text = lresp.read().decode("utf-8", errors="ignore")
                    print(f"Downloaded log ({len(log_text)} bytes)")
                    # Find error lines
                    lines = [line for line in log_text.splitlines() if "ERROR" in line or "FAILED" in line or "e:" in line or "Exception" in line]
                    print(f"Found {len(lines)} error/failure lines in build log:")
                    for line in lines[:20]:
                        print("  >>", line[:200])
            except Exception as le:
                print(f"Error fetching log: {le}")
except Exception as e:
    print(f"Error: {e}")

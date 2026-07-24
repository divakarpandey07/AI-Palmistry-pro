import http.client
import json

conn = http.client.HTTPSConnection("api.github.com")
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    conn.request("GET", "/repos/divakarpandey07/AI-Palmistry-pro/actions/runs", headers=headers)
    res = conn.getresponse()
    print(f"HTTP Status: {res.status}")
    body = res.read().decode("utf-8")
    data = json.loads(body)
    runs = data.get("workflow_runs", [])
    if runs:
        r = runs[0]
        print(f"Run ID: {r.get('id')} | Name: {r.get('name')} | Status: {r.get('status')} | Conclusion: {r.get('conclusion')}")
        conn.request("GET", f"/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/{r.get('id')}/jobs", headers=headers)
        res_jobs = conn.getresponse()
        data_jobs = json.loads(res_jobs.read().decode("utf-8"))
        jobs = data_jobs.get("jobs", [])
        if jobs:
            j = jobs[0]
            print(f"   Job Status: {j.get('status')}, Conclusion: {j.get('conclusion')}")
            for s in j.get("steps", []):
                print(f"      Step: {s.get('name')} -> Status: {s.get('status')}, Conclusion: {s.get('conclusion')}")
    else:
        print(f"Message: {data.get('message')}")
except Exception as e:
    print(f"Error: {e}")

import urllib.request
import json
import time

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs?per_page=1"
headers = {"User-Agent": "Python-URLLib"}

req = urllib.request.Request(url, headers=headers)
try:
    with urllib.request.urlopen(req) as response:
        data = json.loads(response.read().decode())
        runs = data.get("workflow_runs", [])
        if runs:
            latest = runs[0]
            print(f"Run ID: {latest.get('id')}")
            print(f"Name: {latest.get('name')}")
            print(f"Status: {latest.get('status')}")
            print(f"Conclusion: {latest.get('conclusion')}")
            print(f"HTML URL: {latest.get('html_url')}")
        else:
            print("No workflow runs found.")
except Exception as e:
    print(f"Error checking status: {e}")

import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30000982381"
headers = {"User-Agent": "Mozilla/5.0"}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode())
        print(f"Run ID: {data.get('id')}")
        print(f"Status: {data.get('status')}")
        print(f"Conclusion: {data.get('conclusion')}")
        print(f"URL: {data.get('html_url')}")
except Exception as e:
    print(f"Error: {e}")

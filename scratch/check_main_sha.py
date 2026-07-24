import urllib.request
import json

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/commits/main"
req = urllib.request.Request(url, headers={
    "User-Agent": "AntigravityBot/2.0 (Windows)",
    "Accept": "application/vnd.github.v3+json"
})

try:
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
        print(f"Latest Commit SHA on main: {data.get('sha')[:7]}")
        print(f"Commit Message: {data.get('commit', {}).get('message')}")
except Exception as e:
    print(f"Error checking main commit: {e}")

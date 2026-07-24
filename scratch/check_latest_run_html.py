import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions"
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

try:
    req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        # Find run URLs
        runs = re.findall(r'/divakarpandey07/AI-Palmistry-pro/actions/runs/\d+', html)
        if runs:
            latest_run_path = runs[0]
            latest_url = f"https://github.com{latest_run_path}"
            print(f"Latest Run URL: {latest_url}")
            
            # Fetch latest run HTML
            lreq = urllib.request.Request(latest_url, headers=headers)
            with urllib.request.urlopen(lreq) as lresp:
                lhtml = lresp.read().decode("utf-8", errors="ignore")
                print("Latest Run Page Loaded.")
                if "This workflow run completed" in lhtml or "completed" in lhtml.lower():
                    print("Status: COMPLETED")
                if "Success" in lhtml or "success" in lhtml:
                    print("Conclusion: SUCCESS")
                elif "Failure" in lhtml or "failure" in lhtml:
                    print("Conclusion: FAILURE")
                elif "in_progress" in lhtml or "queued" in lhtml:
                    print("Status: IN_PROGRESS")
        else:
            print("No runs found in HTML.")
except Exception as e:
    print(f"Error checking latest run: {e}")

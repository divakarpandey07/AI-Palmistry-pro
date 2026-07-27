import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions"
req = urllib.request.Request(url, headers={
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
})

try:
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        # Extract run links
        run_ids = re.findall(r'/divakarpandey07/AI-Palmistry-pro/actions/runs/(\d+)', html)
        run_ids = list(dict.fromkeys(run_ids))
        print("Found Run IDs:", run_ids[:5])

        if run_ids:
            latest_run_url = f"https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/{run_ids[0]}"
            print(f"Fetching latest run page: {latest_run_url}")
            req_run = urllib.request.Request(latest_run_url, headers={
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            })
            with urllib.request.urlopen(req_run) as resp_run:
                run_html = resp_run.read().decode("utf-8", errors="ignore")
                # Look for failure error messages or logs in html snippet
                snippets = re.findall(r'(Error:[^\n<]+|FAILED[^\n<]+|failed[^\n<]+|Exception[^\n<]+)', run_html)
                print("Error Snippets Found:")
                for s in snippets[:15]:
                    print("  ", s)
except Exception as e:
    print(f"Error fetching actions page: {e}")

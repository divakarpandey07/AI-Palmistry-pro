import urllib.request
import re

url = "https://github.com/divakarpandey07/AI-Palmistry-pro/actions/runs/30083142308"
req = urllib.request.Request(url, headers={
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
})

try:
    with urllib.request.urlopen(req) as resp:
        html = resp.read().decode("utf-8", errors="ignore")
        # Find job URLs
        job_urls = re.findall(r'/divakarpandey07/AI-Palmistry-pro/actions/runs/30083142308/job/\d+', html)
        print("Job URLs:", set(job_urls))
        if job_urls:
            jurl = "https://github.com" + job_urls[0]
            print(f"Fetching job page: {jurl}")
            req_j = urllib.request.Request(jurl, headers={
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            })
            with urllib.request.urlopen(req_j) as resp_j:
                jhtml = resp_j.read().decode("utf-8", errors="ignore")
                # Look for step titles and error messages
                steps = re.findall(r'<span class="[^\"]*">(Step [^<]+|Run [^<]+|Error[^<]+|chmod[^<]+|assembleDebug[^<]+)', jhtml)
                print("Steps & Errors:")
                for s in steps[:30]:
                    print("  ", s)
except Exception as e:
    print(f"Error: {e}")

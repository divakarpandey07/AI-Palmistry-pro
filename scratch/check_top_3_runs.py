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
        runs = list(dict.fromkeys(re.findall(r'/divakarpandey07/AI-Palmistry-pro/actions/runs/\d+', html)))
        print(f"Found {len(runs)} unique run URLs on Actions page:")
        for rpath in runs[:3]:
            rurl = f"https://github.com{rpath}"
            rreq = urllib.request.Request(rurl, headers=headers)
            with urllib.request.urlopen(rreq) as rresp:
                rhtml = rresp.read().decode("utf-8", errors="ignore")
                is_success = "Success" in rhtml or "success" in rhtml
                is_failure = "Failure" in rhtml and "Success" not in rhtml
                status = "SUCCESS (GREEN)" if is_success else ("FAILURE (RED)" if is_failure else "UNKNOWN")
                print(f" -> {rurl} : {status}")
except Exception as e:
    print(f"Error: {e}")

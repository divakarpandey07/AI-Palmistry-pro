import urllib.request
import zipfile
import io

url = "https://api.github.com/repos/divakarpandey07/AI-Palmistry-pro/actions/runs/30026145096/logs"
req = urllib.request.Request(url, headers={
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept": "application/vnd.github.v3+json"
})

try:
    with urllib.request.urlopen(req) as resp:
        content = resp.read()
        z = zipfile.ZipFile(io.BytesIO(content))
        for filename in z.namelist():
            if "Build Debug APK" in filename or "assembleDebug" in filename or "txt" in filename:
                print(f"--- File: {filename} ---")
                text = z.read(filename).decode("utf-8", errors="ignore")
                lines = text.splitlines()
                # Print errors
                for l in lines[-100:]:
                    print(l)
except Exception as e:
    print(f"Error fetching logs: {e}")

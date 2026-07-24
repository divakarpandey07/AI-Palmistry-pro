import urllib.request
import os

os.makedirs("gradle/wrapper", exist_ok=True)
url = "https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar"
target = "gradle/wrapper/gradle-wrapper.jar"

print(f"Downloading {url} to {target}...")
try:
    urllib.request.urlretrieve(url, target)
    print(f"Successfully downloaded {target}, size: {os.path.getsize(target)} bytes")
except Exception as e:
    print(f"Error downloading: {e}")

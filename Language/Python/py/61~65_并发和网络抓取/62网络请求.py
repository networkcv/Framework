import requests

resp = requests.get('https://www..com/')
if resp.status_code == 200:
    print(resp.text)

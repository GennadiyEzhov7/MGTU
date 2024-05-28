import requests

url = 'http://localhost:5000/posts'
token = 'dnGwiMcxkvqR7fZ5'


response = requests.get(url)
print("response code -", response.status_code)
print("response headers -", response.headers)
print("response body -", response.json())
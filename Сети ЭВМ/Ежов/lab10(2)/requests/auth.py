import requests

url = 'http://localhost:5000/auth'
data = {
    'login': 'admin',
    'password': 'admin'
}

response = requests.post(url, json=data)

print("response code -",response.status_code)
print("response headers -", response.headers)
print("response body -",response.text)
import requests


url = 'http://localhost:5000/posts/6/comments'
token = 'dnGwiMcxkvqR7fZ5'
headers = {
    'Authorization': f'Bearer {token}'
}

data = {
    'author': 'Guest User', 
    'comment': 'Очень вкусные пельмешки.'
}

response = requests.post(url = url, data=data, headers=headers)

print("response code -", response.status_code)
print("response headers -", response.headers)
print("response body -", response.json())




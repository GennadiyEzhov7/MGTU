import requests


url = 'http://localhost:5000/posts/6/comments/27'  
token = 'dnGwiMcxkvqR7fZ5' 


headers = {'Authorization': f'Bearer {token}'}

response = requests.delete(url=url, headers=headers)

print("response code -", response.status_code)
print("response headers -", response.headers)
print("response body -", response.json())
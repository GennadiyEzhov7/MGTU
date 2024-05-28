import requests


url = 'http://localhost:5000/posts/tag/pel2meni'




response = requests.get(url = url)

print("response code -", response.status_code)
print("response headers -", response.headers)
print("response body - " ,  response.json())


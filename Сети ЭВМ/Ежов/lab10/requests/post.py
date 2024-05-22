import requests

url = 'http://localhost:5000/posts'
token = 'dnGwiMcxkvqR7fZ5' 
headers = {
    'Authorization': f'Bearer {token}'
}
files = {'image': open('img/pelmeni.png', 'rb')}
data = {
    'title': 'Пельмени543423',
    'anons': 'Вкусные пельмени',
    'text': 'много мясо мало теста',
    'tags': ['pelmeni', 'pushka']
}

response = requests.post(url, headers=headers, files=files, data=data)
print("response code -",response.status_code)
print("response headers -", response.headers)
print("response body -",response.text)
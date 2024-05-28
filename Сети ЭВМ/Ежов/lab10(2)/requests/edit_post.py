import requests

url = 'http://localhost:5000/posts/4'
token = 'dnGwiMcxkvqR7fZ5'
headers = {
    'Authorization': f'Bearer {token}'
}
files = {'image': open('img/pelmeni2.png', 'rb')}
data = {
    'title': 'Новое название блюда',
    'anons': 'Новое краткое описание блюда',
    'text': 'Новое полное описание блюда с указанием веса ингредиентов',
    'tags': ''
}


response = requests.post(url, headers=headers, data=data, files=files)
print("response code -", response.status_code)
print("response headers -", response.headers)
print("response body -", response.json())
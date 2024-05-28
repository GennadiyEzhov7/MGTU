#------------------------libs------------------------------------

from flask import Flask, request, jsonify, make_response
from werkzeug.utils import secure_filename
from flask_sqlalchemy import SQLAlchemy
import secrets
import string
import os
from datetime import datetime as dt

#---------------------------SETTINGS---------------------

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:root@localhost:5432/mgtu'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'your_secret_key'
app.config['UPLOAD_FOLDER'] = 'post_images'
db = SQLAlchemy(app)


#----------------------TABLES-------------------------------

class User(db.Model):
    __tablename__ = 'users'  # Указываем существующую таблицу
    id = db.Column(db.Integer)
    login = db.Column(db.Text, primary_key=True, unique=True, nullable=False)
    password = db.Column(db.Text)
    token = db.Column(db.Text)

class Dishes(db.Model):
    __tablename__ = 'dishes'
    id = db.Column(db.Integer, primary_key=True, unique=True, nullable=False)
    title = db.Column(db.Text, nullable=False, unique=True)
    anons = db.Column(db.Text, nullable=False)
    text = db.Column(db.Text, nullable=False)
    tags = db.Column(db.ARRAY(db.Text))
    image = db.Column(db.Date)
    datetime = db.Column(db.String)
    comment = db.Column(db.ARRAY(db.Integer))


class Comments(db.Model):
    __tablename__ = 'comments'
    id = db.Column(db.Integer, primary_key=True, nullable=False)
    datetime = db.Column(db.String)
    author = db.Column(db.Text)
    comment = db.Column(db.Text)
    post_id = db.Column(db.Integer, nullable=False)

#------------------------UTILS--------------------------------------------

def generate_random_string(length=16):
    characters = string.ascii_letters + string.digits
    return ''.join(secrets.choice(characters) for _ in range(length))

def check_file_format(filename):
    ALLOWED_EXTENSIONS = {'jpg', 'jpeg', 'png'}
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def check_token(func):
    def wrapper(*args, **kwargs):
        auth_header = request.headers.get('Authorization')
        if auth_header and auth_header.startswith('Bearer '):
            token = auth_header.split(' ')[1]
            print(token)
            user = User.query.filter_by(token=token).first()
            if user:
                return func(*args, **kwargs)

        response = make_response(jsonify({
            'message': 'Unauthorized'
        }), 401)
        response.headers['Status-Text'] = 'Unauthorized'
        return response

    wrapper.__name__ = func.__name__  # Для сохранения имени оборачиваемой функции
    return wrapper

#--------------------------------SERVE_CODE-----------------------------------

@app.route('/auth', methods=['POST'])
def auth():
    data = request.get_json()
    login = data.get('login')
    password = data.get('password')

    if not login or not password:
        response = make_response(jsonify({
            'status': False,
            'message': 'Invalid authorization data'
        }), 401)

        response.headers['Status-Text'] = 'Invalid authorization data'
        return response
    
    user = User.query.filter_by(login=login, password=password).first()
    if user != None:
        user.token = generate_random_string()
        db.session.commit()

        response = make_response(jsonify({
            'status': True,
            'token': user.token
        }), 200)
        response.headers['Status-Text'] = 'Successful authorization'
        return response

    response = make_response(jsonify({
        'status': False,
        'message': 'Invalid authorization data'
    }), 401)
    response.headers['Status-Text'] = 'Invalid authorization data'
    return response


@app.route('/posts', methods=['POST'])
@check_token
def posts():
    error_messages = []
    error_flag = False

    title = request.form.get('title')
    anons = request.form.get('anons')
    text = request.form.get('text')
    tags = request.form.getlist('tags')
    tags_array = [tag.strip() for tag in tags]
    image = request.files.get('image')
    dishes = Dishes.query.filter_by(title = title).first()
    if dishes:
        error_flag = True
        error_messages.append({'title': "already exists"})
    if title == None or title.strip() == "":
        error_flag = True
        error_messages.append({'title': "must be not empty"})
    if anons == None or anons.strip() == "":
        error_flag = True
        error_messages.append({'anons': 'must be not empty'})
    if text == None or text.strip() == "":
        error_flag = True
        error_messages.append({'text': 'must be not empty'})
    if check_file_format(image.filename) == False:
        error_messages.append({'image': 'invalid file format'})
    if error_flag:
        response = make_response(jsonify({
        'status': False,
        'message': error_messages
        }), 400)
        response.headers['Status-Text'] = 'Creating error'
        return response
    
    filename = secure_filename(image.filename)
    image_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    image.save(image_path)
    new_dish = Dishes(title=title, anons=anons, text=text, tags=tags_array, image ="post_images/"+image.filename, datetime = dt.now())
    db.session.add(new_dish)
    db.session.commit()

    response = make_response(jsonify({
        'status': True,
        'message': new_dish.id
    }), 201)
    response.headers['Status-Text'] = 'Successful creation'
    return response

@app.route('/posts/<int:post_id>', methods=['POST'])
@check_token
def edit_post(post_id):
    error_messages = {}
    error_flag = False

    title = request.form.get('title')
    anons = request.form.get('anons')
    text = request.form.get('text')
    tags = request.form.get('tags')
    image = request.files.get('image')

    dishes = Dishes.query.filter_by(id = post_id).first()
    if not dishes:
        response = make_response(jsonify({
        'message': "Post not found"
        }), 201)
        response.headers['Status-Text'] = 'Post not found'
        return response
    
    if check_file_format(image.filename) == False:
        error_messages.append({'image': 'invalid file format'})
        response = make_response(jsonify({
        'status': False,
        'message': error_messages
        }), 400)
        response.headers['Status-Text'] = 'Editing error'
        return response
    
    if title is not None:
        dishes.title = title
    if anons is not None:
        dishes.anons = anons
    if text is not None:
        dishes.text = text
    if tags is not None:
        dishes.tags = tags

    # Commit changes to the database
    db.session.commit()

    response_data = {
        'status': True,
        'post': {
            'title': dishes.title,
            'datetime': dishes.datetime.strftime('%H:%M %d.%m.%Y'),
            'anons': dishes.anons,
            'text': dishes.text,
            'tags': dishes.tags,
            'image': dishes.image
        }
    }

    response = make_response(jsonify(response_data), 201)
    response.headers['Status-Text'] = 'Successful creation'
    return response

@app.route('/posts/<int:post_id>', methods=['DELETE'])
@check_token
def delete_post(post_id):
    dish = Dishes.query.filter_by(id=post_id).first()
    if not dish:
        response = make_response(jsonify({'message': 'Post not found'}), 404)
        response.headers['Status-Text'] = 'Post not found'
        return response
    
    db.session.delete(dish)
    db.session.commit()
    
    response = make_response(jsonify({'status': True}), 201)
    response.headers['Status-Text'] = 'Successful delete'
    return response

@app.route('/posts', methods=['GET'])
def list_posts():
    dishes = Dishes.query.all()
    posts = []
    for dish in dishes:
        post = {
            'title': dish.title,
            'datetime': dish.datetime.strftime('%H:%M %d.%m.%Y'),
            'anons': dish.anons,
            'text': dish.text,
            'tags': dish.tags,
            'image': dish.image
        }
        posts.append(post)
    
    response = make_response(jsonify(posts), 200)
    response.headers['Status-Text'] = 'List posts'
    return response

@app.route('/posts/<int:post_id>', methods=['GET'])
def view_post(post_id):
    dish = Dishes.query.filter_by(id=post_id).first()
    if not dish:
        response = make_response(jsonify({'message': 'Post not found'}), 404)
        response.headers['Status-Text'] = 'Post not found'
        return response
    

    
    comments = []
    if dish.comment:
        for comment_id in dish.comment: 
            comment = Comments.query.filter_by(id=comment_id).first()
            comments.append({
                'comment_id': comment.id,
                'datetime': comment.datetime.strftime('%H:%M %d.%m.%Y'),
                'author': comment.author,
                'comment': comment.comment
            })
    
    post = {
        'title': dish.title,
        'datetime': dish.datetime.strftime('%H:%M %d.%m.%Y'),
        'anons': dish.anons,
        'text': dish.text,
        'tags': dish.tags,
        'image': dish.image,
        'comments': comments
    }
    
    response = make_response(jsonify(post), 200)
    response.headers['Status-Text'] = 'View post'
    return response

@app.route('/posts/<int:post_id>/comments', methods=['POST'])
@check_token
def add_comment(post_id):
    data = request.form
    if request.headers.get('Authorization') != None:
        author = 'admin'
    else:
        author = data.get('author')
    comment_text = data.get('comment')

    # Проверка существования блюда
    dish = Dishes.query.filter_by(id=post_id).first()
    if not dish:
        response = make_response(jsonify({'message': 'Post not found'}), 404)
        response.headers['Status-Text'] = 'Post not found'
        return response

    # Проверка обязательных параметров
    error_messages = {}
    if not comment_text or len(comment_text) > 255:
        error_messages['comment'] = 'Comment is required and must be less than 255 characters.'
    
    if 'Authorization' not in request.headers and not author:
        error_messages['author'] = 'Author is required for guest.'

    if error_messages:
        response = make_response(jsonify({'status': False, 'message': error_messages}), 400)
        response.headers['Status-Text'] = 'Creating error'
        return response


    new_comment = Comments(author=author, comment=comment_text, post_id=post_id, datetime = dt.now())
    db.session.add(new_comment)
    db.session.commit()
    new_comment_id = new_comment.id

    dish = Dishes.query.filter_by(id=post_id).first()

    updated_comments = list(dish.comment) if dish.comment else []
    updated_comments.append(new_comment_id)
    
    # Явное присвоение обновленного списка
    dish.comment = updated_comments
    
    print(dish.comment)
    db.session.commit()
    print(dish.comment)


    response = make_response(jsonify({'status': True}), 201)
    response.headers['Status-Text'] = 'Successful creation'
    return response

@app.route('/posts/<int:post_id>/comments/<int:comment_id>', methods=['DELETE'])
@check_token
def delete_comment(post_id, comment_id):
    dish = Dishes.query.filter_by(id=post_id).first()
    if not dish:
        response = make_response(jsonify({'message': 'Post not found'}), 404)
        response.headers['Status-Text'] = 'Post not found'
        return response
    
    dish = Dishes.query.filter_by(id=post_id).first()

    updated_comments = list(dish.comment) 
    updated_comments.remove(comment_id)

    
    # Явное присвоение обновленного списка
    dish.comment = updated_comments
    
    print(dish.comment)
    db.session.commit()
    print(dish.comment)

    comment = Comments.query.filter_by(id=comment_id, post_id=post_id).first()
    if not comment:
        response = make_response(jsonify({'message': 'Comment not found'}), 404)
        response.headers['Status-Text'] = 'Comment not found'
        return response

    db.session.delete(comment)
    db.session.commit()

    response = make_response(jsonify({'status': True}), 201)
    response.headers['Status-Text'] = 'Successful delete'
    return response
    
@app.route('/posts/tag/<tag_name>', methods=['GET'])
def search_by_tag(tag_name):
    dishes = Dishes.query.filter(Dishes.tags.any(tag_name)).all()
    if not dishes:
        response = make_response(jsonify({'message': 'No posts found for this tag'}), 404)
        response.headers['Status-Text'] = 'No posts found'
        return response

    # Формируем ответ
    posts = []
    for dish in dishes:
        post = {
            'title': dish.title,
            'datetime': dish.datetime.strftime('%H:%M %d.%m.%Y'),
            'anons': dish.anons,
            'text': dish.text,
            'tags': dish.tags,
            'image': dish.image
        }
        posts.append(post)
    
    response = make_response(jsonify(posts), 200)
    response.headers['Status-Text'] = 'Found posts'
    return response    

if __name__ == '__main__':
    app.run(debug=True)
from flask import Flask, render_template, request, jsonify
import random
import math

app = Flask(__name__)

MAX_D = 100
MIN_D = 1
#attempts = math.log2(MAX_D-MIN_D+1)+MIN_D

def set_start():
    
    global attempts, secret_number
    secret_number = random.randint(1, 100)
    attempts = int(math.log2(MAX_D-MIN_D+1)+MIN_D)


@app.route('/')
def index():
    set_start()
    return render_template('index.html')

@app.route('/guess', methods=['POST'])
def guess_number():
    global attempts, secret_number
    if request.form['number'] == "":
        return jsonify({'result': "Вы не ввели данные", 'attempts': attempts})
    guess = int(request.form['number'])
    attempts -=1
    if attempts <= 0:
        return jsonify({'result': "Вы проиграли", 'attempts': attempts})
    if guess > secret_number:
        result = 'Меньше'
    elif guess < secret_number:
        result = 'Больше'
    else:
        result = 'Вы угадали!'
    print(secret_number)
    return jsonify({'result': result, 'attempts': attempts})

@app.route('/restart', methods=['POST'])
def restart_game():
    global secret_number, attempts
    set_start()
    secret_number = random.randint(1, 100)
    return jsonify({'result': 'Игра начата заново', 'attempts': attempts})
if __name__ == '__main__':
    app.run(debug=True)
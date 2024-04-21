from flask import Flask, render_template, request
import random
import math

from werkzeug.utils import redirect

app = Flask(__name__)

MAX_D = 100
MIN_D = 1
rules = f"Компьютер загадал число от {MIN_D} до {MAX_D}"
attempts = int(math.log2(MAX_D - MIN_D + 1)) + 1
secret_number = random.randint(MIN_D, MAX_D)
new_game = False
btn1 = False


def set_start():
    global attempts, secret_number, btn1, new_game, result, hint
    hint = ""
    result = ""
    secret_number = random.randint(MIN_D, MAX_D)
    attempts = int(math.log2(MAX_D - MIN_D + 1)) + 1
    btn1 = False
    new_game = False


@app.route('/')
def index():
    global attempts, secret_number, result, btn1, hint, rules
    set_start()
    return render_template('index.html', rules=rules)


@app.route('/guess', methods=['GET'])
def guess_number():
    global attempts, secret_number, btn1, new_game
    if new_game:
        set_start()
    else:
        new_game = True
    return render_template('index.html', rules=rules, result=result, attempts=attempts, btn1=btn1, hint=hint)


@app.route('/check', methods=['POST', 'GET'])
def check():
    global attempts, secret_number, result, btn1, hint, rules, new_game
    new_game = False
    user_number = request.form['user_number']
    print(user_number)
    if user_number == "":
        result = "Вы не ввели данные"
        return redirect('/guess')
    user_number = int(user_number)
    if attempts == 1:
        if user_number == secret_number:
            result = 'Вы угадали!'
            attempts = 0
            btn1 = True
        else:
            result = "Вы проиграли, загаданное число: " + str(secret_number)
            btn1 = True
            attempts = 0
        return redirect('/guess')
    else:
        if user_number > MAX_D or user_number < MIN_D:
            result = "Вне диапазона"
            return redirect('/guess')
        if user_number > secret_number:
            result = "Неверно"
            btn1 = False
            hint = 'Число меньше ' + str(user_number)
        elif user_number < secret_number:
            result = "Неверно"
            btn1 = False
            hint = 'Число больше ' + str(user_number)
        else:
            result = 'Вы угадали!'
            btn1 = True
        attempts -= 1
    return redirect('/guess')


@app.route('/restart', methods=['GET'])
def restart_game():
    global secret_number, btn1, attempts
    set_start()
    return redirect('/')


if __name__ == '__main__':
    app.run(debug=True)

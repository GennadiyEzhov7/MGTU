

button_start = document.getElementById('button_start');
div_main_game = document.getElementById('div_after_start');

button_guess = document.getElementById('button_guess');
input_digit = document.getElementById('input_digit');
button_restart = document.getElementById('button_restart');
labelCount = document.getElementById('labelCount');
input_result = document.getElementById('input_result');



function setCounts(){
    minD=0;
    maxD = 100;
    return Number.parseInt(Math.log2(maxD-minD+1)+minD)
}

function setRandom(){
    minD=0;
    maxD = 100;
    return Math.floor(Math.random() * 100) + 1;
}

digit = setRandom();
counts = setCounts();

document.addEventListener( 'keyup', event => {
      if( event.code === 'Enter' && input_digit.value != "") {
        if (counts > 0){
        labelCount.innerText = "У вас осталось " + counts + " попыток";
        if (Number.parseInt(input_digit.value) == digit){
            button_restart.removeAttribute("disabled");
            input_result.innerText = "Вы угадали";
            button_guess.disabled=true;

        }
        else if (Number.parseInt(input_digit.value) <=0 || Number.parseInt(input_digit.value)>100){
            input_result.innerText = "Неправильный ввод";
        } else {
            if (Number.parseInt(input_digit.value) < digit){
                input_result.innerText = "Загаданное число больше";
                counts = counts-1
            } else {
                input_result.innerText = "Загаданное число меньше";
                counts = counts-1
            }
        }
        } else {
            button_restart.focus();
            labelCount.innerText = "У вас не осталось " + "попыток";
            button_guess.disabled=true;
            button_restart.removeAttribute("disabled");
          }
      }
      if (event.code === 'F5'){
            input_digit.focus();
      }
});

button_guess.addEventListener('click', function(){
    if (counts > 0 && input_digit.value != ""){
        labelCount.innerText = "У вас осталось " + counts + " попыток";
        if (Number.parseInt(input_digit.value) == digit){
            button_restart.removeAttribute("disabled");
            input_result.innerText = "Вы угадали";
            button_guess.disabled=true;
        }  else if (Number.parseInt(input_digit.value) <=0 || Number.parseInt(input_digit.value)>100){
            input_result.innerText = "Неправильный ввод";
        } else {
            if (Number.parseInt(input_digit.value) < digit){
                input_result.innerText = "Загаданное число больше";
                counts = counts-1
            } else {
                input_result.innerText = "Загаданное число меньше";
                counts = counts-1
            }
        }
    } else {
        labelCount.innerText = "У вас не осталось " + "попыток";
        button_guess.disabled=true;
        button_restart.removeAttribute("disabled");
    }
    });


button_restart.addEventListener('click', function(){
input_digit.focus()
    button_restart.disabled=true;
    real_counts = 0;
    digit = setRandom();
    counts = setCounts();
    button_guess.removeAttribute("disabled");
});



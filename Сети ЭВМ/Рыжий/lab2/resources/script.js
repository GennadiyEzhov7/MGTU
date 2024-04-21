let hNumber;
let attemptCount;
const MIN_RANGE = 0;
const MAX_RANGE = 999999;
const MIN_DISTANCE = 100;
let minRange = MIN_RANGE;
let maxRange = 1000;

let numberInp = document.getElementById('numberInp');
let sendNumBtn = document.getElementById('sendNumBtn');
let rules = document.getElementById('rules');
let message = document.getElementById('message');
let minRangeInp = document.getElementById('minRangeInp');
let maxRangeInp = document.getElementById('maxRangeInp');
let restartBtn = document.getElementById('restartBtn');

init();

function init() {
    randomizeNumber();
    document.getElementById('numberForm').addEventListener('submit', (event) => processNumber(event));
    document.getElementById('restartForm').addEventListener('submit', (event) => processRestart(event));
    //numberInp.focus();  
}

function randomizeNumber() {
    minRange = parseInt(minRange);
    maxRange = parseInt(maxRange);
    hNumber = Math.floor(Math.random() * (maxRange - minRange)) + minRange;
    attemptCount = Math.floor(Math.log2(maxRange - minRange + 1));
    sendNumBtn.innerText = "Try to guess (" + attemptCount + ")";
    document.getElementById("titleNum").innerText = "Guess the number! " + hNumber;
}

function numberIsValid(num) {
    if (num == null) {
        message.innerText = "Number should not be empty";
        return false;
    }
    if (!num.toString().match(/^([-+]?)[0-9]{1,6}$/)) {
        message.innerText = "Number is invalid";
        return false;
    }
    if (num < 0) {
        message.innerText = "Number must be positive";
        return false;
    }
    if (num < minRange || num > maxRange) {
        message.innerText = "Number is out of range"
        return false;
    }

    return true;
}

function rangeIsValid() {
    if (minRange == null || maxRange == null) {
        message.innerText = "Range values should not be empty";
        return false;
    }
    if (!minRange.toString().match(/^([-+]?)[0-9]{1,6}$/)) {
        message.innerText = "Min range is invalid";
        return false;
    }
    if (!maxRange.toString().match(/^([-+]?)[0-9]{1,6}$/)) {
        message.innerText = "Max range is invalid";
        return false;
    }
    if ((minRange < MIN_RANGE) || (minRange > MAX_RANGE) || (maxRange < MIN_RANGE) || (maxRange > MAX_RANGE)) {
        message.innerText = "Range values should be in range of [" + MIN_RANGE + "..." + MAX_RANGE + "]";
        return false;
    }
    if (minRange > maxRange) {
        message.innerText = "Min range should be smaller than max range";
        return false;
    }
    if (maxRange - minRange < MIN_DISTANCE) {
        message.innerText = "Range between min and max should be bigger than " + MIN_DISTANCE;
        return false;
    }

    return true;
}

function attemptCountDec() {
    if (attemptCount > 0)
        attemptCount--;

    if (attemptCount == 0) {
        numberInp.disabled = sendNumBtn.disabled = true;
    }

    if (attemptCount != 0)
        sendNumBtn.value = "Try to guess (" + attemptCount + ")";
    else 
        sendNumBtn.value = "Enter new range or restart";
}

function processRestart(event) {
    event.preventDefault(); 
    minRange = minRangeInp.value;
    maxRange = maxRangeInp.value;

    if (!rangeIsValid()) {
        numberInp.disabled = sendNumBtn.disabled = true;
        return;
    }

    randomizeNumber();

    numberInp.disabled = sendNumBtn.disabled = false;
    rules.innerText = "In this game you have to guess a number from " + minRange + " to " + maxRange +". Good luck!"
    message.innerText = "Guess the number"
    sendNumBtn.value = "Try to guess (" + attemptCount + ")";
}

function processNumber(event) {
    event.preventDefault(); 
    
    let num = numberInp.value;

    if (!numberIsValid(num))
        return;
    
    if (attemptCount > 0) {
        if (num > hNumber) 
            message.innerText = "Number is smaller than " + num;
        if (num < hNumber) 
            message.innerText = "Number is bigger than " + num;
        if (num == hNumber) {
            message.innerText = "You win!"
            attemptCount = 0;
            numberInp.disabled = sendNumBtn.disabled = true;
            restartBtn.focus();
        }

        attemptCountDec();
    }

    numberInp.value = "";

}
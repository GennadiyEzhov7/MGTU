
var startBtn = document.getElementById('start');
var fieldNum = document.getElementById('input-num');

function redirect() {
    window.location.href="http:/index.html";
}

// input
fieldNum.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        startBtn.click();
    }
});
fieldNum.focus();


if (startBtn.value === 'Новая игра') {
    startBtn.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            startBtn.click();
        }
    });
    startBtn.focus();
}

function numbersOnly(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if ((charCode < 48 || charCode > 57) || charCode == 45 ) {
      evt.preventDefault();;
    } else {
      return true;
    }
  }











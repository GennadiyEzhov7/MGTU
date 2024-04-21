document.getElementById("button_guess").addEventListener("click", function() {
    var guess = document.getElementById("input_digit").value;
    fetch('/guess', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'number=' + guess,
    })
    .then(response => response.json())
    .then(data => {
        if (data.attempts ==0 || data.result == "Вы угадали!"){
            document.getElementById("button_guess").disabled = true;
        } 
        document.getElementById("input_result").innerText = data.result;
        document.getElementById("labelCount").innerText = "Попыток: " + data.attempts;
    });
});

document.getElementById("button_restart").addEventListener("click", function() {
    fetch('/restart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("button_guess").removeAttribute("disabled");
        document.getElementById("input_result").innerText = data.result;
        document.getElementById("labelCount").innerText = "Попыток: " + data.attempts;
        document.getElementById("input_digit").value = "";
    });
});
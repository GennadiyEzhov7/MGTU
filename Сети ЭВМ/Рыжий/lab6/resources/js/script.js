const canvas = document.getElementById('imageEditor');
const image = document.getElementById('placeholderImg');
const nameInp = document.getElementById('name');
const holidaysSelect = document.getElementById('holidaysSelect');
const genderSelect = document.getElementById('genderSelect');
const downloadBtn = document.getElementById('downloadBtn');
const context = canvas.getContext('2d');
const MIN_FONT_SIZE = 121;
let selectedHoliday;
let holidays;
let gender;
const maleHolidays = [
    {value: "BucketDay", text: "День ведра", draw: (context, name) => {
        context.fillStyle = "#75063a";
        context.strokeStyle = "#0c04db";
        context.lineWidth = 1;
        
        let text = "Дорогой";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 350);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 350);
        
        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 450);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 450); 
    }},
    {value: "BearingDay", text: "День подшипника", draw: (context, name) => {
        context.fillStyle = createGradient(["red", "yellow", "green"]);
        
        let text = "Ура!";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 100);

        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 180);

        text = "День подшипника!";
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 470);
    }},
    {value: "SockDay", text: " День порванного носка", draw: (context, name) => {
        context.fillStyle = createGradient(["blue", "cyan"]);
        
        let text = "Не может этого быть";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, 10, 70);

        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 150);

        text = "С днём порванного носка!";
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 470);
    }}
]

const femaleHolidays = [
    {value: "CottonСandyDay", text: "День сладкой ваты", draw: (context, name) => {
        context.fillStyle = "#cd05f3";
        context.strokeStyle = "black";
        context.lineWidth = 1;
        
        let text = "Такая же сладкая как и ты!";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 350);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 350);
        
        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 450);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 450); 
    }},
    {value: "AloeDay", text: "День алоэ", draw: (context, name) => {
        context.fillStyle = "black";
        context.strokeStyle = "#1ee014";
        context.lineWidth = 1;
        
        let text = "Любимая";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 350);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 350);
        
        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 450);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 450);
    }},
    {value: "AcneDay", text: "День прыщей", draw: (context, name) => {
        context.fillStyle = "#b81dad";
        context.strokeStyle = "white";
        context.lineWidth = 1;
        
        let text = "Не отчаивайся, ";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 100);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 100);
        
        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 200);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 200);
    }}
]

const commonHolidays = [
    {value: "CucumberDay", text: "День солёного огурчика", draw: (context, name) => {
        context.fillStyle = "#108a07";
        context.strokeStyle = "white";
        context.lineWidth = 2;
        
        let text = "Чуть не забыли";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 60);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 60);

        context.lineWidth = 1;
        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 150);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 150); 

        text = "С днём солёного огурчика!";
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 250);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 250);
    }},
    {value: "CornDay", text: "День сладкой кукурузки", draw: (context, name) => {
        context.fillStyle = "#108a07";
        context.strokeStyle = "yellow";
        context.lineWidth = 1;

        let text = "Все любят молодые початки";
        let fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 60);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 60);

        text = "Которые полезны и сладки.";
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 120);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 120);

        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 350);
        context.strokeText(name, canvas.width / 2 - fontSize / 2, 350); 

        text = "С днём сладенького початка кукурузки!";
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 450);
        context.strokeText(text, canvas.width / 2 - fontSize / 2, 450);
    }},
    {value: "FishFingersDay", text: "День рыбных палочек", draw: (context, name) => {
        context.fillStyle = "#9c0516";
        if (gender == 'male')
            text = "Няма, няма, уважаемый";
        else if (gender == 'female')
            text = "Няма, няма, уважаемая";
        else 
            text = "Няма, няма,";
            
        fontSize = calcFontSize(context, text, 90, "calibri");
        context.fillText(text, canvas.width / 2 - fontSize / 2, 350);

        fontSize = calcFontSize(context, name, MIN_FONT_SIZE, "calibri");
        context.fillText(name, canvas.width / 2 - fontSize / 2, 450);
    }}
]

init();

function init() {
    genderSelect.addEventListener('change', () => {
        gender = genderSelect.value;
        switch (genderSelect.value) {
            case "male" : {
                holidays = maleHolidays.concat(commonHolidays);
                break;
            }
            case "female" : {
                holidays = femaleHolidays.concat(commonHolidays);
                break;
            }
            case "common" : {
                holidays = commonHolidays;
            }
        }
        
        holidaysSelect.options.length = 0;

        for (let i = 0; i < holidays.length; i++) {
            let option = document.createElement('option');
            option.value = holidays[i].value;
            option.innerText = holidays[i].text;
            holidaysSelect.appendChild(option);
        }
        holidaysSelect.dispatchEvent(new Event('change'));
    });

    holidaysSelect.addEventListener('change', () => {
        image.src = document.getElementById(holidaysSelect.value).src;
        selectedHoliday = holidays.filter((h) => h.value == holidaysSelect.value)[0];
        console.log(selectedHoliday)
    });

    image.onload = () => {
        context.canvas.width  = image.width;
        context.canvas.height = image.height;
        context.drawImage(image, 0, 0);
        nameInp.dispatchEvent(new Event('input'));
    }

    nameInp.addEventListener('input', () => {
        let name = nameInp.value;

        if (!name.match(/^[А-ЯЁ][а-яё]{1,}([-][А-ЯЁ][а-яё]{1,})?\s[А-ЯЁ][а-яё]{1,}\s[А-ЯЁ][а-яё]{1,}$/)) {
            name = "";
            downloadBtn.disabled = true;
            nameInp.classList.add('wrongName')
            console.log("Incorrect");
        } else {
            nameInp.classList.remove('wrongName')
            downloadBtn.disabled = false;
        }
        context.drawImage(image, 0, 0);
        selectedHoliday.draw(context, name);
    });

    downloadBtn.addEventListener('click', () => {
        var link = document.createElement('a');
        link.download = `${holidaysSelect.value}.png`;
        link.href = canvas.toDataURL()
        link.click();
    })

    genderSelect.dispatchEvent(new Event('change'));
}

function createGradient(colors) {
    var gradient= context.createLinearGradient(0,0,canvas.width,0);
    let c = 1 / colors.length;
    for (let i = 0; i < colors.length; i++)
        gradient.addColorStop((i * c) + "", colors[i]);
    return gradient;
}

function calcFontSize(content, text, fontSize, fontProps) {
        let fontS;
        do {
            context.font = `${fontSize}pt ${fontProps}`;
            fontSize--;
            fontS = context.measureText(text);
        } while(image.naturalWidth <= fontS.width);
        return fontS.width;
}
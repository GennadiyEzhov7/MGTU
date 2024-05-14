const express = require('express');
const path = require('path');
const app = express();
const port = 3000;
const bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(express.static('views'));
app.use(bodyParser.urlencoded({ extended: true }));

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.post('/holidays', (req, res) => {
    const requestData = req.body;
    let holidays = [];

    if (requestData.gender === "m_gender") {
        holidays = ['23 февраля', 'Новый год', 'День программиста', 'День рождения'];
    } else {
        holidays = ['8 марта', 'Новый год', 'День программиста',  'День рождения'];
    }

    const responseData = { holidays };
    // console.log(responseData);
    res.json(responseData);
});

app.get('/', (req, res) => {
    res.render('index')
  //res.sendFile(__dirname + '/views/index.html');
});

app.post('/result', (req, res) =>{
    // res.redirect('/result');
    const requestData = req.body;
    console.log(requestData);
    let resultContentText = '';
    if (requestData.gender == "m_gender"){
        resultContentText = "Уважаемый "+requestData.name+ " "+requestData.surname+" "+requestData.fathername+', поздравляю с';
    } else {
        resultContentText = "Уважаемая "+requestData.name+ " "+requestData.surname+ " "+requestData.fathername+', поздравляю с';
    }

    if (requestData.select_holiday == "23 февраля"){
        resultContentText += " 23 февраля!";
    } else if (requestData.select_holiday == "8 марта"){
        resultContentText += " 8 марта!";
    } else if(requestData.select_holiday == "День программиста"){
        resultContentText += " Днем программиста!";
    } else if (requestData.select_holiday == "День рождения") {
        resultContentText += " Днем рождения!";
    }
    console.log(resultContentText);
    res.render('result', {resultContentText});
    //res.sendFile(__dirname + '/views/result.html', {resultContentText});
});

// app.get('/result', (req, res) =>{
//     res.sendFile(__dirname + '/public/img/8march.png')
// });

app.listen(port, () => {
  console.log(`Сервер запущен на порту ${port}`);
});
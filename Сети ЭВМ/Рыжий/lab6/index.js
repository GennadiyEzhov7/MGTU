const express = require('express');
const path = require('path');
const fs = require('fs');

const PORT = 3000;

const createPath = (page) => path.resolve(__dirname, 'resources',  'views', `${page}.html`);
const app = express();

app.use(express.static(__dirname + '/node_modules/bootstrap/dist'));
app.use(express.static(__dirname + '/resources'));
app.listen(PORT, 'localhost', (error) => {
    error ? console.log(error) : console.log(`listening port ${PORT}`);
})

app.get('/', (req, res) => {
    res.redirect('/editor');
})

app.get('/index', (req, res) => {
    res.redirect('/editor');
})

app.get('/editor', (req, res) => {
    res.sendFile(createPath('editor'));
})

app.use((req, res) => {
    res
    .status(404)
    .sendFile(createPath('404'));
})
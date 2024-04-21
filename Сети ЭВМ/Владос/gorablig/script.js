const shipImageSize = {
    width: 200,
    height: 200
}

class GameOptions {
    shipWeight = 0.0;
    fuelWeight = 0.0;
    impulseWeight = 0.0;
    flowRate = 0.0;
    maxSpeed = 0.0;
    acceleration = 0.0;
    timeLimit = 0.0;
    startDistance = 0.0;

    constructor(shipWeight, fuelWeight, impulseWeight, flowRate, maxSpeed, acceleration, timeLimit, startDistance)
    {    
        this.shipWeight = shipWeight;
        this.fuelWeight = fuelWeight;
        this.impulseWeight = impulseWeight;
        this.flowRate = flowRate;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.timeLimit = timeLimit;
        this.startDistance = startDistance;
    }
}

class Ship {
    x = 0; 
    y = 0;
    speed = 0;
    img = null;
    fireImg = null;
    explostionImg = null;

    constructor(x, y) {
        this.img = new Image();
        this.img.src = "img/ship.png";
        this.fireImg = new Image();
        this.fireImg.src = "img/nitro.png";
        this.explostionImg = new Image();
        this.explostionImg.src = "img/explosion.png";
        // центрируем картинку по x
        this.x = x - shipImageSize.width / 2;
        this.y = y;
    }

    // Тупое падение без двигателя.
    move(gameOptions) 
    {
        this.speed += +gameOptions.acceleration;
        this.y += this.speed;
    }

    // Падение с включенным двигателем.
    moveEngineOn(gameOptions) 
    {
        this.speed = 
            ((gameOptions.shipWeight + gameOptions.fuelWeight) * this.speed - 
            gameOptions.impulseWeight * gameOptions.flowRate) / 
            (gameOptions.shipWeight + gameOptions.fuelWeight - gameOptions.impulseWeight);
        this.y += this.speed;
    }
}

var main = new Vue({
    el: '#main_app',
    data : {
        isGameStarted: false,
        isGameEnded: false,
        isEngineOn: false,
        isTimeOut: false,
        gameOptions: new GameOptions(1500, 500, 10, 300, 10, 1.6, 10, 600),
        ship: null,
        gameCycle: null,
        timeout: null,
        startTime: 0,
        canva: null,
        painter2d: null,
        fuelWeightBackup: 0,
        result: false,
        isGamePaused: false,
        timeDelta: 0,
    },
    mounted() {        
        this.setupCanvas();
        this.gameOptions.startDistance = this.canva.height;
        document.addEventListener("keydown", this.onArrowDown);
        document.addEventListener("keyup", this.onArrowUp);
    },
    methods: {
        startGame() {
            if(this.fuelWeightBackup == 0) {
                this.fuelWeightBackup = this.gameOptions.fuelWeight;
            }
            else {
                this.gameOptions.fuelWeight = this.fuelWeightBackup;
            }

            this.ship = new Ship(this.canva.width / 2, this.canva.height - this.gameOptions.startDistance);
            if(this.gameCycle != null) {
                clearInterval(this.gameCycle);
            }
            this.gameCycle = setInterval(() => 
            {
                this.move(); 
                this.drawScene();
            }, 30);

            if(this.timeout != null) {
                clearTimeout(this.timeout);
            }
            this.timeout = setTimeout(
                () => {
                    this.endGame();
                    this.isTimeOut = true;
                    this.result = false;
                }, this.gameOptions.timeLimit * 1000
            );

            this.startTime = (new Date()).getTime();
            this.isTimeOut = false;
            this.isGameEnded = false;
            this.isGameStarted = true;
            this.isGamePaused = false;
        },
        resetGame() {
            location.reload();
        },
        endGame() {
            if(this.timeout != null) {
                clearTimeout(this.timeout);
            }
            clearInterval(this.gameCycle);
            if(this.ship.speed > this.gameOptions.maxSpeed) {
                this.result = false;
            }
            else {
                this.result = true;
            }
            this.isGameEnded = true;
        },
        pauseGame() {
            if(this.timeout != null) {
                clearTimeout(this.timeout);
            }
            if(this.gameCycle != null) {
                clearInterval(this.gameCycle);
            }
            this.timeDelta = (new Date()).getTime();
            this.isGamePaused = true;
        },
        resumeGame() {
            this.gameCycle = setInterval(() => 
            {
                this.move(); 
                this.drawScene();
            }, 30);
            this.startTime += (new Date()).getTime() - this.timeDelta;
            this.timeout = setTimeout(
                () => {
                    this.endGame();
                    this.isTimeOut = true;
                    this.result = false;
                }, (this.gameOptions.timeLimit * 1000 - ((new Date()).getTime() - this.startTime)) 
            );

            this.isGamePaused = false;    
        },
        setupCanvas() {
            this.canva = document.getElementById('game_canvas');
            this.painter2d = this.canva.getContext('2d');
            this.canva.width = 600;
            this.canva.height = 1000;
            // this.canva.width = 435;
            // this.canva.height = 600;
        },
        drawScene() {
            this.painter2d.clearRect(0, 0, this.canva.width, this.canva.height);
            if(this.ship.y < this.canva.height) {
                this.painter2d.drawImage(this.ship.img, this.ship.x, this.ship.y, shipImageSize.width, shipImageSize.height);
                if(this.isEngineOn && !this.isGameEnded) {
                    this.painter2d.drawImage(this.ship.fireImg, this.ship.x, this.ship.y, shipImageSize.width, shipImageSize.height);
                }
                if(this.isGameEnded && !this.isTimeOut && !this.result) {
                    this.painter2d.drawImage(this.ship.explostionImg, this.ship.x - 10, this.ship.y - 10, shipImageSize.width + 10, shipImageSize.height + 10);
                }
            }
        },
        move() {
            if(this.ship.y < this.canva.height - shipImageSize.height) {
                if(this.isEngineOn && this.gameOptions.fuelWeight > 0) {
                    this.ship.moveEngineOn(this.gameOptions);
                    this.gameOptions.fuelWeight -= this.gameOptions.impulseWeight;
                }
                else {
                    this.ship.move(this.gameOptions);
                }
            }
            else {
                this.ship.y = this.canva.height - shipImageSize.height;                
                this.endGame();
            }
            
        },
        onArrowDown: function (event) {
            if(event.key.toLowerCase() == "enter") this.startGame();
            if(event.key.toLowerCase() == "r") this.resetGame();
            if(event.key.toLowerCase() == "p") {
                if(this.isGamePaused) this.resumeGame();
                else this.pauseGame();
            } 
            if(event.key == " " && this.gameOptions.fuelWeight > 0) this.isEngineOn = true;
        },
        onArrowUp: function (event) {
            this.isEngineOn = false;  
        }
    }
})

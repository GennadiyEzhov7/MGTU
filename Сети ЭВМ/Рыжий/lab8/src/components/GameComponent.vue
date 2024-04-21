<template>
    <div v-if="username == null">

    </div>
    <div id="game-container">
        <div class="control-panel">
            <div class="control-element">
                Score: {{ this.currentScore }}
            </div>
            <div class="control-element" id="message">

            </div>
            <div class="control-element">
                Timer: {{ Math.floor(this.timer / 1000 / 60).toString().padStart(2, '0') }}:{{ Math.floor(this.timer /
                        1000).toString().padStart(2, '0')
                }}
            </div>
        </div>
        <div class="area-score-container">
            <GameArea v-model:currentScore="currentScore" ref="gameArea">
            </GameArea>
            <LeaderBoard class="lead-board" v-bind:scores="scores"></LeaderBoard>
        </div>
        <div class="btn-container">
            <input type="button" id="start-button" v-on:click="startGame" value="Start game" class="m-btn m-btn-left">
            <input type="button" id="pause-button" v-on:click="pause" value="Pause" disabled class="m-btn m-btn-right">
        </div>
    </div>
</template>

<script>
import GameArea from './GameArea.vue'
import LeaderBoard from './LeaderBoard.vue';
import { dbserver } from '@/config';
export default {
    name: "GameComponent",
    components: {
        GameArea,
        LeaderBoard
    },
    props: {
        username: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            currentScore: 0,
            TIME_CONST: 10000,
            timer: 10000,
            paused: false,
            testerMode: false,
            gameStarted: false,
            scores: []
        }
    },
    methods: {
        pause: function () {
            this.paused = !this.paused;
            this.$refs.gameArea.pause(this.paused);
            let message = document.getElementById('message');

            if (this.paused) {
                document.getElementById('pause-button').value = 'Resume';
                message.innerText = 'Game paused';
            }
            else {
                document.getElementById('pause-button').value = 'Pause';
                message.innerText = 'Game started';
            }
        },
        startGame: function () {
            this.currentScore = 0;
            this.timer = this.TIME_CONST;
            let startButton = document.getElementById('start-button');
            let pauseButton = document.getElementById('pause-button');
            startButton.disabled = true;
            pauseButton.disabled = false;
            let message = document.getElementById('message');
            message.innerText = 'Game started';

            this.$refs.gameArea.newGame();

            let timerInterval = setInterval(() => {
                if (this.timer <= 0 && !this.testerMode) {
                    this.$refs.gameArea.pause(true);
                    message.innerText = 'Game over';
                    startButton.disabled = false;
                    pauseButton.disabled = true;
                    this.endGame();
                    clearInterval(timerInterval);
                } else
                    if (!this.paused)
                        this.timer -= 20;
            }, 20);
        },
        endGame: function () {
            fetch(`${dbserver}/scores?score=${this.currentScore}&username=${this.username}`, {
                method: 'POST'
            }).then((data) => {
                return data.json();
            }).then((json) => {
                this.scores = json;
            })
        }
    },
    mounted: function () {
        if (this.username == 'tester') {
            this.testerMode = true;
            document.getElementById('game-container').style = 'background-image: linear-gradient(77deg, #000000 25%, #fff700 25%, #fff700 50%, #000000 50%, #000000 75%, #fff700 75%, #fff700 100%); background-size: 80.00px 346.52px;';
        }
        //КАВОО
        window.onkeyup = function (e) {
            console.log(e.keyCode)
            if (e.keyCode == 32) {
                if (!this.gameStarted)
                    if (!this.gameStarted)
                        this.startGame();
                    else
                        this.pause();
            }
        }
    },
    created() {
        if (this.username == null || this.username === null) {
            this.$router.push({
                path: '/'
            });
        }
    }
}
</script>

<style scoped>
#game-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 25px;
}

.control-element {
    width: 100%;
    text-align: center;
}

.m-btn-left {
    border-radius: 0 0 0 10px;
}

.m-btn-right {
    border-radius: 0 0 10px 0;
}

.btn-container {
    width: 100%;
    display: flex;
    flex-direction: row;
}

.control-panel {
    width: 100%;
    display: flex;
    flex-direction: row;
}

.lead-board {
    position: absolute;
    left: 20%;
    background: rgb(207, 85, 85);
    background: linear-gradient(0deg, rgba(207, 85, 85, 1) 0%, rgba(162, 255, 87, 1) 100%);
}

.area-score-container {
    display: flex;
    flex-direction: row;
    overflow: hidden;
}
</style>
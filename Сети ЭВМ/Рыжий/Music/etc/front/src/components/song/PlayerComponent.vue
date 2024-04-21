<template>
    <div class="player-container">
        <img src="/play_btn.png" v-on:click="pause" class="play-btn-ico">
        <!-- <input type="button" value="Play" v-on:click="pause"> -->
        <div class="player-controller-container">
            <div class="player-controller-time-container">
                <div id="timeText">0:00/0:00</div>
                <input type="range" id="timeline" min="0" step="0.5" value="0" v-on:input="setTime">
            </div>
            <div class="player-controller-volume-container">
                <input type="range" id="volume" min="0" max="1" step="0.01" value="0.3" v-on:input="setVolume">
                <div id="songTitle"></div>
            </div>
            <audio ref="player" id="songPlayer" v-on:play="onPlay" v-on:error="handleError"></audio>
        </div>
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>

<script>
import MessageComponent from '../MessageComponent.vue';
export default {
    name: 'PlayerComponent',
    components: { MessageComponent },
    methods: {
        onPlay: () => {
            let calcM = function (sec) {
                let min;
                if (isNaN(sec))
                    min = 0;
                else
                    min = sec / 60;
                return Math.floor(min).toString() + ":" + Math.floor(min).toString().padStart(2, '0')
            }

            let player = document.getElementById('songPlayer');
            player.volume = document.getElementById('volume').value;
            let timeline = document.getElementById('timeline');
            let timeText = document.getElementById('timeText');
            setInterval(() => {
                timeline.max = player.duration;
                timeline.value = player.currentTime;
                timeText.innerHTML = calcM(player.currentTime) + "/" + calcM(player.duration)
            }, 1000);
        },
        setTime: (e) => {
            let player = document.getElementById('songPlayer');
            player.currentTime = e.target.value;
        },
        setVolume: (e) => {
            let player = document.getElementById('songPlayer');
            player.volume = e.target.value;
        },
        pause: () => {
            let player = document.getElementById('songPlayer');

            if (player.src == '')
                return;

            if (player.paused)
                player.play();
            else
                player.pause();
        },
        toMinutes: function (sec) {
            let minutes = Math.floor(sec / 60);
            let seconds = sec - minutes * 60
            return minutes + ":" + seconds;
        }, 
        handleError: function () {
            this.$refs.messageComp.showMessage("Only registered users can listen to music")
        }
    },
    mounted() {
        document.addEventListener('keypress', (event) => {
            if (event.code == "Space") {
                console.log("!")
                this.pause();
            }
        });
    }
}
</script>

<style>
.player-container {
    display: flex;
    align-items: left;
    min-width: 100%;
    background-color: rgba(0, 0, 0, 0.1);
    height: 10%;
    position: absolute;
    top: 90%
}

.player-controller-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 90%;
}

#timeline {
    width: 100%;
}

#timeText {
    font-size: x-large;
    margin-right: 5px;
}

#songTitle {
    width: 100%;
    font-size: x-large;
    display: flex;
    align-items: center;
    justify-content: center;
}

.player-controller-time-container {
    display: flex;
    align-items: left;
    justify-content: left;
    width: 100%;

}

.player-controller-volume-container {
    display: flex;
    align-items: left;
    justify-content: left;
    width: 100%;
}

.play-btn-ico {
    transition: all 0.2s;
    padding: 5px;
    margin-right: 5px;
}

.play-btn-ico:hover {
    transform: scale(1.1);
    cursor: pointer;
}
</style>
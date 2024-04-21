<template>
  <div class="contaiter">
    <canvas class="canvas" id="canvas" width="600" height="600">
      <p>Ваш браузер не поддерживает рисование.</p>
    </canvas>
  </div>
  <Menu :turnEngine="turnEngine" :pause="turnPause" :inputs="inputs"
        :setInputs="setInputs" :gameState="gameState" :setGameState="setGameState"
        :start="fall" :s="s" :v="v" :setStartTime="setStartTime" :gameTime="gameTime"
        :currentFuel="currentFuel"
  />
</template>

<script>
import Menu from '@/components/Menu.vue'
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Game',
  props: [],
  components: {
    Menu
  },
  data() {
    return {
      gameState: 'set',
      engine: false,
      pause: false,
      x: 265,
      y: 0,
      s: 0,
      v: 0,
      v0: 0,
      s0: 0,
      currentFuel: 0,
      currentTime: new Date().getTime(),
      startTime: 0,
      gameTime: 0,
      pauseTime: 0,
      rocketON: new Image(),
      rocketOFF: new Image(),
      pauseImg: new Image(),
      explosionImg: new Image(),
      inputs: [
        { title: "Вес аппарата, кг", value: "1500" },
        { title: "Вес топлива, кг", value: "500" },
        { title: "Вес импульса, кг", value: "10" },
        { title: "Скорость истечения, м/с", value: "300" },
        { title: "Максимальная скорость касания, м/с", value: "10" },
        { title: "Ускорение свободного падения, м/с", value: "10" },
        { title: "Лимит времени, с", value: "120" },
        { title: "Начальное расстояние, м", value: "1000" },
      ]
    }
  },
  methods: {
    setStartTime(){
      this.startTime = new Date().getTime();
    },
    setGameState(state){
      this.gameState = state;
      this.currentTime = new Date().getTime();
      if (state === 'set') {
        this.restart()
      }
    },
    setInputs(id, newValue) {
      this.inputs[id].value = newValue;
    },
    drawRocket() {
      const canvas = document.getElementById("canvas"),
            ctx = canvas.getContext("2d");

      this.engine ? ctx.drawImage(this.rocketON, this.x, this.y) : ctx.drawImage(this.rocketOFF, this.x, this.y);
    },
    fall() {
      this.currentFuel = Number(this.inputs[1].value);
      const finishY = 460;
      const koef = (this.inputs[7].value / finishY)
      const canvas = document.getElementById("canvas"),
            ctx = canvas.getContext("2d");
      const refreshInterval = setInterval(function() {
        this.disableAndActivateButtons();

        if (this.pause) {ctx.drawImage(this.pauseImg, 0, 0); this.pauseTime = new Date().getTime() - this.currentTime}

        if ((!this.pause) && (this.gameState === 'game')) {
          if (this.y < finishY) {
            this.gameTime = new Date().getTime() - this.startTime;
            if ((this.currentFuel > 0) && (this.engine)) {
              this.currentFuel -= Number(this.inputs[2].value);
              if (this.currentFuel < 0) { this.currentFuel = 0; }
              this.v = (((Number(this.inputs[0].value) + Number(this.inputs[1].value)) * this.v)
                      - (Number(this.inputs[2].value) * Number(this.inputs[3].value)))
                  / (Number(this.inputs[0].value) + Number(this.inputs[1].value) - Number(this.inputs[2].value));
              this.s = this.s0 + (new Date().getTime() -  this.currentTime) / 1000 * this.v;
              this.y = this.s / koef ;
            } else {
              if (this.engine) {
                this.engine = false;
                this.s0 = this.s;
                this.v0 = this.v;
              }
              this.v = this.v0 + (new Date().getTime() - this.currentTime)/1000 * Number(this.inputs[5].value);
              this.s = this.s0 + (new Date().getTime() - this.currentTime)/1000 * this.v;
              this.y = this.s / koef;
            }
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            this.drawRocket();
          } else {
            if (this.v <= Number(this.inputs[4].value)) {
              ctx.fillStyle = "#FFF";
              ctx.font = "30pt Calibri";
              ctx.fillText("You win!", 30, 280);
              this.setGameState('restart');
              clearInterval(refreshInterval);
            } else {
              ctx.clearRect(0, 0, canvas.width, canvas.height);
              ctx.drawImage(this.explosionImg, this.x-20, this.y);
              this.setGameState('restart');
              clearInterval(refreshInterval);
            }
          }
        }
      }.bind(this), 25)
    },
    turnEngine() {
      if (!document.getElementById('engine').classList.contains('disabled') && this.currentFuel > 0) {
        this.engine = !this.engine;
        this.v0 = this.v;
        this.s0 = this.s;
        this.currentTime = new Date().getTime();
        const canvas = document.getElementById("canvas"),
            ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        this.drawRocket();
      }
    },
    turnPause() {
      if (!document.getElementById('pause').classList.contains('disabled')) {
        this.pause = !this.pause;
        this.v0 = this.v;
        this.s0 = this.s;
        this.currentTime = new Date().getTime();
        if (!this.pause) { this.startTime += this.pauseTime; }
      }
    },
    disableAndActivateButtons() {
      if ((this.y > 460) || (this.pause) || (this.gameState === 'set')) {
        const engineButton = document.getElementById('engine');
        engineButton.classList.add('disabled')
      } else {
        const engineButton = document.getElementById('engine');
        engineButton.classList.remove('disabled')
      }
      if ((this.y > 460) || (this.gameState === 'set')) {
        const pauseButton = document.getElementById('pause');
        pauseButton.classList.add('disabled');
      } else {
        const pauseButton = document.getElementById('pause');
        pauseButton.classList.remove('disabled');
      }
    },
    restart() {
      this.v = 0;
      this.v0= 0;
      this.s = 0;
      this.s0 = 0;
      this.y = 0;
      const canvas = document.getElementById("canvas"),
          ctx = canvas.getContext("2d");
      ctx.clearRect(0, 0, canvas.width, canvas.height);
    }
  },
  mounted() {
    this.pauseImg.src='./img/pause.png';
    this.rocketON.src = './img/engineOn.png';
    this.rocketOFF.src = './img/engineOff.png';
    this.explosionImg.src = './img/explosion.png';
  },
}
</script>

<style scoped>
.canvas {
  background: url('/public/img/background.jpg') no-repeat;
  width: 600px;
  height: 600px;
}
</style>

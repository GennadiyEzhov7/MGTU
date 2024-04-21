<template>
  <div class="menu">
    <InputMenu :inputs="inputs" :setInputs="setInputs" :gameState="gameState" />
    <div v-if="(gameState === 'set')" class="form">
      <button class="button" id="start" type="button" @click="setGameState('game'); start(); setStartTime()">Поехали</button>
    </div>
    <div v-if="(gameState === 'restart')" class="form">
      <button class="button" id="restart" type="button" @click="setGameState('set')">Сначала</button>
    </div>
    <div v-if="(gameState === 'game')" class="currentVars">
      <div class = "var-container">
        <div class="title">
          Текущая скорость, м/с
        </div>
        <div class="value">
          {{ Math.abs(v.toFixed(2)) }}
        </div>
      </div>
      <div class="var-container">
        <div class="title">
          Расстояние, м
        </div>
        <div class="value">
          {{ (inputs[7].value - s).toFixed(2) }}
        </div>
      </div>
      <div class="var-container">
        <div class="title">
          Время, с
        </div>
        <div class="value">
          {{ gameTime / 1000 }}
        </div>
      </div>
      <div class="var-container">
        <div class="title">
          Оставшееся топливо, кг
        </div>
        <div class="value">
          {{ currentFuel }}
        </div>
      </div>
    </div>
    <div class="buttons-container">
      <button class="button" id="engine" type="button" @click="turnEngine">Двигатель(Space)</button>
      <button class="button" id="pause" type="button" @click="pause">Пауза(P)</button>
    </div>
  </div>
</template>

<script>
import InputMenu from "@/components/InputMenu";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Menu',
  components: {
    InputMenu
  },
  props: ['turnEngine', 'pause', 'inputs', 'setInputs', 'gameState', 'setGameState', 'start', 's', 'v', 'setStartTime', 'gameTime', 'currentFuel'],
  mounted() {
    document.addEventListener('keydown', function(event) {
      if ((event.code === "Space") && (!document.getElementById('engine').classList.contains('disabled'))) {
        this.turnEngine();
      }
      if ((event.code === "Space") && (this.gameState === 'set')) {
        this.setGameState('game');
        this.start();
        this.setStartTime();
      }
      if ((event.code === "Space") && (this.gameState === 'restart')) {
        this.setGameState('set');
      }
      if ((event.code === "KeyP") && (!document.getElementById('pause').classList.contains('disabled'))) {
        this.pause()
      }
    }.bind(this));
  }
}
</script>

<style scoped>
  .menu {
    display: flex;
    flex-direction: column;
  }
  .buttons-container {
    display: flex;
  }
  .button {
    width: 140px;
    height: 50px;
    margin: 0 0 10px 10px;
    font-size: 14px;
    border: 2px solid white;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.1);
    color: white;
    cursor: pointer;
  }
  .button:hover {
    transform: scale(1.05);
    transition: transform .3s;
  }

  .disabled {
    cursor: auto;
    background-color: transparent;
    opacity: 0.8;
  }

  .disabled:hover{
    transform: scale(1);
  }

  .form {
    margin-top: 25px;
    text-align: center;
  }

  .var-container {
    display: flex;
  }

  .value {
    margin-left: 10px;
  }

  .currentVars {
    padding: 10px;
  }
</style>

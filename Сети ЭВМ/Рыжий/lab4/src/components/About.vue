<template>
  <div class="rules-container">
    <div class="rules">
      <h1>Правила игры</h1><br>
      Необходимо посадить ракету, используя двигатель (кнопка пробел). <br>
      Если хочется поиграться с настройками - они в находятся в меню справа.
      <h2>Удачи!</h2>
    </div>
    <router-link class="button" to="/game">Продолжить</router-link>
  </div>
</template>

<script>
export default {
  //eslint-disable-next-line vue/multi-word-component-names
  name: 'About',
  props: [],
  mounted() {
    document.addEventListener('keydown', function(event) {
      if (event.code === "Space") {
        this.$router.push('/game');
      }
    }.bind(this));
  }
}
</script>

<style scoped>
  .rules-container {
    width: 960px;
    height: 600px;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: #fff;
    background: url('/public/img/back.jpeg');
  }

  .rules {
    margin-top: 50px;
    padding: 24px 48px;
    max-width: 600px;
    font-size: 24px;
  }

  .button {
    width: 140px;
    height: 50px;
    margin: 20px 20px;
    font-size: 24px;
    padding: 20px 24px 0 24px;
    border: 2px solid white;
    border-radius: 5px;
    background-color: transparent;
    color: white;
    cursor: pointer;
    text-decoration: none;
  }

  .button:hover {
    transform: scale(1.05);
    transition: transform .3s;
  }
</style>

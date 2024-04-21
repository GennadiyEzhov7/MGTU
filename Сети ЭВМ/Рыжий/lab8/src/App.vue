<template>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
@import './assets/css/global.css';

#app {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgb(255, 85, 85);
  background: linear-gradient(90deg, rgba(255, 85, 85, 0.9149860627844888) 0%, rgba(252, 176, 69, 1) 49%, rgba(187, 131, 224, 0.8925770991990546) 100%);
  width: 100%;
  height: 100%;
}
</style>

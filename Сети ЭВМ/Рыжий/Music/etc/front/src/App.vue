<template>
  <NavBar></NavBar>
  <router-view></router-view>
  <PlayerComponent v-show="$route.path != '/register' && $route.path != '/authorize'"></PlayerComponent>
</template>

<script>
import NavBar from '@/components/navBar/NavBar.vue'
import PlayerComponent from '@/components/song/PlayerComponent.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    PlayerComponent
  }
}
</script>

<style>
html,
body {
  height: 100%;
  overflow: hidden;
  color: #2c3e50;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  height: 100%;
  color: #2c3e50;
  background-blend-mode: screen;
	background:
		linear-gradient(limegreen, transparent),
		linear-gradient(90deg, skyblue, transparent),
		linear-gradient(-90deg, coral, transparent);
}
</style>

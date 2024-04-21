<template>
  <router-view></router-view>
</template>

<script>

export default {
  name: 'App',
}

</script>

<style>
html, body {
  background-color: black;
}
body {
  margin: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin-right: 0 !important;
  display: flex;
  justify-content: center;
  color: #4e4e4e;
  margin-top: 10px;
  user-select: none;
  overflow: hidden;
}

@media (max-height: 640px) {
  #app {
    margin-top: 0;
  }
}

</style>

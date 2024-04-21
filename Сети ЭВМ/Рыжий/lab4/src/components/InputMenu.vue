<template>
  <div class="startValues">
    <div class="value" v-for="(item, index) in inputs" :key="index">
      <div class="title">{{ item.title }}</div>
      <input v-if="(gameState === 'set')" :value="item.value" @input="onInput($event); setInputs(index, $event.target.value)">
      <input v-if="(gameState === 'game') || (gameState === 'restart')" style="opacity: 0.2" readonly type="number" :value="item.value">
    </div>
  </div>
</template>

<script>
export default {
  name: 'InputMenu',
  props: ['inputs', 'setInputs', 'gameState'],
  methods:{
    onInput(event) {
      event.target.value = event.target.value.replace(/[^0-9.]/g, '')
    }
  }
}
</script>

<style scoped>
  input {
    min-width: 200px;
    font-size: 14px;
    color: white;
    border-style: solid;
    background-color: rgba(255, 255, 255, 0.1);
  }
  .startValues {
    margin-left: 10px;
    padding: 10px 10px;
    border: 2px solid #c9c9c9;
  }
  .value {
    padding-top: 5px;
  }
  .divider {
    border-bottom: 1px dashed #c9c9c9;
    margin-top: 5px;
  }
</style>

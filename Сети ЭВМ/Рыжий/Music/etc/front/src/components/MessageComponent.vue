<template>
    <div class="message-container" ref="messageContainer" v-on:click="hideMessage">
        <div class="message-div" ref="message">
            Hellooo
        </div>
    </div>
</template>

<script>
export default {
    name: 'MessageComponent',
    methods: {
        hideMessage: function() {
            this.$refs.messageContainer.style.display = 'none';
        },
        showMessage: function(text) {
            this.$refs.messageContainer.style.display = 'flex';
            this.$refs.message.innerText = text;
        }
    }

}
</script>

<style>
.message-container {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    cursor: pointer;
    display: none;
    align-items: center;
    justify-content: center;
}

.message-div {
    width: 30%;
    height: 30%;
    background-color: antiquewhite;
    font-size: x-large;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>
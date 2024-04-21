<template>
    <div class="main-container">
        <h1>Рыбки</h1>
        <div class="inp-container">
            <h3>Введите ваше имя</h3>
            <input type="text" v-model="username" v-on:input="checkUsername($event)" class="name-input"
                placeholder="Введите имя" maxlength="15">
            <button id="playButton" @click="$router.push({ path: 'game', query: { username: username } })"
                class="m-btn">Play</button>
        </div>
        <h2>Правила</h2>
        <div class="line"></div>
        <h3>Кликаем на рыбок, получаем удовольствие</h3>
    </div>
</template>

<script>
export default {
    name: "AboutComponent",
    data() {
        return {
            username: "qwerty"
        }
    },
    methods: {
        checkUsername: function (e) {
            let playButton = document.getElementById("playButton");
            if (e.target.value)
                playButton.disabled = false;
            else
                playButton.disabled = true;
        }
    }

}
</script>

<style scoped>
.name-input::placeholder {
    color: red;
}

h1 {
    font-size: 60px;
    margin-bottom: 10%;
}

.inp-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-bottom: 15%;
}


.line {
    border-bottom: 1px solid white;
    width: 70%;
}

.name-input {
    font-size: 20px;
    margin-bottom: 5%;
    color: white;
    background-color: rgba(0, 0, 0, 0);
    border-width: 1px;
    border-style: solid;
    border-radius: 3px;
    transition: all 1s ease;
}

.name-input:placeholder-shown {
    color: rgb(255, 0, 0);
    border-color: rgb(255, 0, 0);
}

.name-input:focus {
    outline: none;
}

.main-container {
    color: white;
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
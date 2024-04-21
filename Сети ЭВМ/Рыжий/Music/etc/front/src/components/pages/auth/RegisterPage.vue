<template>
    <div class="register-page-container">
        Login
        <input type="text" class="my-input" id="login" value="qwerty"> <br>
        Password:
        <input type="password" class="my-input" id="password" value="qwerty"> <br>
        Repeat password
        <input type="password" class="my-input" id="repeatPassword"> <br>
        Name
        <input type="text" class="my-input" id="name" value="qwerty"> <br>
        Age
        <input type="number" class="my-input" id="age" value="22"> <br>
        Gender
        <select name="gender" class="my-input" id="gender">
            <option value="Male" selected>Male</option>
            <option value="Female">Female</option>
        </select> <br>
        <input type="button" class="my-button" value="Register" v-on:click="doRegister">
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>

<script>
import callApi from '@/apiCaller';
import MessageComponent from '@/components/MessageComponent.vue';
export default {
    name: 'RegisterPage',
    components: {
        MessageComponent
    },
    methods: {
        doRegister: function () {
            let login = document.getElementById('login').value;
            let password = document.getElementById('password').value;
            let rPassword = document.getElementById('repeatPassword').value;
            let name = document.getElementById('name').value;
            let age = parseInt(document.getElementById('age').value)
            let gender = document.getElementById('gender').value

            if (login === "" || password === "" || name === "") {
                this.$refs.messageComp.showMessage("Login password, name and age must not be empty");
            }

            if (password != rPassword) {
                this.$refs.messageComp.showMessage("Passwords are not the same");
                return;
            }

            callApi(
                this.$router,
                '/api/register',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        login: login,
                        password: password,
                        gender: gender,
                        age: age,
                        name: name
                    })
                },
                (status, data) => {
                    if (status != 200) {
                        this.$refs.messageComp.showMessage(data.message);
                        return;
                    } else {
                        this.$router.push('authorize');
                    }
                }
            )
        }
    }
}
</script>

<style>
.register-page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}
</style>
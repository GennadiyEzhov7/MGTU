<template>
    <div class="login-page-container">
        Login
        <input type="text" class="my-input login-inp" id="login">
        Password
        <input type="password" class="my-input" id="password">
        <input type="button" class="my-button" value="Authorize" v-on:click="doAuthorization">
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>
<script>
import callApi from '@/apiCaller';
import MessageComponent from '@/components/MessageComponent.vue'
export default {
    name: 'AuthorizationPage',
    components: {
        MessageComponent
    },
    methods: {
        doAuthorization: function () {
            let login = document.getElementById('login').value;
            let password = document.getElementById('password').value;

            if (login === "" || password === "") {
                this.$refs.messageComp.showMessage("Login or password must not be empty");
                return;
            }

            let urlParams = new URLSearchParams({
                login: login,
                password: password,
            });

            callApi(
                this.$router,
                '/api/authorize?' + urlParams,
                {
                    method: 'POST'
                },
                (status, data) => {
                    if (status == 403) {
                        this.$refs.messageComp.showMessage("Неверный логин или пароль");
                        return;
                    } else if (status != 200) {
                        this.$refs.messageComp.showMessage(data.message);
                        return;
                    }
                    else {
                        window.localStorage.setItem('authToken', data.token);
                        window.location = "/"
                    }
                }
            );
        }
    }
}
</script>

<style>
.login-page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}
</style>
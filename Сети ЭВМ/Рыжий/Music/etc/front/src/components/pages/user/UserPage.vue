<template>
    <div>
        <div class="info-container">
            <div ref="name">

            </div>

            <div id="songs" ref="songs">

            </div>
        </div>
    </div>
</template>

<script>
import callApi from '@/apiCaller';
import SongComponent from '@/components/song/SongComponent';
import { defineComponent, createApp } from 'vue'

export default {
    name: 'UserPage',
    mounted() {
        this.loadPage();
    },
    methods: {
        loadPage: function() {
            callApi(
            this.$router,
            '/api/user/',
            {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + window.localStorage.getItem('authToken')
                }
            },
            (status, data) => {
                console.log(data)
                if (status == 401 || status == 403)
                    this.$router.push('/');
                else {
                    let user = data.user;
                    this.$refs.name.innerText = user.name;

                    let songsDiv = document.getElementById("songs");
                    let f  = this.loadPage;
                    data.songs.forEach(song => {
                        let songComp = defineComponent({
                            extends: SongComponent,
                            data() {
                                return {
                                    song: song
                                }
                            },
                            methods: {
                                callRefresh: function () {
                                    console.log("HELLOOOS")
                                    songsDiv.innerHTML = '';
                                    f();
                                }
                            }
                        });
                        let songDiv = document.createElement('div');
                        createApp(songComp).mount(songDiv);
                        songsDiv.appendChild(songDiv);
                    });
                }
            }
        );
        }
    }
}
</script>

<style>
.info-container {
    font-size: x-large;
}
</style>
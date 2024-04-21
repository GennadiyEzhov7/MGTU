<template>
    <div class="find-songs-page-container">
        Song name
        <input type="text" class="my-input" id="songName" ref="songName">
        <input type="button" class="my-button" value="Find" v-on:click="findSong">
        <div ref="songs">
            Find whatever u want!
        </div>
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>

<script>
import callApi from '@/apiCaller'
import SongComponent from '@/components/song/SongComponent';
import MessageComponent from '@/components/MessageComponent.vue';
import { defineComponent, createApp } from 'vue'

export default {
    components: {
        MessageComponent
    },
    methods: {
        findSong: function () {
            let songName = this.$refs.songName.value;
            if (songName === "")
                return;

            callApi(
                this.$router,
                '/api/songs/' + songName,
                {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + window.localStorage.getItem('authToken')
                    }
                },
                (status, data) => {
                    if (status != 200) {
                        this.$refs.messageComp.showMessage(data.message);
                        return;
                    }

                    let songsDiv = this.$refs.songs;
                    songsDiv.innerHTML = '';

                    if (data.songs.length == 0) {
                        songsDiv.innerText = 'Nothing was found((';
                        return;
                    }
                    
                    let routr = this.$router;
                    data.songs.forEach(song => {
                        let songComp = defineComponent({
                            extends: SongComponent,
                            data() {
                                return {
                                    song: song
                                }
                            },
                            methods: {
                                goToArtist: function (e) {
                                    e.stopPropagation();
                                    routr.push("/artist/" + song.artistId);
                                }
                            }
                        });
                        let songDiv = document.createElement('div');
                        createApp(songComp).mount(songDiv);
                        songsDiv.appendChild(songDiv);
                    });
                }
            );
        }
    }
}
</script>

<style>
.my-button {
    margin: 1% 0;
    width: 20%;
    background-color: rgba(0, 0, 0, 0.2);
    border: solid 1px black;
    transition: background-color 0.2s;
}

.my-input {
    margin-bottom: 1%;
    background-color: rgba(0, 0, 0, 0.2);
    border: solid 1px black;
}

.my-button:hover {
    background-color: rgba(0, 0, 0, 0.3);
}

.find-songs-page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}

#songName {
    width: 30%;
}
</style>
<template>
    <div class="song-container" v-on:click="playSong">
        <div class="heart" v-on:click="likeSong">
            <div v-if="this.song.liked">❤️</div>
            <div v-else>🖤</div>
        </div>
        <a class="artist-name" ref="artistName" href="#" v-on:click="goToArtist">{{ this.song.artistName }}</a>
        <div class="song-name" ref="songName">{{ this.song.name }}</div>
        <div class="song-name" ref="songGenre">{{ this.song.genre }}</div>
        <MessageComponent ref="messageComp"></MessageComponent>
    </div>
</template>

<script>

import callApi from '@/apiCaller'
import MessageComponent from '../MessageComponent.vue';

export default {
    name: "SongComponent",
    methods: {
        playSong: function () {
            document.getElementById("songPlayer").src = this.song.fileName + '?token=' + window.localStorage.getItem("authToken");
            document.getElementById("songPlayer").play();
            document.getElementById("songTitle").innerHTML = "";
            document.getElementById("songTitle").appendChild(this.$refs.artistName.cloneNode(true));
            document.getElementById("songTitle").appendChild(this.$refs.songName.cloneNode(true));  
        },
        stopSong: function () {
            this.$refs.player.pause();
        },
        handleError: function (e) {
            console.log(e);
        },
        likeSong: function (e) {
            e.stopPropagation();
            let path;
            let t = !this.song.liked;
            if (t)
                path = "/api/like/" + this.song.id;
            else
                path = "/api/unlike/" + this.song.id;
            callApi(this.$router, path, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + window.localStorage.getItem("authToken")
                }
            }, (code) => {
                if (code == 200) {
                    this.song.liked = !this.song.liked;
                    if (this.callRefresh  != undefined)
                        this.callRefresh()
                }
                else if (code == 403){
                    this.$refs.messageComp.showMessage("Only registered users can save music");
                    return;
                }
            });
        }
    },
    mounted: () => {
    },
    components: { MessageComponent }
}
</script>

<style>
.song-container {
    display: flex;
    flex-direction: row;
    margin-bottom: 10px;
    align-items: center;
    font-size: x-large;
    padding: 5px;
    background-color: rgba(0, 0, 0, 0.1);
    width: 100%;
    transition: all 0.2s;

}

.song-container:hover {
    background-color: rgba(0, 0, 0, 0.2);
    cursor: pointer;
}

.song-name::before {
    content: "-  ";
}

.song-name {
    margin-right: 10px;
}

.heart {
    align-self: right;
    margin-right: 10px;
}

.heart:hover {
    cursor: pointer;
}
</style>
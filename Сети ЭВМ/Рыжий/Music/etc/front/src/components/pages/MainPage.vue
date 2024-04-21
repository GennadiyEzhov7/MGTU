<template>
    <div class="main-page-container">
        <div id="artists" ref="artists" class="artists">
        </div>
        <p class="playlist-title">Playlist, created only for you &#9825;</p>
        <div id="songs" ref="songs">
        </div>
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>

<script>
import callApi from '@/apiCaller'
import SongComponent from '@/components/song/SongComponent';
import MessageComponent from '@/components/MessageComponent';
import { defineComponent, createApp } from 'vue'

export default {
    name: "MainPage",
    components: {
        MessageComponent
    },
    mounted() {
        callApi(this.$router, "/api/artists", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + window.localStorage.getItem("authToken")
            }
        }, (status, data) => {

            if (status != 200) {
                this.$refs.messageComp.showMessage(data.message);
                return;
            }

            let artistsDiv = document.getElementById("artists");
            data.artists.forEach(artist => {
                let artistA = document.createElement("a");
                let artistPic = document.createElement("img");
                let artistName = document.createElement("p");
                artistA.appendChild(artistPic);
                artistA.appendChild(artistName);
                artistPic.className = "artist-pic";
                artistName.className = "artist-name";
                artistA.className = "artist-container";
                artistPic.src = artist.imagePath;
                artistA.onclick = () => {
                    this.$router.push("/artist/" + artist.id);
                };
                artistName.innerText = artist.name;
                artistsDiv.appendChild(artistA);
            });
        });
        callApi(this.$router, "/api/songs", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + window.localStorage.getItem("authToken")
            }
        }, (status, data) => {

            if (status != 200) {
                this.$refs.messageComp.showMessage(data.message);
                return;
            }

            let songsDiv = document.getElementById("songs");
            console.log(data);
            let routr = this.$router;
            data.songs.forEach(song => {
                let songComp = defineComponent({
                    extends: SongComponent,
                    data() {
                        return {
                            song: song
                        };
                    },
                    methods: {
                        goToArtist: function (e) {
                            e.stopPropagation();
                            routr.push("/artist/" + song.artistId);
                        }
                    }
                });
                let songDiv = document.createElement("div");
                createApp(songComp).mount(songDiv);
                songsDiv.appendChild(songDiv);
            });
        });
    }
}
</script>

<style>
.playlist-title {
    font-size: x-large;
}

.artists {
    display: flex;
    flex-direction: row;
    justify-content: center;
}

.artist-pic {
    border-radius: 50%;
    max-width: 200px;
    max-height: 200px;
    min-width: 50px;
    min-height: 50px;
    transition: all .2s;
    box-shadow: 5px 5px 5px 5px rgba(34, 60, 80, 0.3);
}

.artist-pic:hover {
    transform: scale(1.5);
    cursor: pointer;
    box-shadow: 8px 8px 8px 8px rgba(34, 60, 80, 0.3);
}

.artist-name {
    text-decoration: none;
    color: black;
}

.artist-container {
    margin: 0px 5px;
    text-decoration: none;
    font-size: large;
}

.main-page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
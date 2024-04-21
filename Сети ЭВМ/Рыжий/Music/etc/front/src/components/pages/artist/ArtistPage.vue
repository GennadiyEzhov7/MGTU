<template>
    <div>
        <div class="info-container">
            <div class="artist-pic-container">
                <img src="" class="artist-pic" ref="artistPic">
            </div>
            <div class="artist-description-container">
                <p class="artist-description-p" id="artistName" ref="artistName">
                </p>
                <p class="artist-description-p" ref="artistDescription">
                </p>
            </div>
            <div id="songs">

            </div>
        </div>
    </div>
</template>

<script>
import callApi from '@/apiCaller';
import SongComponent from '@/components/song/SongComponent';
import { defineComponent, createApp } from 'vue'

export default {
    name: 'ArtistPage',
    props: {
        id: {
            type: Number
        }
    },
    mounted() {
        let artistId = this.$route.params.id;

        if (!/(-?(\d+\.\d+)|(\d+))/.test(artistId)) {
            this.$router.push('/404');
            return;
        }

        callApi(
            this.$router,
            '/api/artist/' + artistId,
            {
                method: 'GET',
                headers: {
                    'Authorization' : 'Bearer ' + window.localStorage.getItem('authToken')
                }
            },
            (status, data) => {
                console.log(status)
                if (status == 404 || status == 400)
                    this.$router.push('/404');
                else {
                    let artist = data.artists[0];
                    this.$refs.artistPic.src = artist.imagePath;
                    this.$refs.artistDescription.innerText = artist.description;
                    this.$refs.artistName.innerText = artist.name;

                    let songsDiv = document.getElementById("songs");
                    artist.songs.forEach(song => {
                        let songComp = defineComponent({
                            extends: SongComponent,
                            data() {
                                return {
                                    song: song
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
</script>

<style>
.artist-pic {
    border-radius: 50%;
    min-width: 200px;
    min-height: 200px;
    max-width: 200px;
    max-height: 200px;
}

.artist-pic-container {
    background-color: black;
    min-width: 200px;
    min-height: 200px;
    max-width: 200px;
    max-height: 200px;
    border-radius: 50%;
}

.info-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.artist-description-container {
    text-align: center;
    display: flex;
    align-items: center;
    flex-direction: column;
}

.artist-description-p {
    max-width: 40%;
}

#artistName {
    font-size: xx-large;
}
</style>
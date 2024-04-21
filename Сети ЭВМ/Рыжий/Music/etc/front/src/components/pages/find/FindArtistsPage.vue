<template>
    <div class="find-artists-page-container">
        Artist name
        <input type="text" class="my-input" id="artistName" ref="artistName">
        <input type="button" class="my-button" value="Find" v-on:click="findArtist">
        <div ref="artists">
            Find whatever u want!
        </div>
    </div>
</template>

<script>
import callApi from '@/apiCaller'

export default {
    mounted() {

    },
    methods: {
        findArtist: function () {
            let artistName = this.$refs.artistName.value;
            if (artistName === "")
                return;

            callApi(
                this.$router,
                '/api/artists/' + artistName,
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

                    let artistsDiv = this.$refs.artists;
                    artistsDiv.innerHTML = '';

                    if (data.artists.length == 0) {
                        artistsDiv.innerText = 'Nothing was found((';
                        return;
                    }

                    data.artists.forEach(artist => {
                        let artistA = document.createElement('a');
                        let artistPic = document.createElement('img');
                        let artistNameDiv = document.createElement('div');

                        artistA.appendChild(artistPic);
                        artistA.appendChild(artistNameDiv);
                        artistA.href = '/artist/' + artist.id;

                        artistPic.className = 'artist-pic';
                        artistPic.src = artist.imagePath;
                        artistNameDiv.innerText = artist.name;

                        artistsDiv.appendChild(artistA);
                    });
                }
            );
        }
    }
}
</script>

<style>
.find-artists-page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}

#artistName {
    width: 30%;
    text-decoration: none;
}
</style>
<template>
    <div class="create-song-container">
        Name
        <input type="text" class="my-input" ref="songName" value="test">
        Genre
        <input type="text" class="my-input" ref="genre" value="test">
        ArtistId
        <input type="number" class="my-input" ref="artistId" value="1">
        File
        <input type="file" class="my-input" ref="songFile" accept="audio/mpeg">
        <input type="button" class="my-button" value="Upload" ref="file" v-on:click="upload">
    </div>
    <MessageComponent ref="messageComp"></MessageComponent>
</template>

<script>
import callApi from '@/apiCaller';
import MessageComponent from '@/components/MessageComponent.vue';

export default {
    methods: {
        upload: function () {
            let formData = new FormData();
            let songReqBody = {
                name: this.$refs.songName.value,
                genre: this.$refs.genre.value,
                artistId: parseInt(this.$refs.artistId.value),
            };

            /* if (songReqBody.name === "" || songReqBody.genre === "" || artistId === "" || this.$refs.songFile == undefined)
                messageComp("Invalid input data!"); */

            formData.append("file", this.$refs.songFile.files[0]);
            formData.append("songData", new Blob([JSON.stringify(songReqBody)], {
                type: "application/json"
            }));
            callApi(this.$router, "/api/create/song", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + window.localStorage.getItem("authToken")
                },
                body: formData
            }, (code, data) => {
                console.log(code, data);
            });
        }
    },
    mounted() {
        console.log("mounted");
        this.$refs.file.addEventListener("change", (e) => {
            console.log(e.target.files);
        }, false);
    },
    components: { MessageComponent }
}
</script>

<style>
.create-song-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}
</style>
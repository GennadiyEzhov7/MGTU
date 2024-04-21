<template>
    <div class="create-artist-container">
        Name
        <input type="text" class="my-input" ref="artistName" value="testArtist">
        Description
        <input type="text" class="my-input" ref="description" value="testAtrist">
        Picture
        <input type="file" class="my-input" ref="imageFile" accept="image/jpeg">
        <input type="button" class="my-button" value="Upload" ref="file" v-on:click="upload">
    </div>
</template>

<script>
import callApi from '@/apiCaller';

export default {
    methods: {
        upload: function () {
            let formData = new FormData();

            let artistReqBody = {
                name: this.$refs.artistName.value,
                description: this.$refs.description.value
            }

            formData.append('file', this.$refs.imageFile.files[0]);
            formData.append('artistData', new Blob([JSON.stringify(artistReqBody)], {
                type: "application/json"
            }));

            callApi(
                this.$router,
                '/api/create/artist',
                {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + window.localStorage.getItem('authToken')
                    },
                    body: formData
                },
                (code, data) => {
                    console.log(code, data);
                }
            );
        }
    },
    mounted() {
        console.log('mounted')
        this.$refs.file.addEventListener('change', (e) => {
            console.log(e.target.files)
        }, false);
    }

}
</script>

<style>
.create-artist-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: x-large;
}
</style>
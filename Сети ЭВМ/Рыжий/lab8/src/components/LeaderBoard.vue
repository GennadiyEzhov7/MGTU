<template>
    <div class="board-frame">
        <div v-for="score in scores" v-bind:key="score.place">
            <div v-bind:class = "(score.me == true) ? 'score my-score' : 'score other-score'">
                {{ score.username }}: {{ score.score }}
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        scores: {
            type: Array
        }
    }
}
</script>

<style scoped>
.score {
    text-align: left;
    border-style: solid;
    border-radius: 1px;
    border-color: white;
    background-color: transparent;
}

.my-score {
    color: gray;
}

.other-score {
    color: white;
}

.board-frame {
    display: flex;
    flex-direction: column;
}
</style>
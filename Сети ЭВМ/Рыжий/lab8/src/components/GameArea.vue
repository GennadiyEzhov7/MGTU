<template>
    <div id="main-frame">
    </div>
    <img src="@/assets/fish1.png" class="hidden-image" id="fish1">
    <img src="@/assets/fish2.png" class="hidden-image" id="fish2">
    <img src="@/assets/fish3.png" class="hidden-image" id="fish3">
</template>

<script>
export default {
    name: "GameArea",
    props: {
        currentScore: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            fishCount: 0,
            MAX_FISH_COUNT: 10,
            MIN_FISH_COUNT: 3,
            MAX_LIFETIME: 10000,
            MIN_LIFETIME: 5000,
            paused: true,
            fishes: [
                {
                    id: "fish1",
                    score: 10,
                    className: "easy-fish",
                    frames: ["easy1", "easy2", "easy3"]
                },
                {
                    id: "fish2",
                    score: 20,
                    className: "medium-fish",
                    frames: ["medium1", "medium2", "medium3"]
                },
                {
                    id: "fish3",
                    score: 30,
                    className: "hard-fish",
                    frames: ["hard1", "hard2", "hard3"]
                }
            ]
        };
    },
    methods: {
        addFish: function () {
            let mainFrame = document.getElementById("main-frame");
            let fishType = this.fishes[this.randType()];
            let framesType = fishType.frames[this.randType()];
            let fishImg = document.getElementById(fishType.id).cloneNode();
            fishImg.className = `fish-img ${fishType.className}`;
            let fishInterval;
            let fishLifetime = Math.floor((Math.random() * this.MAX_LIFETIME) + this.MIN_LIFETIME);
            fishImg.onload = () => {
                fishImg.style.animationName = framesType;
                fishImg.style.animationDuration = `${Math.floor(fishLifetime) / 1000}s`;
                fishImg.style.animationTimingFunction = this.randCubic();
                fishImg.onclick = (event) => {
                    if (!this.paused) {
                        this.removeFish(fishImg, fishInterval);
                        this.$emit("update:currentScore", this.currentScore + fishType.score);
                        event.stopPropagation(); //Не даем кликнуться
                    }
                };
                mainFrame.appendChild(fishImg);
                fishInterval = setInterval(() => {
                    if (!this.paused) {
                        if (fishLifetime < 0)
                            this.removeFish(fishImg, fishInterval);
                        else
                            fishLifetime -= 50;
                    }
                }, 50);
            };
        },
        removeFish: function (fish, interval) {
            document.getElementById("main-frame").removeChild(fish);
            clearInterval(interval);
            this.fishCount--;
        },
        pause: function (paused) {
            let fishNodes = document.getElementById("main-frame").childNodes;
            this.paused = paused;
            if (paused) {
                fishNodes.forEach((fishNode) => {
                    fishNode.style.animationPlayState = "paused";
                });
            }
            else {
                fishNodes.forEach((fishNode) => {
                    fishNode.style.animationPlayState = "running";
                });
            }
        },
        newGame: function () {
            this.fishCount = 0;
            document.getElementById("main-frame").innerHTML = "";
            this.paused = false;
        },
        randType: function () {
            return Math.floor(Math.random() * 3);
        },
        randCubic: function () {
            return `cubic-bezier(${this.randCubicVals()}, ${this.randCubicVals()}, ${this.randCubicVals()}, ${this.randCubicVals()})`;
        },
        randCubicVals: function () {
            return ((Math.random() * 0.89) + 0.1);
        }
    },
    mounted() {
        setInterval(() => {
            if (this.fishCount < this.MAX_FISH_COUNT && !this.paused) {
                let spawn = true;
                if (this.fishCount > this.MIN_FISH_COUNT)
                    spawn = Math.random() < 0.5;
                if (spawn) {
                    this.addFish();
                    this.fishCount++;
                }
            }
        }, 500);
    }
}
</script>

<style>
#main-frame {
    display: inline-block;
    position: relative;
    width: 600px;
    height: 600px;
    max-width: 600px;
    max-height: 600px;
    background: linear-gradient(3deg, rgba(2, 0, 36, 1) 0%, rgba(9, 9, 121, 1) 11%, rgba(0, 212, 255, 1) 100%);
    overflow: hidden;
    border-radius: 10px 10px 0 0;
}

.hidden-image {
    display: none;
}

.fish-img {
    position: absolute;
    top: -200px;
    left: -200px;
    /* animation-name: easy1; */
    /* animation-duration: 3s; */
    /* animation-timing-function: cubic-bezier(0.17, 1, 0.92, 0.25); */
    /* animation: easy1 3s cubic-bezier(.17, 1, .92, .25) infinite; */
    animation-iteration-count: 1;
}

/*Чтобы выдление было невидимым*/
.fish-img::selection {
    background: rgba(255, 255, 255, 0);
}

.easy-fish {
    max-width: 150px;
}

.medium-fish {
    max-width: 125px;
}

.hard-fish {
    max-width: 100px;
}

@keyframes easy1 {
    0% {
        top: -150px;
        left: -150px;

    }

    100% {
        top: 750px;
        left: 750px;
        rotate: 45deg;
    }
}

@keyframes easy2 {
    0% {
        top: 0px;
        left: 750px;
        transform: scaleX(-1);
    }

    100% {
        top: 400px;
        left: -150px;
        transform: scaleX(-1);
        rotate: -10deg;
    }
}

@keyframes easy3 {
    0% {
        top: -150px;
        left: 300px;
        transform: scaleY(-1);
        rotate: 90deg;
    }

    100% {
        top: 300px;
        left: -150px;
        transform: scaleY(-1);
        rotate: 135deg;
    }
}

@keyframes medium1 {
    0% {
        top: 650px;
        left: 500px;
        rotate: -130deg;
    }

    50% {
        top: 200px;
        left: 200px;
        rotate: -90deg;
    }

    100% {
        top: -150px;
        left: 300px;
        rotate: -60deg;
    }
}

@keyframes medium2 {
    0% {
        top: -150px;
        left: 600px;
        transform: scaleY(-1);
        rotate: 90deg;
    }

    50% {
        top: 350px;
        left: 500px;
        transform: scaleY(-1);
        rotate: 100deg;
    }

    100% {
        top: 750px;
        left: 300px;
        transform: scaleY(-1);
        rotate: 120deg;
    }
}

@keyframes medium3 {
    0% {
        top: 400px;
        left: -150px;
    }

    50% {
        top: 150px;
        left: 150px;
    }

    100% {
        top: 300px;
        left: 750px;
    }
}

@keyframes hard1 {
    0% {
        top: 500px;
        left: 650px;
        transform: scaleX(-1);
        rotate: 0deg;
    }

    33% {
        top: 423px;
        left: 567px;
        rotate: 30deg;
        transform: scaleX(-1);
    }

    66% {
        top: 123px;
        left: 300px;
        rotate: 30deg;
        transform: scaleX(-1);
    }

    70% {
        top: 123px;
        left: 300px;
        rotate: -30deg;
        transform: scaleX(-1);
    }


    100% {
        top: 650px;
        left: 100px;
        rotate: -75deg;
        transform: scaleX(-1);
    }
}

@keyframes hard2 {
    0% {
        top: -150px;
        left: 200px;
        rotate: 90deg;
        transform: scaleX(1);
    }

    33% {
        top: 453px;
        left: 200px;
        rotate: 90deg;
        transform: scaleX(1);
    }

    35% {
        top: 453px;
        left: 200px;
        rotate: -45deg;
        transform: scaleX(1);
    }

    66% {
        top: 150px;
        left: 500px;
        rotate: -45deg;
        transform: scaleX(1);
    }


    67% {
        top: 150px;
        left: 500px;
        rotate: -30deg;
        transform: scaleX(-1);
    }

    100% {
        top: 400px;
        left: -150px;
        rotate: -30deg;
        transform: scaleX(-1);
    }
}

@keyframes hard3 {
    0% {
        top: 650px;
        left: 300px;
        rotate: -30deg;
    }

    33% {
        top: 300px;
        left: 450px;
        rotate: -30deg;
    }

    66% {
        top: 135px;
        left: 600px;
        rotate: -30deg;
    }

    100% {
        top: 300px;
        left: 650px;
        rotate: -30deg;
    }
}
</style>
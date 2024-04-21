<template>
  <div class="navbar-container" ref="navbar">
    <router-link to="/"><img src="/logo.png" class="logo"></router-link>
  </div>
</template>

<script>
import callApi from '@/apiCaller'

export default {
  name: 'NavBar',
  methods: {
  },
  mounted: function () {
    callApi(
      this.$router,
      '/api/actions',
      {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + window.localStorage.getItem('authToken')
        }
      },
      (status, data) => {
        data.actions.forEach(action => {
          let linkA = document.createElement('a');
          linkA.className = 'action-a'
          this.$refs.navbar.appendChild(linkA);

          switch (action) {
            case 'LOGOUT': {
              linkA.innerText = "Log out"
              linkA.href = "#"
              linkA.onclick = () => {
                callApi(
                  this.$router,
                  '/api/logout',
                  {
                    method: 'POST',
                    headers: {
                      'Authorization': 'Bearer ' + window.localStorage.getItem('authToken')
                    }
                  },
                  () => {
                    window.localStorage.clear();
                    window.location = "/"
                  }
                )
              };
              break;
            }
            case 'USER_PAGE': {
              linkA.innerText = "Me";
              linkA.href = "#"
              linkA.onclick = () => {
                this.$router.push("/user");
              }
              break;
            }
            case 'FIND_ARTISTS': {
              linkA.innerText = "Find artists";
              linkA.href = "#"
              linkA.onclick = () => {
                this.$router.push("/find/artists");
              }
              break;
            }
            case 'FIND_SONGS': {
              linkA.innerText = "Find songs"
              linkA.href = "#"
              linkA.onclick = () => {
                this.$router.push("/find/songs");
              }
              break;
            }
            case 'CREATE_ARTIST': {
              linkA.innerText = "Create artist"
              linkA.href = "#"
              linkA.onclick = () => {
                this.$router.push("/create/artist");
              }
              break;
            }
            case 'CREATE_SONG': {
              linkA.innerText = "Create song"
              linkA.href = "#"
              linkA.onclick = () => {
                this.$router.push("/create/song");
              }
              break;
            }
            case 'LOGIN': {
              linkA.innerText = "Log in"
              linkA.href = "/authorize"
              break;
            }
            case 'REGISTER': {
              linkA.innerText = "Register"
              linkA.href = "/register"
              break;
            }
          }
        });

      });
  }
}
</script>

<style>
.navbar-container {
  display: flex;
  min-width: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  margin-bottom: 60px;
  height: 40px;
  /*  box-shadow: 0px 5px 10px 2px rgba(34, 60, 80, 0.2);  */
}

.action-a {
  width: 100%;
  text-decoration: none;
  color: black;
  border-right: solid 1px;
  border-color: black;
  font-size: x-large;

  text-align: center;
  width: 100%;
  padding: 0px 5px;
  transition: background-color 0.2s;
}

.action-a:hover {
  text-decoration: none;
  color: white;
  background-color: rgba(0, 0, 0, 0.2);
}

.logo {
  max-height: 70px;
}
</style>
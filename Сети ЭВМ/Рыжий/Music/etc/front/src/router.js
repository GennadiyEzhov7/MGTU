import { createRouter, createWebHistory } from 'vue-router'
import UserPage from '@/components/pages/user/UserPage'
import AuthorizationPage from '@/components/pages/auth/AuthorizationPage'
import RegisterPage from '@/components/pages/auth/RegisterPage'
import Page404 from '@/components/services/404Page'
import MainPage from '@/components/pages/MainPage'
import ArtistPage from '@/components/pages/artist/ArtistPage'
import FindArtistsPage from '@/components/pages/find/FindArtistsPage'
import FindSongsPage from '@/components/pages/find/FindSongsPage'
import CreateArtistPage from '@/components/pages/create/CreateArtistPage'
import CreateSongPage from '@/components/pages/create/CreateSongPage'

const routes = [
  {
    path: '/',
    component: MainPage
  },
  {
    path: '/user',
    component: UserPage
  },
  {
    path: '/artist/:id',
    component: ArtistPage
  },
  {
    path: '/find/artists',
    component: FindArtistsPage
  },
  {
    path: '/find/songs',
    component: FindSongsPage
  },
  {
    path: '/create/artist',
    component: CreateArtistPage
  },
  {
    path: '/create/song',
    component: CreateSongPage
  },
  {
    path: '/authorize',
    component: AuthorizationPage
  }, 
  {
    path: '/register',
    component: RegisterPage
  },
  {
    path: '/:pathMatch(.*)*',
    component: Page404
  },
  {
    path: '/404',
    component: Page404
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), routes
})

export default router
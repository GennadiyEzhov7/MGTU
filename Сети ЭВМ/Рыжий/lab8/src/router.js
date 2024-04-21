import { createRouter, createWebHistory } from 'vue-router'
import Game from '@/components/GameComponent'
import About from '@/components/AboutComponent'

const routes = [
  {
    path: '/',
    component: About
  },
  {
    path: '/game',
    component: Game,
    props: route => Object.assign({}, route.query, route.params)
  },
  {
    path: '/',
    component: About
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), routes
})

export default router
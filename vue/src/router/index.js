import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from '@/components/Login.vue'
import User from '@/components/User.vue'
import Admin from '@/components/Admin.vue'
import Register from '@/components/Register.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home
    },
    {
        path: '/user',
        name: 'user',
        component: User
    },
    {
        path: '/admin',
        name: 'admin',
        component: Admin
    },
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/register',
        name: 'register',
        component: Register
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router

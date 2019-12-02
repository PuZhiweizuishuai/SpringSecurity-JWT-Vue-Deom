import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        user: {
            username: window.localStorage.getItem('username'),
            power: window.localStorage.getItem('power'),
            expirationTime: window.localStorage.getItem('expirationTime')
        }
    },
    mutations: {},
    actions: {},
    modules: {}
})

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';
import VueCookies from 'vue-cookies'
import axios from 'axios'

// 让ajax携带cookie
axios.defaults.withCredentials=true;
// 注册 axios 为全局变量
Vue.prototype.$axios = axios
// 使用 vue cookie
Vue.use(VueCookies)
Vue.config.productionTip = false
// 使用 ElementUI 组件
Vue.use(ElementUI)
// markdown 解析编辑工具
Vue.use(mavonEditor)
// 后台服务地址
Vue.prototype.SERVER_API_URL = "http://127.0.0.1:8088/api";

router.beforeEach((to, from, next) => {
        if (to.meta.requireAuth) {
            if (store.state.user.username) {
                next()
            } else {
                next({
                    path: 'login',
                    query: {redirect: to.fullPath}
                })
            }
        } else {
            next()
        }
    }
)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')



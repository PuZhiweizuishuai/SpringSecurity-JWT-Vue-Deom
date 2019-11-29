import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';

Vue.config.productionTip = false
// 使用 ElementUI 组件
Vue.use(ElementUI)
// markdown 解析编辑工具
Vue.use(mavonEditor);
// 后台服务地址
Vue.prototype.SERVER_API_URL = "http://127.0.0.1:8088/api";

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

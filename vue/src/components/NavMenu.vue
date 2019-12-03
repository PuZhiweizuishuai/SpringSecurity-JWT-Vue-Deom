<template>
    <el-menu :default-active="activeHome" mode="horizontal" :router="true">
        <el-menu-item>
            <a href="/">
                <img class="app-icon" alt="Spring Icon" src="@/assets/icon/spring.svg"/>
                <el-icon class="el-icon-plus"></el-icon>
                <img class="app-icon" alt="Vue icon" src="@/assets/icon/vue.svg"/>
            </a>
        </el-menu-item>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="user" v-if="judgeShow('ROLE_USER')">普通用户页</el-menu-item>
        <el-menu-item index="admin" v-if="judgeShow('ROLE_ADMIN')">管理员页</el-menu-item>
        <el-menu-item class="float_right" index="login" v-if="showLoginIn()">登陆</el-menu-item>
        <el-menu-item class="float_right" index="register" v-if="showLoginIn()">注册</el-menu-item>
        <el-button id="logout_button" v-if="showLoginIn() === false" @click="logout">退出</el-button>
    </el-menu>
</template>

<style>
    .app-icon {
        width: 50px;
        height: auto;
        margin-right: 10px;
    }
    #logout_button {
        margin-top: 10px;
        float: right;
    }
    .float_right {
        float: right;
    }
</style>

<script>
    import store from "../store";

    export default {
        name: "NavMenu",
        data() {
            return {
                activeHome: '/'
            };
        },
        watch: {
            $route() {
                this.setCurrentRoute()
                window.console.log("$route()" + this.activeHome);
            }
        },

        created() {
            this.setCurrentRoute();
            window.console.log("created()" + this.activeHome);
        },
        methods: {
            setCurrentRoute() {
                //关键   通过他就可以监听到当前路由状态并激活当前菜单
                this.activeHome = this.$route.name;
            },
            judgeShow(role) {
                let date = new Date();
                if (store.state.user.username && store.state.user.power && store.state.user.expirationTime >= date.getTime()) {
                    let power = store.state.user.power.split(',');
                    for (let i = 0; i < power.length; i++) {
                        if (power[i] === role) {
                            return true;
                        }
                    }
                }
                if (store.state.user.expirationTime < date.getTime()) {
                    window.localStorage.removeItem('username');
                    window.localStorage.removeItem('power');
                    window.localStorage.removeItem('expirationTime');
                }
                return false;
            },
            showLoginIn() {
                let date = new Date();
                if (store.state.user.username && store.state.user.power && store.state.user.expirationTime >= date.getTime()) {
                    return false;
                }
                return true;
            },
            logout() {
                fetch(this.SERVER_API_URL + '/logout', {
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    },
                    method: 'GET'
                }).then(response => response.json())
                .then(json => {
                    if (json.status === 200) {
                        window.localStorage.removeItem('username');
                        window.localStorage.removeItem('power');
                        window.localStorage.removeItem('expirationTime');
                        store.state.user.username = null;
                        store.state.user.power = null;
                        store.state.user.expirationTime = null;
                        this.$message({
                            message: json.message,
                            type: 'success'
                        });
                        this.$router.replace('/login');
                    }
                })
            }
        }
    }
</script>
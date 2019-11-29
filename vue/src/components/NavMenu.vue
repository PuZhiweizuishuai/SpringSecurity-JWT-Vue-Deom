<template>
    <el-menu :default-active="activeHome" mode="horizontal" :router="true" @select="handleSelect">
        <el-menu-item>
            <a href="/">
                <img class="app-icon" alt="Spring Icon" src="@/assets/icon/spring.svg"/>
                <el-icon class="el-icon-plus"></el-icon>
                <img class="app-icon" alt="Vue icon" src="@/assets/icon/vue.svg"/>
            </a>
        </el-menu-item>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="user">普通用户页</el-menu-item>
        <el-menu-item index="admin">管理员页</el-menu-item>
        <el-menu-item index="login">登陆</el-menu-item>
        <el-menu-item index="register">注册</el-menu-item>
    </el-menu>
</template>

<style>
    .app-icon {
        width: 50px;
        height: auto;
        margin-right: 10px;
    }
</style>

<script>
    export default {
        name: "NavMenu",
        data() {
            return {
                activeHome: '/'
            };
        },
        created() {
            let menuItem = window.sessionStorage.getItem("menuItem");
            if (menuItem) {
                this.activeHome = menuItem;
            } else {
                this.activeHome = '/';
            }
        },
        methods: {
            handleSelect(key, keyPath) {
                window.sessionStorage.setItem("menuItem", keyPath);
            }
        }
    }
</script>
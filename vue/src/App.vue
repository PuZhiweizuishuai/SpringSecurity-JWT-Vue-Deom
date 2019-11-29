<template>
    <div id="app">
        <NavMenu></NavMenu>

        <router-view/>
    </div>
</template>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }
</style>


<script>
    import NavMenu from "@/components/NavMenu";

    export default {
        components: {NavMenu},
        data() {
            return {
                src: "../assets/logo.png"
            }
        }
    }
</script>
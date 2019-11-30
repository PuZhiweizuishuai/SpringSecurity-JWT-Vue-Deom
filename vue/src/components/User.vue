<template>
    <div>
        <h1>User</h1>
        <h1> {{ count }} </h1>
        <el-button v-on:click="ceshi">Ceshi</el-button>
    </div>
</template>
<script>
    export default {
        name: "User",
        data() {
            return {
                count: 1
            }
        },
        methods: {
            ceshi() {
                this.count ++;
                fetch(this.SERVER_API_URL + "/home", {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    },
                    method: "GET",
                }).then(response => response.json())
                    .then(json => {
                        window.console.log(json);
                    });
            }
        }
    }
</script>

<style scoped>

</style>
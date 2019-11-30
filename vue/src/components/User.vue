<template>
    <div>
        <el-row :key="key" v-for="(key, value) in systemDatas" class="showData">
            <el-col :span="5">
                {{ value }}
            </el-col>
            <el-col :span="10">
                {{ key }}
            </el-col>
            <el-col :span="2">
                <el-button type="danger" @click="deleteItem(value)">删除</el-button>
            </el-col>
        </el-row>

        <el-card>
            <el-row>
                <el-col :span="5">
                    <el-input v-model="selectItemNumber"></el-input>
                </el-col>
                <el-col :span="2">
                    <el-button @click="selectItem">查找</el-button>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="5">
                    <el-input v-model="newItem"></el-input>
                </el-col>
                <el-col :span="2">
                    <el-button @click="createItem">添加</el-button>
                </el-col>
            </el-row>

            <el-button @click="testSystem">Test</el-button>
        </el-card>
    </div>
</template>
<script>
    export default {
        name: "User",
        data() {
            return {
                systemDatas: [],
                selectItemNumber: '',
                newItem: ''
            }
        },
        created() {
            fetch(this.SERVER_API_URL + "/data", {
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                },
                method: "GET",
                credentials: "include"
            }).then(response => response.json())
                .then(json => {
                    this.systemDatas = json;
                });
        },
        methods: {
            testSystem() {
               this.systemDatas = [];
            },
            deleteItem(value) {
                fetch(this.SERVER_API_URL + "/user/data/" + value, {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8",
                        "X-XSRF-TOKEN": this.$cookies.get('XSRF-TOKEN')
                    },
                    method: "DELETE",
                    credentials: "include"
                }).then(response => response.json())
                    .then(json => {
                        if (json.status === 200) {
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        } else {
                            this.$message.error(json.message);
                        }
                    });
            },
            selectItem() {
                fetch(this.SERVER_API_URL + "/data?id=" + this.selectItemNumber, {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8",
                        "X-XSRF-TOKEN": this.$cookies.get('XSRF-TOKEN')
                    },
                    method: "GET",
                    credentials: "include"
                }).then(response => response.json())
                    .then(json => {
                        this.$message({
                            message: json,
                            type: 'success'
                        });
                    });
            },
            createItem() {
                let customData = {
                    data: this.newItem
                };
                fetch(this.SERVER_API_URL + "/user/data", {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8",
                        "X-XSRF-TOKEN": this.$cookies.get('XSRF-TOKEN')
                    },
                    method: "POST",
                    credentials: "include",
                    body: JSON.stringify(customData)
                }).then(response => response.json())
                    .then(json => {
                        this.systemDatas.push(this.newItem);
                        this.$message({
                            message: json,
                            type: 'success'
                        });
                    });
            }
        }
    }
</script>

<style scoped>
    .showData {
        margin-top: 10px;
    }
</style>
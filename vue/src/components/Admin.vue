<template>
    <div>
        <el-card>
            <h3>所有数据显示</h3>
            <el-table class="showSystemData"
                      :data="systemDataList"
                      max-height="400"
                      style="width: 80%">
                <el-table-column
                        prop="id"
                        label="id"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="data"
                        label="数据"
                        width="750">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="editItem">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="deleteItem(scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-card>
            <h3>用户操作</h3>
            <div class="showSystemData">
                <div class="showUserEdit">
                    <el-row :gutter="20">
                        <el-col :span="10">
                            <el-input v-model="selectItemNumber"></el-input>
                        </el-col>
                        <el-col :span="2">
                            <el-button @click="selectItem">查找数据</el-button>
                        </el-col>
                    </el-row>
                    <br/>
                    <el-row :gutter="20">
                        <el-col :span="10">
                            <el-input v-model="newItem"></el-input>
                        </el-col>
                        <el-col :span="2">
                            <el-button @click="createItem">添加数据</el-button>
                        </el-col>
                    </el-row>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "Admin",
        data() {
            return {
                systemDataList: [],
                selectItemNumber: '',
                newItem: ''
            }
        },
        created() {
            // 使用 axios 示例
            // this.$axios
            //     .get(this.SERVER_API_URL + "/data", {
            //         headers: {
            //             "Content-Type": "application/json; charset=UTF-8"
            //         }
            //     })
            //     .then(successResponse => {
            //         this.systemDataList = successResponse.data;
            //     })

            // 判断登陆情况
            // fetch(this.SERVER_API_URL + '/admin/loginJudge', {
            //     headers: {
            //         "Content-Type": "application/json; charset=UTF-8"
            //     },
            //     method: "GET",
            //     credentials: "include"
            // }).then(response => response.json())
            // .then(json => {
            //     if (json.status === 200) {
            //         window.sessionStorage.setItem("menuItem", 'login');
            //         window.localStorage.setItem('username', json.user.username);
            //         window.localStorage.setItem('power', json.user.power);
            //         window.localStorage.setItem('expirationTime', json.user.expirationTime);
            //         return this.$router.replace('/login');
            //     } else {
            //
            //         window.localStorage.removeItem('username');
            //         window.localStorage.removeItem('power');
            //         window.localStorage.removeItem('expirationTime');
            //     }
            // });
            fetch(this.SERVER_API_URL + "/data", {
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                },
                method: "GET",
                credentials: "include"
            }).then(response => response.json())
                .then(json => {
                    this.systemDataList = json;
                });
        },
        methods: {
            deleteItem(data) {
                fetch(this.SERVER_API_URL + "/admin/data/" + data.id, {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8",
                        "X-XSRF-TOKEN": this.$cookies.get('XSRF-TOKEN')
                    },
                    method: "DELETE",
                    credentials: "include"
                }).then(response => response.json())
                    .then(json => {
                        if (json.status === 200) {
                            this.systemDataList.splice(data.id, 1);
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        } else {
                            window.console.log(json);
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
                fetch(this.SERVER_API_URL + "/admin/data", {
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8",
                        "X-XSRF-TOKEN": this.$cookies.get('XSRF-TOKEN')
                    },
                    method: "POST",
                    credentials: "include",
                    body: JSON.stringify(customData)
                }).then(response => response.json())
                    .then(json => {
                        this.systemDataList.push(json);
                        this.newItem = '';
                        this.$message({
                            message: json,
                            type: 'success'
                        });
                    });
            },
            editItem() {
                this.$alert('待完成', '未完成', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.$message({
                            type: 'info',
                            message: `action: ${action}`
                        });
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .showSystemData {
        margin-right: 5%;
        margin-left: 10%;
    }
    .showUserEdit {
        margin-left: 25%;
    }

</style>
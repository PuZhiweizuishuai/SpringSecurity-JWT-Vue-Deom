<template>
    <div id="loginBackground">
        <el-card id="loginCard">
            <h1>登陆</h1>
            <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="100px"
                     >
                <!--            用户名 -->
                <el-form-item label="用户名：" prop="username">
                    <el-input type="text" v-model="loginForm.username" autocomplete="off" placeholder="默认用户账号：user，默认admin账号：admin"
                    />
                </el-form-item>
                <!--            密码-->
                <el-form-item label="密码：" prop="password">
                    <el-input type="password" v-model="loginForm.password" autocomplete="off"  placeholder="默认密码均为：123456"/>
                </el-form-item>

                <el-checkbox id="rememberMe" v-model="loginForm.rememberMe">记住我</el-checkbox>

                <!--验证码-->
                <el-form-item label="验证码：" prop="verifyCode">
                    <el-row>
                        <el-col id="VerifyCodeImage" :span="7">
                            <el-image @click="refreshVerifyCodeImage" :src="verifyCodeImage.url"
                                      :title="verifyCodeImage.title" />
                        </el-col>
                        <el-col :span="7">
                            <el-input type="text" v-model="loginForm.verifyCode" autocomplete="off" />
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="resetForm('loginForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            let validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };
            let validateUserName = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户名！'));
                } else {
                    callback();
                }
            };
            let validateVerifyCode = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('验证码不能为空！'));
                } else {
                    callback();
                }
            };
            return {
                loginForm: {
                    password: '',
                    username: '',
                    verifyCode: '',
                    rememberMe: false
                },
                rules: {
                    password: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    username: [
                        {validator: validateUserName, trigger: 'blur'}
                    ],
                    verifyCode: [
                        {validator: validateVerifyCode, trigger: 'blur'}
                    ]
                },
                verifyCodeImage: {
                    url: this.SERVER_API_URL + '/verifyImage?' + Math.random(),
                    title: '点击刷新！'
                }
            };
        },
        methods: {
            submitForm() {
                this.$refs['loginForm'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                });
                let user = {
                    username: this.loginForm.username,
                    password: this.loginForm.password,
                    verifyCode: this.loginForm.verifyCode,
                    rememberMe: this.loginForm.rememberMe
                };
                fetch(this.SERVER_API_URL + '/login', {
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    },
                    method: "POST",
                    body: JSON.stringify(user)
                }).then(response => response.json())
                    .then(json => {
                        if (json.status === 200) {
                            // 写入登陆信息
                            window.console.log(json);
                            window.localStorage.setItem('username', json.user.username);
                            window.localStorage.setItem('power', json.user.power);
                            window.localStorage.setItem('expirationTime', json.user.expirationTime);

                            this.$router.replace('/');
                        } else {
                            const h = this.$createElement;
                            this.$message.error({
                                message: h('div', null, [
                                    h('h3', null,'消息：' + json.error),
                                    h('p', null, 'path:' + json.path),
                                    h('p',  { style: 'color: teal' }, '状态码：' + json.status),
                                ])
                            });
                        }
                    });
            },
            // 清空输入
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            // 刷新验证码
            refreshVerifyCodeImage() {
                this.verifyCodeImage.url = this.SERVER_API_URL + '/verifyImage?' + Math.random()
            }
        }
    }
</script>

<style scoped>
    #loginBackground {
        background: url("../assets/bg/101-desktop-wallpaper.png") no-repeat;
        background-position: center;
        height: 100%;
        width: 100%;
        background-size: cover;
        background-color: #b8e5f8;
        position: fixed;
    }
    #loginCard {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 90px auto;
        width: 50%;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }

    #VerifyCodeImage {
        cursor: pointer;
    }

    #rememberMe {
        float: right;
        z-index: 99;
    }
</style>
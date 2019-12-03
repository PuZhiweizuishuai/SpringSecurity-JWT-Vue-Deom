# 基于Spring Security， JWT， Vue的前后端分离无状态认证Demo

## 简介

### 运行展示

![主页](http://127.0.0.1:8088/images/home.jpg)

![登陆]()

![管理员页]()

#### 后端

主要展示 Spring Security 与 JWT 结合使用构建后端 API 接口。

主要功能包括登陆（如何在Spring Security中添加验证码登陆），查找，创建，删除并对用户权限进行区分等等。

ps：由于只是 Demo，所以没有调用数据库，以上所说增删改查均在 HashMap 中完成。

#### 前端

展示如何使用 Vue 构建前端后与后端的配合，包括跨域的设置，前端登陆拦截

并实现POST，GET，DELETE请求。包括如何在 Vue 中使用后端的 XSRF-TOKEN 防范 CSRF 攻击

## 技术栈

组件         | 技术
---               | ---
前端          | [Vue.js 2](https://cn.vuejs.org/)
后端 (REST API)    | [SpringBoot](https://projects.spring.io/spring-boot) (Java)
安全          | Token Based (Spring Security, [JJWT](https://github.com/auth0/java-jwt), CSRF)
前端脚手架| [vue-cli3](https://cli.vuejs.org/), Webpack, npm
后端构建| Maven

## 快速运行

#### 测试运行环境

Java11， Node 12

构建工具 Maven3， veu-cil3

克隆项目到本地

```bash
git clone https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom.git
```

#### 后端运行

```bash
cd spring-security-jwt
mvn clean package
```

之后运行，程序默认运行端口8088

```bash
java -jar target/security 0.0.1-SNAPSHOT.jar
```

#### 前端运行

```bash
cd vue
npm install
```

之后运行，默认端口 8080

```bash
npm run serve
```

最后打开浏览器，输入

```
http://127.0.0.1:8080
```

## 实现细节

待更新

## 参考文档

[Spring Security Reference](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/)


[Vue.js](https://cn.vuejs.org/)

## 依赖工具

[mavonEditor](https://github.com/hinesboy/mavonEditor)

[element ui](https://element.eleme.io/)


## 版权和许可

MIT license.


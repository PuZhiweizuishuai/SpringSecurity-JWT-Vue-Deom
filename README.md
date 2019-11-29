# Under development

[Chinese Documents 中文文档](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/README-Zh-CN.md)

## About

This is a demonstration of stateless token-based authentication using JSON Web Token and CSRF protection, Spring Security, Spring Boot and Vue js. 


## Technology Stack

Component         | Technology
---               | ---
Frontend          | [Vue.js 2](https://cn.vuejs.org/)
Backend (REST)    | [SpringBoot](https://projects.spring.io/spring-boot) (Java)
Security          | Token Based (Spring Security, [JJWT](https://github.com/auth0/java-jwt), CSRF)
Client Build Tools| [vue-cli](https://cli.vuejs.org/), Webpack, npm
Server Build Tools| Maven


## Quick start

**Run Environment: Java11, Node 12, Maven3**


**Run back end server**

```bash
mvn clean package
```

**Then**

```bash
java -jar target/security 0.0.1-SNAPSHOT.jar
```

**Run front end server**

```bash
npm install
```

**Then**

```bash
npm run serve
```

**Final**

Open

```
http://127.0.0.1:8080
```

## Security

#### JWT token

To generating and verifying JWT I use JJWT. JJWT – a self-contained Java library providing end-to-end JSON Web Tokens creation and verification.

#### JWT storing strategy

We have a couple of options where to store the token:

HTML5 Web Storage (localStorage or sessionStorage)
Cookies

#### Main problem of Web Storage
It is accessible through JavaScript on the same domain. This means that any JavaScript running on your site will have access to web storage, and because of this can be vulnerable to cross-site scripting (XSS) attacks.

So, to prevent XSS I store the JWT token in a Http-Only/Secure cookie. Cookies, when used with the HttpOnly cookie flag, are not accessible through JavaScript, and are immune to XSS.

#### CSRF attack
However, cookies are vulnerable to a different type of attack: cross-site request forgery (CSRF). A CSRF attack is a type of attack that occurs when a malicious web site, email, or blog causes a user’s web browser to perform an unwanted action on a trusted site on which the user is currently authenticated.

To prevent CSRF attacks, we must create an extra Javascript readable cookie which is called: XSRF-TOKEN. This cookie must be created when the user is logged in and should contain a random, un-guessable string. Every time the JavaScript application wants to make a request, it will need to read this token and send it along in a custom HTTP header.

## Reference document

[Spring Security Reference](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/)


[Vue.js](https://cn.vuejs.org/)

## Dependency software

[mavonEditor](https://github.com/hinesboy/mavonEditor)

[element ui](https://element.eleme.io/)


## Copyright and license

The code is released under the MIT license.
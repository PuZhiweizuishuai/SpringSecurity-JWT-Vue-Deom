# 实现基于Spring Security， JWT， Vue的前后端分离无状态认证Demo

## 项目完整代码

[SpringSecurity-JWT-Vue-Deom](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom)

## 开发环境


创建 Spring boot 项目，添加 JJWT 和 Spring Security 的项目依赖，这个非常简单，有很多的教程都有块内容，唯一需要注意的是，如果你使用的Java版本是11，那么你还需要添加以下依赖，使用 Java8 则不需要。


```xml
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
```

## 后端搭建
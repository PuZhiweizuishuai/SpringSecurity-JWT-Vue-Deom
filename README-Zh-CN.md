# 基于Spring Security， JWT， Vue的前后端分离无状态认证Demo

## 简介

### 运行展示

![主页](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/home.jpg)

![登陆](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/login.jpg)

![管理员页](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/admin.jpg)

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
java -jar target/security-0.0.1-SNAPSHOT.jar
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

### 后端搭建 

#### 基础配置

创建 Spring boot 项目，添加 JJWT 和 Spring Security 的项目依赖，这个非常简单，有很多的教程都有块内容，唯一需要注意的是，如果你使用的Java版本是11，那么你还需要添加以下依赖，使用 Java8 则不需要。


```xml
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
```

要使用 Spring Security 实现对用户的权限控制，首先需要实现一个简单的 User 对象实现 UserDetails 接口，UserDetails 接口负责提供核心用户的信息，如果你只需要用户登陆的账号密码，不需要其它信息，如验证码等，那么你可以直接使用 Spring Security 默认提供的 User 类，而不需要自己实现。

```Java
public class User implements UserDetails {
    private String username;
    private String password;
    private Boolean rememberMe;
    private String verifyCode;
    private String power;
    private Long expirationTime;
    private List<GrantedAuthority> authorities;

    /**
    * 省略其它的 get set 方法
    */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

```

这个就是我们要使用到的 User 对象，其中包含了 记住我，验证码等登陆信息，因为 Spring Security 整合 Jwt 本质上就是用自己自定义的登陆过滤器，去替换 Spring Security 原生的登陆过滤器，这样的话，原生的记住我功能就会无法使用，所以我在 User 对象里添加了记住我的信息，用来自己实现这个功能。

### JWT 令牌认证工具

首先我们来新建一个 TokenAuthenticationHelper 类，用来处理认证过程中的验证和请求

```Java
public class TokenAuthenticationHelper {
    /**
     * 未设置记住我时 token 过期时间
     * */
    private static final long EXPIRATION_TIME = 7200000;

    /**
     * 记住我时 cookie token 过期时间
     * */
    private static final int COOKIE_EXPIRATION_TIME = 1296000;

    private static final String SECRET_KEY = "ThisIsASpringSecurityDemo";
    public static final String COOKIE_TOKEN = "COOKIE-TOKEN";
    public static final String XSRF = "XSRF-TOKEN";

    /**
     * 设置登陆成功后令牌返回
     * */
    public static void addAuthentication(HttpServletRequest request,  HttpServletResponse response, Authentication authResult) throws IOException {
        // 获取用户登陆角色
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        // 遍历用户角色
        StringBuffer stringBuffer = new StringBuffer();
        authorities.forEach(authority -> {
            stringBuffer.append(authority.getAuthority()).append(",");
        });
        long expirationTime = EXPIRATION_TIME;
        int cookExpirationTime = -1;
        // 处理登陆附加信息
        LoginDetails loginDetails = (LoginDetails) authResult.getDetails();
        if (loginDetails.getRememberMe() != null && loginDetails.getRememberMe()) {
            expirationTime = COOKIE_EXPIRATION_TIME * 1000;
            cookExpirationTime = COOKIE_EXPIRATION_TIME;
        }

        String jwt = Jwts.builder()
                // Subject 设置用户名
                .setSubject(authResult.getName())
                // 设置用户权限
                .claim("authorities", stringBuffer)
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                // 签名算法
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        Cookie cookie = new Cookie(COOKIE_TOKEN, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookExpirationTime);
        response.addCookie(cookie);

        // 向前端写入数据
        LoginResultDetails loginResultDetails = new LoginResultDetails();
        ResultDetails resultDetails = new ResultDetails();
        resultDetails.setStatus(HttpStatus.OK.value());
        resultDetails.setMessage("登陆成功！");
        resultDetails.setSuccess(true);
        resultDetails.setTimestamp(LocalDateTime.now());
        User user = new User();
        user.setUsername(authResult.getName());
        user.setPower(stringBuffer.toString());
        user.setExpirationTime(System.currentTimeMillis() + expirationTime);

        loginResultDetails.setResultDetails(resultDetails);
        loginResultDetails.setUser(user);
        loginResultDetails.setStatus(200);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(loginResultDetails));
        out.flush();
        out.close();
    }

    /**
     * 对请求的验证
     * */
    public static Authentication getAuthentication(HttpServletRequest request) {

        Cookie cookie = WebUtils.getCookie(request, COOKIE_TOKEN);
        String token = cookie != null ? cookie.getValue() : null;

        if (token != null) {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // 获取用户权限
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get("authorities").toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            String userName = claims.getSubject();
            if (userName != null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
                usernamePasswordAuthenticationToken.setDetails(claims);
                return usernamePasswordAuthenticationToken;
            }
            return null;
        }
        return null;
    }
}

```

1. addAuthentication 方法负责返回登陆成功的信息，使用 HTTP Only 的 Cookie 可以有效防止 XSS 攻击。

2. 登陆成功后返回用户的权限，用户名，登陆过期时间，可以有效的帮助前端构建合适的用户界面。

3. getAuthentication 方法负责对用户的其它请求进行验证，如果用户的 JWT 解析正确，则向 Spring Security 返回 usernamePasswordAuthenticationToken 用户名密码验证令牌，告诉 Spring Security 用户所拥有的权限，并放到当前的 Context 中，然后执行过滤链使请求继续执行下去。

至此，我们的基本登陆与验证所需要的方法就写完了

ps：其中的 LoginResultDetails 类和 ResultDetails 请看项目源码，篇幅所限，此处不在赘述。

#### JWT 过滤器配置

众所周知，Spring Security 是借助一系列的 Servlet Filter 来来实现提供各种安全功能的，所以我们要使用 JWT 就需要自己实现两个和JWT有关的过滤器

1. 一个是用户登录的过滤器，在用户的登录的过滤器中校验用户是否登录成功，如果登录成功，则生成一个token返回给客户端，登录失败则给前端一个登录失败的提示。

2. 第二个过滤器则是当其他请求发送来，校验token的过滤器，如果校验成功，就让请求继续执行。

**这两个过滤器，我们分别来看，先看第一个：**

在项目下新建一个包，名为 filter， 在 filter 下新建一个类名为 JwtLoginFilter,并使其继承 AbstractAuthenticationProcessingFilter 类，这个类是一个基于浏览器的基于HTTP的身份验证请求的抽象处理器。

```Java
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final VerifyCodeService verifyCodeService;

    private final LoginCountService loginCountService;

    /**
     * @param defaultFilterProcessesUrl 默认要过滤的地址
     * @param authenticationManager 认证管理器，校验身份时会用到
     * @param loginCountService */
    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
                          VerifyCodeService verifyCodeService, LoginCountService loginCountService) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        this.loginCountService = loginCountService;
        // 为 AbstractAuthenticationProcessingFilter 中的属性赋值
        setAuthenticationManager(authenticationManager);
        this.verifyCodeService = verifyCodeService;
    }



    /**
     * 提取用户账号密码进行验证
     * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        // 判断是否要抛出 登陆请求过快的异常
        loginCountService.judgeLoginCount(httpServletRequest);
        // 获取 User 对象
        // readValue 第一个参数 输入流，第二个参数 要转换的对象
        User user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);
        // 验证码验证
        verifyCodeService.verify(httpServletRequest.getSession().getId(), user.getVerifyCode());
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username,
                user.getPassword(),
                user.getAuthorities()
        );
        // 添加验证的附加信息
        // 包括验证码信息和是否记住我
        token.setDetails(new LoginDetails(user.getRememberMe(), user.getVerifyCode()));
        // 进行登陆验证
        return getAuthenticationManager().authenticate(token);
    }

    /**
     * 登陆成功回调
     * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        loginCountService.cleanLoginCount(request);
        // 登陆成功
        TokenAuthenticationHelper.addAuthentication(request, response ,authResult);
    }

    /**
     * 登陆失败回调
     * */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 错误请求次数加 1
        loginCountService.addLoginCount(request, 1);
        // 向前端写入数据
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorDetails.setMessage("登陆失败！");
        errorDetails.setError(failed.getLocalizedMessage());
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setPath(request.getServletPath());
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(errorDetails));
        out.flush();
        out.close();
    }
}
```

这个类主要有以下几个作用

1. 自定义 JwtLoginFilter 继承自 AbstractAuthenticationProcessingFilter，并实现其中的三个默认方法。

2. attemptAuthentication方法中，我们从登录参数中提取出用户名密码，然后调用AuthenticationManager.authenticate()方法去进行自动校验。

3. 第二步如果校验成功，就会来到successfulAuthentication回调中，在successfulAuthentication方法中，将用户角色遍历然后用一个 , 连接起来，然后再利用Jwts去生成token，按照代码的顺序，

4. 生成过程一共配置了四个参数，分别是用户角色、主题、过期时间以及加密算法和密钥，然后将生成的token写出到客户端。

5. 第二步如果校验失败就会来到unsuccessfulAuthentication方法中，在这个方法中返回一个错误提示给客户端即可。

ps：其中的 verifyCodeService 与 loginCountService 方法与本文关系不大，其中的代码实现请看源码

唯一需要注意的就是

验证码异常需要继承 AuthenticationException 异常，

![verifyCode](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/login_duandian_3.png)

可以看到这是一个 Spring Security 各种异常的父类，写一个验证码异常类继承 AuthenticationException，然后直接将验证码异常抛出就好。

以下完整代码位于 com.bugaugaoshu.security.service.impl.DigitsVerifyCodeServiceImpl类下

```Java
    @Override
    public void verify(String key, String code) {
        String lastVerifyCodeWithTimestamp = verifyCodeRepository.find(key);
        // 如果没有验证码，则随机生成一个
        if (lastVerifyCodeWithTimestamp == null) {
            lastVerifyCodeWithTimestamp = appendTimestamp(randomDigitString(verifyCodeUtil.getLen()));
        }
        String[] lastVerifyCodeAndTimestamp = lastVerifyCodeWithTimestamp.split("#");
        String lastVerifyCode = lastVerifyCodeAndTimestamp[0];
        long timestamp = Long.parseLong(lastVerifyCodeAndTimestamp[1]);
        if (timestamp + VERIFY_CODE_EXPIRE_TIMEOUT < System.currentTimeMillis()) {
            throw new VerifyFailedException("验证码已过期！");
        } else if (!Objects.equals(code, lastVerifyCode)) {
            throw new VerifyFailedException("验证码错误！");
        }
    }
```



**第二个用户过滤器**

```Java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = TokenAuthenticationHelper.getAuthentication(httpServletRequest);

            // 对用 token 获取到的用户进行校验
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SignatureException | IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired，登陆已过期");
        }
    }
}
```

这个就很简单了，将拿到的用户Token进行解析，如果正确，就将当前用户加入到 SecurityContext 的上下文中，授予用户权限，否则返回 Token 过期的异常


#### Spring Security 配置

接下来我们来配置 Spring Security,代码如下：

```Java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static String ADMIN = "ROLE_ADMIN";

    public static String USER = "ROLE_USER";

    private final VerifyCodeService verifyCodeService;

    private final LoginCountService loginCountService;

    /**
     * 开放访问的请求
     */
    private final static String[] PERMIT_ALL_MAPPING = {
            "/api/hello",
            "/api/login",
            "/api/home",
            "/api/verifyImage",
            "/api/image/verify",
            "/images/**"
    };

    public WebSecurityConfig(VerifyCodeService verifyCodeService, LoginCountService loginCountService) {
        this.verifyCodeService = verifyCodeService;
        this.loginCountService = loginCountService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // 允许跨域访问的 URL
        List<String> allowedOriginsUrl = new ArrayList<>();
        allowedOriginsUrl.add("http://localhost:8080");
        allowedOriginsUrl.add("http://127.0.0.1:8080");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置允许跨域访问的 URL
        config.setAllowedOrigins(allowedOriginsUrl);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PERMIT_ALL_MAPPING)
                .permitAll()
                .antMatchers("/api/user/**", "/api/data", "/api/logout")
                // USER 和 ADMIN 都可以访问
                .hasAnyAuthority(USER, ADMIN)
                .antMatchers("/api/admin/**")
                // 只有 ADMIN 才可以访问
                .hasAnyAuthority(ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                // 添加过滤器链,前一个参数过滤器， 后一个参数过滤器添加的地方
                // 登陆过滤器
                .addFilterBefore(new JwtLoginFilter("/api/login", authenticationManager(), verifyCodeService, loginCountService), UsernamePasswordAuthenticationFilter.class)
                // 请求过滤器
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 开启跨域
                .cors()
                .and()
                // 开启 csrf
                .csrf()
                // .disable();
                .ignoringAntMatchers(PERMIT_ALL_MAPPING)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 在内存中写入用户数据
        auth.
                authenticationProvider(daoAuthenticationProvider());
                //.inMemoryAuthentication();
//                .withUser("user")
//                .password(passwordEncoder().encode("123456"))
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123456"))
//                .authorities("ROLE_ADMIN")
//                .and()
//                .withUser("block")
//                .password(passwordEncoder().encode("123456"))
//                .authorities("ROLE_USER")
//                .accountLocked(true);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(new CustomUserDetailsService());
        return provider;
    }
```

以上代码的注释很详细，我就不多说了，重点说一下两个地方一个是 csrf 的问题，另一个就是 inMemoryAuthentication 在内存中写入用户的部分。


首先说 csrf 的问题：我看了看网上有很多 Spring Security 的教程，都会将 `.csrf()`设置为 `.disable()` ,这种设置虽然方便，但是不够安全，忽略了使用安全框架的初衷所以为了安全起见，我还是开启了这个功能，顺便学习一下如何使用 XSRF-TOKEN 


因为这个项目是一个 Demo,不涉及数据库部分，所以我选择了在内存中直接写入用户，打个断点我们就能知道种方式调用的是 Spring Security 的是 ProviderManager 这个方法，这种方法不方便我们抛出入用户名不存在或者其异常，它都会抛出 Bad Credentials 异常，不会提示其它错误,如下图所示。

![断点1](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/login_duandian.png)

![断点1](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/login_duandian_1.png)

![断点1](https://github.com/PuZhiweizuishuai/SpringSecurity-JWT-Vue-Deom/blob/master/doc/login_duandian_2.png)


Spring Security 为了安全考虑，会把所有的登陆异常全部归结为 Bad Credentials 异常，所以为了能抛出像用户名不存在的这种异常，如果采用 Spring Security 默认的登陆方式的话,可以采用像GitHub项目[Vhr](https://github.com/lenve/vhr/blob/41dcea34d3a220988e19c34ab88b2822d02c1be9/hrserver/src/main/java/org/sang/config/WebSecurityConfig.java#L58)里的这种处理方式，但是因为这个项目使用 Jwt 替换掉了默认的登陆方式，想要实现详细的异常信息抛出就比较复杂了，我找了好久也没找到比较简单且合适的方法。如果你有好的方法，欢迎分享。

最后我的解决方案是使用 Spring Security 的 DaoAuthenticationProvider 这个类来成为认证提供者，这个类实现了 AbstractUserDetailsAuthenticationProvider 这一个抽象的用户详细信息身份验证功能，查看注释我们可以知道 AbstractUserDetailsAuthenticationProvider 提供了 A base AuthenticationProvider that allows subclasses to override and work with UserDetails objects. The class is designed to respond to UsernamePasswordAuthenticationToken authentication requests.（允许子类重写和使用UserDetails对象的基本身份验证提供程序。该类旨在响应UsernamePasswordAuthenticationToken身份验证请求。）

通过配置自定义的用户查询实现类，我们可以直接在 CustomUserDetailsService 里抛出没有发现用户名的异常，然后再设置 hideUserNotFoundExceptions 为 false 这样就可以区别是密码错误，还是用户名不存在的错误了，

但是这种方式还是有一个问题，不能抛出像账户被锁定这种异常，理论上这种功能可以继承 AbstractUserDetailsAuthenticationProvider 这个抽象类然后自己重写的登陆方法来实现，我看了看好像比较复杂，一个 Demo 没必要，我就放弃了。

另外据说安全信息暴露的越少越好，所以暂时就先这样吧。(算是给自己找个理由)

#### 用户查找服务

```Java
public class CustomUserDetailsService implements UserDetailsService {
    private List<UserDetails> userList = new ArrayList<>();

    public CustomUserDetailsService() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDetails user = User.withUsername("user").password(passwordEncoder.encode("123456")).authorities(WebSecurityConfig.USER).build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("123456")).authorities(WebSecurityConfig.ADMIN).build();
        userList.add(user);
        userList.add(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetails userDetails : userList) {
            if (userDetails.getUsername().equals(username)) {
                // 此处我尝试过直接返回 user
                // 但是这样的话，只有后台服务启动后第一次登陆会有效
                // 推出后第二次登陆会出现  Empty encoded password 的错误，导致无法登陆
                // 这样写就不会出现这种问题了
                // 因为在第一次验证后，用户的密码会被清除，导致第二次登陆系统拿到的是空密码
                // 所以需要new一个对象或将原对象复制一份
                // 这个解决方案来自 https://stackoverflow.com/questions/43007763/spring-security-encoded-password-gives-me-bad-credentials/43046195#43046195
                return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            }
        }
        throw new UsernameNotFoundException("用户名不存在，请检查用户名或注册！");
    }
}
```

这部分就比较简单了，唯一的注意点我在注释中已经写的很清楚了，当然你要是使用连接数据库的话，这个问题就不存在了。

UserDetailsService 这个接口就是 Spring Security 为其它的数据访问策略做支持的。

#### 未完待续

## 参考文档

[Spring Security Reference](https://docs.spring.io/spring-security/site/docs/5.2.2.BUILD-SNAPSHOT/reference/htmlsingle/)


[Vue.js](https://cn.vuejs.org/)

## 依赖工具

[mavonEditor](https://github.com/hinesboy/mavonEditor)

[element ui](https://element.eleme.io/)


## 版权和许可

MIT license.


package com.bugaugaoshu.security.filter;

import com.bugaugaoshu.security.config.TokenAuthenticationHelper;
import com.bugaugaoshu.security.damain.ErrorDetails;
import com.bugaugaoshu.security.exception.VerifyFailedException;
import com.bugaugaoshu.security.model.LoginDetails;
import com.bugaugaoshu.security.model.User;
import com.bugaugaoshu.security.service.LoginCountService;
import com.bugaugaoshu.security.service.VerifyCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.HtmlUtils;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-25 21:57
 * JWT 登陆过滤器
 */
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
        //loginCountService.judgeLoginCount(httpServletRequest);
        // 获取 User 对象
        // readValue 第一个参数 输入流，第二个参数 要转换的对象
        User user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);
        // 验证码验证
        verifyCodeService.verify(httpServletRequest.getSession().getId(), user.getVerifyCode());
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        System.out.println(user.getPassword());
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
        //loginCountService.cleanLoginCount(request);
        // 登陆成功
        TokenAuthenticationHelper.addAuthentication(request, response ,authResult);
    }

    /**
     * 登陆失败回调
     * */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 错误请求次数加 1
        //loginCountService.addLoginCount(request, 1);
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

package com.bugaugaoshu.security.filter;

import com.bugaugaoshu.security.config.TokenAuthenticationHelper;
import com.bugaugaoshu.security.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-25 21:57
 * JWT 登陆过滤器
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * @param defaultFilterProcessesUrl 默认要过滤的地址
     * @param authenticationManager 认证管理器，校验身份时会用到
     * */
    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        // 为 AbstractAuthenticationProcessingFilter 中的属性赋值
        setAuthenticationManager(authenticationManager);
    }

    /**
     * 提取用户账号密码进行验证
     * */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        // 获取 User 对象
        // readValue 第一个参数 输入流，第二个参数 要转换的对象
        User user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);

        // 进行登陆验证
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        ));
    }

    /**
     * 登陆成功回调
     * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 登陆成功
        TokenAuthenticationHelper.addAuthentication(response, authResult);
    }

    /**
     * 登陆失败回调
     * */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 向前端写入数据
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "登陆失败！");
        // 获取异常信息
        map.put("error", failed.getMessage());
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }


}

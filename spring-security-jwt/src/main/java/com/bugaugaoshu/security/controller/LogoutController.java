package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.config.TokenAuthenticationHelper;
import com.bugaugaoshu.security.damain.ResultDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-12-03 16:59
 * 自定义退出
 */
@RestController
public class LogoutController {
    @GetMapping("/api/logout")
    public ResultDetails logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, TokenAuthenticationHelper.COOKIE_TOKEN);
        Authentication authentication = TokenAuthenticationHelper.getAuthentication(request);
        if (cookie != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        ResultDetails resultDetails = new ResultDetails();
        resultDetails.setStatus(HttpStatus.OK.value());
        resultDetails.setMessage("退出成功！");
        resultDetails.setTimestamp(LocalDateTime.now());
        return resultDetails;
    }
}

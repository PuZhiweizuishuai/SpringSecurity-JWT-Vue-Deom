package com.bugaugaoshu.security.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-29 17:35
 * 自定义用户查找类，方便返回未找到用户名异常
 */
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
                // 因为在第一次验证后，用户的密码会被清除，导致第二次系统拿到的是空密码
                // 所以需要new一个对象或将原对象复制一份
                // 这个解决方案来自 https://stackoverflow.com/questions/43007763/spring-security-encoded-password-gives-me-bad-credentials/43046195#43046195
                return new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            }
        }
        throw new UsernameNotFoundException("用户名不存在，请检查用户名或注册！");
    }
}

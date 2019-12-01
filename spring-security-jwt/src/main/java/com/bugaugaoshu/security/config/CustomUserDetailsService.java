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

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomUserDetailsService() {
        UserDetails user = User.withUsername("user").password(passwordEncoder.encode("123456")).authorities(WebSecurityConfig.USER).build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("123456")).authorities(WebSecurityConfig.ADMIN).build();
        userList.add(user);
        userList.add(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetails userDetails : userList) {
            if (userDetails.getUsername().equals(username)) {
                return userDetails;
            }
        }
        throw new UsernameNotFoundException("用户名不存在，请检查用户名或注册！");
    }
}

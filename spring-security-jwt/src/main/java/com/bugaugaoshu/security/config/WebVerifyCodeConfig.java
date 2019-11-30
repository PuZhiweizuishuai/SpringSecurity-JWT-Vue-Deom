package com.bugaugaoshu.security.config;

import com.bugaugaoshu.security.util.VerifyCodeUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 14:40
 */
@Configuration
public class WebVerifyCodeConfig {
    @Bean
    public VerifyCodeUtil verifyCodeUtil() {
        return new VerifyCodeUtil();
    }
}

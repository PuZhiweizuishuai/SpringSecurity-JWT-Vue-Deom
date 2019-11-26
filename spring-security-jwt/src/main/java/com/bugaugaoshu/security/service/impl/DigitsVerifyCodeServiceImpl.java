package com.bugaugaoshu.security.service.impl;

import com.bugaugaoshu.security.service.VerifyCodeService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.SecureRandom;


/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-26 17:07
 * 验证码服务
 */
@Service
public class DigitsVerifyCodeServiceImpl implements VerifyCodeService {
    private static final int VERIFY_CODE_LENGTH = 4;

    private static final long VERIFY_CODE_EXPIRE_TIMEOUT = 60000L;

    private static String randomDigitString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    private static String appendTimestamp(String string) {
        return string + "#" + System.currentTimeMillis();
    }

    @Override
    public void send(String key) {

    }

    @Override
    public void verify(String key, String code) {

    }

    @Override
    public Image image(String key) {
        return null;
    }
}

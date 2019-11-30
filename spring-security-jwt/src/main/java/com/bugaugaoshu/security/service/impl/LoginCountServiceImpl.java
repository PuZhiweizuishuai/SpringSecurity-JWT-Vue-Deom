package com.bugaugaoshu.security.service.impl;

import com.bugaugaoshu.security.exception.LoginCountToManyException;
import com.bugaugaoshu.security.service.LoginCountService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 16:45
 */
@Service
public class LoginCountServiceImpl implements LoginCountService {
    /**
     * 登陆次数
     * */
    private final static String LOGIN_COUNT = "LOGIN_COUNT";

    /**
     * 登陆最大错误次数
     * */
    private final static int MAX_ERROR_LOGIN_COUNT = 3;

    /**
     * 达到最大错误次数后的锁定时间
     * */
    private final static long LOCK_TIME = 60000;

    public String returnNewCount(String lastCount, int count) {
        String[] sp = lastCount.split("#");
        long newCount = Long.parseLong(sp[0]);
        newCount += count;
        return newCount + "#" + System.currentTimeMillis();
    }

    @Override
    public void addLoginCount(HttpServletRequest request, int count) {
        String lastCount = (String) request.getSession().getAttribute(LOGIN_COUNT);
        if (lastCount != null) {
            request.getSession().setAttribute(LOGIN_COUNT, returnNewCount(lastCount, count));
        } else {
            request.getSession().setAttribute(LOGIN_COUNT, "1#" + System.currentTimeMillis());
        }
    }

    @Override
    public void cleanLoginCount(HttpServletRequest request) {
        request.getSession().setAttribute(LOGIN_COUNT, "1#" + System.currentTimeMillis());
    }

    @Override
    public void judgeLoginCount(HttpServletRequest request) {
        String lastCount = (String) request.getSession().getAttribute(LOGIN_COUNT);
        if (lastCount != null) {
            String[] sp = lastCount.split("#");
            int count = Integer.parseInt(sp[0]);
            long time = Long.parseLong(sp[1]);
            if (count >= MAX_ERROR_LOGIN_COUNT) {
                if (time + LOCK_TIME >= System.currentTimeMillis()) {
                    throw new LoginCountToManyException("登陆过于频繁，请稍后重试！");
                } else {
                    request.getSession().setAttribute(LOGIN_COUNT, "0#" + System.currentTimeMillis());
                }
            }
        }
    }
}

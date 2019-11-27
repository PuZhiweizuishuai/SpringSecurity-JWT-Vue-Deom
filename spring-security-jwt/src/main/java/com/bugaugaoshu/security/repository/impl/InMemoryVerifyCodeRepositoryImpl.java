package com.bugaugaoshu.security.repository.impl;

import com.bugaugaoshu.security.repository.VerifyCodeRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-26 17:07
 * 在内存中保存需要验证的验证码
 */
@Repository
public class InMemoryVerifyCodeRepositoryImpl implements VerifyCodeRepository {

    private static final int DEFAULT_VERIFY_CODE_LENGTH = 4;

    private static final Map<String, String> REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public void save(String key, String code) {
        REPOSITORY.put(key, code);
    }

    @Override
    public String find(String key) {
        return REPOSITORY.get(key);
    }

    @Override
    public void remove(String key) {
        REPOSITORY.remove(key);
    }
}

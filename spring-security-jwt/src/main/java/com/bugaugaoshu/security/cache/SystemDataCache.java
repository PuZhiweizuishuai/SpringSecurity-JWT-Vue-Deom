package com.bugaugaoshu.security.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 18:36
 */
@Component
public class SystemDataCache {
    private Map<Integer, Object> map = new HashMap<>();

    public SystemDataCache() {
        for (int i = 0; i < 10; i++) {
            map.put(i, "系统数据来自系统默认： " + i);
        }
    }

    public Map<Integer, Object> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Object> map) {
        this.map = map;
    }
}

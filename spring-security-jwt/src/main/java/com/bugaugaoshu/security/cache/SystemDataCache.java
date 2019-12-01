package com.bugaugaoshu.security.cache;

import com.bugaugaoshu.security.damain.CustomData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 18:36
 */
@Component
public class SystemDataCache {
    private Map<Integer, CustomData> map = new HashMap<>();

    public SystemDataCache() {
        for (int i = 0; i < 5; i++) {
            map.put(i, new CustomData(i ,"系统数据来自系统默认： " + i));
        }
    }

    public Map<Integer, CustomData> getMap() {
        return map;
    }

    public void setMap(Map<Integer, CustomData> map) {
        this.map = map;
    }
}

package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.cache.SystemDataCache;
import com.bugaugaoshu.security.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-25 21:54
 */
@RestController
@RequestMapping("/api")
public class HomeController {
    private final SystemDataCache systemDataCache;

    @Autowired
    public HomeController(SystemDataCache systemDataCache) {
        this.systemDataCache = systemDataCache;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/home")
    public Map<String, Object> home() {
        Map<String, Object> map = new HashMap<>();
        map.put("homeMessage", "# 基于 SpringSecurity + Jwt + Vue 的前后端分离的无状态身份验证 Demo");
        return map;
    }

    @GetMapping("/data")
    public HttpEntity select(@RequestParam(value = "id", required = false) String id) {
        if (id == null) {
            return ResponseEntity.ok().body(systemDataCache.getMap());
        }
        Map<Integer, Object> map = new HashMap<>();
        int dataId = -1;
        try {
            dataId = Integer.parseInt(id);
        } catch (Exception e) {
            throw new CustomizeException("非法的查询请求！ id=" + id);
        }
        String msg = (String) systemDataCache.getMap().get(dataId);
        if (msg == null) {
            throw new CustomizeException("没有发现这个数据！id=" + id);
        } else {
            map.put(dataId, msg);
            return ResponseEntity.ok().body(map);
        }

    }
}

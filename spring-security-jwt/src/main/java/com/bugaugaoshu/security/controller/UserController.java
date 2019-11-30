package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.cache.SystemDataCache;
import com.bugaugaoshu.security.damain.CustomData;
import com.bugaugaoshu.security.damain.ResultDetails;
import com.bugaugaoshu.security.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 17:16
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final SystemDataCache systemDataCache;

    @Autowired
    public UserController(SystemDataCache systemDataCache) {
        this.systemDataCache = systemDataCache;
    }


    @PostMapping("/data")
    public HttpEntity create(@RequestBody CustomData customData) {
        systemDataCache.getMap().put(systemDataCache.getMap().size(), "#这是用户创建的数据： " + customData.getData());
        ResultDetails resultDetails = new ResultDetails();
        resultDetails.setSuccess(true);
        resultDetails.setTimestamp(LocalDateTime.now());
        resultDetails.setMessage("Ok!");
        resultDetails.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(resultDetails);
    }


    @DeleteMapping("/data/{id}")
    public HttpEntity delete(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        int dataId = -1;
        try {
            dataId = Integer.parseInt(id);
        } catch (Exception e) {
            throw new CustomizeException("非法的删除请求！ id=" + id);
        }

        String msg = (String) systemDataCache.getMap().get(dataId);
        if (msg == null) {
            throw new CustomizeException("没有发现这个数据！id=" + id);
        } else if (msg.charAt(0) == '#') {
            systemDataCache.getMap().remove(dataId);
            map.put("message", "删除成功！");
            map.put("status", 200);
            return ResponseEntity.ok().body(map);
        } else {
            throw new CustomizeException("权限不足！");
        }

    }
}

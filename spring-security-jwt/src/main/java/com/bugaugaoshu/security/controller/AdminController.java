package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.config.WebSecurityConfig;
import com.bugaugaoshu.security.damain.CustomData;
import com.bugaugaoshu.security.damain.ResultDetails;
import com.bugaugaoshu.security.service.SystemDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;


/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 17:16
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final SystemDataService systemDataService;

    @Autowired
    public AdminController(SystemDataService systemDataService) {
        this.systemDataService = systemDataService;
    }

    @GetMapping("/isShow")
    public ResultDetails showPage() {
        ResultDetails resultDetails = new ResultDetails();
        resultDetails.setSuccess(false);
        resultDetails.setStatus(HttpStatus.OK.value());
        resultDetails.setMessage("管理员权限确定！");
        resultDetails.setTimestamp(LocalDateTime.now());
        return resultDetails;
    }

    @PostMapping("/data")
    public CustomData create(@RequestBody CustomData customData) {
        // 对 html 标签进行转义，防止 XSS 攻击
        String data = customData.getData();
        data = HtmlUtils.htmlEscape(data);
        customData.setData("*这是管理员建的数据：" + data);
        return systemDataService.create(customData);
    }

    @DeleteMapping("/data/{id}")
    public ResultDetails delete(@PathVariable("id") String id) {
        return systemDataService.delete(id, WebSecurityConfig.ADMIN);
    }
}

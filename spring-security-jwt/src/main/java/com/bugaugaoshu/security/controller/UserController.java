package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.config.WebSecurityConfig;
import com.bugaugaoshu.security.damain.CustomData;
import com.bugaugaoshu.security.damain.LoginResultDetails;
import com.bugaugaoshu.security.damain.ResultDetails;
import com.bugaugaoshu.security.service.LoginService;
import com.bugaugaoshu.security.service.SystemDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;



/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 17:16
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final SystemDataService systemDataService;


    private final LoginService loginService;

    @Autowired
    public UserController(SystemDataService systemDataService, LoginService loginService) {
        this.systemDataService = systemDataService;
        this.loginService = loginService;
    }

    @GetMapping("/loginJudge")
    public LoginResultDetails showPage() {
        return loginService.get();
    }

    @PostMapping("/data")
    public CustomData create(@RequestBody CustomData customData) {
        // 对 html 标签进行转义，防止 XSS 攻击
        String data = customData.getData();
        data = HtmlUtils.htmlEscape(data);
        customData.setData("#这是用户创建的数据：" + data);
        return systemDataService.create(customData);
    }


    @DeleteMapping("/data/{id}")
    public ResultDetails delete(@PathVariable("id") String id) {
        return systemDataService.delete(id, WebSecurityConfig.USER);
    }
}

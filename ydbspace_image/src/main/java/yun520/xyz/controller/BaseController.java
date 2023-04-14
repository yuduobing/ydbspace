package yun520.xyz.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import yun520.xyz.service.LoginService;

@Component
public class BaseController {
    @Autowired
    private LoginService loginService;

    public Long getUserid() {
        JSONObject user = getUser();
        long userid = Long.valueOf((String) user.get("userid"));
        Assert.notNull(userid, "获得userid为空");
        return userid;

    }

    //通过token去user服务调用
    public JSONObject getUser() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        Assert.notNull(details, "获得用户details为空");
        String authention = "bearer " + new JSONObject(details).get("tokenValue");
        Object userInformation = loginService.getUserInformation(authention);
        Assert.notNull(userInformation, "获得用户信息为空");
        return new JSONObject(userInformation);
    }

    public Long getUserid(String authention) {

        Assert.notNull(authention, "获得用户authention为空");
        Object userInformation = loginService.getUserInformation(authention);
        long userid = Long.valueOf((String) new JSONObject(userInformation).get("userid"));
        Assert.notNull(userid, "获得userid为空");
        return userid;

    }
}

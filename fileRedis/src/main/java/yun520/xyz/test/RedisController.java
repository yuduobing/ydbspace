package yun520.xyz.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yun520.xyz.service.RedisService;

@Api(tags = "RedisController", description = "Redis测试")
@Controller
public class RedisController {
    @Autowired(required=false)
    private RedisService redisService;


    @ApiOperation("测试简单缓存")
    @RequestMapping(value = "/redistest", method = RequestMethod.GET)
    @ResponseBody
    public Object  simpleTest() {
        String id="这是value测试";
        String key = "redis:simple:" + "测试";
        //2小时过期
        redisService.set(key, id,120);
        String key2 = "redis:simple:" + "测试map";
        redisService.hSet(key2,"1","value1",120);
        redisService.hSet(key2,"2","value1",120);
        Object ob = redisService.hGetAll(key2);

        return ob;
    }
}
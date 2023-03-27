package yun520.xyz.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("YDBSPACEUSER")
//FeignClient 微服务名
public interface LoginService {
    @GetMapping("/user/login")
//    ，如果发送的是get请求，那么需要在请求参数前加上@RequestParam注解修饰，Controller里面可以不加该注解修饰。
     boolean login(@RequestParam("username")String username, @RequestParam("password") String password);
    @GetMapping("/user/getUserInformation")
    Object getUserInformation(@RequestHeader("Authorization") String Authorization);


}

package yun520.xyz.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * 测试模块
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request) {
        Object principal = authentication.getPrincipal();
        JSONObject json = new JSONObject(principal);
        String username =(String) json.get("username");

        String header = request.getHeader("Authorization");
        String token = StrUtil.subAfter(header, "bearer ", false);
        return authentication.getPrincipal();
//        return Jwts.parser()
//                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
//                .parseClaimsJws(token)
//                .getBody();
    }

}

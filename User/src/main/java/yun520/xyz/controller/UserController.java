package yun520.xyz.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import yun520.xyz.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * 测试模块
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger logger = Logger.getLogger("UserController.class");
    @GetMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request) {
        Object principal2 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    @GetMapping("/getUserInformation")
    public User getUserInformation() {

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("验证用户信息"+user.getEmail());
        return user;
    }
}

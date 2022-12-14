package yun520.xyz.Comtroller;


import com.sun.tools.javac.comp.Check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import yun520.xyz.Service.LoginService;

//账户登陆
@RestController
public class LoginController {


  @Autowired
    private LoginService loginService;

     @GetMapping("/login")
    public boolean login(String username, String password){
        return loginService.login(username,password);

   }

}

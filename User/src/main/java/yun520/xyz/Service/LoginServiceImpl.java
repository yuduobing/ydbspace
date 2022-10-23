package yun520.xyz.Service;


import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class LoginServiceImpl implements LoginService
{
    @Override
    @GetMapping("/user/login")
    public boolean login(String username, String password) {
        System.out.println(username+password);
        return true;
    }
}

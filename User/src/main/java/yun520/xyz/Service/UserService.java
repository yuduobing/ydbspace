package yun520.xyz.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import yun520.xyz.domain.User;
import yun520.xyz.mapper.UserMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by macro on 2019/9/30.
 */
@Service
public class UserService implements UserDetailsService {

    private List<User> userList;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void initData() {
        String password = passwordEncoder.encode("123456");
        userList = new ArrayList<>();

        //这里添加额外的参数

      }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("email",username);
        User user1 = userMapper.selectOne(objectQueryWrapper);


        if (user1!=null) {
            return user1;
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
//        if (!CollectionUtils.isEmpty(findUserList)) {
//            return findUserList.get(0);
//        } else {
//            throw new UsernameNotFoundException("用户名或密码错误");
//        }
    }
}

package yun520.xyz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 切片表 前端控制器
 * </p>
 *
 * @author yuduobin
 * @since 2022-12-04
 */
@RestController
@RequestMapping("/file2")
public class FilechunkController {
    @RequestMapping("/say")
    public String test(){
        return "1111";
    }
}

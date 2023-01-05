package yun520.xyz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuduobin
 * @since 2022-11-27
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @RequestMapping("/say")
    public String test(){
        return "1111";
    }

}

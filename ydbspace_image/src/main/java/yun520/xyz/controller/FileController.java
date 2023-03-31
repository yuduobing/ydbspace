package yun520.xyz.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import yun520.xyz.service.impl.WebDavServiceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
    @Autowired
    WebDavServiceimpl webDavServiceimpl;
    @SneakyThrows
    @RequestMapping("/say")
    public String test(){

        InputStream fis = null;
        fis = new FileInputStream(new File("/Users/yuduobin/Desktop/ydbspace/fileStore/src/main/java/yun520/xyz/service/impl/文档疑问.docx"));
        webDavServiceimpl.upload("a",fis,1231,"文档疑问.docx");
        return "1111";
    }

}

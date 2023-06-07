package yun520.xyz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import yun520.xyz.util.SpringContentUtils;

@SpringBootApplication
@MapperScan("yun520.xyz.service.impl.aliyun.mapper")
//启动定时任务
//@EnableScheduling
@Import(SpringContentUtils.class)
public class FileStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileStoreApplication.class, args);
    }

}
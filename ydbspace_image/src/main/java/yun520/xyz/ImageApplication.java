package yun520.xyz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.logging.Logger;

@SpringBootApplication
@EnableFeignClients
@MapperScan("yun520.xyz.mapper")
//启动定时任务
@EnableScheduling

public class ImageApplication {
    public static void main(String[] args) {

        SpringApplication.run(ImageApplication.class, args);
    }

}

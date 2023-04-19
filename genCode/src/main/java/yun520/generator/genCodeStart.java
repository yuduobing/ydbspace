package yun520.generator;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("yun520.generator.mapper")
public class genCodeStart {
    public static void main(String[] args) {

        SpringApplication.run(genCodeStart.class, args);
    }

}
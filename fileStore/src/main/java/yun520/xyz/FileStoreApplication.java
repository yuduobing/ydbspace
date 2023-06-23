package yun520.xyz;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import yun520.xyz.util.SpringContentUtils;

@SpringBootApplication
@MapperScan("yun520.xyz.service.impl.aliyun.mapper")
//启动定时任务
//@EnableScheduling
@Import({SpringContentUtils.class, FdfsClientConfig.class})
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FileStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileStoreApplication.class, args);
    }

}

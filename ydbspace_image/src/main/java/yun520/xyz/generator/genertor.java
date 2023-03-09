package yun520.xyz.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.SQLException;



import java.util.Collections;


/*  引入这3个依赖
    <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.0</version>
        </dependency>/Users/yu/Desktop/ydbspaceydbspace_image/src/main/java/com/yun520/xyz/controller
        自定义模版https://www.cnblogs.com/chenyanbin/p/13702283.html
        自定义模版https://www.cnblogs.com/chenyanbin/p/13702283.html
 */
public class genertor
{
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://1.116.162.163:3306/webcloud", "codimd", "Tan19970925!")
                .globalConfig(builder -> {
                    builder.author("yuduobin") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir")+ "/ydbspace_image"+"/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("yun520.xyz"); // 设置父包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("filechunk")
                            // 开始实体类配置
                            .entityBuilder()
                            // 开启lombok模型
                            .enableLombok()
                            //表名下划线转驼峰
                            .naming(NamingStrategy.underline_to_camel)
                            //列名下划线转驼峰
                            .columnNaming(NamingStrategy.underline_to_camel);
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}

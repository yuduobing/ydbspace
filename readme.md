   
# 把公共类引入别的工程
      <dependency>
            <groupId>yun520.xyz</groupId>
            <artifactId>utils-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
# swagger
整合Swagger-UI
## 添加项目依赖

在pom.xml中新增Swagger-UI相关依赖

~~~java
<!--Swagger-UI API文档生产工具-->
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger2</artifactId>
  <version>2.7.0</version>
</dependency>
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>2.7.0</version>
</dependency>
~~~
## 添加Swagger-UI的配置
添加Swagger-UI的Java配置文件

注意：Swagger对生成API文档的范围有三种不同的选择

生成指定包下面的类的API文档
生成有指定注解的类的API文档
生成有指定注解的方法的API文档
~~~java
package com.macro.mall.tiny.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.yun520.xyz.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* Swagger2API文档的配置
  */
  @Configuration
  @EnableSwagger2
  public class Swagger2Config {
  @Bean
  public Docket createRestApi(){
  return new Docket(DocumentationType.SWAGGER_2)
  .apiInfo(apiInfo())
  .select()
  //为当前包下controller生成API文档
  .apis(RequestHandlerSelectors.basePackage("com.macro.mall.tiny.controller"))
  //为有@Api注解的Controller生成API文档
  //                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
  //为有@ApiOperation注解的方法生成API文档
  //                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
  .paths(PathSelectors.any())
  .build();
  }

  private ApiInfo apiInfo() {
  return new ApiInfoBuilder()
  .title("SwaggerUI演示")
  .description("mall-tiny")
  .contact("macro")
  .version("1.0")
  .build();
  }
  }
~~~
## 用法
@ApiOperation："用在请求的方法上，说明方法的作用" value="说明方法的作用" notes="方法的备注说明"

@ApiImplicitParams：用在请求的方法上，包含一组参数说明
@ApiImplicitParam：对单个参数的说明	    
name：参数名
value：参数的说明、描述
required：参数是否必须必填
paramType：参数放在哪个地方
· query --> 请求参数的获取：@RequestParam
· header --> 请求参数的获取：@RequestHeader	      
· path（用于restful接口）--> 请求参数的获取：@PathVariable
· body（请求体）-->  @RequestBody User user
· form（普通表单提交）	   
dataType：参数类型，默认String，其它值dataType="Integer"	   
defaultValue：参数的默认值

# 如何上传一个图片流和信息
 


## rabbitmq
 rabbitmq 发送者 简单模式不加交换机；接收者则都是监听消息通道
~~~java
 @RabbitListener
 public void receive（String in）
 {
 System.out.println(in)
 }
 ~~~
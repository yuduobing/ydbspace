package yun520.generator.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author ruoyi
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}

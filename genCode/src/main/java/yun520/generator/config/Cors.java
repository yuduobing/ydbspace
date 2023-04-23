package yun520.generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;

//之前说过用拦截器去处理浏览器的OPTIONS请求来解决跨域问题，但是如果项目中配置了一些校验请求的过滤器则会使其失效，因为过滤器比拦截器要早，所以有可能会使其失效。
@Configuration
public class Cors implements WebMvcConfigurer {
    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 跨域拦截器需放在最上面
//        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");

    }

    //  过滤器
    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        //开放哪些ip、端口、域名的访问权限，星号表示开放所有域，但是返回前端仍然是具体的地址
        config.addAllowedOrigin("*");
        //是否允许发送Cookie信息
        config.setAllowCredentials(true);
         //不能*
        config.setAllowedHeaders( Arrays.asList("Authorization","Content-Type"));
        //开放哪些Http方法，允许跨域访问
        config.addAllowedMethod("*");

//        config.addAllowedMethod("*");

        //添加映射路径，“/**”表示对所有的路径实行全局跨域访问权限的设置
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);

    }
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
//        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
//        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
//        corsConfiguration.addAllowedMethod("*");
//        // corsConfiguration.setAllowCredentials(true); // 允许cookies跨域
//        return corsConfiguration;
//    }
//
//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig());
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
//        // 设置 Filter 的优先级为最高优先级(如果有多个过滤器这些过滤器会有一个先后顺序的问题)
//        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return filterRegistrationBean;
//    }

}

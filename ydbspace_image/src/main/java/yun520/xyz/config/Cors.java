package yun520.xyz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class Cors {
    @Bean
    public CorsFilter corsFilter(){
        // SpringMvc 提供了 CorsFilter 过滤器


        // 初始化cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许跨域的域名，如果要携带cookie,不要写*，*：代表所有域名都可以跨域访问
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);  // 设置允许携带cookie
        corsConfiguration.addAllowedMethod("*"); // 代表所有的请求方法：GET POST PUT DELETE...
        corsConfiguration.addAllowedHeader("*"); // 允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        // 返回corsFilter实例，参数：cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}

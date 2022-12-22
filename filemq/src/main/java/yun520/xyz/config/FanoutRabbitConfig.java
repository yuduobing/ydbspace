package yun520.xyz.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConditionalOnProperty(value = "ydb.rabbitmq.fanout")
public class FanoutRabbitConfig {
//    从配置文件中获取配置使用@value，当你在bean加载时调用@value时会出现空指针异常。因为bean加载完成后才会执行@value。
   @Autowired
   MqConfig mqConfig;

    //第一步绑定交换机
    @Bean
    public FanoutExchange fontexchange(){
        //todo 配置文件
        return new FanoutExchange(mqConfig.exchangname);
    }
    /**
     * 2.
     * 声明队列
     * @return
     */
    @Bean
    public Queue emailQueue() {
        /**
         * Queue构造函数参数说明
         * 1. 队列名
         * 2. 是否持久化 true：持久化 false：不持久化   队列得名称 是否持久化 是否自动删除，是否独占
         */
        return new Queue(mqConfig.emailQueue, true);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(fontexchange());
    }

}

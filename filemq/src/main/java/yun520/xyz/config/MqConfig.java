package yun520.xyz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqConfig {
    public String getExchangname() {
        return exchangname;
    }

    public void setExchangname(String exchangname) {
        this.exchangname = exchangname;
    }

    public String getEmailQueue() {
        return emailQueue;
    }

    public void setEmailQueue(String emailQueue) {
        this.emailQueue = emailQueue;
    }

    @Value("${ydb.rabbitmq.exchangname:morenexchangname}")
    public  String exchangname;
    @Value("${ydb.rabbitmq.emailQueue:morenemailQueue}")
    public  String emailQueue;
}

package yun520.xyz.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.config.MqConfig;
import yun520.xyz.service.MqService;

public class MqServiceImpl implements MqService {
    @Autowired
    private RabbitTemplate template;
    @Override
    public void send(String message) {
        template.convertAndSend(MqConfig.exchangname,"",message);

    }


}

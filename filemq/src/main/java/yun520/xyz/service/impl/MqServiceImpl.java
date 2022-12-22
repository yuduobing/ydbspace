package yun520.xyz.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yun520.xyz.config.MqConfig;
import yun520.xyz.service.MqService;
@Component("MqService")
public class MqServiceImpl implements MqService {
    @Autowired
    MqConfig mqConfig;
    @Autowired
    private RabbitTemplate template;
    @Override
    public void send(String message) {
        template.convertAndSend(mqConfig.exchangname,"",message);

    }


}

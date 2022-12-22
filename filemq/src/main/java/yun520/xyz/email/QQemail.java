package yun520.xyz.email;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "ydb.email")
public class QQemail {


    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @RabbitListener(queues="deletequeue")
    public  void receive(String in){
        System.out.println("接受rabbitmq信息"+in);
        sendMessage();
    }

    public void sendMessage(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是一个标题");
        message.setText("这是邮箱正文的内容");
        message.setFrom("1510557673@qq.com");//发送者的邮箱地址
        message.setTo("1510557673@qq.com"); //接收者的邮箱地址
        javaMailSender.send(message);
    }
}

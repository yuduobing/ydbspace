package yun520.xyz.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class deleteshare {
    @Scheduled(fixedRate = 2000)
    public void deleteshare(){
        //删除完毕调用tabbitmq发送邮件通知
        System.out.println("删除定时任务");
    }
}

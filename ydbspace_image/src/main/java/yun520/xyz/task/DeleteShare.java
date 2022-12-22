package yun520.xyz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yun520.xyz.entity.File;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.service.impl.MqServiceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import yun520.xyz.service.impl.MqServiceImpl;

@Component
public class DeleteShare {
    @Autowired
    MqServiceImpl mqService;
    @Autowired
    yun520.xyz.mapper.FileMapper filemapper;
    @Autowired
    SharelinksMapper sharelinksmapper;
    @Autowired
    FilechunkMapper filechunkMapper;
    @RabbitListener(queues="deletequeue")
    public  void receive(String in){
        System.out.println("接受rabbitmq信息2"+in);
    }

//    @Scheduled(cron = "0 35 1 * * ?")
@Scheduled(fixedRate = 2000)
    public void deleteshare(){
        //删除所有已经过了24小时的链接表

        //删除24小时的文件  1根据文件表md5找到文件  2删除切片后删除文件表
        QueryWrapper<File> queryWrapperfile = new QueryWrapper<File>();
     Calendar instance = Calendar.getInstance();
    instance.add(Calendar.DAY_OF_MONTH,-1);
    Date delettime = instance.getTime();
    queryWrapperfile.apply(
            "DATE_FORMAT (createTime,'%Y-%m-%d') <= DATE_FORMAT ({0},'%Y-%m-%d')", delettime);

    queryWrapperfile.eq("fileSaveType","0");
    //todo 数量过大要分页
             List<File> userInfoList2 = filemapper.selectList(queryWrapperfile);

            //   遍历删除还是要确定其他永久文件md5是否存在
         userInfoList2.forEach(val->{
             val.getFilemd5();

         });


        QueryWrapper<Filechunk> queryWrapper = new QueryWrapper<Filechunk>();
        queryWrapper.eq("chunkmd5",userInfoList2.get(0).getFilemd5()).orderByAsc("chunksnum");
        List<Filechunk> userInfoList = filechunkMapper.selectList(queryWrapper);


        //删除完毕调用tabbitmq发送邮件通知
        mqService.send("hello"+new Date());
        System.out.println("删除定时任务");
    }


    //先把永久性文件存进一个map，不
   public boolean isSafedelte(String identifier) {
        //1 永久性文件



   }



}

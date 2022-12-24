package yun520.xyz.task;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yun520.xyz.entity.File;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.entity.Sharelinks;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.service.impl.FastDfsServiceimpl;
import yun520.xyz.service.impl.MqServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    @Autowired
    FastDfsServiceimpl fastdfs;


        @Scheduled(cron = "0 35 1 * * ?")
    @Scheduled(fixedRate = 2000)
    public void deleteshare() {
        try {

            //void
            AtomicReference<Integer> num = new AtomicReference<>(0);
            AtomicInteger nums = new AtomicInteger();
            //删除所有已经过了24小时的链接表

            //删除24小时的文件  1根据文件表md5找到文件  2删除切片后删除文件表
            QueryWrapper<File> queryWrapperfile = new QueryWrapper<File>();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DAY_OF_MONTH, -1);
            Date delettime = instance.getTime();
            queryWrapperfile.apply(
                    "DATE_FORMAT (createTime,'%Y-%m-%d') <= DATE_FORMAT ({0},'%Y-%m-%d')", delettime);
            queryWrapperfile.eq("fileSaveType", "0");
            //todo 数量过大要分页
            List<File> userInfoList2 = filemapper.selectList(queryWrapperfile);
            //遍历删除还是要确定其他永久文件md5是否存在
            userInfoList2.forEach(val -> {
                boolean safedelte = isSafedelte(val.getFilemd5());
                //切片是否可以安全删除
                if (safedelte) {
                    nums.getAndIncrement();
//                开始删除  删除文件  删除切片表  删除文件表
                    QueryWrapper<Filechunk> queryWrapperfilechunk = new QueryWrapper<Filechunk>();
                    queryWrapperfilechunk.eq("chunkmd5",val.getFilemd5());

                    List<Filechunk> userInfoListchunk= filechunkMapper.selectList(queryWrapperfilechunk);
                    userInfoListchunk.forEach(valchunk->{
                        fastdfs.delete(valchunk.getChunkpath());

                    });
                    //删除切片表
                    int delete = filechunkMapper.delete(queryWrapperfilechunk);


                }

            });
            //删除文件，有些文件
            filemapper.delete(queryWrapperfile);
             //删除过期链接 todo 分享时间判断更加灵活
                        QueryWrapper<Sharelinks> queryWrapperfilesare = new QueryWrapper<Sharelinks>();
                        queryWrapperfilesare.apply(
                                "DATE_FORMAT (sharetime,'%Y-%m-%d') <= DATE_FORMAT ({0},'%Y-%m-%d')", delettime);
            int deleteshare = sharelinksmapper.delete(queryWrapperfilesare);


            //删除完毕调用tabbitmq发送邮件通知
            mqService.send("删除任务成功，删除临时文件总数" + nums.get());


        }
        catch (Exception e) {
            mqService.send("发生异常了" + e);
        }
    }

    //是否可以安全删除
    public boolean isSafedelte(String identifier) {
        AtomicReference<Boolean> bz = new AtomicReference<>(true);
        //1 永久性文件是否包含
        QueryWrapper<File> queryWrapperfile2 = new QueryWrapper<File>();
        queryWrapperfile2.eq("filemd5", identifier);
        List<File> userInfoListpermanent = filemapper.selectList(queryWrapperfile2);

        userInfoListpermanent.forEach(val -> {
            if (Integer.valueOf(val.getFileSaveType()) != 0) {
                bz.set(false);
            } else {
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = LocalDateTime.now();
                ZonedDateTime zdt = val.getCreateTime().atZone(zoneId);
                Date datefrom = Date.from(zdt.toInstant());
                if (DateUtil.between(datefrom, new Date(), DateUnit.DAY) < 1) {
                    bz.set(false);
                }
            }
        });

        return bz.get();

    }

}

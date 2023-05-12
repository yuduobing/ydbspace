package yun520.xyz.service.impl.aliyun.entity;

import lombok.Builder;
import lombok.Data;
import org.apache.poi.hpsf.Date;
import yun520.xyz.util.DefaultHttpProxy;

//阿里云账户实体
@Data
@Builder
public class Aliyun {
    //用户id admin 用户为
    private      String  userid="";
    private      String  driveId="";
    private      String  accessToken="";
    private      String  refreshToken="";
    //是否激活正常使用 0关闭 1开启
    private      String  active="1";
    //刷新时间
    private Date updatetimer=null;
    //过期时间 ms
    private   int timeout=1;

}

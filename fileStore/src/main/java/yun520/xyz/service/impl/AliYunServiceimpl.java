package yun520.xyz.service.impl;

import cn.hutool.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;
import yun520.xyz.service.impl.aliyun.AliyunSDK;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component("AliYunService")
@Scope(value = "prototype")
public class AliYunServiceimpl implements StoreService {
    @Resource
    AliyunSDK aliyunSDK;


    //groupName传dev
    @Override
    public String upload(String deviceid, InputStream inputStream, long fileSize, String fileExtName) {
        //s
        JSONObject uplaodfile = aliyunSDK.uplaodfile(deviceid, fileExtName, inputStream);


        return  deviceid+"+"+uplaodfile.getStr("file_id");
    }

    @Override
    public String upload(byte[] bytes, long fileSize, String extension) {
        return null;
    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }
//下载

    @Override
    public byte[] download(String path) {
        return new byte[0];
    }

    //devricid+fileid
    @Override
    public String downloadUrl(String path) {
        //个人信息里的driverid
        String[] split = path.split("\\+");
        JSONObject json = null;
        try {
            json = aliyunSDK.downfile(split[0],split[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url =json!=null?json.getStr("url"):"";
       return url;
    }

    @Override
    public void delete(String path) {

    }
}

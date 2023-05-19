package yun520.xyz.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;

import java.io.InputStream;

@Component("AliYunService")
@Scope(value = "prototype")
public class AliYunServiceimpl implements StoreService {
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        return null;
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

    @Override
    public void delete(String path) {

    }
}

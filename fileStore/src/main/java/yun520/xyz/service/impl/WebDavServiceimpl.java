package yun520.xyz.service.impl;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//fastdfs 实现类
@Component("WebDavServiceimpl")
@Scope(value = "prototype")
@Primary
public class WebDavServiceimpl implements StoreService {
    //  //文件名不可有中文
    Sardine sardine = SardineFactory.begin("admin","MNR52kWq");
    @Autowired
    private FastFileStorageClient storageClient;

    //上传有group
    @SneakyThrows
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String day = sdf.format(new Date());
        String baseurl = "http://1.116.162.163:5244/dav/webcloud/" + day.substring(0,6) + "/"+ day.substring(6,8) + "/";
            if (!sardine.exists(baseurl)) {

                sardine.createDirectory(baseurl);
            }
            baseurl=baseurl+fileExtName;
            sardine.put(baseurl, inputStream);

        return baseurl;
    }

    @Override
    public String upload(byte[] bytes, long fileSize, String extension) {

        return null;

    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }


    @SneakyThrows
    @Override
    public InputStream download(String filePath) {

        InputStream inputStream = sardine.get(filePath);

        return inputStream;

    }

    @SneakyThrows
    @Override
    public void delete(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {

                sardine.delete(filePath);

        }

    }
}

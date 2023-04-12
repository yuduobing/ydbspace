package yun520.xyz.service.impl;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
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
import java.util.logging.Logger;

//fastdfs 实现类
@Component("WebDavService")
@Scope(value = "prototype")
@Primary
public class WebDavServiceimpl implements StoreService {
    private static Logger logger = Logger.getLogger("WebDavServiceimpl.class");
    //  //文件名不可有中文
    Sardine sardine = SardineFactory.begin("admin", "MNR52kWq");
    @Autowired
    private FastFileStorageClient storageClient;

    //上传有group
    //文件扩展名
    @SneakyThrows
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String day = sdf.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String filename = sdf2.format(new Date());
        String baseurl = "http://1.116.162.163:5244/dav/webcloud/" + day.substring(0, 6) + "/" + day.substring(6, 8) + "/";
//   判断存在报错         if (!sardine.exists(baseurl)) {
        sardine.createDirectory(baseurl);
//            }

        baseurl = baseurl + filename + "." + fileExtName;
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
    public byte[] download(String filePath) {
        int a = 1;
        boolean begin =true;
//        失败重试机制
        InputStream inputStream = null;

        while ( 1< a&& a < 5 ||begin==true ) {
            if (begin==true ){
                begin=false;
            }
            try {

                inputStream = sardine.get(filePath);
            } catch (Exception e) {
                a++;
                logger.severe("webdav下载失败" + e.getMessage() + "重试次数" + a+ "路径" + filePath);
                if (a > 4) {
                    throw e;
                }
            }
        }

        return IOUtils.toByteArray(inputStream);

    }

    @SneakyThrows
    @Override
    public void delete(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {

            sardine.delete(filePath);

        }

    }
}

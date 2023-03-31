package yun520.xyz.service.impl;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WebDavServiceimpl implements StoreService {
    @Autowired
    private FastFileStorageClient storageClient;

    //上传有group
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        //  //文件名不可有中文
        Sardine sardine = SardineFactory.begin("admin","MNR52kWq");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String day = sdf.format(new Date());
        String baseurl = "http://1.116.162.163:5244/dav/webcloud/" + day + "/";
        try {
            if (sardine.exists(baseurl)) {
                System.out.println("/content/dam folder exists");
            }
            sardine.createDirectory(baseurl);
            baseurl=baseurl+fileExtName;

            sardine.put(baseurl, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseurl+fileExtName;

    }

    @Override
    public String upload(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // 元数据
        Set<MetaData> metaDataSet = new HashSet<MetaData>();
        metaDataSet.add(new MetaData("dateTime", LocalDateTime.now().toString()));
        metaDataSet.add(new MetaData("Author", "Layne"));
        StorePath storePath = storageClient.uploadFile(bais, fileSize, extension, metaDataSet);
        return storePath.getFullPath();

    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }

    @Override
    public byte[] download(String filePath) {

        byte[] bytes = null;
        if (StringUtils.isNotBlank(filePath)) {
            String group = filePath.substring(0, filePath.indexOf("/"));
            String path = filePath.substring(filePath.indexOf("/") + 1);
            DownloadByteArray byteArray = new DownloadByteArray();
            bytes = storageClient.downloadFile(group, path, byteArray);
        }
        return bytes;

    }

    @Override
    public void delete(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            storageClient.deleteFile(filePath);
        }

    }
}

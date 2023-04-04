package yun520.xyz.service.impl;


import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadFileStream;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//fastdfs 实现类
@Component("FastDfsService")
@Scope(value = "prototype")
public class FastDfsServiceimpl  implements StoreService{
    @Autowired
    private FastFileStorageClient storageClient;
     //fastdfs非集群默认有group
    final static  String group="group1";


    //上传有group
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        //文件名不可有中文
        StorePath sp=storageClient.uploadFile(groupName,inputStream, fileSize,fileExtName);
        return sp.getFullPath();
    }

    @Override
    public String upload(byte[] bytes,  long fileSize, String extension){
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // 元数据
        Set<MetaData> metaDataSet = new HashSet<MetaData>();
        metaDataSet.add(new MetaData("dateTime", LocalDateTime.now().toString()));
        metaDataSet.add(new MetaData("Author","Layne"));
        StorePath storePath = storageClient.uploadFile(bais, fileSize, extension, metaDataSet);
        return storePath.getFullPath();

    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }

    @Override
    public InputStream download(String filePath) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (StringUtils.isNotBlank(filePath)) {
            String group = filePath.substring(0, filePath.indexOf("/"));
            String path = filePath.substring(filePath.indexOf("/") + 1);
            DownloadFileStream stream = new DownloadFileStream(outputStream);
            storageClient.downloadFile(group, path, stream);

        }
        return inputStream;


    }

    @Override
    public void delete(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            storageClient.deleteFile(filePath);
        }

    }
}

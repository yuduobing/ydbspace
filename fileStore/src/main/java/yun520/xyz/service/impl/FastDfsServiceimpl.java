package yun520.xyz.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;

import java.io.InputStream;
import java.util.logging.Logger;

//fastdfs 实现类
@Component("FastDfsService")
public class FastDfsServiceimpl implements StoreService {
    private static Logger logger = Logger.getLogger("StoreService.class");
    @Autowired
    private FastFileStorageClient storageClient;

    //fastdfs非集群默认有group
    final static String group = "group1";

    //上传有group
    //fileExtName 文件扩展名
    @Override
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        //文件名不可有中文
        StorePath sp = storageClient.uploadFile(groupName, inputStream, fileSize, fileExtName);
        return sp.getFullPath();
    }

    @Override
    public String upload(byte[] bytes, long fileSize, String extension) {
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        // 元数据
//        Set<MetaData> metaDataSet = new HashSet<MetaData>();
//        metaDataSet.add(new MetaData("dateTime", LocalDateTime.now().toString()));
//        metaDataSet.add(new MetaData("Author","Layne"));
//        StorePath storePath = storageClient.uploadFile(bais, fileSize, extension, metaDataSet);
        return null;

    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }

    @Override
    public byte[] download(String filePath) {
        byte[] bytes = null;
        int a = 1;
        boolean begin = true;
        String group = filePath.substring(0, filePath.indexOf("/"));
        String path = filePath.substring(filePath.indexOf("/") + 1);
        while (1 < a && a < 5 || begin == true) {
            if (begin == true) {
                begin = false;
            }
            try {
                if (StringUtils.isNotBlank(filePath)) {
                    DownloadByteArray byteArray = new DownloadByteArray();
                    bytes = storageClient.downloadFile(group, path, byteArray);
                }
            } catch (Exception e) {
                a++;
                logger.severe("webdav下载失败" + e.getMessage() + "重试次数" + a + "路径" + filePath);
                if (a > 4) {
                    throw e;
                }
            }
        }

        return bytes;

    }

    @Override
    public String downloadUrl(String path) {
        return null;
    }

    @Override
    public void delete(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            storageClient.deleteFile(filePath);
        }
    }
}

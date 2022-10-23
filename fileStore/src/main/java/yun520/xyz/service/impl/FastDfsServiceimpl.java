package yun520.xyz.service.impl;


import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;
//fastdfs 实现类
@Component("FastDfsService")
public class FastDfsServiceimpl  implements StoreService{
    @Autowired
    private FastFileStorageClient storageClient;
     //fastdfs非集群默认有group
    final static  String group="0000";


    //上传有group
    @Override
    public String upload(String group, byte[] bytes, String fileName) {
        StorePath sp=storageClient.uploadFile(group, bytes, fileName);
        return sp.getFullPath();
    }
    //上传无group
    @Override
    public String upload(byte[] bytes, String fileName) {
        StorePath sp=storageClient.uploadFile(group, bytes, fileName);
        return sp.getFullPath();
    }
    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }

    @Override
    public byte[] download(String path) {
        return new byte[0];
    }

    @Override
    public void delete(String path) {

    }
}

package yun520.xyz.service.impl;


import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yun520.xyz.service.StoreService;
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
    public String upload(String group, byte[] bytes, String fileName) {
        StorePath sp=storageClient.uploadFile(group, bytes, fileName);
        return sp.getFullPath();
    }
    //上传无group
    @Override
    public String upload(byte[] bytes, String fileName) {
        //  //文件名不可有中文
        StorePath sp=storageClient.uploadFile(bytes, fileName);
        return sp.getFullPath();
    }

    @Override
    public byte[] download(String group, String path) {
        return new byte[0];
    }

    @Override
    public byte[] download(String path) {

        String substring = path.split("/")[0];
        String path2 = path.substring( path.indexOf("/")+1,path.length());
        //todo 这里必须填上分组
        byte[] bytes=storageClient.downloadFile("group1",path2);
        return bytes;
    }

    @Override
    public void delete(String path) {

    }
}

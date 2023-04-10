package yun520.xyz.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface StoreService {
    /**
     * 上传
     * @param group
     * @param bytes
     * @param fileName
     */
    public String upload(String groupName, InputStream inputStream, long fileSize, String fileExtName);

    public String upload(byte[] bytes,  long fileSize, String extension);
    /**
     * 下载
     * @param group
     * @param path
     * @return
     */
    public byte[] download(String group,String path);
    /**
     * 下载
     * @param path
     * @return
     */
    public  byte[]  download(String path);

    /**
     * 删除
     * @param path
     */
    public void delete(String path);


}

package yun520.xyz.service;

public interface StoreService {
    /**
     * 上传
     * @param group
     * @param bytes
     * @param fileName
     */
    public String upload(String group,byte[] bytes,String fileName);

    public String upload(byte[] bytes,String fileName);
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
    public byte[] download(String path);
    /**
     * 删除
     * @param path
     */
    public void delete(String path);


}

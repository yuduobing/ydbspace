package yun520.xyz.entity;

import lombok.Data;

@Data
public class FileWeb {
    public  String  filename;
    public  String  totalSize;
    public  String  chunkSize;
  //当前第几块
    public  int  chunkNumber;
    public  int  totalChunks;
    public  String  identifier;

    //文件id
    public  String  fid;
}

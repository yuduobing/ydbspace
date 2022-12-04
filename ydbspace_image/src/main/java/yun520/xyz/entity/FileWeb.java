package yun520.xyz.entity;

import lombok.Data;

@Data
public class FileWeb {
    public  String  filename;
    public  String  totalSize;
    public  String  chunkSize;
    public  String  chunkNumber;
    public  String  totalChunks;
    public  String  identifier;
}

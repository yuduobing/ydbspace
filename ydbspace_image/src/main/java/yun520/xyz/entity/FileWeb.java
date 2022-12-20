package yun520.xyz.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileWeb {
    public  String  filename;
    public  String  totalSize;
    public  String  chunkSize;
  //当前第几块
    public  int  chunkNumber;
    public  int  totalChunks;
    public  String  identifier;

    //文件id
    public Integer fid;

      //文件id
     public  String  filetype;

    //是否加密
    public  Boolean  toencrypt;


}

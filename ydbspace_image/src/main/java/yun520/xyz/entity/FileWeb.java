package yun520.xyz.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yun520.xyz.chain.core.ContextRequest;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileWeb  extends ContextRequest {
    public  String  filename;
    public  String  userId;
    public  String  totalSize;
    public  String  chunkSize;
  //当前第几块
    public  int  chunkNumber;
    public  int  totalChunks;
    public  String  identifier;
    public  String  filePath;
    //文件id
    public Integer fid;

      //文件id
     public  String  filetype;

    //是否加密
    public  Boolean  toencrypt;
//切片存储路径
    public  String  chunkpath;
    //删除会用到的文件id
    public List<String> deleteList;
    //是否是文件夹
    public Integer isDir;
    public Integer userFileId;
}

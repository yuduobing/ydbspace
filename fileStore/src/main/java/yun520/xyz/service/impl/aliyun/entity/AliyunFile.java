package yun520.xyz.service.impl.aliyun.entity;

import lombok.Data;

//文件信息
@Data
public class AliyunFile {
    //父文件id
  public String  parent_file_id;
  //文件名
    public String  filename;
//    文件类型
    public String  type;
}

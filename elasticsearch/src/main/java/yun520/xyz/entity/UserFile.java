package yun520.xyz.entity;

import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;

import java.io.Serializable;

@IndexName(value = "userfile-mysql")
@Data
public class UserFile implements Serializable {
    private static final long serialVersionUID = -1L;
    @IndexId(type = IdType.CUSTOMIZE)
    private int  userFileId;
    private String fileName;
    private String filePath;
}

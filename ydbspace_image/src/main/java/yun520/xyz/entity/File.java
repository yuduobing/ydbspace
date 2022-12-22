package yun520.xyz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuduobin
 * @since 2022-11-27
 */

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "File对象", description = "")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;
     @TableId(type= IdType.AUTO)
      private Integer fid;

    @ApiModelProperty("文件名")

    private String fileName;

    @ApiModelProperty("文件类型zip txt")
    private String fileType;

    @ApiModelProperty("文件大小")
    private String fileSize;

    @ApiModelProperty("文件种类")
    private String fileSaveType;

    @ApiModelProperty("文件md5")
    private String filemd5;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}

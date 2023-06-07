package yun520.xyz.entity;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-01-09
 */
@Data
@ApiModel(value = "Userfile对象", description = "")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Userfile implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type= IdType.AUTO)
    private Integer userFileId;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;

    @ApiModelProperty("删除标识(0-未删除，1-已删除)")
    private Integer deleteFlag;

    @ApiModelProperty("删除时间")
    private String deleteTime;

    @ApiModelProperty("扩展名")
    private String extendName;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("修改时间")
    private Date uploadTime;

    @ApiModelProperty("用户id")
    private Long userId;


}

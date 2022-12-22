package yun520.xyz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuduobin
 * @since 2022-12-09
 */
@Getter
@Setter
@ApiModel(value = "Sharelinks对象", description = "")
@Builder
public class Sharelinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文件id")
    private Long fileid;

    @ApiModelProperty("允许下载的次数")
    private Integer permitdownnum;

    @ApiModelProperty("生成下载的网址")
    private String downweb;

    @ApiModelProperty("是否加密")
    private Boolean toencrypt;

    @ApiModelProperty("加密密钥")
    private String passworld;

    @ApiModelProperty("分享时间")
    private LocalDateTime sharetime;


}

package yun520.xyz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 切片表
 * </p>
 *
 * @author yuduobin
 * @since 2022-12-04
 */
@Getter
@Setter
@Builder
@ApiModel(value = "Filechunk对象", description = "切片表")
public class Filechunk implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("文件md5")
    private String chunkmd5;

    @ApiModelProperty("切片大小")
    private String chunksize;

    @ApiModelProperty("切片路径")
    private String chunkpath;

    @ApiModelProperty("切片总数")
    private Integer chunktotalnum;

    @ApiModelProperty("第几片")
    private Integer chunksnum;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}

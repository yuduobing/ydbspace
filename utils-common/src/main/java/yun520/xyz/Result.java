package yun520.xyz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    //响应编码
    @ApiModelProperty(value="返回值成功为0")
    private Integer code;
    private String msg;
    private Object data;
}

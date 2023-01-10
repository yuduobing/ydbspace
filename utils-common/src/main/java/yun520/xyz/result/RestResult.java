package yun520.xyz.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一结果返回
 * @param <T> 参数泛型
 */
@Data
@Schema(name = "统一结果返回",required = true)
public class RestResult<T> {
    @Schema(description = "请求是否成功", example = "true")
    private Boolean success = true;
    @Schema(description = "返回码", example = "000000")
    private Integer code = 000000;
    @Schema(description = "返回信息", example = "成功")
    private String message;
    @Schema(description = "返回数据")
    private T data;

    // 通用返回成功
    public static RestResult success() {
        RestResult r = new RestResult();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    // 通用返回失败，未知错误
    public static RestResult fail() {
        RestResult r = new RestResult();
        r.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    // 设置结果，形参为结果枚举
    public static RestResult setResult(ResultCodeEnum result) {
        RestResult r = new RestResult();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    // 自定义返回数据
    public RestResult data(T param) {
        this.setData(param);
        return this;
    }

    // 自定义状态信息
    public RestResult message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public RestResult code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public RestResult success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

}

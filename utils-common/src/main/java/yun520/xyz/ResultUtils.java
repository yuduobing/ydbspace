package yun520.xyz;

import java.util.Map;

public class ResultUtils {
    public static Result success(String message, Map<String,Object> data){
        Result result=new Result();
        result.setCode(0);
        result.setMsg(message);
        result.setData(data);
        return result;
    }
    public static Result success( Map<String,Object> data){
        Result result=new Result();
        result.setCode(0);
        result.setMsg("δΊ€ζζε");
        result.setData(data);
        return result;
    }
    public static  Result  error(String errorMsg){
        Result ar=new Result();
        ar.setCode(1);
        ar.setMsg(errorMsg);
        ar.setData(null);
        return ar;
    }
}

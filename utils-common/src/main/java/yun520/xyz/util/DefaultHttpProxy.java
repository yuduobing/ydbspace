package yun520.xyz.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

import java.util.Map;
import java.util.logging.Logger;
/*
http工具类
 */
public class DefaultHttpProxy {
    private static Logger logger = Logger.getLogger("DefaultHttpProxy.class");
//    请求头
    private Map<String,String>  headerMap;
    private  Integer   timeout=300000;

    private  String  charset="utf-8";
    public  DefaultHttpProxy(Map<String,String> headerMap,Integer timeout,String charset) {
        this.headerMap=headerMap;
        this.headerMap.put("Content-Type","application/json");

        this.timeout=timeout;
        this.charset=charset;
    }
    //请求json
    //返回json post
    public JSONObject post(String url,JSONObject  body){
        logger.info("请求参数"+body+"请求地址"+url+"请求头"+headerMap);
        String bodyJSon = HttpUtil.createPost(url).timeout(timeout).charset(charset).body(body.toString())
                .headerMap(headerMap, true).execute().body();
        logger.info("返回参数"+bodyJSon);
        return new JSONObject(bodyJSon);

    }
    public JSONObject post(String url){
        logger.info("请求地址"+url+"请求头"+headerMap);
        String bodyJSon = HttpUtil.createPost(url).timeout(timeout).charset(charset)
                .headerMap(headerMap, true).execute().body();
        logger.info("返回参数"+bodyJSon);
        return new JSONObject(bodyJSon);

    }
    public JSONObject get(String url,JSONObject  body){
        logger.info("请求参数"+body+"请求地址"+url+"请求头"+headerMap);
        String bodyJSon = HttpUtil.createGet(url).timeout(timeout).charset(charset).body(body.toString())
                .headerMap(headerMap, true).execute().body();
        logger.info("返回参数"+bodyJSon);
        return new JSONObject(bodyJSon);

    }
}

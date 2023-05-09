package yun520.xyz.service.impl.aliyun;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import yun520.xyz.util.DefaultHttpProxy;

import java.util.HashMap;

public class AliyunSDK {
//    1access_token 获取用户信息，并把用户信息保存到threadlocal
//GET 域名 + /oauth/users/info
    private  static  final  String  baseUrl="https://open.aliyundrive.com/";
    private  static  final  String  client_id="6a596f96a26544e7897f01ce321f0cd8";
    private  static  final  String  client_secret="8b7ba7ff53e44d1dba04c3dd355dc6da";
    private  static  final  String  grant_type="authorization_code";
    private  static  final  String  code="55eb9ee2f6064ca29a0d366381aec30a";
    private    final DefaultHttpProxy  client=null;

    /*
    获取授权吗
     */
//    {
//        "token_type": "Bearer",
//            "access_token": "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMmRlMTA3M2I3MDM0N2MwYjk3ODcwNzU0ZDA3OTkzNiIsImF1ZCI6IjZhNTk2Zjk2YTI2NTQ0ZTc4OTdmMDFjZTMyMWYwY2Q4IiwiaXNzIjoiaHR0cHM6Ly9vcGVuLmFsaXl1bmRyaXZlLmNvbSIsImV4cCI6MTY4MzYyNTk1OCwiaWF0IjoxNjgzNjE4MTU4fQ.sLuDXSwL-HquiKwGxG-WDYNWGbWQTDZ4Tr_hct916A8",
//            "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMmRlMTA3M2I3MDM0N2MwYjk3ODcwNzU0ZDA3OTkzNiIsImF1ZCI6IjZhNTk2Zjk2YTI2NTQ0ZTc4OTdmMDFjZTMyMWYwY2Q4IiwiZXhwIjoxNjkxMzk0MTU4LCJpYXQiOjE2ODM2MTgxNTh9.gKLfK_m16RAZ-knyAS5eg_zFlPU1CfAFt3ezOoSoOexM2yXWuz9CVmJgUDx6ELudYBDMQgLJzAONgARI65osug",
//            "expires_in": 7200
//    }
    public JSON getAccess_token(){
        String url=baseUrl+"oauth/access_token ";
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("client_id",client_id);
        objectObjectHashMap.put("client_secret",client_secret);
        objectObjectHashMap.put("grant_type",grant_type);
        objectObjectHashMap.put("code",code);
        JSONObject entries = new JSONObject(objectObjectHashMap);
        String post = HttpUtil.post(url, entries.toString(),30000);
        //这里有个很重要的accesstoken存储
        return  new JSONObject(post);

    }
     /*
    下载文件
     */
}

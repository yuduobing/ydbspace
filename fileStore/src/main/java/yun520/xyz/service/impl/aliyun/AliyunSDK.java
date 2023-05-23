package yun520.xyz.service.impl.aliyun;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yun520.xyz.service.RedisService;
import yun520.xyz.service.impl.aliyun.entity.Aliyun;
import yun520.xyz.service.impl.aliyun.mapper.AliyunMapper;
import yun520.xyz.util.DefaultHttpProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AliyunSDK {
    //    https://open.aliyundrive.com/o/oauth/authorize?client_id=6a596f96a26544e7897f01ce321f0cd8&redirect_uri=https://alist.nn.ci/tool/aliyundrive/callback&scope=user:base,file:all:read,file:all:write&state=&response_type=code
    private static Logger logger = Logger.getLogger("AliyunSDK.class");
    //    1access_token 获取用户信息，并把用户信息保存到threadlocal
//GET 域名 + /oauth/users/info
    private static final String baseUrl = "https://openapi.aliyundrive.com";
    private static final String client_id = "6a596f96a26544e7897f01ce321f0cd8";
    private static final String client_secret = "8b7ba7ff53e44d1dba04c3dd355dc6da";
    private static final String grant_type = "authorization_code";
    //022567c019fa4ebda1b13aa52dcf63eb
    private static final String code = "f749d0665413456da931c15dbf24e8b4";
    private final DefaultHttpProxy client = null;
    //存放header
    public final Map<String, Map<String, String>> headers = new HashMap<>();
    public static final Map<String, Aliyun> aliaccount = new HashMap<>();
    //存账号数据
    @Autowired
    AliyunMapper aliyunMapper;
    @Autowired
    RedisService redisService;
    /*
    获取授权吗
     */
// 方案2  1
//   通过用户ID获取用户driveId

    //   通过driverid获取对呀的实体类   缺点统一的时候不能多id
// 方案2   2 文件存储时候自动切换存储 ，
//
//
// 2文件存储账号轮训：轮训规则，用户ID取余  下载地址 存储地址driveId+fileid  上传地址：webcloud/日期/账号/文件
    //网盘： 论选判断用哪个账号，再上传，最后用driveId存储地址。100m以下不分片
    //初始化账号
    public boolean initaccount(String codescan, String userid) {
        //获得用户信息
        try {
            JSONObject token = getAccess_token(codescan);
//            JSONObject token = new JSONObject();
//            token.set("access_token", "eyJraWQiOiJLcU8iLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMmRlMTA3M2I3MDM0N2MwYjk3ODcwNzU0ZDA3OTkzNiIsImF1ZCI6IjZhNTk2Zjk2YTI2NTQ0ZTc4OTdmMDFjZTMyMWYwY2Q4IiwiaXNzIjoiaHR0cHM6Ly9vcGVuLmFsaXl1bmRyaXZlLmNvbSIsImV4cCI6MTY4NDQ5NTIwMywiaWF0IjoxNjg0NDg3NDAzfQ.aM6QUNupAYAcua2ndQ7qXKnegNVJ0ipDU4UOL-gmGnE");
            String driveId = getdriveId(token);
            Aliyun aliyun = Aliyun.builder().accessToken(token.getStr("access_token")).refreshToken(token.getStr("refresh_token")).driveId(driveId).active("1").updatetimer(new Date()).timeout(token.getInt("expires_in")).userid(userid).build();
            //todo 热缓存，把driveId和access_token存到rendis
            //提前2分钟过期
            expertime(aliyun);
            //存到数据库
            aliyunMapper.insert(aliyun);

        } catch (Exception e) {
            logger.severe("错误" + e.getMessage());
            System.out.println(e);
            return false;
        }
        return true;

    }

    //过期时间策略
    public void expertime(Aliyun alidto_update) {
        //提前2分钟过期
        alidto_update.setTimeout(alidto_update.getTimeout() * 1000 - 2 * 60 * 1000);
    }

    //初始化方法，判断token是否过期并刷新
    public void init(String driveId) throws Exception {
        //一个设备id对应唯一一个账号
        if (!aliaccount.containsKey(driveId)) {
            //如果本地没有去数据库查
            QueryWrapper<Aliyun> queryWrapperfile = new QueryWrapper<Aliyun>();
            //true null拼接  false 不拼接
            queryWrapperfile.eq(false, "driveId", driveId);
            Aliyun userInfo = aliyunMapper.selectOne(queryWrapperfile);
            aliaccount.put(userInfo.getDriveId(), userInfo);
        }
        //判断是否超时
        Aliyun aliyun = aliaccount.get(driveId);
        Assert.notNull(aliyun, "没有对应账号" + driveId);
        Date updatetimer = aliyun.getUpdatetimer();
        long between = DateUtil.between(updatetimer, new Date(), DateUnit.MS);
        if (between >= Long.valueOf(aliyun.getTimeout())) {
            logger.info("刷新token" + aliyun);
            //token过期要刷新了
            JSONObject entries = reFreshAccess_token(aliyun.getRefreshToken());
            //过期时间 entries.getInt("expires_in")
            Aliyun alidto_update = Aliyun.builder().accessToken(entries.getStr("access_token")).refreshToken(entries.getStr("refresh_token")).updatetimer(new Date()).timeout(entries.getInt("expires_in")).build();
            expertime(alidto_update);
            QueryWrapper<Aliyun> queryWrapperfile = new QueryWrapper<Aliyun>();
            queryWrapperfile.eq(false, "driveId", driveId);
            if (aliyunMapper.update(alidto_update, queryWrapperfile) > 1) {
                //刷新一些本地数据
                aliyun = aliaccount.get(driveId);
                aliaccount.put(alidto_update.getDriveId(), aliyun);
            }

        }
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + aliyun.getAccessToken());
        headers.put(aliyun.getDriveId(), headerMap);

    }

    //初始化方法，判断token是否过期并刷新,更具不同策略热更新
    //type，存redis的接口    jsonObject要传递的参数
    public JSONObject init(String driveId, JSONObject jsonObject, String type) throws Exception {
        JSONObject returnjson = new JSONObject();
        //redis暂存一些结果
        //todo  读取配置
        boolean openredis = true;
        if (openredis) {
            String keyrules = keyrules(type, jsonObject);
            returnjson = redisService.getJSONObject(keyrules);
            if (!StrUtil.isEmptyIfStr(returnjson)) {
                return returnjson;
            }

        }

        //一个设备id对应唯一一个账号
        if (!aliaccount.containsKey(driveId)) {
            //如果本地没有去数据库查
            QueryWrapper<Aliyun> queryWrapperfile = new QueryWrapper<Aliyun>();
            //true null拼接  false 不拼接
            queryWrapperfile.eq(false, "driveId", driveId);
            Aliyun userInfo = aliyunMapper.selectOne(queryWrapperfile);
            aliaccount.put(userInfo.getDriveId(), userInfo);
        }
        //redis暂存一些结果结束




        //判断是否超时
        Aliyun aliyun = aliaccount.get(driveId);
        Assert.notNull(aliyun, "没有对应账号" + driveId);
        Date updatetimer = aliyun.getUpdatetimer();
        long between = DateUtil.between(updatetimer, new Date(), DateUnit.MS);
        if (between >= Long.valueOf(aliyun.getTimeout())) {
            logger.info("刷新token" + aliyun);
            //token过期要刷新了
            JSONObject entries = reFreshAccess_token(aliyun.getRefreshToken());
            //过期时间 entries.getInt("expires_in")
            Aliyun alidto_update = Aliyun.builder().accessToken(entries.getStr("access_token")).refreshToken(entries.getStr("refresh_token")).updatetimer(new Date()).timeout(entries.getInt("expires_in")).build();
            expertime(alidto_update);
            QueryWrapper<Aliyun> queryWrapperfile = new QueryWrapper<Aliyun>();
            queryWrapperfile.eq( "driveId", driveId);
            if (aliyunMapper.update(alidto_update, queryWrapperfile) > 1) {
                //刷新一些本地数据
                aliyun = aliaccount.get(driveId);
                aliaccount.put(alidto_update.getDriveId(), aliyun);
            }

        }
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + aliyun.getAccessToken());
        headers.put(aliyun.getDriveId(), headerMap);
        return returnjson;
    }

    public String keyrules(String type, JSONObject jsonObject) throws Exception {
        String key = "filestore:aliyun:";
        switch (type) {
            case "down":
                key = key + type + ":"+ jsonObject.getStr("file_id");
                break;
                //精确搜索
            case "searchOneFile":
                key = key + type + ":"+ jsonObject.getStr("drive_id") +":"+jsonObject.getStr("query");
                break;
            default:
                throw new Exception("无key规则");
        }
        return  key;

    }

    //获取accesstoken
    public JSONObject getAccess_token(String codescan) throws Exception {
        //以下都是不变的
        String url = "https://openapi.aliyundrive.com/" + "oauth/access_token";
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("client_id", client_id);
        objectObjectHashMap.put("client_secret", client_secret);
        objectObjectHashMap.put("grant_type", "authorization_code");
        //授权码
        objectObjectHashMap.put("code", codescan);
        JSONObject entries = new JSONObject(objectObjectHashMap);
        String post = HttpUtil.post(url, entries.toString(), 30000);
        Assert.notNull(new JSONObject(post).get("access_token"), "access_token为空" + post + codescan);
        String access_token = new JSONObject(post).get("access_token").toString();
//        headers.put("Authorization", "Bearer "+access_token);
        //这里有个很重要的accesstoken存储
        return new JSONObject(post);

    }

    //刷新token
    public JSONObject reFreshAccess_token(String token) throws Exception {
        //以下都是不变的
        String url = baseUrl + "/oauth/access_token";
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("client_id", client_id);
        objectObjectHashMap.put("client_secret", client_secret);
        objectObjectHashMap.put("grant_type", "refresh_token");
        //todo 授权码
        objectObjectHashMap.put("refresh_token", token);
        JSONObject entries = new JSONObject(objectObjectHashMap);
        String post = HttpUtil.post(url, entries.toString(), 30000);
        Assert.notNull(new JSONObject(post).get("access_token"), "access_token为空" + post);
        //这里有个很重要的accesstoken存储
        return new JSONObject(post);

    }

    //获取driveId
    public String getdriveId(JSONObject header) throws Exception {
        String url = baseUrl + "/adrive/v1.0/user/getDriveInfo";
        //这个要自己
        String access_token = new JSONObject(header).get("access_token").toString();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + access_token);
        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headerMap, 30000, "utf-8");
        JSONObject getbody = defaultHttpProxy.post(url);
        Assert.notNull(new JSONObject(getbody).get("default_drive_id").toString(), "default_drive_id为空" + getbody);
        return new JSONObject(getbody).get("default_drive_id").toString();
    }

    /*
   搜索文件  jsonObject包含文件名和driverid
    */
    public JSONObject searchFile(JSONObject jsonObject) throws Exception {

        String driveId = jsonObject.get("driveId").toString();
        String filename = jsonObject.get("filename").toString();
        String url = baseUrl + "/adrive/v1.0/openFile/search";
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("drive_id", driveId);
        bodyMap.put("parent_file_id", jsonObject.get("parent_file_id").toString());

        bodyMap.put("query", "name  match  '" + filename + "'");

        if (jsonObject.containsKey("type")){
            bodyMap.put("query", "name  match  '" + filename +"' and   type ='" + jsonObject.get("type") + "'");
        }
        JSONObject entries = new JSONObject(bodyMap);
        JSONObject init = init(driveId, entries, "searchOneFile");
        if (init!=null&&init.containsKey("total_count")) {
            return init;
        }
        logger.info("开始搜索文件");
        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headers.get(driveId), 30000, "utf-8");
        JSONObject post = defaultHttpProxy.post(url, entries);

        //结果缓存
        long timeout= 120;
        String key = keyrules("searchOneFile", entries);
        redisService.set(key,post.toJSONString(0),timeout);
        //这里有个很重要的accesstoken存储
        return post;
    }

    /*
    下载文件 根据文件id获取下载链接
     */
    public JSONObject downfile(String driveId, String fileid) throws Exception {
        //过期时间 ，单位s
        long timeout= 115200;
        JSONObject bodyMap = new JSONObject();
        String url = baseUrl + "/adrive/v1.0/openFile/getDownloadUrl";
        bodyMap.put("drive_id", driveId);
        bodyMap.put("file_id", fileid);
        //最长32小时
        bodyMap.put("expire_sec", "115200");
        JSONObject init = init(driveId, bodyMap, "down");
        if (init.containsKey("url")) {
            return init;
        }
        JSONObject entries = new JSONObject(bodyMap);
        logger.info("开始搜索文件");
        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headers.get(driveId), 30000, "utf-8");
        JSONObject post = defaultHttpProxy.post(url, entries);
        //结果缓存
        String key = keyrules("down", bodyMap);
        redisService.set(key,post,timeout/60);
        //这里有个很重要的accesstoken存储
        return post;
    }
    /*
    获得上传地址   name文件名
     */

@SneakyThrows
    public  String getuploadUrl(String driveId,String name){

        //判读上传文件路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String day = sdf.format(new Date());
        String path="webcloud"+  "/" +day.substring(0, 6) + "/" + day.substring(6, 8) ;
        String parent_file_id = isFileExit(driveId, path, "ROOT");
        //获得上传文件的参数
        JSONObject json= CreatennewFile(driveId,parent_file_id,name,"file");

        //获得上传文件地址
//    String url = baseUrl + "/adrive/v1.0/openFile/getUploadUrl";
//    JSONObject queryjson = new JSONObject();
//    queryjson.set("file_id", json.get("file_id"));
//    queryjson.set("upload_id",  json.get("upload_id"));
//        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headers.get(driveId), 30000, "utf-8");
//        JSONObject post = defaultHttpProxy.post(url,queryjson);
        Assert.notNull(json.getJSONArray("part_info_list"),"获得文件上传地址失败");
        return  json.getJSONArray("part_info_list").getJSONObject(0).getStr("upload_url");
    }


    //新建文件或文件夹
    @SneakyThrows
    public  JSONObject CreatennewFile(String driveId,String parent_file_id,String name,String type){
        //文件创建
        String url = baseUrl + "/adrive/v1.0/openFile/create";
        JSONObject queryjson = new JSONObject();

            init(driveId);
        queryjson.set("drive_id", driveId);
        queryjson.set("parent_file_id", parent_file_id);
        queryjson.set("name", name);
        queryjson.set("type", type);
//        同名不创建
        queryjson.set("check_name_mode", "refuse");

        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headers.get(driveId), 30000, "utf-8");
        JSONObject post = defaultHttpProxy.post(url,queryjson);
        if (post==null){
            logger.info("创建文件夹失败");
        }
        return post;

    }
    //判断文件夹是否存在，不存在创建，存在返回文件夹id

//    /webcloud/日期
    public String isFileExit(String driveId,String path,String parent_file_id) throws Exception{
        //sc:webcloud/日期
        String[] finame = path.split("/");
        for (int i = 0; i < finame.length; i++) {
//            JSONObject init = init(driveId, bodyMap, "down");
//            if (init.containsKey("url")) {
//                return init;
//            }
            init(driveId);
            //设备ID
            JSONObject queryjson = new JSONObject().set("driveId", driveId);
            //文件名
            queryjson.set("filename", finame[i]);
            //父路径
            queryjson.set("parent_file_id", parent_file_id);
            //查询条件文件夹
            queryjson.set("type", "folder");
            JSONObject   searchjson = searchFile(queryjson);
            int url = searchjson.getJSONArray("items").size();
            if (url<=0){
                //创建文件夹
                JSONObject jsonObject  = CreatennewFile(driveId, parent_file_id, finame[i], "folder");

                parent_file_id = jsonObject.getStr("file_id");;
                continue;
            }
            parent_file_id = searchjson.getJSONArray("items").getJSONObject(0).getStr("file_id");
        }
return   parent_file_id;

    }


}

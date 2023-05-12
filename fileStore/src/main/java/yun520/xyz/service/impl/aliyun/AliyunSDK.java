package yun520.xyz.service.impl.aliyun;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.sun.org.apache.xml.internal.security.Init;
import org.apache.poi.hpsf.Date;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sun.security.jca.GetInstance;
import yun520.xyz.service.impl.aliyun.entity.Aliyun;
import yun520.xyz.util.DefaultHttpProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AliyunSDK {
    private static Logger logger = Logger.getLogger("AliyunSDK.class");
    //    1access_token 获取用户信息，并把用户信息保存到threadlocal
//GET 域名 + /oauth/users/info
    private static final String baseUrl = "https://open.aliyundrive.com";
    private static final String client_id = "6a596f96a26544e7897f01ce321f0cd8";
    private static final String client_secret = "8b7ba7ff53e44d1dba04c3dd355dc6da";
    private static final String grant_type = "authorization_code";
    //022567c019fa4ebda1b13aa52dcf63eb
    private static final String code = "f749d0665413456da931c15dbf24e8b4";
    private final DefaultHttpProxy client = null;
    //存放header
    public final Map<String, Map<String, String>> headers = new HashMap<>();
    public static final Map<String, Aliyun> aliaccount = new HashMap<>();
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
   //code码
    public boolean init(String codescan,String  userid) {
        //获得用户信息
        try {
            JSONObject token = getAccess_token(codescan);
            String driveId = getdriveId(token);
            Aliyun aliyun = Aliyun.builder().accessToken(token.getStr("access_token")).refreshToken(token.getStr("refresh_token")).driveId(driveId).active("1").updatetimer(new Date()).timeout(token.getInt("expires_in")).build();
             //todo 热缓存，把driveId和access_token存到rendis
             //存到数据库

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
        return true;

    }

    //获取accesstoken
    public JSONObject getAccess_token(String codescan) throws Exception {

        String url = baseUrl + "oauth/access_token ";
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("client_id", client_id);
        objectObjectHashMap.put("client_secret", client_secret);
        objectObjectHashMap.put("grant_type", grant_type);
        //授权码
        objectObjectHashMap.put("code", codescan);
        JSONObject entries = new JSONObject(objectObjectHashMap);
        String post = HttpUtil.post(url, entries.toString(), 30000);
        Assert.isNull(new JSONObject(post).get("access_token"), "access_token为空" + post);
        String access_token = new JSONObject(post).get("access_token").toString();
//        headers.put("Authorization", "Bearer "+access_token);
        //这里有个很重要的accesstoken存储
        return new JSONObject(post);

    }

    //获取driveId
    public String getdriveId(JSONObject header) throws Exception {
        String url = baseUrl + "/adrive/v1.0/user/getDriveInfo";
        String access_token = new JSONObject(header).get("access_token").toString();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + access_token);
        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headerMap, 30000, "utf-8");
        JSONObject getbody = defaultHttpProxy.get(url, null);
        Assert.isNull(new JSONObject(getbody).get("default_drive_id").toString(), "default_drive_id为空" + getbody);
        return new JSONObject(getbody).get("default_drive_id").toString();
    }

    /*
   搜索文件  jsonObject包含文件名和driverid
    */
    public JSON searchFile(JSONObject jsonObject) {
        String driveId = jsonObject.get("driveId").toString();
        String filename = jsonObject.get("filename").toString();
        String url = baseUrl + "/adrive/v1.0/openFile/search";
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("drive_id", driveId);
        bodyMap.put("query", "name  match  '" + filename + "'");
        JSONObject entries = new JSONObject(bodyMap);
        DefaultHttpProxy defaultHttpProxy = new DefaultHttpProxy(headers.get(driveId), 30000, "utf-8");
        JSONObject post = defaultHttpProxy.post(url, entries);
        //这里有个很重要的accesstoken存储
        return post;
    }
}

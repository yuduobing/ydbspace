package yun520.xyz;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import yun520.xyz.service.impl.aliyun.AliyunSDK;

import javax.annotation.Resource;
import java.text.ParseException;

//@RunWith(PowerMockRunner.class)

//@PowerMockRunnerDelegate(SpringRunner.class)
//@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
////@PrepareForTest({AliyunMapper.class,AliyunSDK.class})
////@SpringBootTest(classes = { AliyunMapper.class,AliyunSDK.class})
//@SpringBootTest(classes = { FileStoreApplication.class})
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommonUtilsTest {
    String code = "00519dad21644f6db891a1bac3bf59ec";

    @Resource
    AliyunSDK aliyunSDK;


    @Test
    public void test() throws ParseException {
        boolean initaccount = aliyunSDK.initaccount(code, "1");
        if (initaccount) {
            JSONObject set = new JSONObject().set("driveId", "driveId");
            set.set("filename", "202304142334280598");
            try {
                JSON json = aliyunSDK.searchFile(set);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //文件索索
    @Test
    public void test2() throws ParseException {
        //设备ID
        JSONObject set = new JSONObject().set("driveId", "8520066");
        //文件名
        set.set("filename", "04530317");
        //父路径 ，这里是webcloud路径id  获得是通过搜索出来的
        set.set("parent_file_id", "6426a851348c613eb43a4d05b2ab5f40ff045e8a");

        JSONObject json = null;
        try {
            json = aliyunSDK.searchFile(set);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = json.getJSONArray("items").getJSONObject(0).getStr("url");
        String fileid = json.getJSONArray("items").getJSONObject(0).getStr("file_id");
        //643eb1958b3188361c074914a52a17d79ec72c77
        Assert.notNull(url, "路径为空" + json);

        System.out.println("下载地址：       "+url);

    }
}

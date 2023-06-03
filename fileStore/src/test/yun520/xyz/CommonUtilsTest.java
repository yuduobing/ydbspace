package yun520.xyz;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import yun520.xyz.service.impl.aliyun.AliyunSDK;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;

//@RunWith(PowerMockRunner.class)

//@PowerMockRunnerDelegate(SpringRunner.class)
//@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
////@PrepareForTest({AliyunMapper.class,AliyunSDK.class})
////@SpringBootTest(classes = { AliyunMapper.class,AliyunSDK.class})
//@SpringBootTest(classes = { FileStoreApplication.class})
@SpringBootTest(classes = { FileStoreApplication.class})
@RunWith(SpringRunner.class)
public class CommonUtilsTest {
    String code = "dc73eceb8da541ba86e40efd425c42f8";

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
    public void search() throws ParseException {
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
    //文件下载
    @Test
    public void down() throws ParseException {
        //todo redis热更新

        //个人信息里的driverid
        String driveId= "8520066";
        String fileid="643eb1958b3188361c074914a52a17d79ec72c77";
        JSONObject json = null;
        try {
            json = aliyunSDK.downfile(driveId,fileid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = json.getJSONArray("items").getJSONObject(0).getStr("url");
//        String fileid = json.getJSONArray("items").getJSONObject(0).getStr("file_id");
//        //643eb1958b3188361c074914a52a17d79ec72c77
//        Assert.notNull(url, "路径为空" + json);

//        System.out.println("下载地址：       "+url);

    }


    @SneakyThrows
    @Test
    public void upload()  throws ParseException {
        //shnagchaunwenjian
        String driveId= "526997152";
        String name="1909.png";
        File file = new File("/Users/yu/Desktop/1908.png");

//        String url="https://bj29-hz.cn-hangzhou.data.alicloudccp.com/3os9yDmv%2F526997152%2F6479b10f56af64c673e84023a03e8a9905a2a415%2F6479b10fb73efbf606d3460181a673f6f16dcd49?partNumber=1&security-token=CAIS%2BgF1q6Ft5B2yfSjIr5eDAdX1nqhp9vDfRHz5lnkjY%2BFt1qecpDz2IHFPeHJrBeAYt%2FoxmW1X5vwSlq5rR4QAXlDfNWbKDm%2B3qFHPWZHInuDox55m4cTXNAr%2BIhr%2F29CoEIedZdjBe%2FCrRknZnytou9XTfimjWFrXWv%2Fgy%2BQQDLItUxK%2FcCBNCfpPOwJms7V6D3bKMuu3OROY6Qi5TmgQ41Uh1jgjtPzkkpfFtkGF1GeXkLFF%2B97DRbG%2FdNRpMZtFVNO44fd7bKKp0lQLukMWr%2Fwq3PIdp2ma447NWQlLnzyCMvvJ9OVDFyN0aKEnH7J%2Bq%2FzxhTPrMnpkSlacGoABogANrGtk%2BmJGXbVpOioyb3CVyetuOkxRav%2FJkra5bjUoU0YoYnz6Q1JUZjSl2JpH%2FoaK8c31n%2BwsadZ7ItVE6JQDMReouBeGB16me50HQkYc%2BrO%2B13%2BzFfagYtYC8PMUFvEJtMRegRidLUifOrjQJXj2qHAFqMDpiAyY2AssimI%3D&uploadId=B9AE2ECDA9DD4C7B9561B8AC115B90D5&x-oss-access-key-id=STS.NT6JoAswHA24FZHryvonA9b7F&x-oss-expires=1685700384&x-oss-signature=%2FUzzY8Yud1%2BIzqEvOw0lY65seB2wbsHZk98mgc6padM%3D&x-oss-signature-version=OSS2";
//        HttpRequest put = HttpRequest.put(geturl()).body(data);
//        HttpResponse execute = put.execute();
//
//        System.out.println("1");
        FileInputStream inputStream2 = new FileInputStream(file);

        aliyunSDK.uplaodfile(driveId,name,inputStream2);



    }

}

package javatest;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.swaggerbootstrapui.util.CommonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yun520.xyz.service.impl.aliyun.AliyunSDK;
import yun520.xyz.service.impl.aliyun.mapper.AliyunMapper;

import javax.annotation.Resource;
import java.text.ParseException;

@RunWith(PowerMockRunner.class)

@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
@PrepareForTest({AliyunSDK.class, AliyunMapper.class})
@SpringBootTest(classes = { AliyunSDK.class, AliyunMapper.class})
public class CommonUtilsTest {
    String code = "dca10fa0304b4871af4dd7ef804b77f4";

    @Resource
    AliyunSDK aliyunSDK;
    @Resource
    AliyunMapper aliyunMapper;

    @Test
    public void test() throws ParseException {
        boolean initaccount = aliyunSDK.initaccount(code, "1");
        if (initaccount) {
            JSONObject set = new JSONObject().set("driveId", "driveId");
            set.set("filename", "22");
            try {
                JSON json = aliyunSDK.searchFile(set);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() throws ParseException {

    }
}

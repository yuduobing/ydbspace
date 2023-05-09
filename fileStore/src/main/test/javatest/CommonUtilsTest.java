package javatest;

import cn.hutool.json.JSON;
import com.github.xiaoymin.swaggerbootstrapui.util.CommonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import yun520.xyz.service.impl.aliyun.AliyunSDK;

import java.text.ParseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommonUtils.class})
public class CommonUtilsTest {

    @Test
    public void test() throws ParseException {
        AliyunSDK aliyunSDK = new AliyunSDK();
        JSON access_token = aliyunSDK.getAccess_token();
        System.out.println(access_token);
    }
}

package yun520.xyz.context;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import yun520.xyz.service.StoreService;
import yun520.xyz.util.SpringContentUtils;

//文件上传
//选择不同协议上传文件
@Component
public class StoreContext {

    public StoreService getStoreService(String type) {
        SpringContentUtils springContentUtils = new SpringContentUtils();

        Object storeService = null;
        Assert.notNull(type,"type为空");
        switch (type) {
            case "1":
                //临时问价
                storeService = springContentUtils.getBean("WebDavService");
                break;
            case "0":
                //永久文件
                storeService = springContentUtils.getBean("FastDfsService");
                break;
            case "FastDfs":
                storeService = springContentUtils.getBean("FastDfsService");
                break;
            case "WebDav":
                storeService = springContentUtils.getBean("WebDavService");
                break;
            case "AliYunOpen":
                storeService = springContentUtils.getBean("AliYunService");
                break;
        }

        Assert.notNull(storeService,"storeService为空");
        return (StoreService) storeService;

    }

}

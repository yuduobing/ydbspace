package yun520.xyz.chain.autohandle;

import com.gomyck.util.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.chain.core.*;
import yun520.xyz.chain.handle.uploadhand;
import yun520.xyz.util.SpringContentUtils;

//自动组装责任链第构造链
public class UserfileNewFolder extends HandlerInitializer {

    @Autowired
    private Bootstrap bootstrap;
    @Autowired
    private SpringContentUtils scu;

    public UserfileNewFolder(ContextRequest request, ContextResponse response) {
        super(request, response);
    }

    //通过反射获取类
//
//    public void autoAssemble(ContextRequest contextRequest, ContextResponse contextResponse){
//        new HandlerInitializer(contextRequest,contextResponse){
//
//        }
//        bootstrap.childHandler();
//
//    }


    @Override
    protected void initChannel(Pipeline pipeline) {

        pipeline.addLast(scu.getHandler(uploadhand.class));
        //1获取接口的所有实现类
        //2把实现类拼接进来
//        pipeline.addLast();
    }

    //通过自定义注解 反射排序，确定顺讯

}

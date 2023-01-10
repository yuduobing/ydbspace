package yun520.xyz.chain.autohandle;

import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.chain.core.*;

//自动组装责任链第一种
public class UserfileNewFolder implements HandlerInitializer {

    @Autowired
    private Bootstrap bootstrap;

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


        pipeline.addLast();


    }

    //通过自定义注解 反射排序，确定顺讯

}

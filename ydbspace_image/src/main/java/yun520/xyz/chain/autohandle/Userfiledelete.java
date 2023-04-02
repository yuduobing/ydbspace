package yun520.xyz.chain.autohandle;

import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.HandlerInitializer;
import yun520.xyz.chain.core.Pipeline;
import yun520.xyz.chain.handle.uploadhand;
import yun520.xyz.util.SpringContentUtils;

//自动组装责任链第构造链

public class Userfiledelete extends HandlerInitializer {


    public Userfiledelete(ContextRequest request, ContextResponse response) {
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
        SpringContentUtils sc = new SpringContentUtils();

        //文件表上传
        pipeline.addLast(sc.getHandler(uploadhand.class));
        //个人文件表也要上传
        pipeline.addLast(sc.getHandler(uploadhand.class));
//        pipeline.addLast(scu.getHandler(uploadhand.class));
        //1获取接口的所有实现类
        //2把实现类拼接进来
    }

    //通过自定义注解 反射排序，确定顺讯

}
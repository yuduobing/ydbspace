package yun520.xyz.chain.autohandle;

import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.HandlerInitializer;
import yun520.xyz.chain.core.Pipeline;
import yun520.xyz.chain.handle.deleteMyFile;
import yun520.xyz.chain.handle.deleteMyFodle;
import yun520.xyz.chain.handle.uploadhand;
import yun520.xyz.util.SpringContentUtils;

//自动组装责任链第构造链

public class Userfiledelete extends HandlerInitializer {
  //创建一个数组

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

        //1删除文件夹
        pipeline.addLast(sc.getHandler(deleteMyFodle.class));
        //2删除文件
        pipeline.addLast(sc.getHandler(deleteMyFile.class));
        //3更新容量

    }



}

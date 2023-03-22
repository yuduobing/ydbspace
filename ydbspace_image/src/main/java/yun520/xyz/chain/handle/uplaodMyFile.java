package yun520.xyz.chain.handle;

import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.Handler;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.mapper.FilechunkMapper;

import java.time.LocalDateTime;

public class uplaodMyFile extends Handler {


    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileWeb){
            FileWeb fileparams=(FileWeb)request;



        }

    }
}

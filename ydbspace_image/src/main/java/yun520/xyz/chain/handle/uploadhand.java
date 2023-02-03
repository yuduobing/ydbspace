package yun520.xyz.chain.handle;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.Handler;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.mapper.FilechunkMapper;

import java.time.LocalDateTime;

//文件表上传
@Component
public class uploadhand extends Handler {
    @Autowired
    FilechunkMapper filechunkMapper;

    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileWeb){
            FileWeb fileparams=(FileWeb)request;

            //填充切片表
            Filechunk filechunk = Filechunk.builder().chunkmd5(fileparams.getIdentifier()).chunksize(fileparams.getChunkSize()).chunkpath(fileparams.getChunkpath()).chunktotalnum(fileparams.getTotalChunks()).chunksnum(fileparams.getChunkNumber())
                    .createTime(LocalDateTime.now()).build();

             filechunkMapper.insert(filechunk);

        }

    }
}

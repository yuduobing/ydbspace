package yun520.xyz.chain.handle;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.Handler;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.mapper.UserfileMapper;
import yun520.xyz.service.StoreService;
import yun520.xyz.service.impl.FastDfsServiceimpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class deleteMyFile extends Handler {
    private static Logger logger = Logger.getLogger("deleteMyFodle.class");
    @Autowired
    FileMapper filemapper;
    @Autowired
    SharelinksMapper sharelinksmapper;
    @Autowired
    FilechunkMapper filechunkMapper;
    @Autowired
    StoreService storeService;
    @Autowired
    UserfileMapper  userfileMapper;

    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileWeb){
            FileWeb fileparams=(FileWeb)request;


            QueryWrapper<File> queryWrapperfile = new QueryWrapper<>();
            queryWrapperfile.in("file_id", fileparams.getDeleteList());

            List<File> userInfoList2 = filemapper.selectList(queryWrapperfile);
            //遍历删除还是要确定其他永久文件md5是否存在
            userInfoList2.forEach(val -> {
                boolean safedelte = isSafedelte(val.getFilemd5());
                //切片是否可以安全删除
                if (safedelte) {

//                开始删除  删除文件  删除切片表  删除文件表
                    QueryWrapper<Filechunk> queryWrapperfilechunk = new QueryWrapper<Filechunk>();
                    queryWrapperfilechunk.eq("chunkmd5", val.getFilemd5());

                    List<Filechunk> userInfoListchunk = filechunkMapper.selectList(queryWrapperfilechunk);
                    userInfoListchunk.forEach(valchunk -> {
                        try {
                            storeService.delete(valchunk.getChunkpath());
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                    });
                    filechunkMapper.delete(queryWrapperfilechunk);


                }

            });
            //删除文件表
            filemapper.delete(queryWrapperfile);



        }

    }
    //是否可以安全删除
    public boolean isSafedelte(String identifier) {
        AtomicReference<Boolean> bz = new AtomicReference<>(true);
        //1 永久性文件是否包含
        QueryWrapper<File> queryWrapperfile2 = new QueryWrapper<File>();
        queryWrapperfile2.eq("filemd5", identifier);
        List<File> userInfoListpermanent = filemapper.selectList(queryWrapperfile2);

        if(userInfoListpermanent.size()>0){
            return false;

        }



        return true;

    }
}
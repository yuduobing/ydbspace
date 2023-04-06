package yun520.xyz.chain.handle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.lang.Collections;
import lombok.val;
import org.checkerframework.checker.signedness.qual.UnknownSignedness;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.chain.core.ContextRequest;
import yun520.xyz.chain.core.ContextResponse;
import yun520.xyz.chain.core.Handler;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.UserfileMapper;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class deleteMyFodle extends Handler {
    private static Logger logger = Logger.getLogger("deleteMyFodle.class");

    @Autowired
    UserfileMapper userfileMapper;

    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileWeb) {
            FileWeb fileparams = (FileWeb) request;

            //文件夹删除所有前缀相同的
            if (1 == fileparams.getIsDir()) {
                QueryWrapper<Userfile> objectQueryWrapper = new QueryWrapper<>();
                objectQueryWrapper.eq("userId", fileparams.getUserId());
                objectQueryWrapper.like("name", fileparams.getFilePath()+"%");
                List<Userfile> userfiles = userfileMapper.selectList(objectQueryWrapper);
                //删除文件夹，返回文件id
                userfiles.stream().filter(val -> val.getIsDir() == 1).forEach(val -> {
                    userfileMapper.deleteById(val.getFileId());

                });
                //获得所有文件id
                List<String> list = userfiles.stream().filter(val -> val.getIsDir() == 0).map(Userfile::getFileId).collect(Collectors.toList());
                logger.info("删除" + fileparams.getUserId() + list);
                //所有需要删除的文件
                fileparams.setDeleteList(list);
            }

        }

    }
}

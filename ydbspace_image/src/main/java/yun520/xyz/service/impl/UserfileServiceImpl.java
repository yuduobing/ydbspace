package yun520.xyz.service.impl;

import cn.hutool.core.net.URLDecoder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.UserfileMapper;
import yun520.xyz.service.IUserfileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yun520.xyz.vo.file.FileListVo;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
@Service
public class UserfileServiceImpl extends ServiceImpl<UserfileMapper, Userfile> implements IUserfileService {


    @Autowired
    public UserfileMapper  userFileMapper;
//    查询文件目录
    @Override
    public IPage<FileListVo> getFileList(Long userId, String filePath, Long currentPage, Long pageCount) {
        //这里userid写死
       long   userId2= 1L;
        Page<FileListVo> page = new Page<>(currentPage, pageCount);
        Userfile userFile = new Userfile();
//        JwtUser sessionUserBean = SessionUtil.getSession();
        //userId2 co=从session获取
        if (userId == null) {
            userFile.setUserId(userId2);
        } else {
            userFile.setUserId(userId);
        }

        userFile.setFilePath(URLDecoder.decodeForPath(filePath, StandardCharsets.UTF_8));

        return userFileMapper.selectPageVo(page, userFile, null);
    }
}

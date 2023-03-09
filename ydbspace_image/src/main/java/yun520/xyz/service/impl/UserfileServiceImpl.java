package yun520.xyz.service.impl;

import cn.hutool.core.net.URLDecoder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.chain.autohandle.UserfileNewFolder;
import yun520.xyz.chain.core.Bootstrap;
import yun520.xyz.chain.handle.uploadhand;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.mapper.UserfileMapper;
import yun520.xyz.service.IUserfileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yun520.xyz.service.StoreService;
import yun520.xyz.vo.file.FileListVo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
    StoreService fastdfs;
    @Autowired
    FileMapper filemapper;
    @Autowired
    SharelinksMapper sharelinksmapper;
    @Autowired
    FilechunkMapper filechunkMapper;

    @Autowired
     UserfileMapper  userFileMapper;
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

    @Override
    public Boolean newMk(Long userId, String path, String filename) {
        Userfile userfile=new Userfile();
        if (userId == null) {
            userfile.setUserId(1L);
        } else {
            userfile.setUserId(userId);
        }

        userfile.setFilePath(path);
        userfile.setFileName(filename);
        int insert = userFileMapper.insert(userfile);
        if (insert>0){
            return true;
        }
        return false;
    }
    //上传文件
    @Override
    public  void upload(MultipartFile file, FileWeb fileparams) throws Exception{


        //文件上传
        String originalFilename = file.getOriginalFilename();
         String     chunkpath = fastdfs.upload("group1",file.getInputStream(),file.getSize(), String.valueOf(fileparams.getChunkNumber()));
        fileparams.setChunkpath(chunkpath);

        //执行责任链
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new UserfileNewFolder(fileparams,null));
        bootstrap.execute();


    }
}
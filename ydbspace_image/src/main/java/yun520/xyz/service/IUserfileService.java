package yun520.xyz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Userfile;
import com.baomidou.mybatisplus.extension.service.IService;
import yun520.xyz.vo.file.FileListVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
public interface IUserfileService extends IService<Userfile> {
     //获取文件旧爱
     public IPage<FileListVo> getFileList (Long userId, String filePath, Long beginCount, Long pageCount);
     //创建文件夹
     public Boolean newMk (Long userId, String path, String filename);
     public void upload (MultipartFile file, FileWeb fileparams) throws Exception;
     public Boolean deletemk(FileWeb fileparams);

}

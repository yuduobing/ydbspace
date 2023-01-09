package yun520.xyz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     public IPage<FileListVo> getFileList (Long userId, String filePath, Long beginCount, Long pageCount);

}

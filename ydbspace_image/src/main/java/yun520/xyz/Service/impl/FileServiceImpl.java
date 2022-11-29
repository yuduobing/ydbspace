package yun520.xyz.service.impl;

import yun520.xyz.entity.File;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuduobin
 * @since 2022-11-27
 */
@Service(value="FileServiceImpl")
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {


}

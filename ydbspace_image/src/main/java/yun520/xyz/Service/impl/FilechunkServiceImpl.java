package yun520.xyz.service.impl;

import yun520.xyz.entity.Filechunk;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.service.IFilechunkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 切片表 服务实现类
 * </p>
 *
 * @author yuduobin
 * @since 2022-12-04
 */
@Service
public class FilechunkServiceImpl extends ServiceImpl<FilechunkMapper, Filechunk> implements IFilechunkService {

}

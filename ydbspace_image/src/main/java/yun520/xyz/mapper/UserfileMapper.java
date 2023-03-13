package yun520.xyz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import yun520.xyz.entity.Userfile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import yun520.xyz.vo.file.FileListVo;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
@Mapper
public interface UserfileMapper extends BaseMapper<Userfile> {
    IPage<FileListVo> selectPageVo(Page<?> page, @Param("userFile") Userfile userFile, @Param("fileTypeId") Integer fileTypeId);

}

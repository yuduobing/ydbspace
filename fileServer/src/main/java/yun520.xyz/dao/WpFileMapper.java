package yun520.xyz.dao;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import yun520.xyz.entity.WpFile;

/**
 * @author qjf
 * @date 2022/10/23 22:24
 */
@Mapper
@Repository
public interface WpFileMapper {

    /**
     * 新增
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    int insert(WpFile wpFile);

    /**
     * 刪除
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    int delete(int id);

    /**
     * 更新
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    int update(WpFile wpFile);

    /**
     * 查询 根据主键 id 查询
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    WpFile load(int id);

    /**
     * 查询 分页查询
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    List<WpFile> pageList(int offset,int pagesize);

    /**
     * 查询 分页查询 count
     * @author zhengkai.blog.csdn.net
     * @date 2022/10/23
     **/
    int pageListCount(int offset,int pagesize);

}

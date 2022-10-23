package yun520.xyz.server;

import java.util.Map;

import yun520.xyz.entity.WpFile;

/**
 * @author qjf
 * @date 2022/10/23 22:22
 */
public interface WpFileService {

    /**
     * 新增
     */
    public Object insert(WpFile wpFile);

    /**
     * 删除
     */
    public Object delete(int id);

    /**
     * 更新
     */
    public Object update(WpFile wpFile);

    /**
     * 根据主键 id 查询
     */
    public WpFile load(int id);

    /**
     * 分页查询
     */
    public Map<String,Object> pageList(int offset, int pagesize);

}

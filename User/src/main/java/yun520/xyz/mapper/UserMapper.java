package yun520.xyz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import yun520.xyz.domain.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}

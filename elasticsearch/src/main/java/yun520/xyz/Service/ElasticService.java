package yun520.xyz.Service;

import cn.easyes.core.conditions.update.LambdaEsUpdateWrapper;

import java.io.Serializable;
import java.util.List;

public interface ElasticService<T extends Serializable> {
    List<T> searchAllText(T t, String seachercontnet);

    //全字段查询
    Integer insert(T t);

    Integer update(T t);

    Integer update(T t, LambdaEsUpdateWrapper<T> updateWrapper);
}

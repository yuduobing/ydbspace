package yun520.xyz.Service.impl;

import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import cn.easyes.core.core.BaseEsMapper;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yun520.xyz.Service.ElasticService;
import yun520.xyz.entity.Doc;
import yun520.xyz.util.SpringContentUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticServiceImpl<T extends  Object> implements ElasticService<T>{
   @Autowired
   SpringContentUtils springContentUtils;

    private Class<T> clazz;
    @Autowired
    public ElasticServiceImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

     public BaseEsMapper getMapper() {

         T t = (T) new Object();
         String name = t.getClass().getSimpleName();
       String  esmapper= StrUtil.lowerFirst(name) +"Mapper";
       BaseEsMapper mapper =(BaseEsMapper) springContentUtils.getBean(esmapper);
    return mapper;
   }
//查询，通过映射mapper查找
    @Override
    public List<T> searchAllText(T t, String seachercontnet) {

        LambdaEsQueryWrapper<T> wrapper = new LambdaEsQueryWrapper<>();
        //分词匹配查询 测试数据匹配头不匹配尾
        //查询所有字段
        wrapper.queryStringQuery("测试");
        //所有字段查询

//从springcontext找ß
         List<T> list = getMapper().selectList(wrapper);
        return list;
    }

    @Override
    public Integer insert(T t) {
        return getMapper().insert(t);
    }


    @Override
    public Integer update(T t, LambdaEsUpdateWrapper<T> updateWrapper) {
        Integer update1 = getMapper().update(null, updateWrapper);
        return update1;
    }
    @Override
    public Integer update(T t) {
//        Integer update1 = getMapper().insert(t, updateWrapper);
        return null;
    }
}
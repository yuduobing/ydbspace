package yun520.xyz.Service.impl;

import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import cn.easyes.core.core.BaseEsMapper;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yun520.xyz.Service.ElasticService;
import yun520.xyz.util.SpringContentUtils;

import java.io.Serializable;
import java.util.List;

@Service
//在 Java 中，所有的类都隐式地继承自 Object 类，因此在声明泛型类型参数时，不需要显式地指定类型参数的上限为 Object，这是一个多余的操作。如果你在声明泛型类型参数时，将类型参数的上限指定为 Object，可能会导致一些类型转换异常的问题
public class ElasticServiceImpl<T extends Serializable> implements ElasticService<T>{
   @Autowired
    SpringContentUtils springContentUtils;

    private Class<T> clazz;
//    @Autowired
//    @Lazy
//    ElasticServiceFactoryBean<Class<T>> factoryBea;
//    这里用autowried必须全部是Spring管理的
//    @Autowired
//    public ElasticServiceImpl(ElasticServiceFactoryBean<Class<T>> factoryBean) throws Exception {
//        this.clazz = (Class<T>) factoryBean.getObject();
//    }
//    需要注意的是，在范型方法中如果没有传入实参，是无法获取范型实例化时传的类型的。因为在范型方法中，范型类型参数的类型信息也是在运行时才确定的。如果需要在范型方法中获取范型类型参数的类型信息，可以将范型类型参数传递给方法，例如：
     public BaseEsMapper getMapper() {

         T t = (T) new Object();
         t.getClass().getGenericSuperclass();
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
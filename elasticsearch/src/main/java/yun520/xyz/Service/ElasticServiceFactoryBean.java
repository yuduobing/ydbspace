package yun520.xyz.Service;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author yu
 */
//@Component
public class ElasticServiceFactoryBean<T> implements FactoryBean<Class<T>> {

    private Class<T> clazz;


    public ElasticServiceFactoryBean( Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<T> getObject() throws Exception {
        return clazz;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

//    为了解决这个问题，你可以考虑将ElasticServiceFactoryBean设置为原型模式，这样每次获取bean时都会创建新的实例，从而避免循环依赖的问题。修改ElasticServiceFactoryBean的isSingleton方法，示例代码如下：
    @Override
    public boolean isSingleton() {
        return false;
    }
}

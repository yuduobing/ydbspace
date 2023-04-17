import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yun520.xyz.EsApplication;
import yun520.xyz.Service.ElasticService;
import yun520.xyz.Service.impl.ElasticServiceImpl;
import yun520.xyz.entity.Doc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class test2SpringContext {
    //测试自动注入mapper
    @Autowired
    ElasticService<Doc> elasticService;


    @Test
    public  void tst(){
        Class<?> genericType = getGenericType(elasticService.getClass());
        System.out.println(genericType);
        ElasticServiceImpl<Doc> docElasticService = new ElasticServiceImpl<>();
//       报错java.lang.ClassCastException: sun.reflect.generics.reflectiveObjects.TypeVariableImpl cannot be cast to java.lang.Class  您遇到的问题可能是因为类型参数是一个类型变量而不是具体的类。在这种情况下，您需要使用另一种方法来获取实际类型参数。
        Doc doc = new Doc();
        ParameterizedType genericInterface = (ParameterizedType) docElasticService.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = genericInterface.getActualTypeArguments()[0];
        Class<?> clazz2=(Class<?>)actualTypeArgument;

        Class<?> clazz = (Class<?>) ((ParameterizedType) elasticService.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        System.out.println(clazz);
        List<Doc> list = elasticService.searchAllText(doc,"测试");
        System.out.println(list);
    }

    public static Class<?> getGenericType(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (genericInterfaces.length > 0) {
            Type genericType = genericInterfaces[0];
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (actualTypeArgument instanceof Class) {
                        return (Class<?>) actualTypeArgument;
                    } else if (actualTypeArgument instanceof TypeVariable) {
                        TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                        Type[] bounds = typeVariable.getBounds();
                        if (bounds.length > 0) {
                            Type boundType = bounds[0];
                            if (boundType instanceof Class) {
                                return (Class<?>) boundType;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
 }

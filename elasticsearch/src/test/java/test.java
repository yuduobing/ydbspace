import cn.easyes.core.conditions.select.LambdaEsQueryChainWrapper;
import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import cn.easyes.core.core.EsWrappers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yun520.xyz.EsApplication;
import yun520.xyz.entity.Doc;
import yun520.xyz.mapper.es.DocMapper;

import java.util.List;

import static java.lang.Thread.sleep;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class test {
    @Autowired
    DocMapper docMapper;

    @Test
    public void testSelectquery() {

        LambdaEsQueryWrapper<Doc> wrapper = new LambdaEsQueryWrapper<>();
        //分词匹配查询 测试数据匹配头不匹配尾
        //查单个
//        wrapper.matchPhrase(Doc::getLastName, "测试");
        //所有字段查询
        wrapper.queryStringQuery("你好");
        List<Doc> docs = docMapper.selectList(wrapper);
        System.out.println(docs);
        // 测试查询 写法和MP一样 可以用链式,也可以非链式 根据使用习惯灵活选择即可
        List<Doc> age = EsWrappers.lambdaChainQuery(docMapper).eq("age", "111").list();

        System.out.println(age);

    }

    //    插入
    @Test
    public void testInseryt() {

        Doc document = new Doc();
        //主建要设置对
        document.setId(1004l);
        ;
        document.setAge(110);
        ;
        document.setLastName("数据测试啦");
        document.setFirstName("你好呀");
        Integer insert = docMapper.insert(document);
        List<Doc> age = EsWrappers.lambdaChainQuery(docMapper).eq("age", "110").list();

        System.out.println(age);

    }

    //    更新
    @Test
    public void testUpdate() {
        // case1: 根据条件更新和实体对象更新  这种更新是全部值都得设置，否则就是null
        LambdaEsUpdateWrapper<Doc> wrapper = new LambdaEsUpdateWrapper<>();
        wrapper.eq(Doc::getAge, 110);
        //更新
        Doc document = new Doc();
        document.setFirstName("隔壁老王王");
        document.setAge(110);
        Integer update = docMapper.update(document, wrapper);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始查询1,有延迟要等会再查" + update);
        LambdaEsQueryWrapper<Doc> wrapper2 = new LambdaEsQueryWrapper<>();
        wrapper2.eq(Doc::getAge, 110);
        List<Doc> docs = docMapper.selectList(wrapper2);
        System.out.println(docs);

        // case2 另一种省略实体的简单写法,语法与MP一致  直接在wapper中设置    这种更新设置部分值
        LambdaEsUpdateWrapper<Doc> wrapper1 = new LambdaEsUpdateWrapper<>();
//        Doc::getAge 表示将 Doc 类中的 getAge() 方法作为参数传递给另一个方法，或者将其作为函数式接口的实现。这个函数对于这个方法的实现就是获取参数名称，防止自己打错
        wrapper1.eq(Doc::getAge, 110);
        wrapper1.set(Doc::getFirstName, "推*技术过软");
        Integer update1 = docMapper.update(null, wrapper1);
        System.out.println("开始查询2" + update1);
        List<Doc> list2 = EsWrappers.lambdaChainQuery(docMapper).eq("age", 110).list();
        System.out.println(list2);
    }
}

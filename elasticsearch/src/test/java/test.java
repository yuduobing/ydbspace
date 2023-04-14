import cn.easyes.core.conditions.select.LambdaEsQueryChainWrapper;
import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes= EsApplication.class)
public class test {
    @Autowired
    DocMapper docMapper;
    @Test
    public void testSelect() {
        // 测试查询 写法和MP一样 可以用链式,也可以非链式 根据使用习惯灵活选择即可
        String title = "老汉";

        List<Doc> age = EsWrappers.lambdaChainQuery(docMapper).eq("age","23").list();

        System.out.println(age);

    }
}

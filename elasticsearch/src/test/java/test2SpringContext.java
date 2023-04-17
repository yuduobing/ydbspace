import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yun520.xyz.EsApplication;
import yun520.xyz.Service.ElasticService;
import yun520.xyz.entity.Doc;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class test2SpringContext {
    //测试自动注入mapper
    @Autowired
    ElasticService<Doc>  elasticService;


    @Test
    public  void tst(){
        Doc doc = new Doc();
        List<Doc> list = elasticService.searchAllText(doc,"测试");
        System.out.println(list);
    }
 }

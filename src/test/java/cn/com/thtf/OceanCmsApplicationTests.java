package cn.com.thtf;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class OceanCmsApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(JSON.toJSONString("111"));
    }

}

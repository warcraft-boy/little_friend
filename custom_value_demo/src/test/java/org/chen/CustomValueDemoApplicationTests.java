package org.chen;

import org.chen.config.CustomConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CustomValueDemoApplication.class)
@RunWith(SpringRunner.class)
public class CustomValueDemoApplicationTests {

    @Autowired
    private CustomConfig customConfig;

    @Test
    public void test01() {
        //设置单个值
        System.out.println(customConfig.getValue());
        //对象
        System.out.println(customConfig.getPerson());
        //数组
        int[] arr = customConfig.getArr();
        for(int i : arr){
            System.out.println(i);
        }
        //集合
        System.out.println(customConfig.getList());
        //map
        System.out.println(customConfig.getMap().toString());
        //包含集合的map
        System.out.println(customConfig.getListMap());
    }
}

package org.chen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chen.mq.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = RabbitmqDemoApplication.class)
@RunWith(SpringRunner.class)
public class RabbitmqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private ObjectMapper objectMapper;

    //===============================rabbitmq=================================

    /**
     * direct 点对点模式
     */
    //发送消息
    @Test
    public void test32(){
        Map<String,Object> map = new HashMap<>();
        map.put("key", "this is the fist message");
        map.put("value", 110);
        map.put("data", Arrays.asList("hello", 3, true));
        //只需要传入要发送的对象，自动序列化发送给rabbitmq
        rabbitTemplate.convertAndSend("exchange.direct", "atguigu", map); //第一个参数表示交换机，第二个表示交换机中定义的队列的key值，第三个是发的消息

        //这种可定制消息内容和消息头，message要序列化成字节数组，在org.springframework.amqp.core.Message里面
        //rabbitTemplate.send(exchange, routeKey, message);
    }
    //接受消息
    @Test
    public void test33(){
        Object obj = rabbitTemplate.receiveAndConvert("atguigu");
        System.out.println(obj.getClass());
        System.out.println(obj);
    }


    /**
     * fanout 广播模式
     */
    //发送消息
    @Test
    public void test34(){
        Book book = new Book();
        book.setName("西游记");
        book.setAuthor("吴承恩");
        //由于fanout是将消息群发到所有队列，所以路由键routKey有没有都无所谓
        rabbitTemplate.convertAndSend("exchange.fanout","", book);
    }
    //接收消息
    @Test
    public void test35(){
        Object obj = rabbitTemplate.receiveAndConvert("atguigu");
        System.out.println(obj.getClass());
        System.out.println(obj);
    }


    /**
     * topic 订阅模式
     */
    //发送消息
    @Test
    public void test36() throws JsonProcessingException {
        Book book = new Book();
        book.setName("西游记");
        book.setAuthor("吴承恩");
        //routKey路由键为"*.news"，*表示通配符，表示发送给所有已".news"结尾的队列
        rabbitTemplate.convertAndSend("exchange.topic", "*.news", book);
        //2.将对象序列化成字节数组传输
        byte[] data = objectMapper.writeValueAsBytes(book);
        rabbitTemplate.convertAndSend("exchange.topic", "*.news", data);
    }
    //接收消息
    @Test
    public void test37(){
        Object obj = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(obj.getClass());
        System.out.println(obj);
    }

    /**
     * 创建交换机，队列
     */
    //创建交换机
    @Test
    public void test38(){
        amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange.direct"));
        System.out.println("创建交换机完成");
    }
    //删除交换机
    @Test
    public void test39(){
        amqpAdmin.deleteExchange("amqpadmin.exchange.direct");
        System.out.println("删除交换机完成");
    }
    //创建队列
    @Test
    public void test40(){
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue", true));
        System.out.println("创建队列完成");

    }
    //删除队列
    @Test
    public void test41(){
        amqpAdmin.deleteQueue("amqpadmin.queue");
        System.out.println("删除队列完成");
    }
    //创建交换机和队列的绑定规则
    @Test
    public void test42(){
        amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.exchange.direct", "amqpadmin.queue", null));
    }

}

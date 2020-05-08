package org.chen.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

/**
 * @Description: <br>监听消息队列的消息
 * @Date: Created in 2020/3/9 <br>
 * @Author: chenjianwen
 */
@Service
public class BookSevice {

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "atguigu.news") //监听队列"atguigu.news"里面的消息
    public void receive(Book book){
        System.out.println("收到消息：" + book);
    }


    /**
     * 返回message可得到消息头
     * @param message
     */
    @RabbitListener(queues = "atguigu")
    public void receive2(Message message){
        System.out.println(message.getBody()); //消息体
        System.out.println(message.getMessageProperties()); //消息头信息
    }

    /**
     * 从对象的字节数组中读取对象
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = "atguigu.news") //监听队列"atguigu.news"里面的消息
    public void receive3(@Payload byte[] message) throws IOException {
        Book book = objectMapper.readValue(message, Book.class);
        System.out.println("收到消息：" + book);
    }
}

package org.chen.mq;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description: <br>自定义rabbitmq配置类
 * @Date: Created in 2020/3/9 <br>
 * @Author: chenjianwen
 */
@Configuration
@Component
@Data
public class CustomAMQPConfig {

    private String queue = "admin.log.queue";
    private String exchange = "admin.log.exchange.direct";
    private String routingKey = "admin.operate.log";

    /**
     * 自定义MessageConverter
     * 因为rabbitmq默认的SimpleMessageConverter不支持对象，会报出如下异常
     * java.lang.IllegalArgumentException: SimpleMessageConverter only supports String, byte[] and Serializable payloads, received: com.chen.rabbitmq.Book
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 创建队列，交换机，并绑定
     */
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Bean
    public Queue queue(){
        Queue queue = new Queue(this.queue);
        amqpAdmin.declareQueue(queue);
        return queue;
    }
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(this.exchange);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }
    @Bean
    public Binding binding(){
        Binding binding = BindingBuilder.bind(queue()).to(directExchange()).with(this.routingKey);
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}

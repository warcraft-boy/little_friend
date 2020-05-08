package org.chen.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description: 使用jedis配置redis服务
 * @Date: Created in 2019/12/13 <br>
 * @Author: chenjianwen
 */
@Configuration
@PropertySource("classpath:redis.properties")
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

//    @Value("${}")
//    private String password;

    @Value("${spring.redis.block-when-exhausted}")
    private boolean blockWhenExhausted;

    @Bean
    public JedisPool redisPoolFactory(){
        log.info("JedisPool注入成功");
        log.info("redis地址："+host+":"+port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(this.maxWaitMillis);
        jedisPoolConfig.setBlockWhenExhausted(this.blockWhenExhausted);
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, this.host, this.port, this.timeout);
        return jedisPool;
    }
}

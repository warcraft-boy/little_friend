package org.chen.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 使用jedis实现redis操作工具类
 * @Date: Created in 2019/12/13 <br>
 * @Author: chenjianwen
 */
@Component
public class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    //String类型操作
    public void set(String key, String value){
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
    }

    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }

    public void delete(String key){
        Jedis jedis = jedisPool.getResource();
        jedis.del(key);
    }
}

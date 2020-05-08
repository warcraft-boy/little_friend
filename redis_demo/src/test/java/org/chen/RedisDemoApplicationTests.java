package org.chen;

import org.chen.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = RedisDemoApplication.class)
@RunWith(SpringRunner.class)
class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //==================================redis测试=======================================
    @Test
    public void test06(){
        //redisTemplate.opsForValue().set("age",18);
        //Integer age = (Integer) redisTemplate.opsForValue().get("age");
        //String name = (String) redisTemplate.opsForValue().get("key");
        //System.out.println(age);
//        redisTemplate.opsForSet().add("keys", "123");
//        redisTemplate.opsForSet().add("keys", "234");
//        redisTemplate.opsForSet().add("keys", "123");
//        Set<String> keys = redisTemplate.opsForSet().members("keys");
//        System.out.println(keys);
        stringRedisTemplate.opsForValue().set("str", "chenjianwen", 50, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("str"));
    }
    @Test
    public void test07(){
//        redisUtil.set("device", "computer");
//        System.out.println(redisUtil.get("device"));
        //redisUtil.delete("device");
    }

    @Test
    public void test04(){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //jedis.set("lunch","rice");
        System.out.println(jedis.get("dinner"));
        jedis.set("dinner", "nuddle", new SetParams().nx().ex(5));
        //System.out.println(jedis.get("lunch"));
        System.out.println(jedis.get("dinner"));
    }

}

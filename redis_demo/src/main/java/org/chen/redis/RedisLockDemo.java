package org.chen.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description:利用redisTemplate实现redis分布式锁测试用例
 * @Author chenjianwen
 * @Date 2019-12-16
 **/
public class RedisLockDemo {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Redisson redisson;

    /**
     * 出现问题：多线程情况下出现超卖现象
     * 解决方法：同步块synchronized(){}
     *
     * @return
     */
    public String demo1() {
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if (stock > 0) {
            int leftStock = stock - 1;
            stringRedisTemplate.opsForValue().set("stock", leftStock + "");
        } else {
            System.out.println("扣减失败，库存不足");
        }
        return "end";
    }

    /**
     * 出现问题：多服务器节点出现超卖现象
     * 解决方法：使用redis分布式锁最基础版本
     *
     * @return
     */
    public String demo2() {
        synchronized (this) {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        }
        return "end";
    }

    /**
     * 出现问题：如果业务代码抛出异常，那么就无法释放锁，就会出现 “死锁”
     * 解决方法：使用finally，无论业务代码出现什么异常，都会执行finally代码块
     *
     * @return
     */
    public String demo3() {
        String lockKey = "product_001";
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "stock");
        if (!result) {
            return "error";
        }
        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if (stock > 0) {
            int leftStock = stock - 1;
            stringRedisTemplate.opsForValue().set("stock", leftStock + "");
        } else {
            System.out.println("扣减失败，库存不足");
        }
        //释放锁
        stringRedisTemplate.delete(lockKey);
        return "end";
    }

    /**
     * 出现问题：如果释放锁之前整个程序挂了，那么这个锁就没法释放，也会出现 “死锁”
     * 解决方法：设置redis锁失效时间
     *
     * @return
     */
    public String demo4() {
        String lockKey = "product_001";
        try {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "stock");
            if (!result) {
                return "error";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            //释放锁
            stringRedisTemplate.delete(lockKey);
        }
        return "end";
    }

    /**
     * 出现问题：设置redis值和失效时间之间系统挂了，也会出现死锁
     * 解决方法：将设置值和失效时间放在一块
     *
     * @return
     */
    public String demo5() {
        String lockKey = "product_001";
        try {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "stock");
            stringRedisTemplate.expire(lockKey, 10, TimeUnit.SECONDS); //设置失效时间
            if (!result) {
                return "error";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            //释放锁
            stringRedisTemplate.delete(lockKey);
        }
        return "end";
    }

    /**
     * 出现问题：失效时间不知道具体值，如果线程的执行时间大于失效时间，那么释放锁之前该锁就会失效，即后面的线程释放了前面线程的锁，下一个线程就会进来，这样两个或多个线程同时存在，回归到第一个问题，出现“超卖”
     * 解决方法：value的值不再写死，每个线程拥有自己的value，释放所之前判断这把锁是不是自己的
     *
     * @return
     */
    public String demo6() {
        String lockKey = "product_001";
        String clientId = UUID.randomUUID().toString();
        try {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
            if (!result) {
                return "error";
            }
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))){
                //释放锁
                stringRedisTemplate.delete(lockKey);
            }
        }
        return "end";
    }

    /**
     * 出现问题：失效时间还没有更好的解决
     * 解决方法：使用redisson在线程内部再开启一个子线程给此线程续命
     *
     * @return
     */
    public String demo7() {
        String lockKey = "product_001";
        String clientId = UUID.randomUUID().toString();
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);
            if (!result) {
                return "error";
            }
            //在业务代码之前用redisson锁续命此线程
            redissonLock.lock(30, TimeUnit.SECONDS);
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            //redisson释放锁
            redissonLock.unlock();
        }
        return "end";
    }

    /**
     * 出现问题：redis集群下，主redis节点挂掉，锁未同步到新的主节点上，后面的线程会同时持有该锁，也会出现"超卖"现象
     * 解决方法：使用redissond的boolean tryLock(long waitTime, long leaseTime, TimeUnit unit)方法，
     * 如果获得了锁，则返回true，且leaseTime后或者调用unlock()方法释放锁。否则，在waitTime时间内一直尝试请求去获得锁
     * watiTime时间应该大于网络超时时间，这样保证主redis挂掉至从redis升级到主的时间段内，此线程一直保持死死的请求状态
     *
     * @return
     */
    public String demo8() throws InterruptedException {
        String lockKey = "product_001";
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            //redisson开启锁
            Boolean result = redissonLock.tryLock(500,30000,TimeUnit.MICROSECONDS);
            if (!result) {
                return "error";
            }
            //业务代码
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int leftStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", leftStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            //redisson释放锁
            redissonLock.unlock();
        }
        return "end";
    }

}

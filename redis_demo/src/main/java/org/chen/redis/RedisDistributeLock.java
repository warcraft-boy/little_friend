package org.chen.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.UUID;

/**
 * @Description: <br>使用jedis配置redis分布式锁
 * @Date: Created in 2019/12/9 <br>
 * @Author: chenjianwen
 */
public class RedisDistributeLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final String LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final Long RELEASE_SUCCESS = 1L;


    /**
     * 获取requestId
     * @return
     */
    public static String getRequestId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 获取分布式锁
     * @param jedis         jedis
     * @param lockKey       锁
     * @param requestId    请求标识
     * @param expireTime   过期时间
     * @return
     */
    public static boolean getDistributeLock(Jedis jedis, String lockKey, String requestId, int expireTime){

        String result = jedis.set(lockKey, requestId, new SetParams().nx().ex(expireTime));
        if(LOCK_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }

    /**
     *
     * @param jedis         jedis
     * @param lockKey       锁
     * @param requestId     请求标识
     * @return
     */
    public static boolean releaseDistributeLock(Jedis jedis, String lockKey, String requestId){
        Object obj = jedis.eval(LUA_SCRIPT, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if(RELEASE_SUCCESS.equals(obj)){
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String lockKey = "lock";
        String requestId = getRequestId();
        if(getDistributeLock(jedis, lockKey, requestId, 10)){
            System.out.println("加锁");
            releaseDistributeLock(jedis, lockKey, requestId);
        }else{
            System.out.println("获取锁失败");
        }
    }
}

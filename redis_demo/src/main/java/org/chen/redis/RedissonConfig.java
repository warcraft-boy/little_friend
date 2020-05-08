package org.chen.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:配置各种情况下的redisson
 * @Author chenjianwen
 * @Date 2019-12-25
 **/
@Configuration
public class RedissonConfig {

    /**
     * 单节点redisson连接
     * @return
     */
    @Bean
    public Redisson SingleRedisson(){
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer(); //表示单点连接配置
        singleServerConfig.setAddress("redis://127.0.0.1:6379").setDatabase(0); //设置连接
        //singleServerConfig.setPassword("123456"); //设置密码
        singleServerConfig.setConnectTimeout(1000);  //设置连接等待超时时间
        return (Redisson) Redisson.create(config);
    }

    /**
     * 哨兵模式redisson连接
     * @return
     */
//    @Bean
//    public Redisson sentinelRedisson(){
//        Config config = new Config();
//        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
//        sentinelServersConfig.addSentinelAddress("redis://127.0.0.1:6379").setDatabase(0)
//                             .addSentinelAddress("redis://127.0.0.1:6380").setDatabase(0)
//                             .addSentinelAddress("redis://127.0.0.1:6381").setDatabase(0);
//        //sentinelServersConfig.setPassword("123456");    //设置密码
//        sentinelServersConfig.setMasterConnectionPoolSize(500);  //设置master节点连接池最大连接数为500
//        sentinelServersConfig.setSlaveConnectionPoolSize(500);  //设置slave节点连接池最大连接数为500
//        return (Redisson) Redisson.create(config);
//    }

    /**
     * 集群方式redisson连接
     * @return
     */
//    @Bean
//    public Redisson clusterRedisson(){
//        Config config = new Config();
//        ClusterServersConfig clusterServersConfig = config.useClusterServers();
//        clusterServersConfig.addNodeAddress("redis://127.0.0.1:6379")
//                            .addNodeAddress("redis://127.0.0.1:6380")
//                            .addNodeAddress("redis://127.0.0.1:6381");
//        //clusterServersConfig.setPassword("123456");    //设置密码
//        clusterServersConfig.setMasterConnectionPoolSize(500);   //设置master节点连接池最大连接数为500
//        clusterServersConfig.setSlaveConnectionPoolSize(500);  //设置slave节点连接池最大连接数为500
//        return (Redisson) Redisson.create(config);
//    }

    /**
     * 主从模式redisson连接
     * @return
     */
//    @Bean
//    public Redisson masterSlaveRedisson(){
//        Config config = new Config();
//        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
//        masterSlaveServersConfig.setMasterAddress("redis://127.0.0.1:6379")
//                                .addSlaveAddress("redis://127.0.0.1:6380")
//                                .addSlaveAddress("redis://127.0.0.1:6381");
//        //masterSlaveServersConfig.setPassword("123456"); //设置密码
//        masterSlaveServersConfig.setMasterConnectionPoolSize(500);   //设置master节点连接池最大连接数为500
//        masterSlaveServersConfig.setSlaveConnectionPoolSize(500);  //设置slave节点连接池最大连接数为500
//        return (Redisson) Redisson.create(config);
//    }

    /**
     * 云托管redisson连接（这种方式主要解决redis提供商为云服务的提供商的redis连接，比如亚马逊云的AWS ElastiCache和微软云的Azure Redis 缓存）
     * @return
     */
//    @Bean
//    public Redisson replicatedRedisson(){
//        Config config = new Config();
//        ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers();
//        replicatedServersConfig.addNodeAddress("redis://123.57.221.104.1:6379")
//                               .addNodeAddress("redis://123.57.221.105:6380")
//                               .addNodeAddress("redis://123.57.221.106:6382");
//        //replicatedServersConfig.setPassword("123456"); //设置密码
//        replicatedServersConfig.setMasterConnectionPoolSize(500);   //设置master节点连接池最大连接数为500
//        replicatedServersConfig.setSlaveConnectionPoolSize(500);  //设置slave节点连接池最大连接数为500
//        return (Redisson) Redisson.create(config);
//    }
}

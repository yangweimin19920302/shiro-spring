package com.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/12/1.
 * redis集群的客户端，在集群中，实例化一个JedisCluster就可以了
 */
public class JedisClusterManage {

    private static JedisCluster jedisCluster;
    private static int dataTimeout = 0;

    //静态代码初始化池配置,集群里一个JedisCluster实例就可以
    static {
        try {
            Properties properties_ip = new Properties();
            Properties properties_pool = new Properties();
            try {
                properties_ip.load(JedisClusterManage.class.getClassLoader().getResourceAsStream("redis-ip.properties"));
                properties_pool.load(JedisClusterManage.class.getClassLoader().getResourceAsStream("jedis-pool.properties"));

                //设置连接池
                GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
                genericObjectPoolConfig.setMaxTotal(Integer.valueOf(properties_pool.getProperty("jedis.pool.maxActive")));
                genericObjectPoolConfig.setMaxIdle(Integer.valueOf(properties_pool.getProperty("jedis.pool.maxIdle")));
                genericObjectPoolConfig.setMaxWaitMillis(Long.valueOf(properties_pool.getProperty("jedis.pool.maxWait")));
                genericObjectPoolConfig.setTestOnBorrow(Boolean.valueOf(properties_pool.getProperty("jedis.pool.testOnBorrow")));
                genericObjectPoolConfig.setTestOnReturn(Boolean.valueOf(properties_pool.getProperty("jedis.pool.testOnReturn")));
                dataTimeout = Integer.valueOf(properties_pool.getProperty("data.timeout"));

                //获取集群IP
                List addresss = new ArrayList(properties_ip.entrySet());
                Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
                for (Object object : addresss) {
                    String str = object.toString();
                    String address = str.substring(str.indexOf("=") + 1, str.length());
                    String [] ipAndPort = address.split(":");
                    String ip = ipAndPort[0];
                    int port = Integer.parseInt(ipAndPort[1]);
                    HostAndPort hostAndPort = new HostAndPort(ip, port);
                    hostAndPorts.add(hostAndPort);
                }
                jedisCluster = new JedisCluster(hostAndPorts, genericObjectPoolConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加
     * @param key
     * @param value
     */
    public static void set(byte [] key, byte [] value) {
        try {
            jedisCluster.set(key, value);
            jedisCluster.pexpire(key, dataTimeout);//单位毫秒
        } catch (Exception e) {

        }
    }

    /**
     * 获取
     * @param key
     * @return
     */
    public static byte [] get(byte [] key) {
        byte [] value = null;
        try {
            value = jedisCluster.get(key);
        } catch (Exception e) {

        }
        return value;
    }

    /**
     * 删除
     * @param key
     */
    public static void del(byte [] key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {

        }
    }

    /**
     * 获取所有key(包括String和byte，如果key是byte，则自动转成了String类型)
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        Set<String> keys = null;
        try {
            keys = JedisClusterKeys.keys(pattern);
        } catch (Exception e) {

        }
        return keys;
    }

    public static JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public static void main(String[] args) {
        System.out.println(jedisCluster.get("Id:430df16e-2c11-4459-ae9c-5cdd16fa5746".getBytes()));
    }
}

package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * 单机版redis的客户端
 */
public class RedisManager {
    private static JedisPool jedisPool = null;
    private static int dataTimeout = 0;

    //静态代码初始化池配置
    static {
        try {
            Properties props = new Properties();
            try {
                props.load(RedisManager.class.getClassLoader().getResourceAsStream("redis.properties"));
                dataTimeout = Integer.valueOf(props.getProperty("data.timeout"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();

            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));

            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));

            config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));

            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));

            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));

            //根据配置实例化jedis池
            jedisPool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public static void set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            jedis.expire(key, dataTimeout);
        } finally {
            jedis.close();
        }
    }

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public static void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            jedis.expire(key, dataTimeout);
        } finally {
            jedis.close();
        }
    }

    /**
     * del
     *
     * @param key
     */
    public static void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * flush
     */
    public static void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        } finally {
            jedis.close();
        }
    }

    /**
     * size
     */
    public static Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } finally {
            //jedisPool.returnResource(jedis);
            jedis.close();
        }
        return dbSize;
    }

    /**
     * keys
     *
     * @return
     */
    public static Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        } finally {
            //jedisPool.returnResource(jedis);
            jedis.close();
        }
        return keys;
    }

    public static void main(String[] args) {
        Jedis jedis = jedisPool.getResource();
        jedis.set("a".getBytes(),"b".getBytes());

        System.out.println(jedisPool.getResource().keys("sessionId:*"));
    }
}

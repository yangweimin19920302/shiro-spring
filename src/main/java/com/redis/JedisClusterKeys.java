package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JedisCluster没有提供获取所有key的方法，所有只能自定义获取keys的方法
 */
public class JedisClusterKeys {

    /**
     * 获取所有的key（String和byte类型都能获取，获取后，byte型的key会自动转成String类型）
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern){
        HashSet<String> keys = new HashSet<String>();
        Map<String, JedisPool> clusterNodes = JedisClusterManage.getJedisCluster().getClusterNodes();
        for(String k : clusterNodes.keySet()){
            JedisPool jp = clusterNodes.get(k);
            Jedis jedis = jp.getResource();
            try {
                keys.addAll(jedis.keys(pattern));
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                jedis.close();//用完一定要close这个链接！！！
            }
        }
        return keys;
    }

    public static void main(String[] args) {
        Set<String> keys = JedisClusterKeys.keys("Id:*");
        for (String key : keys) {
            System.out.println(key);
        }
    }
}
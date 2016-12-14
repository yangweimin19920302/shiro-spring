package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JedisClusterû���ṩ��ȡ����key�ķ���������ֻ���Զ����ȡkeys�ķ���
 */
public class JedisClusterKeys {

    /**
     * ��ȡ���е�key��String��byte���Ͷ��ܻ�ȡ����ȡ��byte�͵�key���Զ�ת��String���ͣ�
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
                jedis.close();//����һ��Ҫclose������ӣ�����
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
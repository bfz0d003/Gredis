package com.gry.redis.model;

import com.gry.redis.config.RedisConstant;
import com.gry.redis.utils.RhinoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * redis连接工具类
 * @author gaorongyi
 */
public class RedisConnector {
    private static Logger logger = LoggerFactory.getLogger(RedisConnector.class);
    private static JedisPoolConfig config;
    private static JedisPool pool ;
    private static Jedis jedis ;
    private static Map<String,Integer> countControlMap = null ;

    /**
     * 初始化JedisPool
     * 需要容器启动注入以下信息：<br>{RedisConstant.setSysConfig("REDIS_CONF_PUBLISH_ON","1") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_PWD","123456") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_POOLTIMEOUT","1000*1000") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_TESTONBORROW","1") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_MAXWAITMILLIS","1000*1000") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_MAXIDLE","10") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_MAXTOTAL","500") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_PORT","6379") ;
     *         <br>RedisConstant.setSysConfig("REDIS_CONF_IP","127.0.0.1") ;}
     * @throws Exception
     */
    private static synchronized void initJedisPool() throws Exception {
        if (pool != null) return ;
        String ip = RedisConstant.getSysConfig("redis_conf_ip");
        String password = RedisConstant.getSysConfig("redis_conf_pwd");
        logger.info("#### get redis pwd - " + (password==null?"NULL":"NOT NULL"));
        int port = (int) getDoubleConfig("redis_conf_port") ;
        logger.info("#### initJedisPool() LINK --> " + ip + ":" + port);
        config = new JedisPoolConfig() ;
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
        //如果赋值为-1，则表示不限制；如果pool已经分配了MaxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)
        int maxTotal = (int) getDoubleConfig("redis_conf_maxTotal") ;
        config.setMaxTotal(maxTotal);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
        int maxIdle = (int) getDoubleConfig("redis_conf_maxIdle") ;
        config.setMaxIdle(maxIdle);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
        long maxWaitMillis = (long) getDoubleConfig("redis_conf_maxWaitMillis") ;
        config.setMaxWaitMillis(maxWaitMillis);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
        int isOrNot = (int) getDoubleConfig("redis_conf_maxIdle") ;
        boolean testOnBorrow = isOrNot == 1;
        config.setTestOnBorrow(testOnBorrow);
        int poolTimeOut = (int) getDoubleConfig("redis_conf_poolTimeout") ;
        pool = new JedisPool(config, ip, port, poolTimeOut,"".equals(password)?null:password);
    }

    /**
     * 获取Jedis实例对象
     * @param randomName 随机名称
     * @param pool jedis连接池对象（可选，否则使用config通过{@link #initJedisPool()}进行注入）
     * @return
     */
    protected static synchronized Jedis getJedis(String randomName, JedisPool pool) {
        initCountControlMap(randomName) ;
        int countControl = countControlMap.get(randomName) ;
        try {
            if (pool==null) {
                initJedisPool();
            }
            jedis = pool.getResource() ;
        }catch (Exception e) {
            e.printStackTrace();
            printConnectionException("getJedis()") ;
            pool = null ;
            if (countControl<10) {
                countControlMap.put(randomName,countControl++) ;
                return getJedis(randomName,pool);
            }
        }
        countControlMap.remove(randomName) ;
        return jedis ;
    }

    private static void initCountControlMap(String randomName){
        if (countControlMap==null) {
            countControlMap = new HashMap<String, Integer>() ;
            countControlMap.put(randomName,0) ;
        }else if (countControlMap.get(randomName)==null) {
            countControlMap.put(randomName,0) ;
        }
    }

    /**
     * 关闭Jedis对象，开放此方法
     * @param jedises 对象数组
     */
    public static void close(Jedis... jedises) {
        if (jedises!=null && jedises.length>0) {
            for (Jedis jedis : jedises) {
                if (jedis!=null) {
                    jedis.close();
                }
            }
        }
    }

    /**
     * 打印异常信息，开放此方法
     * @param methodName 调用处方法名
     */
    public static void printConnectionException(String methodName) {
        logger.error("==== ["+methodName+"] Jedis connection exception, try reconnect. ====");
    }

    private static double getDoubleConfig(String config) {
        return RhinoUtils.compute(RedisConstant.getSysConfig(config),null) ;
    }
}

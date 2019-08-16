package com.gry.redis.model;

import com.gry.redis.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.commands.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Redis操作类
 *
 * @author gaorongyi
 */
@SuppressWarnings("all")
public class RedisModel implements InvocationHandler {
    private Logger logger = LoggerFactory.getLogger(RedisModel.class);
    int redisDBIndex = 0 ;
    String methodName ;
    protected JedisPool jedisPool ;
    ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<>() ;

    /**
     * Redis操作类，封装redis各种存取操作
     * 默认构造用于Spring容器加载
     */
    public RedisModel() {
        this.methodName = "RedisModel-Constructor" ;
    }

    /**
     * Redis操作类，封装redis各种存取操作
     *
     * @param redisDBIndex redis库索引号，从Constant中读取
     * @param methodName 调用处的方法名，用于变量区分
     */
    public RedisModel(int redisDBIndex, String methodName) {
        this.redisDBIndex = redisDBIndex ;
        this.methodName = methodName ;
    }

    /**
     * Redis操作类，封装redis各种存取操作
     *
     * @param jedisPool Jedis连接池
     * @param redisDBIndex redis库索引号，从Constant中读取
     * @param methodName 调用处的方法名，用于变量区分
     */
    public RedisModel(JedisPool jedisPool, int redisDBIndex, String methodName) {
        this.jedisPool = jedisPool ;
        this.redisDBIndex = redisDBIndex ;
        this.methodName = methodName ;
    }

    /**
     * 获取Jedis通用接口，包含各种Jedis常用基本操作
     * @return JedisCommands
     * @see JedisCommands
     */
    public JedisCommands getJedisCommands() {
        return (JedisCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取基本命令接口，此接口包含了操作Redis服务的常用命令，例如ping、quit、save、info等
     * @return BasicCommands
     * @see BasicCommands
     */
    public BasicCommands getBasicCommands() {
        return (BasicCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取多Key操作接口，如批量删除、map操作、sort操作等等
     * @return MultiKeyCommands
     * @see MultiKeyCommands
     */
    public MultiKeyCommands getMultiKeyCommands() {
        return (MultiKeyCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取Jedis高级操作接口，操作config、slowlog等
     * @return AdvancedJedisCommands
     * @see AdvancedJedisCommands
     */
    public AdvancedJedisCommands getAdvancedJedisCommands() {
        return (AdvancedJedisCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取脚本操作接口，可直接调用底层系统命令
     * @return ScriptingCommands
     * @see ScriptingCommands
     */
    public ScriptingCommands getScriptingCommands() {
        return (ScriptingCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取集群操作接口，用于集群命令操作
     * @return ClusterCommands
     * @see ClusterCommands
     */
    public ClusterCommands getClusterCommands() {
        return (ClusterCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    /**
     * 获取哨兵操作接口，用于哨兵模式操作
     * @return SentinelCommands
     * @see SentinelCommands
     */
    public SentinelCommands getSentinelCommands() {
        return (SentinelCommands) Proxy.newProxyInstance(Jedis.class.getClassLoader(),Jedis.class.getInterfaces(),this);
    }

    protected Jedis getConnectJedis() {
        return RedisConnector.getJedis(this.methodName + new SnowFlake().nextId(),getJedisPool()) ;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null ;
        Jedis jedis = jedisThreadLocal.get() ;
        try{
            if (jedis==null) {
                jedis = getConnectJedis() ;
                if (jedis==null)
                    return result ;
                else
                    jedisThreadLocal.set(jedis);
            }
            try {
                result = method.invoke(jedis,args); ;
            }catch (Exception e) {
                jedis.close();
                result = method.invoke(jedis,args);
                logger.error(e.getMessage());
            }
        }catch (Exception e) {
            RedisConnector.printConnectionException(method.getName());
        }finally {
            if (jedis!=null) {
                jedis.close();
                jedis = null ;
                jedisThreadLocal.remove();
            }
        }
        return result ;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}

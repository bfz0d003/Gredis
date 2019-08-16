# 基于Jedis包封装的redis开发包
## 1. 调用过程处理封装
利用反射封装了jedis的调用过程，调用jedis方法不用再关注close和异常处理等操作
## 2. 消息广播封装 - 参见DEMO
## 3. 可配合spring容器，实现jedispool注入后调用，也可手动配置jedispool实现独立调用

## DEMO:
<pre><code>
package com.gry.redis.test.demo;

import com.gry.redis.config.RedisConstant;
import com.gry.redis.model.RedisModel;
import com.gry.redis.model.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.commands.*;

/**
 * Description:
 * Author: gaorongyi
 * Version: 1.0
 * Create Date Time: 2018/7/27 17:07.
 */
public class RedisDemo {
    private static Logger logger = LoggerFactory.getLogger(RedisDemo.class);
    private static final String SOURCE_FLAG = "TEST" ;
    private static final String CHECKDATA_CHANNEL_NAME = SOURCE_FLAG + "_CHECKDATA" ;
    private static final String NOTICE_CHANNEL_NAME = SOURCE_FLAG + "_NOTICE" ;
    private static final int REDIS_DB_INDEX = 0 ;

    public static void main(String[] args) throws Exception {
        //0、初始化连接信息
        initRedisConfig();
        //1、获取RedisModel
        RedisModel model = new RedisModel(REDIS_DB_INDEX,"main_redis") ;
        // ---------------- redis常规操作示例 --------------------
        //2、获取Jedis操作接口JedisCommands
        JedisCommands jedis = model.getJedisCommands() ;
        //3、利用JedisCommands接口进行相应的jedis存取操作
        jedis.set("TEST_KEY","TEST_VALUE") ;
        logger.info("### GET VALUE IN REDIS THAT KEY IS 'TEST_KEY' : " + jedis.get("TEST_KEY"));

        // ---------------- redis订阅发布系统操作示例 --------------------
        //2、启动订阅系统
        startSubscriber();
        //睡眠5秒，等待订阅系统成功启动
        Thread.sleep(5000);
        //3、获取订阅发布系统操作接口
        MultiKeyCommands pubSubJedis = model.getMultiKeyCommands() ;
        //4、推送消息
        for (int i=0;i<50;i++) {
            pubSubJedis.publish(CHECKDATA_CHANNEL_NAME + "_" + i,"@@@ TEST PUBLISH MESSAGE: HELLO WORLD["+i+"]!") ;
            //每隔1秒推送一次消息
            Thread.sleep(1000);
        }
    }

    /**
     * 初始化Redis连接信息(首先加载)
     */
    private static void initRedisConfig() {
        RedisConstant.setSysConfig("REDIS_CONF_PUBLISH_ON","1") ;
        RedisConstant.setSysConfig("REDIS_CONF_PWD","123456") ;
        RedisConstant.setSysConfig("REDIS_CONF_POOLTIMEOUT","1000*1000") ;
        RedisConstant.setSysConfig("REDIS_CONF_TESTONBORROW","1") ;
        RedisConstant.setSysConfig("REDIS_CONF_MAXWAITMILLIS","1000*1000") ;
        RedisConstant.setSysConfig("REDIS_CONF_MAXIDLE","10") ;
        RedisConstant.setSysConfig("REDIS_CONF_MAXTOTAL","500") ;
        RedisConstant.setSysConfig("REDIS_CONF_PORT","6379") ;
        RedisConstant.setSysConfig("REDIS_CONF_IP","127.0.0.1") ;
    }

    /**
     * 启动消息订阅系统
     */
    private static void startSubscriber() {
        new Thread(new Runnable() {
            public void run() {
                runCheckData();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                runNotice();
            }
        }).start();
    }

    /**
     * 异步启动CHECKDATA消息订阅服务
     */
    private static void runCheckData(){
        logger.info("### CHECKDATA SUBSCRIBER STARTING IN CHANNEL "+CHECKDATA_CHANNEL_NAME+"* ...");
        try {
            MySubscriber subscriber = new MySubscriber() ;
            subscriber.setSourceFlag(SOURCE_FLAG);
            new RedisModel(REDIS_DB_INDEX,"runCheckData").getMultiKeyCommands().psubscribe(subscriber, CHECKDATA_CHANNEL_NAME + "*");
        } catch (Exception e) {
            logger.info("### CHECKDATA SUBSCRIBER START FAILED.");
            e.printStackTrace();
        }
    }

    /**
     * 异步启动NOTICE消息订阅服务
     */
    private static void runNotice(){
        logger.info("### NOTICE SUBSCRIBER STARTING IN CHANNEL "+NOTICE_CHANNEL_NAME+"* ...");
        try {
            MySubscriber subscriber = new MySubscriber() ;
            subscriber.setSourceFlag(SOURCE_FLAG);
            new RedisModel(REDIS_DB_INDEX,"runCheckData").getMultiKeyCommands().psubscribe(subscriber, NOTICE_CHANNEL_NAME + "*");
        } catch (Exception e) {
            logger.info("### NOTICE SUBSCRIBER START FAILED.");
            e.printStackTrace();
        }
    }
}

/**
 * 订阅者类实现
 */
class MySubscriber extends Subscriber {
    private static final String SOURCE_FLAG = "TEST" ;
    private static final String CHANNEL_NAME = SOURCE_FLAG + "_NOTICE" ;
    private static final int REDIS_DB_INDEX = 0 ;

    @Override
    public void setSourceFlag(String sourceFlag) {
        this.sourceFlag = sourceFlag ;
    }

    @Override
    public void handleMessage(String channel, String message) {

    }

    @Override
    public void handlePMessage(String pattern, String channel, String message) {

    }

    /**
     * CHECKDATA频道处理操作
     *
     * @param channel   通道名
     * @param message   接收信息
     */
    @Override
    public void handleCheckData(String channel, String message) {
        try {
            logger.info("#### CHECK DATA MESSAGE[ CHANNEL-"+channel+", MESSAGE-"+message+" ]");
            //收到消息后向Notice频道推送
            new RedisModel(REDIS_DB_INDEX,"handleCheckData").getMultiKeyCommands().publish(CHANNEL_NAME + channel.substring(channel.lastIndexOf("_")), "I HAVE GET MESSAGE["+message+"] FROM CHANNEL " + channel) ;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * NOTICE频道处理操作
     *
     * @param channel   通道名
     * @param primaryKey    redis中的主键
     */
    @Override
    public void handleCheckDataNotice(String channel, String primaryKey) {
        logger.info("#### CHECK DATA NOTICE[ CHANNEL-"+channel+", PRIMARYKEY-"+primaryKey+" ]");
    }
}
</code></pre>

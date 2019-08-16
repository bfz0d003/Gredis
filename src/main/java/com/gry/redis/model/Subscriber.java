package com.gry.redis.model;

import com.gry.redis.config.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订阅者 - redis
 * @author gaorongyi
 */
public abstract class Subscriber extends JedisPubSub {
    public static Logger logger = LoggerFactory.getLogger(Subscriber.class);
    public static byte[] lock = new byte[0] ;  //同步锁，用于同步Map操作
    public Map<String,Stack<String>> resultMap ;
    public String sourceFlag = null ;

    /**
     * 普通订阅者每当接收到新消息之后的操作
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);
        this.handleMessage(channel,message) ;
    }

    /**
     * 模板订阅者每当接收到新消息之后的操作
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        if (channel.contains(RedisConstant.CHANNEL_CHECKDATA_PREFIX)) {
            this.handleCheckData(channel,message) ;
        }else if (channel.contains(RedisConstant.CHANNEL_NOTICE_PREFIX)) {
            this.handleCheckDataNotice(channel,message);
        }else {
            this.handlePMessage(pattern,channel,message) ;
        }
    }

    /**
     * 普通订阅者启动时的初始化操作
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("#### subscribe start.");
    }

    /**
     * 普通订阅者终止订阅时触发的操作
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("#### subscribe finish.");
    }


    /**
     * 模板订阅者启动时的初始化操作
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("#### [P]Subscribe-"+pattern+" start.");
    }

    /**
     * 模板订阅者终止订阅时触发的操作
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("#### [P]Subscribe finish.");
    }

    /**
     * {@link #onMessage(String, String)} 调用
     * @param channel   通道名
     * @param message   接收信息
     */
    public abstract void handleMessage(String channel, String message) ;

    /**
     * {@link #onPMessage(String, String, String)} 调用
     * @param channel   通道名
     * @param message   接收信息
     */
    public abstract void handlePMessage(String pattern, String channel, String message) ;

    /**
     * 处理Redis中的数据，通过多线程写入mysql
     * @param channel   通道名
     * @param message   接收信息
     */
    public abstract void handleCheckData(String channel, String message) ;

    /**
     * 数据处理完之后的回复通知，用于删除已经处理完成的redis对应数据
     * @param channel   通道名
     * @param primaryKey    redis中的主键
     */
    public abstract void handleCheckDataNotice(String channel, String primaryKey) ;

    public Stack<String> getResultStack(String tblNm){
        return this.resultMap.get(tblNm);
    }

    public abstract void setSourceFlag(String sourceFlag) ;
}

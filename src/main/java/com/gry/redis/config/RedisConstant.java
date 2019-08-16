package com.gry.redis.config;

import java.util.HashMap;
import java.util.Map;

public class RedisConstant {
    //缓存方式
    public static String CACHE_TYPE_REDIS ="redis";

    public static String LOG_ERROR = "error";

    /**
     * Redis DB index list
     * REDIS_DB_CACHE - 用于通常的数据临时缓存操作
     * REDIS_DB_HBASE - 用于HBASE的数据持久化存储操作
     */
    public static final int REDIS_DB_CACHE = 0 ;
    public static final int REDIS_DB_ACCESSTOKEN = 1 ;
    public static final int REDIS_DB_HBASE = 6 ;

    //用于通知写入mysql传输的map，利用此key回删redis数据
    public static final String REDIS_KEY = "redis_key" ;

    /**
     * Redis PUB/SUB CHANNEL
     * <br>此处命名规则必须一致，全部大写
     * <br>ADM端用adm对应的channel，CRON端用cron对应的channel
     * <br>CHECKDATA类型的channel用于传入mysql，NOTICE类型的channel用于回删redis数据
     */
    public static final String CHANNEL_CHECKDATA_PREFIX = "CHECKDATA_" ;
    public static final String CHANNEL_NOTICE_PREFIX = "NOTICE_" ;

    public static String MODEL_NAME ;

    public static String CHANNEL_MODEL_CHECKDATA_PREFIX;
    public static String CHANNEL_MODEL_NOTICE_PREFIX;

    /**
     * 系统各种配置项 请使用 getSysConfig(String key)
     */
    public static Map<String, String> configMap = new HashMap<String, String>();

    /**
     * 系统各种配置项
     */
    public static String getSysConfig(String key) {
        return configMap.get(key.toUpperCase());
    }
    public static String setSysConfig(String key, String value) {
        return configMap.put(key,value);
    }
    public static Map<String,String> getConfigMap(){
        return new HashMap<>(configMap) ;
    }

    public static void setModelName(String modelName) {
        MODEL_NAME = modelName ;
        setModelCheckDataPrefix();
        setModelNoticePrefix();
    }

    private static void setModelCheckDataPrefix() {
        CHANNEL_MODEL_CHECKDATA_PREFIX = MODEL_NAME + CHANNEL_CHECKDATA_PREFIX;
    }

    private static void setModelNoticePrefix() {
        CHANNEL_MODEL_NOTICE_PREFIX = MODEL_NAME + CHANNEL_NOTICE_PREFIX;
    }

}

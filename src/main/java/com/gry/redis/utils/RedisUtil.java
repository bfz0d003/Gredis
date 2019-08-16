package com.gry.redis.utils;

import com.alibaba.fastjson.JSONObject;
import com.gry.redis.config.RedisConstant;

import java.util.Date;

/**
 * Description:
 * Author: gaorongyi
 * Version: 1.0
 * Create Date Time: 2018/7/26 16:46.
 * Update Date Time:
 *
 * @see
 */
public class RedisUtil {
    public static String getCheckdataChannel(String modelName) {
        return modelName.toUpperCase() + RedisConstant.CHANNEL_CHECKDATA_PREFIX + "[ "+TimeUtil.date2Str(new Date(),"yyyyMMddhhmmssSSS") +" ]" ;
    }
    public static String getCheckdataChannel(String modelName, String channelKey) {
        return modelName.toUpperCase() + RedisConstant.CHANNEL_CHECKDATA_PREFIX + channelKey ;
    }
    public static String getNoticeChannel(String modelName) {
        return modelName.toUpperCase() + RedisConstant.CHANNEL_NOTICE_PREFIX + "[ "+TimeUtil.date2Str(new Date(),"yyyyMMddhhmmssSSS") +" ]" ;
    }
    public static String getNoticeChannel(String modelName, String channelKey) {
        return modelName.toUpperCase() + RedisConstant.CHANNEL_NOTICE_PREFIX + channelKey ;
    }
    public static String getChannelKey(String channel){
        return channel.substring(channel.indexOf("_")+1).toLowerCase() ;
    }
    public static String generateChannelKey(JSONObject json){
        return "[ " + Md5.encode(TimeUtil.date2Str(new Date(),"yyyyMMddhhmmssSSS") + RandomUtil.getRandomName("generateChannelKey") + json.toJSONString()) + " ]";
    }
}

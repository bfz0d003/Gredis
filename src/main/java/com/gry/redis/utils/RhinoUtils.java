package com.gry.redis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Set;

/**
 * Rhino引擎计算
 *
 * @author gaorongyi
 */
public class RhinoUtils {
    private static Logger logger = LoggerFactory.getLogger(RhinoUtils.class);
    private static ScriptEngine se=new ScriptEngineManager().getEngineByName("js");
    /**
     * 利用Rhino进行公式解析和计算
     *
     * @return
     */
    public static Double compute(String exp, Map<String,String> paramsAndVals) {
        exp = removeFullChar(exp) ;
        String params = "", vals = "" ;
        if (paramsAndVals!=null) {
            Set<Map.Entry<String,String>> entrySet = paramsAndVals.entrySet() ;
            for (Map.Entry<String, String> entry : entrySet) {
                params += entry.getKey()+"," ;
                vals += entry.getValue()+"," ;
            }
            logger.info("params ==> " + params + "   vals ==> " + vals);
        }
        String script="function compute("+("".equals(params)?"":params.substring(0,params.length()-1))+") { return ("+exp+"); }";
        logger.info("#### |-EXP ==> " + exp + ", |-Script ==> " + script);
        try {
            se.eval(script);
            Object r = se.eval("compute("+("".equals(vals)?"":vals.substring(0,vals.length()-1))+")") ;
            Double result = r==null?null: Double.valueOf(r.toString()) ;
            logger.info("#### |-Result ==> " + result);
            return result==null ? null : (Double.isInfinite(result) ? null : ( Double.isNaN(result) ? null : result ));
        } catch (ScriptException e) {
            e.printStackTrace();
            logger.error(" === Loriot === Rhino Script eval Exception[exp='"+exp+"',params='"+params+"']. === ");
            return null ;
        }
    }

    /**
     * 全角字符转为半角，同时将中文标点替换为英文标点
     * @param str
     * @return
     */
    public static String removeFullChar(String str) {
        if (str==null) return null ;
        str = toDBC(str) ;
        str = str.replaceAll("，",",").replaceAll("。",".").replaceAll("‘","'").replaceAll("’","'");
        str = str.replaceAll("“","\"").replaceAll("”","\"").replaceAll("；",";") ;
        str = str.replaceAll("《","<").replaceAll("》",">").replaceAll("？","?") ;
        str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!").replaceAll("·","`") ;
        replaceAllFullCharByHalfChar(str) ;
        return str ;
    }

    /**
     * 字符串全角转半角
     *
     * @param str
     * @return
     */
    public static String toDBC(String str) {
        if (str==null) return null ;
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);
        return returnString;
    }

    public static String replaceAllFullCharByHalfChar(String old) {
        if (old==null) return null ;
        return old.replaceAll("＋","+").replaceAll("－","-").replaceAll("）",")").replaceAll("（","(").replaceAll("？","?").replaceAll("：",":").replaceAll("％","%") ;
    }
}

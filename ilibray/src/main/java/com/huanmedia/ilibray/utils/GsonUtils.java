package com.huanmedia.ilibray.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Create by Administrator
 * time: 2017/12/11.
 * Email:eric.yang@huanmedia.com
 * 用于Gson 修复当接收对象为 List<Object> new TypeToken<Map<String,Object>>() 的时候int 被转换成double 问题
 */

public class GsonUtils {

    public static GsonBuilder getDefGsonBulder(){
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Object>>(){}.getType(),
                        new CustomizedObjectTypeAdapter())//防止Obj int 转换成 double
                .registerTypeAdapter(new TypeToken<Map<String,Object>>(){}.getType(),
                new CustomizedObjectTypeAdapter());//防止Obj int 转换成 double
    }
}

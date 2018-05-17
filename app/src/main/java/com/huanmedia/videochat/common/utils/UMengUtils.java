package com.huanmedia.videochat.common.utils;

import android.content.Context;

import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.videochat.common.manager.UserManager;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class UMengUtils {

    /**
     * 友盟 红日日活统计
     *
     * @param context
     */
    public static void RedManActive(Context context) {
        try {
            int starButton = UserManager.getInstance().getCurrentUser().getUserinfo().getStarbutton();
            int isStarauth = UserManager.getInstance().getCurrentUser().getUserinfo().getIsstarauth();
            long userId = UserManager.getInstance().getCurrentUser().getId();

            if (starButton == 1 && isStarauth == 1 && userId != 0) {
                String eventID = "red_active";
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("red_id", userId + "");
                dataMap.put("system_time", TimeUtils.getNowString());
                baseSendMsg(context, eventID, dataMap, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 友盟 首页轮播广告点击
     *
     * @param context
     */
    public static void AdsBanner(Context context, String bannerId) {
        try {
            String eventID = "banner_ads";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("banner_id", bannerId);
            baseSendMsg(context, eventID, dataMap,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 友盟自定义事件
     *
     * @param context
     * @param eventID
     * @param dataMap
     * @param du
     */
    public static void baseSendMsg(Context context, String eventID, Map<String, String> dataMap, int du) {
        MobclickAgent.onEventValue(context, eventID, dataMap, du);
    }

    /**
     * 友盟自定义事件
     *
     * @param context
     * @param eventID
     * @param dataMap
     */
    public static void baseSendMsg(Context context, String eventID, Map<String, String> dataMap) {
        MobclickAgent.onEvent(context, eventID, dataMap);
    }

    /**
     * 友盟自定义事件
     *
     * @param context
     * @param eventID
     */
    public static void baseSendMsg(Context context, String eventID) {
        MobclickAgent.onEvent(context, eventID);
    }
}

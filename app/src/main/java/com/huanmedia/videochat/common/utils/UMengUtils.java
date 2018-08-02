package com.huanmedia.videochat.common.utils;

import android.content.Context;

import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.videochat.BuildConfig;
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
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 预约提示点击
     * @param context
     */
    public static void AppointmentHintClick(Context context) {
        try {
            long userId = UserManager.getInstance().getCurrentUser().getId();
            String eventID = "appointment_hint";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", userId + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 开屏页广告点击
     * @param context
     */
    public static void LuanchAdsClick(Context context, String url) {
        try {
            String eventID = "luanch_ads";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("ads_url", url + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 短视频播放
     */
    public static void ShortVideoPlay(Context context, int videoId) {
        try {
            String eventID = "video_play";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("video_id", videoId + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约进入按钮
     *
     * @param context
     * @param readmanId
     */
    public static void AppointmentIn(Context context, int readmanId) {
        try {
            String eventID = "appointment_in";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", UserManager.getInstance().getId() + "");
            dataMap.put("readman_id", readmanId + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 预约发起视频
     *
     * @param context
     * @param toid
     * @param inType  0个人信息界面拨打 1聊天界面拨打
     */
    public static void AppointmentVideo(Context context, int toid, int inType) {
        try {
            String eventID = "appointment_video";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("from_id", UserManager.getInstance().getId() + "");
            dataMap.put("to_id", toid + "");
            dataMap.put("in_type", inType + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约进入按钮
     *
     * @param context
     * @param readmanId
     */
    public static void AppointmentSubmit(Context context, int readmanId) {
        try {
            String eventID = "appointment_submit";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", UserManager.getInstance().getId() + "");
            dataMap.put("readman_id", readmanId + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通用广告点击统计
     *
     * @param context
     * @param clickType 0 取消  1点击进入
     */
    public static void CommonCoinAds(Context context, int clickType, String jumpUrl) {
        try {
            String eventID = "common_coin_ads";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", UserManager.getInstance().getId() + "");
            dataMap.put("jump_url", jumpUrl + "");
            dataMap.put("click_type", clickType + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转APP页面统计
     *
     * @param context
     * @param jumpType  0 充值頁 1，意见反馈 2 红人认证
     * @param jumpStart 0.網頁
     */
    public static void JumpActivity(Context context, int jumpType, int jumpStart) {
        try {
            String eventID = "jump_activty";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", UserManager.getInstance().getId() + "");
            dataMap.put("jump_type", jumpType + "");
            dataMap.put("jump_start", jumpStart + "");
            baseSendMsg(context, eventID, dataMap, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 按钮点击统计
     *
     * @param context
     * @param buttonType 1,编辑资料 2红人模式 3开始认证
     */
    public static void ButtonClick(Context context, int buttonType) {
        try {
            String eventID = "button_click";
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("user_id", UserManager.getInstance().getId() + "");
            dataMap.put("button_type", buttonType + "");
            baseSendMsg(context, eventID, dataMap, 1);
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
        if (BuildConfig.DEBUG) {
            return;
        }
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

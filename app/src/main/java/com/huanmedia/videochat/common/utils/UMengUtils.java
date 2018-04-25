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

    public static void baseSendMsg(Context context, String eventID, Map<String, String> dataMap, int du) {
        MobclickAgent.onEventValue(context, eventID, dataMap, du);
    }

    public static void baseSendMsg(Context context, String eventID) {
        MobclickAgent.onEvent(context, eventID);
    }
}

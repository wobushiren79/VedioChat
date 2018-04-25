package com.huanmedia.videochat.common.utils;

/**
 * @TODO Created by yb(yb498869020@hotmail.com) on 2016/8/7.
 */

public class DoubleClickUtils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean isFast=timeD<=800;
        if (!isFast)
        lastClickTime = time;
        return isFast;
    }
    public static boolean isFastDoubleClick(int duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean isFast=timeD<=duration;
        if (!isFast)
            lastClickTime = time;
        return isFast;
    }

    /**
     * 最少消耗时间每次点击间隔
     * @param minTime
     * @return
     */
    public static boolean isclickForMinTime(long minTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean isFast=timeD>=minTime;
        if (isFast)
            lastClickTime = time;
        return isFast;
    }
}

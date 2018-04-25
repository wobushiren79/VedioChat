package com.huanmedia.videochat.common.service.notifserver.ringtone;

import android.net.Uri;

import com.huanmedia.videochat.R;

/**
 * Created by ericYang on 2017/9/3.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class AlertSoundAndVibrateHelper {
    Uri soundUri;
    long[] vibrate;

    public AlertSoundAndVibrateHelper(int type, String packageName) {
        switch (type){
            case 1:
                soundUri= Uri.parse("android.resource://"+packageName+"/raw/"+ R.raw.default_ringtone);
                vibrate = new long[]{600,600};
                break;
//            case 2:
//                soundUri= Uri.parse("android.resource://"+packageName+"/raw/"+ R.raw.alert_2);
//                vibrate = new long[]{50,50,50,50,50,50,50};
//                break;
//            case 3:
//                soundUri= Uri.parse("android.resource://"+packageName+"/raw/"+ R.raw.alert_3);
//                vibrate = new long[]{800,800,300,800,800,300,800,800};
//                break;
//            case 4:
//                soundUri= Uri.parse("android.resource://"+packageName+"/raw/"+ R.raw.alert_4);
//                vibrate = new long[]{500,500};
//                break;
//            case 5:
//                soundUri= Uri.parse("android.resource://"+packageName+"/raw/"+ R.raw.alert_5);
//                vibrate = new long[]{50,50,50,50,50,50,50};
//                break;
        }
    }

    public Uri getSoundUri() {
        return soundUri;
    }

    public long[] getVibrate() {
        return vibrate;
    }
    public static String getTypeForString(int type) {
        switch (type) {
            case 1://孩子点击了SOS！
                return "向您发起视频聊天，是否接受？";
            case 2://离开电子围栏 ！
                return "%s视频聊天中，轻点继续";
//            case 3://孩子跌倒了 ！
//                return "跌倒报警";
//            case 4://孩子突然加速 ！
//                return "突然加速报警";
//            case 5://手环已脱落
//                return "手环已脱落报警";
        }
        return "未知类型";
    }
}

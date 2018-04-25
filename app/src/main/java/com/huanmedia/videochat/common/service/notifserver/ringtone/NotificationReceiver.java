package com.huanmedia.videochat.common.service.notifserver.ringtone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {


    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        if ((RingtoneManager.ACTION_DISMIS).equals(intent.getAction())) {
            RingtoneManager.getInstance().stopSoundVibrate();
        }
        if ((RingtoneManager.ACTION_CONTENT).equals(intent.getAction())) {
//                mService.stopSoundVibrate();
            intent.setAction(null);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(intent.getParcelableExtra("ComponentName"));
            context.startActivity(intent);
        }
        if ((RingtoneManager.ACTION_START_VIDEOCALL).equals(intent.getAction())) {
            intent.setAction(null);
            intent.setComponent(intent.getParcelableExtra("ComponentName"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
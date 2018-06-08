package com.huanmedia.videochat.common.service.notifserver.ringtone;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.huanmedia.ilibray.utils.AppUtil;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.my.SettingActivity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.CallingActivity;

import mvp.data.store.DataKeeper;

/**
 * Create by Administrator
 * time: 2017/12/15.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class NotificationHandler {

    public static String channelId_videoCall = "com.huanmedia.videochat.MainPresenter.VideoCall";
    public static String channelId_NoticeNoSound = "com.huanmedia.videochat.MainPresenter.NoticeNoSound";
    public static String channelId_Notice = "com.huanmedia.videochat.MainPresenter.Notice";

    /**
     * 视频电话通知
     *
     * @param videoChatEntity
     */
    public static void sendNotification(VideoChatEntity videoChatEntity) {

        String packageName = FApplication.getApplication().getPackageName();
        int alertType = 1;
        if (Check.isEmpty(packageName)) return;
        DataKeeper mDataKeeper = new DataKeeper(FApplication.getApplication(), DataKeeper.DEFULTFILE);
        if (mDataKeeper.get(SettingActivity.SETTING_KEY_SOUND, true)) {
            RingtoneManager.getInstance().aleartSoundVibrate(new AlertSoundAndVibrateHelper(alertType, packageName));
        }
        if (mDataKeeper.get(SettingActivity.SETTING_KEY_SOUND, true) ||
                !mDataKeeper.get(SettingActivity.SETTING_KEY_NO_NOTIFICATION, false)) {
            screenState();
        }
        if (AppUtil.isRunningForeground(FApplication.getApplication())) {
            return;
        }
        if (!mDataKeeper.get(SettingActivity.SETTING_KEY_NO_NOTIFICATION, false)) {
            videoNotification(videoChatEntity, packageName, alertType);
        }
    }

    /**
     * 系统消息通知
     *
     * @param mode
     */
    public static void sendNotification(NotificationMode mode) {
        sendNotification(mode, true, channelId_Notice);
    }

    public static void sendNotification(NotificationMode mode, boolean isSound, String channelID) {

        String packageName = FApplication.getApplication().getPackageName();
        if (Check.isEmpty(packageName)) return;
        DataKeeper mDataKeeper = new DataKeeper(FApplication.getApplication(), DataKeeper.DEFULTFILE);
//        if (AppUtil.isRunningForeground(FApplication.getApplication())){
//            return;
//        }
        if (!mDataKeeper.get(SettingActivity.SETTING_KEY_NO_NOTIFICATION, false)) {
            notification(mode, packageName, isSound, channelID);
        }
    }

    /**
     * 屏幕状态处理
     */
    private static void screenState() {
        PowerManager pm = (PowerManager) FApplication.getApplication().getSystemService(Context.POWER_SERVICE);
        boolean screenState;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            screenState = pm.isInteractive();
        } else {
            screenState = pm.isScreenOn();
        }

        if (!screenState) {//熄屏状态
//        KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }

    private static void notification(NotificationMode mode, String packageName, boolean isSound, String channelId) {
        int currentNotificationID = mode.getNotifiID();
        //获取NotificationManager实例
        NotificationCompat.Builder builder = new NotificationCompat.Builder(FApplication.getApplication(), channelId);
        Intent dismissIntent = new Intent(RingtoneManager.ACTION_DISMIS, null);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(FApplication.getApplication(), currentNotificationID, dismissIntent,
                0);
        ComponentName cn;
        if (mode.getIntentActivity() != null) {
            cn = new ComponentName(FApplication.getApplication(), mode.getIntentActivity());
        } else {
            cn = new ComponentName(FApplication.getApplication(), MainActivity.class);
        }
        Intent nfIntent = new Intent();
        nfIntent.putExtra("ComponentName", cn);
        nfIntent.setAction(RingtoneManager.ACTION_CONTENT);
        PendingIntent contentPendingIntent = PendingIntent.getBroadcast(FApplication.getApplication(), currentNotificationID, nfIntent, 0);
        builder.setContentIntent(contentPendingIntent) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(FApplication.getApplication().getResources(),
                        R.mipmap.icon_logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(mode.getTitle()) // 设置下拉列表里的标题
                .setContentText(mode.getContent()) // 设置上下文内容
                .setSmallIcon(R.mipmap.icon_logo) // 设置状态栏内的小图标
                .setWhen(System.currentTimeMillis())// 设置该通知发生的时间
                .setDeleteIntent(dismissPendingIntent)
                .setFullScreenIntent(PendingIntent.
                        getActivity(FApplication.getApplication(), currentNotificationID, nfIntent, 0), true)
                .setTicker(mode.getContent())
                .setAutoCancel(true);
        if (!isSound) {
            builder.setVibrate(new long[]{0, 0});
            builder.setSound(null);
        }
        NotificationManagerCompat.from(FApplication.getApplication()).notify(currentNotificationID, builder.build());
    }

    public static void videoNotification(VideoChatEntity videoChatEntity, String packageName, int alertType) {
        int currentNotificationID = Constants.NOTIFICATION_ID_VIDEO_CALL;
        //获取NotificationManager实例
        NotificationCompat.Builder builder = new NotificationCompat.Builder(FApplication.getApplication());
        Intent dismissIntent = new Intent(RingtoneManager.ACTION_DISMIS, null);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(FApplication.getApplication(), currentNotificationID, dismissIntent,
                0);
        ComponentName cn = new ComponentName(FApplication.getApplication(), CallingActivity.class);
        Intent nfIntent = new Intent();
        nfIntent.putExtra("ComponentName", cn);
        nfIntent.setAction(RingtoneManager.ACTION_START_VIDEOCALL);
        PendingIntent contentPendingIntent = PendingIntent.
                getBroadcast(FApplication.getApplication(), currentNotificationID, nfIntent, 0);
        builder.setContentIntent(contentPendingIntent) // 设置PendingIntent
                .setChannelId(channelId_videoCall)
                .setLargeIcon(BitmapFactory.decodeResource(FApplication.getApplication().getResources(),
                        R.mipmap.icon_logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(videoChatEntity.getTouidinfo().getNickname()) // 设置下拉列表里的标题
                .setContentText(String.format(AlertSoundAndVibrateHelper.getTypeForString(alertType), videoChatEntity.getTouidinfo().getNickname())) // 设置上下文内容
                .setSmallIcon(R.mipmap.icon_logo) // 设置状态栏内的小图标
                .setWhen(System.currentTimeMillis())// 设置该通知发生的时间
                .setDeleteIntent(dismissPendingIntent)
                .setFullScreenIntent(PendingIntent.
                        getActivity(FApplication.getApplication(), currentNotificationID, nfIntent, 0), false)
                .setTicker(AlertSoundAndVibrateHelper.getTypeForString(alertType))
                .setOngoing(true)
                .setAutoCancel(false);
        NotificationManagerCompat.from(FApplication.getApplication()).notify(currentNotificationID, builder.build());
    }

    public static void videoNotificationNoSound(VideoChatEntity videoChatEntity, int alertType) {
        int currentNotificationID = Constants.NOTIFICATION_ID_VIDEO_CALL;
        //获取NotificationManager实例
        NotificationCompat.Builder builder = new NotificationCompat.Builder(FApplication.getApplication());
        Intent dismissIntent = new Intent(RingtoneManager.ACTION_DISMIS, null);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(FApplication.getApplication(), currentNotificationID, dismissIntent,
                0);
        ComponentName cn = new ComponentName(FApplication.getApplication(),
                CallingActivity.class);
        Intent nfIntent = new Intent();
        nfIntent.putExtra("ComponentName", cn);
        nfIntent.setAction(RingtoneManager.ACTION_START_VIDEOCALL);
        PendingIntent contentPendingIntent = PendingIntent.
                getBroadcast(FApplication.getApplication(), currentNotificationID, nfIntent, 0);
        builder.setContentIntent(contentPendingIntent) // 设置PendingIntent
                .setChannelId(channelId_NoticeNoSound)
                .setLargeIcon(BitmapFactory.decodeResource(FApplication.getApplication().getResources(),
                        R.mipmap.icon_logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(videoChatEntity.getTouidinfo().getNickname()) // 设置下拉列表里的标题
                .setContentText(String.format(AlertSoundAndVibrateHelper.getTypeForString(alertType), videoChatEntity.getTouidinfo().getNickname())) // 设置上下文内容
                .setSmallIcon(R.mipmap.icon_logo) // 设置状态栏内的小图标
                .setWhen(System.currentTimeMillis())// 设置该通知发生的时间
                .setDeleteIntent(dismissPendingIntent)
                .setSound(null)
                .setVibrate(new long[]{0, 0})
                .setLights(0, 0, 0)
                .setTicker(AlertSoundAndVibrateHelper.getTypeForString(alertType))
                .setOngoing(true)
                .setAutoCancel(false);
        NotificationManagerCompat.from(FApplication.getApplication()).notify(currentNotificationID, builder.build());
    }

    public static void createChannelId(String channelId, String name) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) FApplication.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            String description = "视频通话";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public static void createChannelNoSound(String channelId, String name) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) FApplication.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            String description = "视频通话";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(false);
            mChannel.setSound(null, null);
            mChannel.setVibrationPattern(new long[]{0, 0});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    public static void cancle(int id) {
        NotificationManagerCompat.from(FApplication.getApplication()).cancel(id);
    }
}

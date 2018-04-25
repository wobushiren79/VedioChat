package com.huanmedia.videochat.common.service.update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.FApplication;

import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.base.DownloadNotifier;
import org.lzh.framework.updatepluginlib.impl.DefaultDownloadNotifier;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;
import java.util.UUID;

/**
 * <p>
 * 很多小伙伴提意见说需要一个下载时在通知栏进行进度条显示更新的功能。
 * 此类用于提供此种需求的解决方案。以及如何对其进行定制。满足任意场景使用
 * 默认使用参考：{@link DefaultDownloadNotifier}
 * </p>
 */
public class NotificationDownloadCreator implements DownloadNotifier {
    public static String channelId = "com.huanmedia.videochat.common.service.UpdateService";

    @Override
    public DownloadCallback create(Update update, Activity activity) {
        createChannelId();
        // 返回一个UpdateDownloadCB对象用于下载时使用来更新界面。
        return new NotificationCB(activity);
    }

    private static class NotificationCB implements DownloadCallback {

        NotificationManager manager;
        NotificationCompat.Builder builder;
        int id;
        int preProgress;

        NotificationCB(Activity activity) {
            this.manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(activity, channelId);
            builder.setProgress(100, 0, false)
                    .setSmallIcon(activity.getApplicationInfo().icon)
                    .setAutoCancel(false)
                    .setContentText(activity.getString(R.string.app_name) + "更新")
                    .setSubText("0%")
                    .build();
            id = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onDownloadStart() {
            // 下载开始时的通知回调。运行于主线程
            manager.notify(id, builder.build());
        }

        @Override
        public void onDownloadComplete(File file) {
            // 下载完成的回调。运行于主线程
            manager.cancel(id);
            installAPK( file);
        }

        @Override
        public void onDownloadProgress(long current, long total) {
            // 下载过程中的进度信息。在此获取进度信息。运行于主线程
            int progress = (int) (current * 1f / total * 100);
            // 过滤不必要的刷新进度
            if (preProgress < progress) {
                preProgress = progress;
                builder.setSubText(progress + "%")
                        .setProgress(100, progress, false);
                manager.notify(id, builder.build());
            }
        }

        @Override
        public void onDownloadError(Throwable t) {
            // 下载时出错。运行于主线程
            manager.cancel(id);
        }
    }

    protected static void createChannelId() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) FApplication.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            String description = "萌面更新";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(channelId, "UpData", importance);
            mChannel.setDescription(description);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            mChannel.setSound(null, null);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private static void installAPK(File file) {
        if (Build.VERSION.SDK_INT < 23) {
            Uri uri = Uri.fromFile(file);
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(uri);
            intents.setDataAndType(uri, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FApplication.getApplication().getApplicationContext().startActivity(intents);
        } else {
            if (file.exists()) {
                openFile(file);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private static void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            FApplication.getApplication().getApplicationContext().startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(FApplication.getApplication().getApplicationContext(), "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    private static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }


}

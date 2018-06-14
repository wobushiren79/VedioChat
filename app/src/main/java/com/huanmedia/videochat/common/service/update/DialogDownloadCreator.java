package com.huanmedia.videochat.common.service.update;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.io.FileUtils;
import com.huanmedia.videochat.R;

import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.base.DownloadNotifier;
import org.lzh.framework.updatepluginlib.impl.DefaultDownloadNotifier;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

import java.io.File;

/**
 * <p>
 * 很多小伙伴提意见说需要一个下载时在通知栏进行进度条显示更新的功能。
 * 此类用于提供此种需求的解决方案。以及如何对其进行定制。满足任意场景使用
 * 默认使用参考：{@link DefaultDownloadNotifier}
 * </p>
 */
public class DialogDownloadCreator implements DownloadNotifier {
    private DownloadCallback mNotification;
    private Activity mActivity;

    @Override
    public DownloadCallback create(Update update, Activity activity) {
        this.mActivity = activity;
        if (activity == null || activity.isFinishing()) {
            Log.e("DownDialogCreator--->", "show download dialog failed:activity was recycled or finished");
            return null;
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .theme(Theme.LIGHT)
                .title("新版本下载")
                .content("萌面-" + update.getVersionName() + "\n" + "大小:" + FileUtils.formatSize(update.getApkSize()) + "\n" + update.getUpdateContent())
                .widgetColorRes(R.color.base_yellow)
                .progress(false, 100, false)
                .cancelable(false)
                .canceledOnTouchOutside(false);
        if (!update.isForced()) {
            builder.negativeText("后台更新");
            builder.onNegative((dialog, which) -> {
                mNotification = new NotificationDownloadCreator().create(update, activity);
                mNotification.onDownloadStart();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }
        MaterialDialog dialog = builder.build();
        SafeDialogHandle.safeShowDialog(dialog);
        return new DownloadCallback() {
            @Override
            public void onDownloadStart() {
            }

            @Override
            public void onDownloadComplete(File file) {
                if (mNotification != null) {
                    mNotification.onDownloadComplete(file);
                } else {
                    SafeDialogHandle.safeDismissDialog(dialog);
                }
            }

            @Override
            public void onDownloadProgress(long current, long total) {
                if (mNotification != null) {
                    mNotification.onDownloadProgress(current, total);
                } else {
                    int percent = (int) (current * 1.0f / total * 100);
                    int artificialPercent = (int) (percent * 0.6f + 40f);
                    dialog.setProgress(artificialPercent);
                }
            }

            @Override
            public void onDownloadError(Throwable t) {
                ToastUtils.showToastLongInCenter(mActivity,"下载APK失败");
                if (mNotification != null) {
                    mNotification.onDownloadError(t);
                } else {
                    SafeDialogHandle.safeDismissDialog(dialog);
                }
            }
        };
    }
}

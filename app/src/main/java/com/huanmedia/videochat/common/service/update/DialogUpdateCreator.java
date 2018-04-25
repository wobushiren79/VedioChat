package com.huanmedia.videochat.common.service.update;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.videochat.R;

import org.lzh.framework.updatepluginlib.base.CheckNotifier;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

/**
 * 简单实现：使用通知对用户提示：检查到有更新
 */
public class DialogUpdateCreator extends CheckNotifier {
    @Override
    public Dialog create(Activity activity) {

        if (activity == null || activity.isFinishing()) {
            Log.e("CheckNotifier--->","Activity was recycled or finished,dialog shown failed!");
            return null;
        }

        String updateContent = activity.getText(org.lzh.framework.updatepluginlib.R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .content(updateContent)
                .title(org.lzh.framework.updatepluginlib.R.string.update_title)
                .positiveText(org.lzh.framework.updatepluginlib.R.string.update_immediate)
                .positiveColorRes(R.color.base_yellow)
                .neutralColorRes(R.color.base_yellow)
                .negativeColorRes(R.color.base_gray)
                .onPositive((dialog, which) -> {
                    sendDownloadRequest();
                    SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                });
        if (update.isIgnore() && !update.isForced()) {
            builder.neutralText(org.lzh.framework.updatepluginlib.R.string.update_ignore);
            builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    sendUserIgnore();
                    SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                }
            });
        }

        if (!update.isForced()) {
            builder.negativeText(org.lzh.framework.updatepluginlib.R.string.update_cancel);
            builder.onNegative((dialog, which) -> {
                sendUserCancel();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }
        builder.cancelable(false);
        return builder.build();
    }
}

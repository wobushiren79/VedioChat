package com.huanmedia.videochat.common.service.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.videochat.R;

import org.lzh.framework.updatepluginlib.base.InstallNotifier;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

/**
 * Create by Administrator
 * time: 2018/1/9.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CustomInstallNotifier extends InstallNotifier{

    @Override
    public Dialog create(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            Log.e("DownDialogCreator--->","show install dialog failed:activity was recycled or finished");
            return null;
        }
        String updateContent = activity.getText(org.lzh.framework.updatepluginlib.R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        MaterialDialog.Builder builder =new MaterialDialog.Builder(activity)
                .positiveColorRes(R.color.base_yellow)
                .negativeColorRes(R.color.base_gray)
                .neutralColorRes(R.color.base_gray)
                .title(org.lzh.framework.updatepluginlib.R.string.install_title)
                .content(updateContent)
                .positiveText(org.lzh.framework.updatepluginlib.R.string.install_immediate)
                .onPositive((dialog, which) -> {
                    if (!update.isForced()) {
                        SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                    }
                    sendToInstall();
                });
        if (!update.isForced() && update.isIgnore()) {
            builder.neutralText(org.lzh.framework.updatepluginlib.R.string.update_ignore);
            builder.onNeutral((dialog, which) -> {
                sendCheckIgnore();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }

        if (!update.isForced()) {
            builder.negativeText(org.lzh.framework.updatepluginlib.R.string.update_cancel);
            builder.onNegative((dialog, which) -> {
                sendUserCancel();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }
        MaterialDialog installDialog = builder.build();
        installDialog.setCancelable(false);
        installDialog.setCanceledOnTouchOutside(false);
        return installDialog;
    }

}

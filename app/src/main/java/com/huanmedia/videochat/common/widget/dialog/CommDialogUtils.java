package com.huanmedia.videochat.common.widget.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.videochat.R;

/**
 * Create by Administrator
 * time: 2017/12/19.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CommDialogUtils {
    public static MaterialDialog showInsufficientBalance(Context context, MaterialDialog.SingleButtonCallback callback){
        return new MaterialDialog
                .Builder(context)
                .title("余额不足")
                .content("您的钻石余额不足，是否需要充值？")
                .negativeText("前往充值")
                .negativeColorRes(R.color.base_yellow)
                .positiveColorRes(R.color.base_gray)
                .positiveText("取消")
                .onNegative(callback).show();
    }

    public static MaterialDialog showNavToLogin(Context context,MaterialDialog.SingleButtonCallback callback) {
        return new MaterialDialog.Builder(context)
                .content("您还没有登录，请登录后再试")
                .negativeText("取消")
                .negativeColorRes(R.color.base_gray)
                .positiveColorRes(R.color.base_yellow)
                .positiveText("去登录")
                .onPositive(callback)
                .show();
    }

}

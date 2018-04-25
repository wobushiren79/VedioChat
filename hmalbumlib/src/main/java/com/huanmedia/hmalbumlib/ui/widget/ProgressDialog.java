package com.huanmedia.hmalbumlib.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.huanmedia.hmalbumlib.R;

/**
 * Create by Administrator
 * time: 2017/11/22.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ProgressDialog extends Dialog {
    public ProgressDialog(@NonNull Context context) {
        this(context,R.style.HM_AppBaseTheme_DialogTheme);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_dialog_loading);

    }
}

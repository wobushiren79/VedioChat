package com.huanmedia.ilibray.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToastShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}

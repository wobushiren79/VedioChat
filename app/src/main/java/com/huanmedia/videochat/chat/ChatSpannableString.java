package com.huanmedia.videochat.chat;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.google.gson.internal.LinkedTreeMap;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.videochat.R;

import java.util.Map;

public class ChatSpannableString extends Spanny {

    private Context mContext;

    public ChatSpannableString(Context context, CharSequence source) {
        super(source);
        this.mContext = context;
        initEmoticon();
    }

    private void initEmoticon() {
        this.findAndSpan("[测试]", () -> new ImageSpan(mContext, R.mipmap.ic_launcher));
    }
}

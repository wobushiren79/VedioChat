package com.huanmedia.videochat.chat;

import android.text.SpannableString;

import com.google.gson.internal.LinkedTreeMap;
import com.huanmedia.videochat.R;

import java.util.Map;

public class ChatSpannableString extends SpannableString {

    private Map<String, Integer> EmoticonList = new LinkedTreeMap<>();

    public ChatSpannableString(CharSequence source) {
        super(source);
        initEmoticon();
    }

    private void initEmoticon() {
        EmoticonList.put("<ex_test>", R.mipmap.ic_launcher);
    }
}

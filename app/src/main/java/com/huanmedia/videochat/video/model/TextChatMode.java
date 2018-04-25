package com.huanmedia.videochat.video.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Create by Administrator
 * time: 2017/12/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class TextChatMode {
    private String mDeffColorText;


    @Retention(RetentionPolicy.SOURCE)
    public @interface ChatType {
        int TEXT = 1, TAGTEXT = 2, GIFT = 3;
    }
    private String text;
    private @ChatType int Type;

    public TextChatMode(String text, int type) {
        this.text = text;
        Type = type;
    }
    public String getDeffColorText() {
        return mDeffColorText;
    }

    public void setDeffColorText(String deffColorText) {
        mDeffColorText = deffColorText;
    }

    public String getText() {
        return text;
    }
    public int getType() {
        return Type;
    }
}

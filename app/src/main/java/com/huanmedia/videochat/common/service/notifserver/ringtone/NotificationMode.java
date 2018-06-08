package com.huanmedia.videochat.common.service.notifserver.ringtone;

/**
 * Create by Administrator
 * time: 2018/1/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class NotificationMode {
    int notifiID;
    String title;
    String content;
    Class intentActivity;

    public Class getIntentActivity() {
        return intentActivity;
    }

    public void setIntentActivity(Class intentActivity) {
        this.intentActivity = intentActivity;
    }

    public int getNotifiID() {
        return notifiID;
    }

    public void setNotifiID(int notifiID) {
        this.notifiID = notifiID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

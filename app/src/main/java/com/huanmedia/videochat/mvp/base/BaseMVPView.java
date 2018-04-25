package com.huanmedia.videochat.mvp.base;

import android.content.Context;

public interface BaseMVPView {
    Context getContext();

    void showToast(String toast);
}

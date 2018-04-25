package com.huanmedia.videochat.mvp.base;

public interface DataCallBack<T> {
    void getDataSuccess(T data);

    void getDataFail(String msg);
}

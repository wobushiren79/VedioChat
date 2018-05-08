package com.huanmedia.videochat.mvp.base;

public interface DataFileCallBack<T> {
    void getDataSuccess(T data);

    void getDataFail(String msg);

    void getDataOnProgress(long currentSize, long totalSize);
}

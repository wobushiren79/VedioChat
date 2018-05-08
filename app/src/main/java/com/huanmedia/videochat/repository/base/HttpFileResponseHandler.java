package com.huanmedia.videochat.repository.base;

public abstract class HttpFileResponseHandler<T> {
    public abstract void onSuccess(T result);

    public abstract void onError(String message);

    public abstract void onProgress(long currentSize, long totalSize);
}

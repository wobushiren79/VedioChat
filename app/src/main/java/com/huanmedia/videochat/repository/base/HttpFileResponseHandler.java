package com.huanmedia.videochat.repository.base;

public abstract class HttpFileResponseHandler<T> extends HttpResponseHandler<T> {

    public abstract void onProgress(long currentSize, long totalSize);
}

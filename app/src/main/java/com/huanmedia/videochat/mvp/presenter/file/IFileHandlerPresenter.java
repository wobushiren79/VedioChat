package com.huanmedia.videochat.mvp.presenter.file;

import android.content.ContentResolver;
import android.net.Uri;

import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;

public interface IFileHandlerPresenter {
    /**
     * 从系统相册获取的信息中提取视频数据
     * @param uri
     * @param contentResolver
     * @return
     */
    VideoInfoRequest getVideoInfoByUri(Uri uri, ContentResolver contentResolver);
}

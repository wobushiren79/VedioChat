package com.huanmedia.videochat.mvp.presenter.file;

import com.huanmedia.videochat.mvp.entity.request.UploadImagesRequest;

import java.util.List;

public interface IFileUpLoadImagePresenter {
    void uploadImage(UploadImagesRequest params, List<String> images);
}

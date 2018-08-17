package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.request.UploadImagesRequest;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.ArrayList;
import java.util.List;

public interface IFileUpLoadImageView extends BaseMVPView {
    /**
     * 上传图片成功
     *
     * @param listData
     */
    void uploadImageSuccess(ArrayList<PhotosEntity> listData);

    /**
     * 上传图片失败
     *
     * @param msg
     */
    void uploadImageFail(String msg);

}

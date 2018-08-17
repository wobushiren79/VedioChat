package com.huanmedia.videochat.mvp.view.photo;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IPhotoInfoChangeView extends BaseMVPView {

    /**
     * 修改图片信息成功
     * @param photoId
     */
    void changePhotoInfoSuccess(int photoId);

    /**
     * 修改图片信息失败
     * @param msg
     */
    void changePhotoInfoFail(String msg);
}

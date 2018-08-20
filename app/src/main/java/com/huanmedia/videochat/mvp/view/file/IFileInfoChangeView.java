package com.huanmedia.videochat.mvp.view.file;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IFileInfoChangeView extends BaseMVPView {

    /**
     * 修改图片信息成功
     *
     * @param fileId
     */
    void changeFileInfoSuccess(int fileId);

    /**
     * 修改图片信息失败
     *
     * @param msg
     */
    void changeFileInfoFail(String msg);
}

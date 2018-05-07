package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;

public interface IUploadUserDataView extends BaseMVPView {
    /**
     * 获取纬度
     * @return
     */
    String getLat();

    /**
     * 获取经度
     * @return
     */
    String getLng();

    /**
     * 上传用户数据成功
     */
    void uploadUserDataSuccess();

    /**
     * 上传用户数据失败
     */
    void uploadUserDataFail(String msg);
}

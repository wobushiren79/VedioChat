package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;

public interface IBusinessCardInfoView extends BaseMVPView {
    /**
     * 获取个人信息成功
     * @param results
     */
    void getBusinessCardInfoSuccess(BusinessCardInfoResults results);

    /**
     * 获取个人信息失败
     * @param msg
     */
    void getBusinessCardInfoFail(String msg);
}

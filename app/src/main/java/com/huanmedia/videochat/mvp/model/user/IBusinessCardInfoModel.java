package com.huanmedia.videochat.mvp.model.user;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;

public interface IBusinessCardInfoModel {
    /**
     * 获取个人名片信息
     *
     * @param params
     * @param callBack
     */
    void getContactUnLockInfo(Context context, BusinessCardInfoRequest params, DataCallBack callBack);
}

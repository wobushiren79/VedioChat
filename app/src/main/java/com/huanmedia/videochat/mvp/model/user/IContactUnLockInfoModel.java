package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ContactUnLockInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.UnLockContactResults;

public interface IContactUnLockInfoModel {
    /**
     * 获取联系方式解锁信息
     *
     * @param params
     * @param callBack
     */
    void getContactUnLockInfo(ContactUnLockInfoRequest params, DataCallBack callBack);


    /**
     * 解锁联系方式
     * @param params
     * @param callBack
     */
    void unlockContact(ContactUnLockInfoRequest params, DataCallBack callBack);
}

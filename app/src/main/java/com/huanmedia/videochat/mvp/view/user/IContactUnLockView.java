package com.huanmedia.videochat.mvp.view.user;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;

public interface IContactUnLockView extends BaseMVPView {
    void getContactUnLockInfoSuccess(ContactUnLockInfoResults results);

    void getContactUnLockInfoFail(String msg);

    void unlockSuccess();

    void unlockFail(String msg);


    long getReadManId();
}

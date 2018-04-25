package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.ContactUnLockInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.UnLockContactResults;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

public class ContactUnLockInfoModelImpl extends BaseMVPModel implements IContactUnLockInfoModel {
    private MainRepostiory mainRepostiory;

    public ContactUnLockInfoModelImpl() {
        this.mainRepostiory = new MainRepostiory();
    }

    @Override
    public void getContactUnLockInfo(ContactUnLockInfoRequest params, DataCallBack callBack) {
        mainRepostiory.getotheruserwxqq(params.getOuid()).subscribe(
                infoResults -> {
                    callBack.getDataSuccess(infoResults);
                },
                throwable -> {
                    callBack.getDataFail(throwable.getMessage());
                }
        );
    }

    @Override
    public void unlockContact(ContactUnLockInfoRequest params, DataCallBack callBack) {
        mainRepostiory.viewotherwxqqcoin(params.getOuid()).subscribe(
                infoResults -> {
                    callBack.getDataSuccess(infoResults);
                },
                throwable -> {
                    callBack.getDataFail(throwable.getMessage());
                }
        );
    }
}

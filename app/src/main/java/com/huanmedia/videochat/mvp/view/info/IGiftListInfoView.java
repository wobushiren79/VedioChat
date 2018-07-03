package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.repository.entity.GiftEntity;

import java.util.List;

public interface IGiftListInfoView extends BaseMVPView {
    /**
     * 获取礼物列表成功
     * @param listData
     */
    void getGiftListSuccess(List<GiftEntity> listData);

    /**
     * 获取礼物列表失败
     * @param msg
     */
    void getGiftListFail(String msg);
}

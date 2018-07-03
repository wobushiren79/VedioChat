package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;
import com.huanmedia.videochat.video.model.GiftModeMapping;

import java.util.ArrayList;
import java.util.List;

public class GiftListInfoModelImpl extends BaseMVPModel implements IGiftListInfoModelImpl {
    @Override
    public void getGiftList(Context context, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getGiftList(context, new HttpResponseHandler<List<ArrayList<GiftEntity>>>() {
            @Override
            public void onSuccess(List<ArrayList<GiftEntity>> result) {
                List<GiftEntity> listData = new ArrayList<>();
                for (ArrayList<GiftEntity> itemList : result) {
                    for (GiftEntity item : itemList) {
                        item.set_localMode(GiftModeMapping.getGiftMapping().get(item.getId()));
                    }
                    listData.addAll(itemList);
                }
                callBack.getDataSuccess(listData);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}

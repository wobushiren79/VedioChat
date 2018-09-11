package com.huanmedia.videochat.mvp.model.user;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.DynamicListRequset;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;

import java.util.ArrayList;
import java.util.List;

public class DynamicModelImpl extends BaseMVPModel implements IDynamicModel {
    @Override
    public void getDynamicList(DynamicListRequset params, DataCallBack callBack) {
        DynamicListResults dynamicListResults = new DynamicListResults();
        List<DynamicListResults.DynamicItem> itemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicListResults.DynamicItem item = new DynamicListResults.DynamicItem();
            itemList.add(item);
        }
        dynamicListResults.setList(itemList);
        callBack.getDataSuccess(dynamicListResults);
    }
}

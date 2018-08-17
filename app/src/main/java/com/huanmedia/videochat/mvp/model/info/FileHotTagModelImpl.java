package com.huanmedia.videochat.mvp.model.info;

import android.content.Context;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.results.FileHotTagResults;
import com.huanmedia.videochat.repository.base.HttpResponseHandler;
import com.huanmedia.videochat.repository.net.MHttpManagerFactory;

import java.util.List;

public class FileHotTagModelImpl extends BaseMVPModel implements IFileHotTagModel {
    @Override
    public void getHotTagList(Context context, DataCallBack callBack) {
        MHttpManagerFactory.getMainManager().getFileHotTagList(context, new HttpResponseHandler<List<FileHotTagResults>>() {
            @Override
            public void onSuccess(List<FileHotTagResults> result) {
                callBack.getDataSuccess(result);
            }

            @Override
            public void onError(String message) {
                callBack.getDataFail(message);
            }
        });
    }
}

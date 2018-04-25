package com.huanmedia.videochat.my;

import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.hmalbumlib.ui.widget.ProgressDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import mvp.presenter.Presenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Create by Administrator
 * time: 2018/1/2.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class FeedBackPresenter extends Presenter<FeedBackView> {
    private final MainRepostiory mMainRepostiory;
    private ProgressDialog mDialog;

    public FeedBackPresenter() {
        mMainRepostiory=new MainRepostiory();
    }

    public void opinionFeedBack(String msg, List<HM_ImgData> images) {
        HashMap<String, RequestBody> fileMap = new HashMap<>();
        for (int i = 0; i < images.size(); i++) {
            File file = new File(images.get(i).getImage());
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileMap.put("img["+i+"]\"; filename=\"" + file.getName() + ".png", fileBody);
        }
        RequestBody msgBody = RequestBody.create(MediaType.parse("multipart/form-data"), msg);
        fileMap.put("message",msgBody);

        getView().showLoading(null);
        addDisposable(mMainRepostiory.uploadFileWithPartMap("/index/userext/submitmessage", new HashMap<>(), fileMap).subscribe(
                response -> {
                    getView().hideLoading();
                    getView().showError(0, "反馈已收到，我们会尽快处理回复");
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }
}

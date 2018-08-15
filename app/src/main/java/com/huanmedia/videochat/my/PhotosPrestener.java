package com.huanmedia.videochat.my;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mvp.presenter.Presenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Create by Administrator
 * time: 2017/12/29.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class PhotosPrestener extends Presenter<PhotosView> {
    private final MainRepostiory mMainRepostiory;

    public PhotosPrestener() {
        mMainRepostiory = new MainRepostiory();
    }

    public void upImages(String url, List<String> images) {
        HashMap<String, RequestBody> fileMap = new HashMap<>();
        if (Check.isEmpty(images)) return;
        for (int i = 0; i < images.size(); i++) {
            File file = new File(images.get(i));
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileMap.put("img[" + i + "]\"; filename=\"" + file.getName() + ".png", fileBody);
        }
        getView().showLoading(null);
        addDisposable(mMainRepostiory.uploadFileWithPartMap(url, new HashMap<>(), fileMap).subscribe(
                response -> {
                    Gson gson = GsonUtils.getDefGsonBulder().create();
                    String string = gson.toJson(response.getResult());
                    ArrayList<PhotosEntity> photos = gson.fromJson(string, new TypeToken<List<PhotosEntity>>() {
                    }.getType());
                    getView().hideLoading();
                    getView().upPhotoSuccess(photos);
                    getView().showError(0, "照片已更新");
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }


    public void deletePhotos(String s) {
        getView().showLoading(null);
        addDisposable(mMainRepostiory.delomorephoto(s).subscribe(
                response -> {
                    getView().hideLoading();
                    getView().deleteSuccess();
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }

    public void updateUserPhotosOrder(List<PhotosEntity> datas) {
        if (datas != null && datas.size() == 0) {
            return;
        }
        getView().showLoading(null);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < datas.size(); i++) {
            PhotosEntity item = datas.get(i);
            if (item instanceof PhotosEntity) {
                int id = ((PhotosEntity) item).getId();
                buffer.append(id).append("-").append(i+1).append(",");
            }
        }
        String parms = buffer.toString();
        if (parms.endsWith(",")) {
            parms = parms.substring(0, parms.length() - 1);
        }
        addDisposable(mMainRepostiory.updateuserphotosorder(parms).subscribe(
                response -> {
                    getView().hideLoading();
                    getView().updateUserPhotosOrderSuccess();
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

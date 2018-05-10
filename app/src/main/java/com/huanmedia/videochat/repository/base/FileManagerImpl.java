package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.mvp.entity.request.UserVideoUpLoadRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoUpLoadResults;
import com.huanmedia.videochat.repository.aliyun.AliyunHandler;
import com.huanmedia.videochat.repository.entity.PhpotsEntity;
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mvp.data.dispose.interactor.ThreadExecutorHandler;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class FileManagerImpl extends BaseManagerImpl implements FileManager {
    private static FileManager manager;
    private RemoteApiService mApiService;

    private FileManagerImpl() {
        super();
        mApiService = ResourceManager.getInstance().getDefaultApiService();
    }

    public static FileManager getInstance() {
        if (manager == null) {
            synchronized (FileManagerImpl.class) {
                if (manager == null) {
                    manager = new FileManagerImpl();
                }
            }
        }
        return manager;
    }

    @Override
    public OSSAsyncTask upLoadFileToAliyun(Context context, FileUpLoadResults upLoadResults, HttpFileResponseHandler handler) {
        AliyunHandler aliyunHandler = new AliyunHandler();
        //初始化阿里云
        OSS oss = aliyunHandler.initData(FApplication.getApplication(), upLoadResults.getAccessKeyID(), upLoadResults.getAccessKeySecret(), upLoadResults.getToken());
        //上传文件
        OSSAsyncTask uploadTask = aliyunHandler.uploadFile(context, oss, upLoadResults.getBucket(), upLoadResults.getFilename(), upLoadResults.getFilePath(), handler);
        return uploadTask;
    }

    @Override
    public void upLoadFile(Context context, FileUpLoadResults upLoadResults, HttpFileResponseHandler handler) {

    }

    @Override
    public void upLoadImage(Context context, List<String> images, HttpResponseHandler<ArrayList<PhpotsEntity>> handler) {

    }

    @Override
    public void userVideoUpLoad(Context context, UserVideoUpLoadRequest params, HttpResponseHandler<UserVideoUpLoadResults> handler) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bindfilename", params.getBindfilename());
        paramsMap.put("fullname", params.getFullname());
        upImages(context, "index/userextv2/ossvoidupload", paramsMap, params.getImg(), handler);
    }


    /**
     * 上传图片
     * @param context
     * @param url
     * @param params
     * @param images
     * @param handler
     */
    private void upImages(Context context, String url, HashMap<String, String> params, List<String> images, HttpResponseHandler handler) {
        HashMap<String, RequestBody> fileMap = new HashMap<>();
        if (Check.isEmpty(images)) return;
        for (int i = 0; i < images.size(); i++) {
            File file = new File(images.get(i));
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileMap.put("img[" + i + "]\"; filename=\"" + file.getName() + ".png", fileBody);
        }
        mApiService.uploadFileWithPartMap(url, params, fileMap)
                .compose(ThreadExecutorHandler.toMain(mThreadProvider))
                .subscribe(
                        response -> {
                            if (response.getCode() == 0) {
                                handler.onSuccess(response.getResult());
                            } else {
                                handler.onError(response.getMessage());
                            }
                        },
                        throwable -> {
                            handler.onError(throwable.getMessage());
                        }
                );
    }

}

package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.mvp.entity.request.ChatSendRequest;
import com.huanmedia.videochat.mvp.entity.request.FileInfoChangeRequest;
import com.huanmedia.videochat.mvp.entity.request.UploadImagesRequest;
import com.huanmedia.videochat.mvp.entity.request.UserVideoDataRequest;
import com.huanmedia.videochat.mvp.entity.results.ChatSendResults;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.repository.aliyun.AliyunHandler;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public void upLoadImage(Context context, UploadImagesRequest params, List<String> images, HttpResponseHandler<ArrayList<PhotosEntity>> handler) {
        Map<String, Object> map = objectToMap(params);
        HashMap<String, RequestBody> fileMap = upImages(images);
        requestPost(context, mApiService.uploadImages(map, fileMap), handler);
    }

    @Override
    public void userVideoUpLoad(Context context, UserVideoDataRequest params, List<String> videoImages, HttpResponseHandler<UserVideoDataResults> handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        HashMap<String, RequestBody> fileMap = upImages(videoImages);
        requestPost(context, mApiService.uploadViedoData(paramsMap, fileMap), handler);
    }

    @Override
    public void chatSend(Context context, ChatSendRequest params, HttpResponseHandler<ChatSendResults> handler) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("message", params.getMessage());
        paramsMap.put("msgtype", params.getMstype());
        paramsMap.put("touid", params.getTouid());
        paramsMap.put("virid", params.getVirid());
        HashMap<String, RequestBody> fileMap = upImages(params.getImg());
        if (fileMap == null || fileMap.size() == 0) {
            requestPost(context, mApiService.chatSend(paramsMap), handler);
        } else {
            requestPost(context, mApiService.chatSend(paramsMap, fileMap), handler);
        }
    }

    @Override
    public void changeFileInfo(Context context, FileInfoChangeRequest params, String image, HttpResponseHandler handler) {
        Map<String, Object> paramsMap = objectToMap(params);
        if (image == null || image.length() == 0) {
            requestPost(context, mApiService.updateFileData(paramsMap), handler);
        } else {
            HashMap<String, RequestBody> fileMap = upImages(image);
            requestPost(context, mApiService.updateFileData(paramsMap, fileMap), handler);
        }

    }


    /**
     * 上传图片
     *
     * @param image
     */
    private HashMap<String, RequestBody> upImages(String image) {
        List<String> imagesList = new ArrayList<>();
        imagesList.add(image);
        return upImages(imagesList);
    }

    /**
     * 上传图片
     *
     * @param images
     */
    private HashMap<String, RequestBody> upImages(List<String> images) {
        HashMap<String, RequestBody> fileMap = new HashMap<>();
        if (Check.isEmpty(images))
            return fileMap;
        for (int i = 0; i < images.size(); i++) {
            File file = new File(images.get(i));
            String fileName = UUID.randomUUID().toString();
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileMap.put("img[" + i + "]\"; filename=\"" + fileName + ".png", fileBody);
        }
        return fileMap;
    }

}

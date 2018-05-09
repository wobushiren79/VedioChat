package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.repository.aliyun.AliyunHandler;


public class FileManagerImpl extends BaseManagerImpl implements FileManager {
    private static FileManager manager;

    private FileManagerImpl() {

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
        OSSAsyncTask uploadTask = aliyunHandler.uploadFile(oss, upLoadResults.getBucket(), upLoadResults.getFilename(), upLoadResults.getFilePath(), handler);
        return uploadTask;
    }
}

package com.huanmedia.videochat.common.service.update;


import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.FApplication;

import org.lzh.framework.updatepluginlib.base.FileCreator;
import org.lzh.framework.updatepluginlib.impl.DefaultFileCreator;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;

import mvp.data.store.FilePathManager;

/**
 * 生成下载apk文件的文件地址
 * 默认使用参考 {@link DefaultFileCreator}
 */
public class CustomApkFileCreator extends FileCreator {
    @Override
    public File create(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        return new File(getCacheDir(),FApplication.getApplication().getString(R.string.app_name) +"-"+ update.getVersionName()+".apk");
    }

    @Override
    public File createForDaemon(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        return new File(getCacheDir(),FApplication.getApplication().getString(R.string.app_name) +"-"+ update.getVersionName()+"_temp");
    }
    private File getCacheDir() {
        return FilePathManager.getApkDir(FApplication.getApplication());
    }
}

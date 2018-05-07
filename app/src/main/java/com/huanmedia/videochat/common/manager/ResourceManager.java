package com.huanmedia.videochat.common.manager;

import android.content.Context;

import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.repository.net.CacheProviders;
import com.huanmedia.videochat.repository.net.RemoteApiService;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.lzh.framework.updatepluginlib.model.Update;

import mvp.data.dispose.interactor.DefaultThreadProvider;
import mvp.data.store.DataKeeper;

/**
 * Created by ericYang on 2017/5/24.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class ResourceManager {
    private static ResourceManager instance;
    private DefaultThreadProvider mDefaultThreadProvider;
    private RemoteApiService mApiService;//远程API请求
    private Navigator navigator;
    private CacheProviders mCacheProviders;
    private String mCurrentUser;
    private Update mNewVersion;
    private DataKeeper mDataKeeper;

    private ResourceManager(Context context) {
        init(context);
    }

    private void init(Context context) {
        mDefaultThreadProvider = DefaultThreadProvider.getInstance();
        //创建API服务 及 API 缓存服务

        mApiService = RemoteApiService.Factory.createService();
        mCacheProviders = CacheProviders.Factory.createProviders(context);
        mDataKeeper = new DataKeeper(context, DataKeeper.USERCONFIG);
        navigator = new Navigator();
        Logger.init("EYArch").logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE).hideThreadInfo().methodCount(0);
    }

    public DataKeeper getUserDataKeep() {
        return mDataKeeper;
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            synchronized (ResourceManager.class) {
                if (instance == null) {
                    instance = new ResourceManager(FApplication.getApplication());
                }
            }
        }
        return instance;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public DefaultThreadProvider getDefaultThreadProvider() {
        return mDefaultThreadProvider;
    }

    public RemoteApiService getDefaultApiService() {
        return mApiService;
    }

    public CacheProviders getCacheProviders() {
        return mCacheProviders;
    }


    public void reset() {
        instance = null;
        navigator = null;
        mDefaultThreadProvider = null;
        mApiService = null;
        mCacheProviders = null;
        mCurrentUser = null;
        mNewVersion = null;
    }

    public void setNewVersion(Update newVersion) {
        mNewVersion = newVersion;
    }

    public Update getNewVersion() {
        return mNewVersion;
    }

}

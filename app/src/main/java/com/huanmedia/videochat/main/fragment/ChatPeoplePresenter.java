package com.huanmedia.videochat.main.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ChatPeoplePresenter extends Presenter<ChatPeopleView> {
    private final MainRepostiory mMainRepostiory;
    private String mTag;

    public ChatPeoplePresenter() {
        mMainRepostiory = new MainRepostiory();
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setView(@NonNull ChatPeopleView view) {
        super.setView(view);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnUpDataChatPeopleData(Intent intent) {
        if (intent != null && intent.getAction() != null)
            switch (intent.getAction()) {
                case EventBusAction.ACTION_CHATPEOPLEUPATA:
                    if (intent.getStringExtra("tag").equals(mTag)) {
                        getDatas(intent.getStringExtra("tag"), 2);
                    }
                    break;
            }
    }

    public void getDatas(String tag) {
        getDatas(tag, 0);
    }

    /**
     * 获取数据
     *
     * @param tag   数据接口标记
     * @param times 延迟访问时间
     */
    public void getDatas(String tag, int times) {
        getView().showLoading(null);
        if (tag.equals(getContext().getString(R.string.calling_people))) {
            addDisposable(mMainRepostiory.calleachList(1, 1)
                    .delay(times, TimeUnit.SECONDS)
                    .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                    .subscribe(
                    chatPeopleEntity -> {
                            getView().showDatas(chatPeopleEntity);
                    },
                    throwable -> {
                        if (!isNullView()) {
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    },
                    () -> getView().hideLoading()
            ));
        } else if (tag.equals(getContext().getString(R.string.attention_people))) {
            addDisposable(mMainRepostiory.myfavoriteList(1, 1).delay(times, TimeUnit.SECONDS)
                    .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                    .subscribe(
                    chatPeopleEntity -> {
                            getView().showDatas(chatPeopleEntity);
                    },
                    throwable -> {
                        if (!isNullView()) {
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
                    ,
                    () -> getView().hideLoading()
            ));
        }
    }

    public void setTag(String tag) {
        mTag = tag;
    }
}

package com.huanmedia.videochat.main2.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.data.net.ApiException;
import mvp.presenter.Presenter;
import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/18.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ComeAcrossFriendPresenter extends Presenter<ComeAcroossView> {
    private final MainRepostiory mMainRepostiory;
    private PageBean mPage = new PageBean();
    private String mTag;


    public PageBean getPage() {
        return mPage;
    }

    public void setPage(PageBean page) {
        mPage = page;
    }
    public void resetPage() {
        mPage = new PageBean();
    }
    public ComeAcrossFriendPresenter() {
        mMainRepostiory = new MainRepostiory();
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setView(@NonNull ComeAcroossView view) {
        super.setView(view);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnUpDataChatPeopleData(Intent intent) {
        if (intent != null && intent.getAction() != null)
            switch (intent.getAction()) {
                case EventBusAction.ACTION_CHATPEOPLEUPATA:
                    if (intent.getStringExtra("tag").equals(mTag)) {
                        loadMoreData(LoadDataView.LOADING_STATUS_REFRESH, 1);
                    }
                    break;
            }
    }

    /**
     * 获取数据
     */
    public void loadMoreData(int flag, int page) {
        addDisposable(mMainRepostiory.calleachList(page, 0)
                .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                .subscribe(
                        chatPeopleEntity -> getView().showDatas(chatPeopleEntity),
                        throwable -> {
                            if (!isNullView()) {
                                switch (flag) {
                                    case LoadDataView.LOADING_STATUS_REFRESH:
                                        getPage().reset();
                                        getView().svComputeScroll();
                                        if (throwable instanceof ApiException && ((ApiException) throwable).getErrorCode()==ApiException.NULL)
                                        {
                                            getView().showError(ApiException.NULL, getGeneralErrorStr(throwable));
                                        }
                                        else {
                                            getView().showError(0, getGeneralErrorStr(throwable));
                                        }
                                        break;
                                    case LoadDataView.LOADING_STATUS_MORE:
                                        getPage().setCurrentPage(getPage().getCurrentPage()-1);
                                        getView().loadMoreError();

                                        break;
                                }
                            }
                        }
                ));
    }

    public void setTag(String tag) {
        mTag = tag;
    }


    public void removeComeAcrossFriend(ChatPeopleEntity.ItemsEntity itemData) {
        getView().showLoading(null);
        addDisposable(mMainRepostiory.deltalkeachuser(itemData.getUid())
                .subscribe(response -> {
                            getView().hideLoading();
                            getView().showError(0, "删除成功!");
                            Intent intent = new Intent(EventBusAction.ACTION_CHATPEOPLEUPATA);
                            intent.putExtra("tag", mTag);
                            EventBus.getDefault().post(intent);
                        },
                        throwable -> {
                            if (!isNullView()) {
                                getView().hideLoading();
                                getView().showError(HintDialog.HintType.WARN, throwable.getMessage());
                            }
                        }
                ));
    }
    /**
     * @param id
     */
    void reportothe(String id, int type) {
        getView().showLoading(null);
        addDisposable(mMainRepostiory.reportother(id, type)
                .subscribe(response -> {
                            getView().hideLoading();
                            getView().showError(0, "举报成功!");
                        },
                        throwable -> {
                            if (!isNullView()) {
                                getView().hideLoading();
                                getView().showError(HintDialog.HintType.WARN, throwable.getMessage());
                            }
                        }
                ));
    }

}

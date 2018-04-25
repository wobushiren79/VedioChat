package com.huanmedia.videochat.discover;

import android.content.Intent;

import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import org.greenrobot.eventbus.EventBus;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2017/12/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BusinessCardPresenter extends Presenter<BusinessCardView> {
    private final MainRepostiory mRepository;
    private PageBean mPageBean = new PageBean();
    private boolean isNeedCallChatPeopleAttentionUpData;

    public PageBean getPageBean() {
        return mPageBean;
    }

    public BusinessCardPresenter() {
        this.mRepository = new MainRepostiory();
    }
    public void getBusinessCard(int uid){
        getView().showLoading(null);
        getPageBean().nextpage();
        addDisposable(mRepository.getUserBusinessCard(uid).subscribe(
                businessCard -> getView().showHeadData(businessCard),
                throwable -> {
                    if (!isNullView()){
                        getPageBean().reset();
                        getView().showError(0, getGeneralErrorStr(throwable));
                        getView().hideLoading();
                        getView().showRetry();
                    }
                }
                ,() -> {
                    getView().hideLoading();
                }
        ));
    }
    public  void loadMoreData(String uid){
        addDisposable(mRepository.usertagslist(getPageBean().nextpage()+"",uid).subscribe(
                userEvaluateEntity  -> getView().showMore(userEvaluateEntity),
                throwable -> {
                    if (!isNullView()){
                        getPageBean().setCurrentPage(getPageBean().getCurrentPage()-1);
                        getView().showError(0, getGeneralErrorStr(throwable));
                        getView().loadMoreFail();
                    }
                }
        ));
    }
    /**
     *
     * @param id
     * @param flag 1 收藏  0取消收藏
     */
    void favorite(String id, int flag) {
     getView().showLoading(null);
        addDisposable(mRepository.favorite(id, flag)
                .subscribe(response ->{
                            getView().hideLoading();
                            isNeedCallChatPeopleAttentionUpData=true;
                            getView().resultFavoriteSuccess(flag);
                        },
                        throwable -> {
                            if (!isNullView()) {
                                getView().hideLoading();
                                getView().showError(HintDialog.HintType.WARN, throwable.getMessage());
                            }
                        }
                ));
    }

    @Override
    public void destroy() {
        super.destroy();
        if (isNeedCallChatPeopleAttentionUpData) {
            Intent intent = new Intent(EventBusAction.ACTION_CHATPEOPLEUPATA);
            intent.putExtra("tag", "关注的人");
            EventBus.getDefault().post(intent);
        }
    }


    }

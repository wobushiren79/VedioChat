package com.huanmedia.videochat.main2.fragment;

import com.huanmedia.videochat.common.PageBean;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.HashMap;

import mvp.presenter.Presenter;
import mvp.view.LoadDataView;

/**
 * Create by Administrator
 * time: 2017/12/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class DiscoverPresenter extends Presenter<DiscoverView> {
    private final MainRepostiory mRepository;
    private PageBean mPageBean = new PageBean();

    public PageBean getPageBean() {
        return mPageBean;
    }

    public DiscoverPresenter() {
        this.mRepository = new MainRepostiory();
    }

    void getDiscoverData(int flag) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page",mPageBean.nextpage()+"");
        addDisposable(mRepository.readMainList(map)
                .subscribe(
                        matchListEntities -> {
                            getView().resultDiscoverData(flag,matchListEntities);
                        },
                        throwable -> {
                            if (!isNullView()) {
                                switch (flag){
                                    case LoadDataView.LOADING_STATUS_REFRESH:
                                        getView().svComputeScroll();
                                        break;
                                }
                                getView().loadDataError( HintDialog.HintType.WARN,getGeneralErrorStr(throwable));
                            }
                        }
                ));
    }

    @Override
    public void destroy() {
        super.destroy();
//        if (isNeedCallChatPeopleAttentionUpData){//修改UI后关注的人更新数据到个人资料中更新
//            Intent intent = new Intent(EventBusAction.ACTION_CHATPEOPLEUPATA);
//            intent.putExtra("tag","关注的人");
//            EventBus.getDefault().post(intent);
//        }

    }
}

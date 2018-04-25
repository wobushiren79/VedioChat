package com.huanmedia.videochat.my;

import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.util.ArrayList;
import java.util.List;

import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/3/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class LevelPresenter extends Presenter<LevelView> {
    private final MainRepostiory mRepostory;
    private List<String> mLeveDiscs;

    public LevelPresenter() {
        mRepostory = new MainRepostiory();
    }

    public List<String> getLeveDiscs() {
        if (mLeveDiscs == null) {
            mLeveDiscs = new ArrayList<>();
        }
        mLeveDiscs.add("登录-每日上限经验 3");
        mLeveDiscs.add("分享-每日上限经验 3");
        mLeveDiscs.add("连线时长30分钟-每日上限经验 15");
        mLeveDiscs.add("提现1次-每日上限经验 10");
        mLeveDiscs.add("充值1次-每日上限经验 3");
        mLeveDiscs.add("送礼、收礼-每日上限经验 5000");
        return mLeveDiscs;
    }

    public String getLeveExperienceColor(int level) {
        if (level <= 4) {
            return "#6666ff";
        } else if (level <= 8) {
            return "#fc47ad";
        } else//9级以上
        {
            return "#fc4141";
        }
    }

    public void getMyLevelData(){
        getView().hideLoading();
        addDisposable(mRepostory.userlevelprivilege().subscribe(
                myLevelEntity -> {
                    getView().hideLoading();
                    getView().showLevelData(myLevelEntity);
                },
                throwable -> {
                    if (!isNullView()){
                        getView().hideLoading();
                        getView().showError(HintDialog.HintType.WARN,getGeneralErrorStr(throwable));
                    }
                }
        ));
    }
}

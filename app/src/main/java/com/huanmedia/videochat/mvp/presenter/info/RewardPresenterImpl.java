package com.huanmedia.videochat.mvp.presenter.info;

import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.base.DataCallBack;
import com.huanmedia.videochat.mvp.entity.request.RewardRequest;
import com.huanmedia.videochat.mvp.entity.results.RewardResults;
import com.huanmedia.videochat.mvp.model.info.RewardModelImpl;
import com.huanmedia.videochat.mvp.view.info.IRewardView;

public class RewardPresenterImpl extends BaseMVPPresenter<IRewardView, RewardModelImpl> implements IRewardPresenter {

    public RewardPresenterImpl(IRewardView mMvpView) {
        super(mMvpView, RewardModelImpl.class);
    }

    @Override
    public void videoReward() {
        if (mMvpView.getContext() == null)
            return;
        if (mMvpView.getRewardCoin() == 0) {
            mMvpView.showToast("没有奖励金币");
            return;
        }
        if (mMvpView.getRewardVideoId() == 0) {
            mMvpView.showToast("没有视频ID");
            return;
        }
        if (mMvpView.getRewardGiftNum() == 0) {
            mMvpView.showToast("没有礼物数量");
            return;
        }
        if (mMvpView.getRewardGiftId() == 0) {
            mMvpView.showToast("没有礼物ID");
            return;
        }
        RewardRequest params = new RewardRequest();
        params.setType(1);
        params.setUid(mMvpView.getRewardUid());
        params.setCoin(mMvpView.getRewardCoin());
        params.setVideoid(mMvpView.getRewardVideoId());
        params.setGgiftcount(mMvpView.getRewardGiftNum());
        params.setGiftid(mMvpView.getRewardGiftId());
        mMvpModel.videoReward(mMvpView.getContext(), params, new DataCallBack<RewardResults>() {
            @Override
            public void getDataSuccess(RewardResults results) {
                UserManager.getInstance().getCurrentUser().getUserinfo().setCoin(results.getCoin());
                mMvpView.rewardSuccess(results);
            }

            @Override
            public void getDataFail(String msg) {
                mMvpView.rewardFail(msg);
            }
        });
    }
}

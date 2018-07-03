package com.huanmedia.videochat.mvp.view.info;

import com.huanmedia.videochat.mvp.base.BaseMVPView;
import com.huanmedia.videochat.mvp.entity.results.RewardResults;

public interface IRewardView extends BaseMVPView {

    /**
     * 获取奖励UID
     *
     * @return
     */
    int getRewardUid();

    /**
     * 获取奖励金币
     *
     * @return
     */
    int getRewardCoin();

    /**
     * 获取视频ID
     *
     * @return
     */
    int getRewardVideoId();

    /**
     * 获取礼物DI
     *
     * @return
     */
    int getRewardGiftId();

    /**
     * 获取礼物数量
     *
     * @return
     */
    int getRewardGiftNum();

    /**
     * 奖励成功
     */
    public void rewardSuccess(RewardResults results);

    /**
     * 奖励失败
     */
    public void rewardFail(String msg);
}

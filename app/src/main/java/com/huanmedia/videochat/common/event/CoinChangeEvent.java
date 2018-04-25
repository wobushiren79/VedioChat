package com.huanmedia.videochat.common.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Create by Administrator
 * time: 2017/12/21.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CoinChangeEvent {
    @IntDef({CoinChangeEvent.CoinChangeType.PAY,//支付
            CoinChangeEvent.CoinChangeType.DRAWINGS,//提款
            CoinChangeEvent.CoinChangeType.RECHARGE,//充值
            CoinChangeEvent.CoinChangeType.EXCHANGE})//兑换
    @Retention(RetentionPolicy.SOURCE)
    public @interface CoinChangeType {
        int PAY = 1, DRAWINGS = 2, RECHARGE = 3, EXCHANGE = 4;
    }

    public CoinChangeEvent(@CoinChangeType int type) {
        this.type = type;
    }

    public @CoinChangeType int type;
}

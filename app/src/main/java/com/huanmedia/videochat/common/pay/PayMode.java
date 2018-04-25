package com.huanmedia.videochat.common.pay;

import android.os.Parcelable;

/**
 * Create by Administrator
 * time: 2017/12/17.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public interface PayMode extends Parcelable{
     String getOut_trade_no();
     String getMoney() ;
     String getName() ;
     String getDetail() ;

}

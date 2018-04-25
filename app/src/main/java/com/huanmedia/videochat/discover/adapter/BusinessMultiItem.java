package com.huanmedia.videochat.discover.adapter;

import android.support.annotation.IntDef;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.huanmedia.videochat.discover.adapter.BusinessMultiItem.BusinessType.EVALUATE;
import static com.huanmedia.videochat.discover.adapter.BusinessMultiItem.BusinessType.HEADER;
import static com.huanmedia.videochat.discover.adapter.BusinessMultiItem.BusinessType.TAG;


/**
 * Create by Administrator
 * time: 2018/2/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public abstract class BusinessMultiItem implements MultiItemEntity {

    /**
     * int LOADING = 0, TOAST = 1,WARN = 2, SUCCEED = 3;
     */
    @IntDef({HEADER, TAG,EVALUATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BusinessType {
        int HEADER = 0, TAG = 1, EVALUATE = 2;
    }
    @Override
    public abstract int getItemType() ;
}

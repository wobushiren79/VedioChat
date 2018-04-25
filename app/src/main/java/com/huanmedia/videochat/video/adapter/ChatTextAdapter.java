package com.huanmedia.videochat.video.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.video.model.TextChatMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Administrator
 * time: 2017/12/12.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ChatTextAdapter extends BaseQuickAdapter<TextChatMode,BaseViewHolder> {


    public ChatTextAdapter(int layoutResId, @Nullable List<TextChatMode> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TextChatMode item) {
        if (item.getType()==TextChatMode.ChatType.TAGTEXT){
            Spanny spanny = new Spanny(Check.checkReplace(item.getText()));
            spanny.findAndSpan(item.getDeffColorText(),
                    () -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.base_green)));
            helper.setText(R.id.item_video_call_item_tv_chat,spanny);
        }else {
            helper.setText(R.id.item_video_call_item_tv_chat,Check.checkReplace(item.getText()));
        }
    }

    /**
     * 计时器应该在Adapter创建的时候调用
     * 切记，不能重复调用
     * @return
     */
    public Disposable startTimeDown(){
      return Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aLong -> {
                            if (getData().size() > 0){
                                remove(0);
                            }
                        },
                        Throwable::printStackTrace
                );
    }
}

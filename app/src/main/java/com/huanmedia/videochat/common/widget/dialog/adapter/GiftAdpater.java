package com.huanmedia.videochat.common.widget.dialog.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.repository.entity.GiftEntity;

import mvp.data.store.glide.GlideUtils;

public class GiftAdpater extends BaseRCAdapter<GiftEntity> {

    private int mSelectPosition = -1;
    private CallBack mCallBack;

    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public GiftAdpater(Context context) {
        super(context, R.layout.item_gift_common);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GiftEntity giftEntity, int i) {
        TextView tvName = baseViewHolder.getView(R.id.dialog_item_gift_tv_name);
        ImageView ivPic = baseViewHolder.getView(R.id.dialog_item_gift_iv_cover);
        TextView tvMoney = baseViewHolder.getView(R.id.dialog_item_gift_tv_price);
        ConstraintLayout clLayout = baseViewHolder.getView(R.id.dialog_gift_cl);

        //设置名称
        tvName.setText(giftEntity.getName());
        //设置图片
        String path = "";
        if (giftEntity.get_localMode() != null && giftEntity.get_localMode().getFistImageAbsolute() != null)
            path = "file:///android_asset/" + giftEntity.get_localMode().getFistImageAbsolute();
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), path, ivPic);
        //设置价格
        tvMoney.setText(String.valueOf(giftEntity.getCoin()) + "钻石");
        //设置选中
        if (mSelectPosition == i) {
            clLayout.setBackgroundResource(R.drawable.dialog_gift_selectbg_stroke);
        } else {
            clLayout.setBackgroundResource(0);
        }
        clLayout.setOnClickListener(view -> {
            mSelectPosition = i;
            notifyDataSetChanged();
            if (mCallBack != null)
                mCallBack.selectGift(mDatas.get(i));
        });
    }

    /**
     * 获取选择礼物
     *
     * @return
     */
    public GiftEntity getSelectGift() {
        if (mSelectPosition == -1) {
            return null;
        }
        return mDatas.get(mSelectPosition);
    }

    public interface CallBack {
        void selectGift(GiftEntity giftEntity);
    }
}

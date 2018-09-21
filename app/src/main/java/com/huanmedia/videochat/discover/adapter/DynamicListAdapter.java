package com.huanmedia.videochat.discover.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.mvp.entity.results.DynamicListResults;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;

import mvp.data.store.glide.GlideUtils;

public class DynamicListAdapter extends BaseRCAdapter<DynamicListResults.DynamicItem> {
    private BusinessCardEntity.BaseInfo mUserInfo;
    private ArtistsGroupShowResults.Base mGroupInfo;

    public DynamicListAdapter(Context context) {
        super(context, R.layout.item_dynamic_list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, DynamicListResults.DynamicItem dynamicItem, int i) {
        if (mUserInfo == null || mGroupInfo == null)
            return;

        ImageView ivHeard = baseViewHolder.getView(R.id.iv_heard);
        TextView tvName = baseViewHolder.getView(R.id.tv_name);
        TextView tvGroupName = baseViewHolder.getView(R.id.tv_group_name);
        TextView tvContent = baseViewHolder.getView(R.id.tv_content);
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);


        GlideUtils.getInstance().loadBitmapNoAnim(mContext, mUserInfo.getUserphoto_thumb(), ivHeard);
        tvName.setText(mUserInfo.getNickname());
        tvGroupName.setText(mGroupInfo.getName());
        tvContent.setText(dynamicItem.getContent());
        if (dynamicItem.getImg() != null && dynamicItem.getImg().length() != 0) {
            ivContent.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadBitmapNoAnim(mContext, dynamicItem.getImg(), ivContent);
            ivContent.setOnClickListener(view -> {
                if (dynamicItem.getJmpurl() != null && dynamicItem.getJmpurl().length() != 0) {
                    ((BaseActivity) mContext).getNavigator().navtoWebActivity((Activity) mContext, dynamicItem.getJmpurl(), null);
                }
            });
        } else {
            ivContent.setVisibility(View.GONE);
            ivContent.setOnClickListener(null);
        }
    }

    public void setUserInfo(BusinessCardEntity.BaseInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public void setGroupInfo(ArtistsGroupShowResults.Base groupInfo) {
        this.mGroupInfo = groupInfo;
    }
}

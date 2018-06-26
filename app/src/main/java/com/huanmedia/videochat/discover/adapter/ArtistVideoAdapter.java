package com.huanmedia.videochat.discover.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class ArtistVideoAdapter extends BaseRCAdapter<VideoEntity> {
    public ArtistVideoAdapter(Context context) {
        super(context, R.layout.item_artist_video);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        GlideUtils.getInstance().loadBitmapNoAnim(mContext, videoEntity.getImgurl(), ivContent);

        ivContent.setOnClickListener(view -> {
            ((BaseActivity) mContext).getNavigator().navtoMediaPlay((Activity) mContext, (ArrayList<VideoEntity>) mDatas, i);
        });
    }
}

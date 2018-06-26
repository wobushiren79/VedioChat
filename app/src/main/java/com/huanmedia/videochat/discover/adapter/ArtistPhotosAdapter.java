package com.huanmedia.videochat.discover.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import mvp.data.store.glide.GlideUtils;

public class ArtistPhotosAdapter extends BaseRCAdapter<PhotosEntity> {
    public ArtistPhotosAdapter(Context context) {
        super(context, R.layout.item_artist_photos);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, PhotosEntity PhotosEntity, int i) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        GlideUtils.getInstance().loadBitmapNoAnim(mContext, PhotosEntity.getPhoto_thumb(), ivContent);

        ivContent.setOnClickListener(view -> {
            new HM_StartSeePhoto.Bulide(mContext, new HM_GlideEngine())
                    .setList(mDatas)
                    .setCurrentSelect(i)
                    .bulide();
        });
    }
}

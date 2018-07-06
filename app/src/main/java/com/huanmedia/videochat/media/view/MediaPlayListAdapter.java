package com.huanmedia.videochat.media.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class MediaPlayListAdapter extends BaseRCAdapter<VideoEntity> {

    private int mPlayPosition;

    public MediaPlayListAdapter(Context context) {
        super(context, R.layout.layout_media_play_list_item);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        EmptyVideoPlayer videoPlayer = baseViewHolder.getView(R.id.video_play);
        ImageView ivThumb = new ImageView(mContext);

        videoPlayer.setUp(videoEntity.getVoideurl(), true, "");
        videoPlayer.setLooping(true);
        Glide
                .with(mContext)
                .load(videoEntity.getImgurl())
                .into(ivThumb);
        videoPlayer.setThumbImageView(ivThumb);

        if (i == mPlayPosition) {
            videoPlayer.startPlayLogic();
        }
    }

    /**
     * 播放视频
     *
     * @param position -1 时默认播放原视频
     */
    public void playVideo(int position) {
        if ((position + 1) > mDatas.size())
            return;
        else if (position < 0) {
            if (mDatas != null && mDatas.size() > 0) {
                if (mPlayPosition != -1) {

                } else {
                    mPlayPosition = 0;
                }
                notifyDataSetChanged();
            } else {

            }
        } else {
            mPlayPosition = position;
            notifyDataSetChanged();
        }
    }
}

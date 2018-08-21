package com.huanmedia.videochat.media.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.presenter.file.FileManagePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileManagePresenter;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.BlurTransformation;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

public class MediaPlayListAdapter extends BaseRCAdapter<VideoEntity> implements IFileManageCheckView, IFileManagePayView {

    private int mPlayPosition;
    private boolean mIsShowMask;
    private IFileManagePresenter mFileManagePresenter;

    public MediaPlayListAdapter(Context context) {
        super(context, R.layout.layout_media_play_list_item);
        mFileManagePresenter = new FileManagePresenterImpl(this, this);
    }

    public void setIsShowMask(boolean isShowMask) {
        mIsShowMask = isShowMask;
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        EmptyVideoPlayer videoPlayer = baseViewHolder.getView(R.id.video_play);
        ImageView ivMask = baseViewHolder.getView(R.id.iv_mask);
        ImageView ivThumb = new ImageView(mContext);

        videoPlayer.setUp(videoEntity.getVoideurl(), true, "");
        videoPlayer.setLooping(true);
        Glide
                .with(mContext)
                .load(videoEntity.getImgurl())
                .into(ivThumb);
        videoPlayer.setThumbImageView(ivThumb);

        if (mIsShowMask) {
            if (videoEntity.getPlevel() == 1) {
                ivMask.setVisibility(View.GONE);
            } else {
                ivMask.setVisibility(View.VISIBLE);
                GlideApp.with(mContext)
                        .asBitmap()
                        .override(100, 100)
                        .load(videoEntity.getImgurl())
                        .transform(new BlurTransformation(mContext, 25))
                        .into(ivMask);
            }
        } else {
            ivMask.setVisibility(View.GONE);
        }

        if (i == mPlayPosition) {
            if (mIsShowMask) {
                if (videoEntity.getPlevel() == 1)
                    videoPlayer.startPlayLogic();
                else
                    GSYVideoManager.onPause();
            } else
                videoPlayer.startPlayLogic();
        }
    }


    public void checkFileVideo() {
        VideoEntity itemData = mDatas.get(mPlayPosition);
        if (mIsShowMask && itemData.getPlevel() == 2) {
            mFileManagePresenter.checkHasVideoFile(itemData.getId());
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
        checkFileVideo();
    }

    @Override
    public void checkHasFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (results.getHasread() == 1) {
            mDatas.get(mPlayPosition).setPlevel(1);
            notifyItemChanged(mPlayPosition);
        } else {
            GeneralDialog dialog = new GeneralDialog(mContext);
            dialog
                    .setContent("将消耗您" + results.getVcoin() + "钻石")
                    .setTitle("您确定要查看视频吗？")
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < results.getVcoin()) {
                                NoMoreMoneyDialog(getContext(), "抱歉，无法查看，没有更多钻石了。");
                            } else {
                                mFileManagePresenter.payVideoFile(fileID);
                            }
                        }

                        @Override
                        public void cancelClick(Dialog dialog) {

                        }
                    })
                    .show();
        }
    }

    @Override
    public void checkHasFileFail(String msg) {
        showToast(msg);
    }

    @Override
    public void payFileSuccess(int fileID, int fileType, FileManageResults results) {
        mDatas.get(mPlayPosition).setPlevel(1);
        notifyItemChanged(mPlayPosition);
    }

    @Override
    public void payFileFail(String msg) {
        showToast(msg);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }
}

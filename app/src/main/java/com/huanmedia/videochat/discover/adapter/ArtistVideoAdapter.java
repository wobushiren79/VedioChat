package com.huanmedia.videochat.discover.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.presenter.file.FileManagePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileManagePresenter;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.BlurTransformation;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

public class ArtistVideoAdapter extends BaseRCAdapter<VideoEntity> implements IFileManagePayView, IFileManageCheckView {
    private IFileManagePresenter mFileManagePresenter;

    public ArtistVideoAdapter(Context context) {
        super(context, R.layout.item_artist_video);
        mFileManagePresenter = new FileManagePresenterImpl(this, this);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        TextView tvTag = baseViewHolder.getView(R.id.tv_tag);

        if (videoEntity.getPlevel() == 1) {
            tvTag.setVisibility(View.GONE);
            GlideApp.with(mContext)
                    .asBitmap()
                    .load(videoEntity.getImgurl())
//                            .videoEntity(mItemSize, mItemSize)
                    .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot).error(com.huanmedia.ilibray.R.drawable.bg_logot)
                    .into(ivContent);
        } else {
            if (videoEntity.getTag() != null && !videoEntity.getTag().equals("")) {
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText(videoEntity.getTag());
            } else {
                tvTag.setVisibility(View.GONE);
            }

            GlideApp.with(mContext)
                    .asBitmap()
                    .override(100, 100)
                    .load(videoEntity.getImgurl())
                    .transform(new BlurTransformation(mContext, 25))
                    .into(ivContent);
        }

        ivContent.setOnClickListener(view -> {
            if (videoEntity.getPlevel() == 2) {
                mFileManagePresenter.checkHasVideoFile(videoEntity.getId());
            } else {
                seePublicVideo(mDatas,i);
            }
        });
    }

    /**
     * 查看公开视频
     *
     * @param listVideo
     * @param position
     */
    private void seePublicVideo(List<VideoEntity> listVideo, int position) {
        ((BaseActivity) mContext)
                .getNavigator()
                .navtoMediaPlay((Activity) mContext, listVideo, position, true);
    }


    @Override
    public void checkHasFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (results.getHasread() == 1) {
            payFileSuccess(fileID, fileType, results);
        } else {
            String dialogTitle = "您确定要查看视频吗？";
            GeneralDialog dialog = new GeneralDialog(mContext);
            dialog
                    .setContent("将消耗您" + results.getVcoin() + "钻石")
                    .setTitle(dialogTitle)
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < results.getVcoin()) {
                                NoMoreMoneyDialog(mContext, "抱歉，无法查看，没有更多钻石了。");
                            } else {
                                if (fileType == 1) {
                                    mFileManagePresenter.payPhotoFile(fileID);
                                } else if (fileType == 2) {
                                    mFileManagePresenter.payVideoFile(fileID);
                                }
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
        for (int i = 0; i < mDatas.size(); i++) {
            VideoEntity item = mDatas.get(i);
            if (item.getId() == fileID) {
                item.setPlevel(1);
                this.notifyItemChanged(i);
                seePublicVideo(mDatas, i);
                break;
            }
        }
    }

    @Override
    public void payFileFail(String msg) {
        showToast(msg);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(mContext, toast);
    }
}

package com.huanmedia.videochat.discover.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.presenter.file.FileManagePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileManagePresenter;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.BlurTransformation;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

public class ArtistPhotosAdapter extends BaseRCAdapter<PhotosEntity> implements IFileManagePayView, IFileManageCheckView {
    private IFileManagePresenter mFileManagePresenter;

    public ArtistPhotosAdapter(Context context) {
        super(context, R.layout.item_artist_photos);
        mFileManagePresenter = new FileManagePresenterImpl(this, this);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, PhotosEntity PhotosEntity, int i) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        TextView tvTag = baseViewHolder.getView(R.id.tv_tag);

        if (PhotosEntity.getPlevel() == 1) {
            tvTag.setVisibility(View.GONE);
            GlideApp.with(mContext)
                    .asBitmap()
                    .load(PhotosEntity.getImage())
//                            .override(mItemSize, mItemSize)
                    .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot).error(com.huanmedia.ilibray.R.drawable.bg_logot)
                    .into(ivContent);
        } else {
            if (PhotosEntity.getTag() != null && !PhotosEntity.getTag().equals("")) {
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText(PhotosEntity.getTag());
            } else {
                tvTag.setVisibility(View.GONE);
            }

            GlideApp.with(mContext)
                    .asBitmap()
                    .override(100, 100)
                    .load(PhotosEntity.getImage())
                    .transform(new BlurTransformation(mContext, 25))
                    .into(ivContent);
        }


        ivContent.setOnClickListener(view -> {
            if (PhotosEntity.getPlevel() == 2) {
                mFileManagePresenter.checkHasPhotoFile(PhotosEntity.getId());
            } else {
                seePublicPhoto(mDatas, i);
            }
        });
    }

    private void seePublicPhoto(List<PhotosEntity> listPhoto, int position) {
        ((BaseActivity) mContext)
                .getNavigator()
                .navtoPhotoShow((Activity) mContext, listPhoto, position, true);
    }


    @Override
    public void checkHasFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (results.getHasread() == 1) {
            payFileSuccess(fileID, fileType, results);
        } else {
            String dialogTitle = "您确定要查看私照吗？";
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
            PhotosEntity item = mDatas.get(i);
            if (item.getId() == fileID) {
                item.setPlevel(1);
                this.notifyItemChanged(i);
                seePublicPhoto(mDatas, i);
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

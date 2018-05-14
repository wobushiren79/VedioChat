package com.huanmedia.videochat.media;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.applecoffee.devtools.utils.IntentHelpUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.mvp.presenter.file.FileUpLoadPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileUpLoadPresenter;
import com.huanmedia.videochat.mvp.presenter.user.IUserVideoDataPresenter;
import com.huanmedia.videochat.mvp.presenter.user.UserVideoDataPresenterImpl;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadView;
import com.huanmedia.videochat.mvp.view.user.IUserVideoDataView;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MediaUpLoadAdapter extends BaseRCAdapter<VideoEntity> implements EasyPermissions.PermissionCallbacks, IFileUpLoadView, IUserVideoDataView {
    private static final int REQUEST_WRITE_READ_PERM = 1;//权限标识符

    private IFileUpLoadPresenter mFileUpLoadPresenter;
    private IUserVideoDataPresenter mUserVideoDataPresenter;

    private VideoInfoRequest mUpLoadVideoInfo;//上传视频信息
    private FileUpLoadResults mUpLoadResults;//上传视频信息
    private OSSAsyncTask mUpLoadTask;//上传任务
    private boolean mHasUpLoadTask = false;//是否还有下载任务
    private List<VideoEntity> deleteUserVideoData;

    private @ItemType
    int mItemType = ItemType.NORMAL;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemType {
        //普通模式          //编辑模式
        int NORMAL = 1, EDIT = 2;
    }

    public MediaUpLoadAdapter(Context context) {
        super(context, R.layout.layout_media_upload_item);
        mFileUpLoadPresenter = new FileUpLoadPresenterImpl(this);
        mUserVideoDataPresenter = new UserVideoDataPresenterImpl(this);
    }

    /**
     * 是否还有下载任务
     *
     * @return
     */
    public boolean isHasUpLoadTask() {
        return mHasUpLoadTask;
    }

    /***
     * 改变模式 1 普通模式 2编辑模式
     */
    public void changeItemMode() {
        if (mItemType == 1) {
            mItemType = 2;
        } else {
            mItemType = 1;
        }
        notifyDataSetChanged();
    }

    /**
     * 删除用户数据
     */
    public void deleteUserVideo() {
        mUserVideoDataPresenter.deleteUserVideoInfo();
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        RelativeLayout rlNormal = baseViewHolder.getView(R.id.rl_layout_normal);
        RelativeLayout rlUpload = baseViewHolder.getView(R.id.rl_layout_upload);
        RelativeLayout rlLoading = baseViewHolder.getView(R.id.rl_layout_loading);
        RadioButton rbSelect = baseViewHolder.getView(R.id.normal_rb);
        ImageView ivPlay = baseViewHolder.getView(R.id.normal_iv_play);
        if (videoEntity.getUploadStatus() == 0) {
            //普通状态
            rlNormal.setVisibility(View.VISIBLE);
            rlUpload.setVisibility(View.GONE);
            rlLoading.setVisibility(View.GONE);
            TextView tvContent = baseViewHolder.getView(R.id.normal_tv_content);
            RoundedImageView ivIcon = baseViewHolder.getView(R.id.normal_icon);
            GlideUtils.getInstance().loadBitmapNoAnim(mContext, videoEntity.getImgurl(), ivIcon);
            if (mItemType == 1) {
                rbSelect.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
                if (videoEntity.getStatus() == -1) {
                    tvContent.setText("审核未通过");
                    tvContent.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                } else {
                    tvContent.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.VISIBLE);
                }
            } else {
                tvContent.setVisibility(View.GONE);
                rbSelect.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.GONE);
            }

        } else if (videoEntity.getUploadStatus() == -1) {
            //上传按钮
            rlNormal.setVisibility(View.GONE);
            rlUpload.setVisibility(View.VISIBLE);
            rlLoading.setVisibility(View.GONE);
        } else {
            //上传中状态
            rlNormal.setVisibility(View.GONE);
            rlUpload.setVisibility(View.GONE);
            rlLoading.setVisibility(View.VISIBLE);
            RoundedImageView ivIcon = baseViewHolder.getView(R.id.loading_icon);
            GlideUtils.getInstance().loadBitmapNoAnim(mContext, videoEntity.getImgurl(), ivIcon);
        }

        ProgressBar progressBar = baseViewHolder.getView(R.id.loading_pb);
        progressBar.setProgress(videoEntity.getLoadPB());
        ImageView ivCancel = baseViewHolder.getView(R.id.loading_iv_cancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpLoadTask != null && !mUpLoadTask.isCanceled() && !mUpLoadTask.isCompleted()) {
                    mUpLoadTask.cancel();
                    uploadFileByAliyunFail("已取消上传视频");
                }
            }
        });
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoEntity.getUploadStatus() == -1) {
                    if (mDatas.size() > 6) {
                        showToast("最多只能上传6个视频");
                        return;
                    }
                    openAlbum();
                } else if (videoEntity.getUploadStatus() == 0) {
                    if (mItemType == 1) {
                        Navigator navigator = new Navigator();
                        List<String> listData = new ArrayList<>();
                        for (VideoEntity item : mDatas) {
                            if (item.getVoideurl() == null)
                                continue;
                            listData.add(item.getVoideurl());
                        }
                        navigator.navtoMediaPlay((Activity) mContext, (ArrayList<String>) listData, i);
                    } else {
                        if (rbSelect.isChecked()) {
                            videoEntity.setIsSelect(0);
                            rbSelect.setChecked(false);
                        } else {
                            videoEntity.setIsSelect(1);
                            rbSelect.setChecked(true);
                        }
                    }
                }
            }
        });

        rbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    videoEntity.setIsSelect(1);
                } else {
                    videoEntity.setIsSelect(0);
                }
            }
        });
    }

    /**
     * 设置上传的视频文件
     */
    public void setUpLoadVideoFile(VideoInfoRequest request) {
        this.mUpLoadVideoInfo = request;
        if (mFileUpLoadPresenter != null) {
            mHasUpLoadTask = true;
            mFileUpLoadPresenter.getAliyunUpLoadInfo();
        }
    }

    @AfterPermissionGranted(REQUEST_WRITE_READ_PERM)
    private void openAlbum() {
        if (DoubleClickUtils.isFastDoubleClick()) return;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            IntentHelpUtils.IntentToVideoSelect(((Activity) mContext), 1);
        } else {
            EasyPermissions.requestPermissions(((Activity) mContext), mContext.getString(R.string.rationale_camera_write_read), REQUEST_WRITE_READ_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) mContext, perms)) {
            new AppSettingsDialog.Builder((Activity) mContext).build().show();
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLong(mContext, toast);
    }

    //------获取阿里云数据------------------------
    @Override
    public void getAliyunUpLoadInfoSuccess(FileUpLoadResults results) {
        mUpLoadTask = mFileUpLoadPresenter.startUpLoadByAliyun(results);
    }

    @Override
    public void getAliyunUpLoadInfoFail(String msg) {
        showToast(msg);
        mHasUpLoadTask = false;
    }
    //------------------------阿里云文件上传--------------------------------------------

    @Override
    public void uploadFileByAliyunSuccess() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> images = new ArrayList<>();
                images.add(mUpLoadVideoInfo.getImagePath());
                mUserVideoDataPresenter.uploadUserVideoInfo();
            }
        });
    }

    @Override
    public void uploadFileByAliyunFail(String msg) {
        mHasUpLoadTask = false;
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
                videoEntity.setUploadStatus(-1);
                videoEntity.setLoadPB(0);
                notifyItemChanged(mDatas.size() - 1);
                if (!msg.contains("Task is cancel"))
                    showToast(msg);
            }
        });
    }

    @Override
    public void uploadFileOnProgress(long currentSize, long totalSize) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int loadPB = (int) (((float) currentSize / (float) totalSize) * 100f);
                VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
                if (loadPB != videoEntity.getLoadPB()) {
                    videoEntity.setLoadPB(loadPB);
                    notifyItemChanged(mDatas.size() - 1);
                }
            }
        });
    }

    @Override
    public VideoInfoRequest getVideoInfo() {
        return mUpLoadVideoInfo;
    }

    @Override
    public void startUploadAliyun(FileUpLoadResults results) {
        this.mUpLoadResults = results;
        VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
        videoEntity.setUploadStatus(1);
        videoEntity.setImgurl(mUpLoadVideoInfo.getImagePath());
        videoEntity.setVoideurl(results.getFilePath());
        notifyDataSetChanged();
    }


    //------------------视频数据上傳接口------------------------
    @Override
    public void uploadUserVideoSuccess(UserVideoDataResults results) {
        mHasUpLoadTask = false;
        VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
        videoEntity.setUploadStatus(0);
        videoEntity.setId(results.getId());
        videoEntity.setLoadPB(0);
        VideoEntity newAddItem = new VideoEntity();
        newAddItem.setUploadStatus(-1);
        mDatas.add(newAddItem);
        notifyDataSetChanged();
        showToast("上传视频成功");
    }

    @Override
    public void uploadUserVideoFail(String msg) {
        uploadFileByAliyunFail(msg);
    }

    @Override
    public void deleteUserVideoSuccess() {
        removeData(deleteUserVideoData);
    }

    @Override
    public void deleteUserVideoFail(String msg) {
        showToast("删除视频失败：" + msg);
        deleteUserVideoData.clear();
    }

    @Override
    public String getUpLoadVideoName() {
        return mUpLoadResults.getFilename();
    }

    @Override
    public String getUpLoadVideoIcon() {
        if (mUpLoadVideoInfo != null && mUpLoadVideoInfo.getImagePath() != null)
            return mUpLoadVideoInfo.getImagePath();
        return null;
    }

    @Override
    public List<String> getDeleteVideoIds() {
        deleteUserVideoData = new ArrayList<>();
        List<String> stringData = new ArrayList<>();
        for (VideoEntity itemData : getData()) {
            if (itemData.getIsSelect() == 1) {
                deleteUserVideoData.add(itemData);
                stringData.add(itemData.getId() + "");
            }
        }
        return stringData;
    }
}

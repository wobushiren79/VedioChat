package com.huanmedia.videochat.media;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.presenter.file.FileUpLoadPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileUpLoadPresenter;
import com.huanmedia.videochat.mvp.presenter.user.IUserVideoUpLoadPresenter;
import com.huanmedia.videochat.mvp.presenter.user.UserVideoUpLoadPresenterImpl;
import com.huanmedia.videochat.mvp.view.file.IFileUpLoadView;
import com.huanmedia.videochat.mvp.view.user.IUserVideoUpLoadView;
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

public class MediaUpLoadAdapter extends BaseRCAdapter<VideoEntity> implements EasyPermissions.PermissionCallbacks, IFileUpLoadView, IUserVideoUpLoadView {
    private static final int REQUEST_WRITE_READ_PERM = 1;//权限标识符

    private IFileUpLoadPresenter mFileUpLoadPresenter;
    private IUserVideoUpLoadPresenter mUserVideoUpLoadPresenter;

    private VideoInfoRequest mUpLoadVideoInfo;//上传视频信息
    private FileUpLoadResults mUpLoadResults;//上传视频信息
    private OSSAsyncTask mUpLoadTask;//上传任务
    public static boolean mHasUpLoadTask = false;//是否有下载任务

    /**
     * 下载任务处理
     */
    public static Handler taskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MediaUpLoadActivity mediaActivity =
                    (MediaUpLoadActivity) ActivitManager.getAppManager().getActivity(MediaUpLoadActivity.class);
            switch (msg.what) {
                case 1:

                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };

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
        mUserVideoUpLoadPresenter = new UserVideoUpLoadPresenterImpl(this);
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

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        RelativeLayout rlNormal = baseViewHolder.getView(R.id.rl_layout_normal);
        RelativeLayout rlUpload = baseViewHolder.getView(R.id.rl_layout_upload);
        RelativeLayout rlLoading = baseViewHolder.getView(R.id.rl_layout_loading);
        RadioButton rbSelect = baseViewHolder.getView(R.id.normal_rb);
        rbSelect.setEnabled(false);
        ImageView ivPlay = baseViewHolder.getView(R.id.normal_iv_play);
        if (videoEntity.getUploadStatus() == 0) {
            //普通状态
            rlNormal.setVisibility(View.VISIBLE);
            rlUpload.setVisibility(View.GONE);
            rlLoading.setVisibility(View.GONE);
            RoundedImageView ivIcon = baseViewHolder.getView(R.id.normal_icon);
            GlideUtils.getInstance().loadBitmapNoAnim(mContext, videoEntity.getImgurl(), ivIcon);
            if (mItemType == 1) {
                rbSelect.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
            } else {
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
                    uploadFileByAliyunFail("已取消下载任务");
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
                            videoEntity.setUploadStatus(0);
                            rbSelect.setChecked(false);
                        } else {
                            videoEntity.setUploadStatus(1);
                            rbSelect.setChecked(true);
                        }
                    }
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
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            ((Activity) mContext).startActivityForResult(intent, 1);
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
        mHasUpLoadTask = false;
        showToast(msg);
    }
    //------------------------阿里云文件上传--------------------------------------------

    @Override
    public void uploadFileByAliyunSuccess() {
        List<String> images = new ArrayList<>();
        images.add(mUpLoadVideoInfo.getImagePath());
        mUserVideoUpLoadPresenter.uploadUserVideoInfo();
        taskHandler.sendEmptyMessage(1);
    }

    @Override
    public void uploadFileByAliyunFail(String msg) {
        mHasUpLoadTask = false;
        VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
        videoEntity.setUploadStatus(-1);
        videoEntity.setLoadPB(0);
        notifyItemChanged(mDatas.size() - 1);
        if (!msg.contains("Task is cancel"))
            showToast(msg);
        taskHandler.sendEmptyMessage(2);
    }

    @Override
    public void uploadFileOnProgress(long currentSize, long totalSize) {

        int loadPB = (int) (((float) currentSize / (float) totalSize) * 100f);
        VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
        if (loadPB != videoEntity.getLoadPB()) {
            videoEntity.setLoadPB(loadPB);
            notifyItemChanged(mDatas.size() - 1);
        }
        taskHandler.sendEmptyMessage(3);
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
    public void uploadUserVideoSuccess() {
        mHasUpLoadTask = false;
        VideoEntity videoEntity = mDatas.get(mDatas.size() - 1);
        videoEntity.setUploadStatus(0);
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
    public String getUpLoadVideoName() {
        return mUpLoadResults.getFilename();
    }

    @Override
    public String getUpLoadVideoIcon() {
        if (mUpLoadVideoInfo != null && mUpLoadVideoInfo.getImagePath() != null)
            return mUpLoadVideoInfo.getImagePath();
        return null;
    }
}

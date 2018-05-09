package com.huanmedia.videochat.media;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.FilePathManager;
import mvp.data.store.glide.GlideUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MediaUpLoadAdapter extends BaseRCAdapter<VideoEntity> implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_WRITE_READ_PERM = 1;//权限标识符

    public MediaUpLoadAdapter(Context context) {
        super(context, R.layout.layout_media_upload_item);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, VideoEntity videoEntity, int i) {
        RelativeLayout rlNormal = baseViewHolder.getView(R.id.rl_layout_normal);
        RelativeLayout rlUpload = baseViewHolder.getView(R.id.rl_layout_upload);
        if (videoEntity.getUploadStatus() == 0) {
            //普通状态
            rlNormal.setVisibility(View.VISIBLE);
            rlUpload.setVisibility(View.GONE);
            RoundedImageView ivIcon = baseViewHolder.getView(R.id.normal_icon);
            GlideUtils.getInstance().loadBitmapNoAnim(mContext, videoEntity.getImgurl(), ivIcon);
        } else if (videoEntity.getUploadStatus() == -1) {
            //上传按钮
            rlNormal.setVisibility(View.GONE);
            rlUpload.setVisibility(View.VISIBLE);
        } else {
            //上传中状态
            rlNormal.setVisibility(View.GONE);
            rlUpload.setVisibility(View.GONE);
        }

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoEntity.getUploadStatus() == -1) {
                    openAlbum();
                } else if (videoEntity.getUploadStatus() == 0) {
                    Navigator navigator = new Navigator();
                    List<String> listData = new ArrayList<>();
                    for (VideoEntity item : mDatas) {
                        listData.add(item.getVoideurl());
                    }
                    navigator.navtoMediaPlay((Activity) mContext, (ArrayList<String>) listData, i);
                }
            }
        });
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

}

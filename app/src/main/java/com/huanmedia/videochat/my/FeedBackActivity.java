package com.huanmedia.videochat.my;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewSpaceItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.my.adapter.FeedbackAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.FilePathManager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class FeedBackActivity extends BaseMVPActivity<FeedBackPresenter> implements FeedBackView , EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CAMERA_WRITE_READ_PERM = 1;//权限标识符
    private final int REQUEST_CODE_IMAGES = 2;//相册请求码
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.opinion_publish_et)
    EditText mOpinionPublishEt;
    @BindView(R.id.opinion_publish_rv)
    RecyclerView mOpinionPublishRv;
    @BindView(R.id.opinion_publish_iv_image)
    ImageView mOpinionPublishIvImage;
    @BindView(R.id.opinion_publish_tv_count)
    TextView mOpinionPublishTvCount;
    @BindView(R.id.opinion_publish_btn_puhlish)
    Button mOpinionPublishBtnPuhlish;
    @BindView(R.id.opinion_publish_buttom_rl)
    RelativeLayout mOpinionPublishButtomRl;
    @BindView(R.id.opinion_publish_rl)
    FrameLayout mOpinionPublishRl;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;
    private List<HM_ImgData> images;
    private int maxChoose;
    private FeedbackAdapter mAadapter;
    private int mEtSize;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        return intent;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initView() {
        initToolbar();
        mAadapter = new FeedbackAdapter(this, R.layout.item_feedback, images);
        mAadapter.setListener(new FeedbackAdapter.OnCloseListener() {
            @Override
            public void close(int position) {
                setSubmitEnable();
            }

            @Override
            public void plusClick() {
               openAlbumWithCheck();
            }
        });
        mOpinionPublishRv.setAdapter(mAadapter);
        RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewSpaceItemDecoration.Builder(context())
                .marginHorizontal(DisplayUtil.dip2px(context(), 4))
                .marginHorizontal(DisplayUtil.dip2px(context(), 4))
                .create();
        mOpinionPublishRv.addItemDecoration(mCurrentItemDecoration);
        mAadapter.setAddShow(false);
        mOpinionPublishEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mEtSize = s.length();
                mOpinionPublishTvCount.setText((300 - mEtSize) + "");
                setSubmitEnable();
            }
        });

            maxChoose=5;
            mOpinionPublishEt.setHint("请描述具体的建议意见或违规行为……(最多300个字符)");
    }

    private void setSubmitEnable() {
        if (mEtSize > 0 || mAadapter.getData().size()>0) {//设置提交按钮必须在字数大于10的情况下可编辑
            mOpinionPublishBtnPuhlish.setEnabled(true);
        } else {
            mOpinionPublishBtnPuhlish.setEnabled(false);
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }
    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText("正在上传...");
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        if (message.equals("反馈已收到，我们会尽快处理回复")){
            RxCountDown.delay(2).subscribe(
                    integer -> finish()
            );
        }
        showHint(HintDialog.HintType.WARN, message);
    }

    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(this, HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }

    @Override
    public Context context() {
        return this;
    }
    @OnClick({R.id.opinion_publish_tv_count, R.id.opinion_publish_btn_puhlish,R.id.opinion_publish_iv_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.opinion_publish_btn_puhlish://发送按钮
                FeedbackAdapter adapter = (FeedbackAdapter) mOpinionPublishRv.getAdapter();
                getBasePresenter().opinionFeedBack(mOpinionPublishEt.getText().toString(), adapter.getData());
                break;
            case R.id.opinion_publish_iv_image:
                   openAlbumWithCheck();
                break;
        }
    }


    @SuppressLint("DefaultLocale")
    @AfterPermissionGranted(REQUEST_CAMERA_WRITE_READ_PERM)
    private void openAlbumWithCheck() {
        if (DoubleClickUtils.isFastDoubleClick())return;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context(), perms)) {
            int choosSize = maxChoose - mAadapter.getData().size();
            if (choosSize == 0) {
                showHint(HintDialog.HintType.WARN, String.format("最多上传%d张照片",maxChoose));
                return;
            }
            new HM_StartAlbum
                    .Bulide(context(), new HM_GlideEngine())
                    .setChooseImages((ArrayList<HM_ImgData>) mAadapter.getData())
                    .setMaxChoose(maxChoose)
                    .setCrop(false)
                    .setShowCamera(true)
                    .setRequestCode(REQUEST_CODE_IMAGES)
                    .setTargetPath(FilePathManager.getUpImage(context()))
                    .bulide();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_write_read),
                    REQUEST_CAMERA_WRITE_READ_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(context(), (String[]) perms.toArray())) {
                openAlbumWithCheck();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder((Activity) context()).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openAlbumWithCheck();
        }
        if (resultCode == Activity.RESULT_OK) {
            if ((requestCode == REQUEST_CODE_IMAGES) && data != null) {//头像设置
                ArrayList<HM_ImgData> chooseImages = data.getParcelableArrayListExtra("chooseImages");
                mAadapter.setData(chooseImages);
                mAadapter.notifyDataSetChanged();
//                mAadapter.getData().addAll(chooseImages);
//                mAadapter.notifyItemRangeChanged(mAadapter.getItemCount()-chooseImages.size(),chooseImages.size());
                setSubmitEnable();
            }
        }
    }

}

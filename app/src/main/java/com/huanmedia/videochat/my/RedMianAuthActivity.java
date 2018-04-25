package com.huanmedia.videochat.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.ilibray.utils.InputMethodUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.FilePathManager;
import mvp.data.store.glide.GlideUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RedMianAuthActivity extends BaseMVPActivity<ReadMainAuthPresenter> implements ReadMainAuthView ,EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CAMERA_WRITE_READ_PERM = 1;//权限标识符
    private final int REQUEST_CODE_HANDCARD =1;//相册请求码
    private final int REQUEST_CODE_CARD =2;//相册请求码
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.readmain_auth_riv_hand_card)
    RoundedImageView mReadmainAuthRivHandCard;
    @BindView(R.id.readmain_auth_tv_hand_card_title)
    TextView mReadmainAuthTvHandCardTitle;
    @BindView(R.id.readmain_auth_rl_hand_card)
    RelativeLayout mReadmainAuthRlHandCard;
    @BindView(R.id.readmain_auth_riv_card)
    RoundedImageView mReadmainAuthRivCard;
    @BindView(R.id.readmain_auth_tv_card_title)
    TextView mReadmainAuthTvCardTitle;
    @BindView(R.id.readmain_auth_rl_card)
    RelativeLayout mReadmainAuthRlCard;
    @BindView(R.id.readmain_auth_et_name)
    EditText mReadmainAuthEtName;
    @BindView(R.id.readmain_auth_et_card_number)
    EditText mReadmainAuthEtCardNumber;
    @BindView(R.id.readmain_auth_et_phone)
    EditText mReadmainAuthEtPhone;
    @BindView(R.id.redmain_auth_btn_ok)
    Button mReadmainAuthBtnOk;
    private HintDialog mLoadingDialog;
    private int mCurrentReqId;
    private HintDialog mHintDialog;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, RedMianAuthActivity.class);
        return intent;
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_mian_auth;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean isImmersionBarEnabled() {
        return super.isImmersionBarEnabled();
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig()
                .statusBarDarkFont(true).keyboardEnable(true);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }
    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
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
    @OnClick({R.id.readmain_auth_rl_hand_card,R.id.readmain_auth_rl_card,R.id.redmain_auth_btn_ok})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.readmain_auth_rl_hand_card:
            case R.id.readmain_auth_rl_card:
                openAlbum(view.getId());
                break;
                case R.id.redmain_auth_btn_ok:
                    String cardnumber = mReadmainAuthEtCardNumber.getText().toString();
                    String cardName = mReadmainAuthEtName.getText().toString();
                    String phone = mReadmainAuthEtPhone.getText().toString();
                    String cardPhoto = (String) mReadmainAuthRivCard.getTag(R.id.image_tag);
                    String cardHandPhoto = (String) mReadmainAuthRivHandCard.getTag(R.id.image_tag);
                getBasePresenter().submitReadmain(cardnumber,cardName,phone,cardPhoto,cardHandPhoto);
                break;
        }
    }
    @AfterPermissionGranted(REQUEST_CAMERA_WRITE_READ_PERM)
    private void openAlbum(int viewId) {
        if (DoubleClickUtils.isFastDoubleClick())return;
        if (viewId==0)return;
        mCurrentReqId =viewId;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context(),perms)) {
            int reqCode = 0;
            switch (viewId){
                case R.id.readmain_auth_rl_hand_card:
                    reqCode =REQUEST_CODE_HANDCARD;
                    break;
                case R.id.readmain_auth_rl_card:
                    reqCode =REQUEST_CODE_CARD;
                    break;
            }
            if (reqCode==0)return;
            new HM_StartAlbum
                    .Bulide(context(),new HM_GlideEngine())
                    .setMaxChoose(1)
                    .setCrop(false)
                    .setShowCamera(true)
                    .setMaxOutPutH(400)
                    .setMaxOutPutW(400)
                    .setRequestCode(reqCode)
                    .setTargetPath(FilePathManager.getUpImage(context()))
                    .bulide();
        }  else {
        EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_write_read),
                REQUEST_CAMERA_WRITE_READ_PERM,perms);
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
                openAlbum(mCurrentReqId);
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
            openAlbum(mCurrentReqId);
        }
        if (resultCode== Activity.RESULT_OK){
            if ((requestCode==REQUEST_CODE_HANDCARD ||requestCode==REQUEST_CODE_CARD) && data!=null){//头像设置
                ArrayList<HM_ImgData> chooseImages = data.getParcelableArrayListExtra("chooseImages");
                if (chooseImages!=null && chooseImages.size()>0){
                    String file = new File(chooseImages.get(0).getImage()).getAbsolutePath();
                    RoundedImageView iv=null;
                    if (requestCode==REQUEST_CODE_HANDCARD){
                        iv=mReadmainAuthRivHandCard;
                        iv.setTag(R.id.image_tag,file);
                   }
                   if (requestCode == REQUEST_CODE_CARD)
                   {
                       iv = mReadmainAuthRivCard;
                       iv.setTag(R.id.image_tag,file);
                   }
                       GlideUtils.getInstance()
                               .loadBitmapNoAnim(
                                       context(),
                                       new File(iv.getTag(R.id.image_tag).toString()),
                                       iv
                               );
                }
            }
        }
    }
    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodUtils.hideSoftInput(this);
    }

    @Override
    public void showError(int flag, String message) {
        showHint(HintDialog.HintType.WARN,message);
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

    @Override
    public void submitSuccess() {
        UserManager.getInstance().getCurrentUser().getUserinfo().setIsstarauth(0);
        new MaterialDialog.Builder(this)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .content("提交成功，系统将在1-2个工作日发送审核通知")
                .positiveText("确定")
                .positiveColorRes(R.color.base_yellow)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }
}

package com.huanmedia.videochat.launch.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.extar.HM_ExtarCropImageView;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.SimpleToolBar;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.dialog.SexChooseDialog;
import com.huanmedia.videochat.launch.StartActivity;
import com.huanmedia.videochat.repository.entity.UserEntity;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.net.DataResponse;
import mvp.data.store.FilePathManager;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.RoundedCornersTransformation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 登录后完善用户资料
 *
 * @author Eric<br/>
 * @version <br/>
 * @description <br/>
 * @email yb498869020@hotmail.com<br/>
 * Create by ericYang on 2017/11/16.
 * @since <br/>
 */
public class CompleteInformationFragment extends BaseMVPFragment<CompleteInformationPresenter> implements CompleteInformationView , EasyPermissions.PermissionCallbacks{
    public final static String TAG = "/CompleteInformationFragment";
    private static String KEY_USER = "user";
    private static final int REQUEST_CAMERA_WRITE_READ_PERM = 1;//权限标识符
    private static final int REQUEST_CODE_ALBUM =2;//相册请求码
    @BindView(R.id.complete_info_iv_header)
    ImageView mCompleteInfoIvHeader;
    @BindView(R.id.complete_info_fl_header)
    FrameLayout mCompleteInfoFlHeader;
    @BindView(R.id.complete_info_ll_info)
    LinearLayout mCompleteInfoLlInfo;
    @BindView(R.id.complete_info_ll_hint)
    LinearLayout mCompleteInfoLlHint;
    @BindView(R.id.complete_info_btn_complete)
    Button mCompleteInfoBtnComplete;
    @BindView(R.id.complete_info_et_nickName)
    EditText mCompleteInfoEtNickName;
    @BindView(R.id.complete_info_tv_sex)
    TextView mCompleteInfoTvSex;
    @BindView(R.id.complete_info_tv_age)
    TextView mCompleteInfoTvAge;
    private UserEntity mUserEntity;
    private UserEntity.UserinfoEntity mUserinfo=new UserEntity.UserinfoEntity();
    private MaterialDialog progressDialog;
    private boolean isCompleInfo;

    public CompleteInformationFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(UserEntity user) {
        CompleteInformationFragment fragment = new CompleteInformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_information;
    }

    @Override
    protected void initData() {
        mUserEntity = getArguments().getParcelable(KEY_USER);
        if (mUserEntity == null) {
            return;
        }
    }

    @Override
    protected void initView(View view) {
        SimpleToolBar simpleToolBar = new SimpleToolBar((ViewGroup) view);
        simpleToolBar.centerBtn("完善资料");
        simpleToolBar.getLiftBtn().setVisibility(View.GONE);
        mCompleteInfoEtNickName.addTextChangedListener(nickNameWatcher);
    }
    private TextWatcher nickNameWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        private int maxLen = 20; // the max byte
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = mCompleteInfoEtNickName.getSelectionStart();
            editEnd = mCompleteInfoEtNickName.getSelectionEnd();
            mCompleteInfoEtNickName.removeTextChangedListener(nickNameWatcher);
            if (!TextUtils.isEmpty(mCompleteInfoEtNickName.getText())) {
                while (s.toString().getBytes(Charset.forName("GBK")).length > maxLen) {
                    s.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            }
            // 恢复监听器
            mCompleteInfoEtNickName.addTextChangedListener(nickNameWatcher);
            mUserinfo.setNickname(s.toString());
        }
    };

    @Override
    public void showError(String message) {
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void upDateSuccess(DataResponse response) {
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        isCompleInfo=true;
        UserManager.getInstance().getCurrentUser().setExtdataflag(1);
        UserManager.getInstance().saveUser(UserManager.getInstance().getCurrentUser());
        getNavigator().navToMain(getContext(),true);
        ActivitManager.getAppManager().finishAlikeActivity(StartActivity.class);
    }

    @Override
    public void onDetach() {
        if (!isCompleInfo){
            UserManager.getInstance().outLogin(null);
        }
        super.onDetach();
    }

    @OnClick({R.id.complete_info_fl_header,R.id.complete_info_rl_sex,R.id.complete_info_rl_age,R.id.complete_info_btn_complete})
    public void onclickView(View view){
        switch (view.getId()){
            case R.id.complete_info_fl_header:
                openAlbum();
                break;
                case R.id.complete_info_rl_sex:
                    SexChooseDialog dialog = new SexChooseDialog(getContext());
                    dialog.setCheckedSexListener((sex, tag) -> {
                        mCompleteInfoTvSex.setText(sex);
                        mCompleteInfoTvSex.setTag(tag);
                        mUserinfo.setSex(tag);
                    });
                    dialog.setDefault(mCompleteInfoTvSex.getTag()==null?0:Integer.parseInt(mCompleteInfoTvSex.getTag().toString()));
                    dialog.show();
                break;
                case R.id.complete_info_rl_age:
                    DialogPick pick = new DialogPick(this.getContext());
                   pick.setDatelistener(obj -> {
                       mCompleteInfoTvAge.setText(obj);
                       mUserinfo.setBirthday(obj);
                   });
                    pick.showPickerDate(mCompleteInfoTvAge.getText().toString(), "%s-%s-%s");
                break;
                case R.id.complete_info_btn_complete:
                    getBasePresenter().checkUploadUserInfo(mUserinfo);
                break;
        }
    }


    @Override
    public void confirmUpdate(UserEntity.UserinfoEntity userinfo) {
        new MaterialDialog.Builder(context())
                .title("信息确认")
                .content("信息填写后不可修改，您想好了吗？")
                .negativeColorRes(R.color.base_gray)
                .positiveColorRes(R.color.base_yellow)
                .negativeText("我再想想")
                .positiveText("我想好了")
                .onPositive((dialog1, which) -> {
                    if (progressDialog==null)
                        progressDialog=new MaterialDialog.Builder(getContext())
                                .title("")
                                .content(R.string.login_submit_infomation)
                                .progress(true, 0)
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
                                .build();
                    progressDialog.show();
                    getBasePresenter().uploadUserInfo(userinfo);
                })
                .show();
    }


    @AfterPermissionGranted(REQUEST_CAMERA_WRITE_READ_PERM)
    private void openAlbum() {
        if (DoubleClickUtils.isFastDoubleClick())return;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(),perms)) {
                new HM_StartAlbum
                        .Bulide(getContext(),new HM_GlideEngine())
                        .setMaxChoose(1)
                        .setCrop(true)
                        .setMaxOutPutH(400)
                        .setMaxOutPutW(400)
                        .setRequestCode(REQUEST_CODE_ALBUM)
                        .setTargetPath(FilePathManager.getUpImage(getContext()))
                        .setCropMode(HM_ExtarCropImageView.CropMode.SQUARE)
                        .bulide();
        } else {
            // Ask for one permission
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
            if (EasyPermissions.hasPermissions(getContext(), (String[]) perms.toArray())) {
                 openAlbum();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(getActivity()).build().show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openAlbum();
        }
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==REQUEST_CODE_ALBUM && data!=null){//头像设置
                ArrayList<HM_ImgData> chooseImages = data.getParcelableArrayListExtra("chooseImages");
                if (chooseImages!=null && chooseImages.size()>0){
                    mUserinfo.setUserphoto(new File(chooseImages.get(0).getImage()).getAbsolutePath());
                    GlideUtils.getInstance()
                            .loadContextRoundBitmap(
                                    getContext(),
                                    mUserinfo.getUserphoto(),
                                    mCompleteInfoIvHeader,
                                    new RoundedCornersTransformation(getContext(),
                                            DisplayUtil.dip2px(getContext(),3),1));
                }
            }
        }

    }
}

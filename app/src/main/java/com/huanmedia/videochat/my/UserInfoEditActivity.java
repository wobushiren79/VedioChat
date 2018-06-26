package com.huanmedia.videochat.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.extar.HM_ExtarCropImageView;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewSpaceItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.main2.MainActivity;
import com.huanmedia.videochat.repository.entity.ItemMenuEntity;
import com.huanmedia.videochat.repository.entity.OccupationsEntity;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.FilePathManager;
import mvp.data.store.glide.GlideUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

@SuppressWarnings("ButterKnifeInjectNotCalled")
public class UserInfoEditActivity extends BaseMVPActivity<UserInfoEditPresenter> implements UserInfoEditView, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CAMERA_WRITE_READ_PERM = 1;//权限标识符
    private static final int REQUEST_CODE_ALBUM = 2;//相册请求码
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_info_edit_iv_header)
    RoundedImageView mUserInfoEditIvHeader;
    @BindView(R.id.user_info_edit_tv_enter)
    TextView mUserInfoEditIvEnter;
    @BindView(R.id.user_info_edit_rl_head)
    RelativeLayout mUserInfoEditRlHead;
    @BindView(R.id.user_info_edit_rv_photos)
    RecyclerView mUserInfoEditRvPhotos;
    @BindView(R.id.user_info_edit_tv_photos_enter)
    TextView mUserInfoEditTvPhotosEnter;
    @BindView(R.id.user_info_edit_rl_photos)
    RelativeLayout mUserInfoEditRlPhotos;
    @BindView(R.id.user_info_edit_tv_balse_name)
    TextView mUserInfoEditTvBalseName;
    @BindView(R.id.user_info_edit_base_rl_name)
    RelativeLayout mUserInfoEditBaseRlName;
    @BindView(R.id.user_info_edit_tv_balse_sex)
    TextView mUserInfoEditTvBalseSex;
    @BindView(R.id.user_info_edit_base_rl_sex)
    RelativeLayout mUserInfoEditBaseRlSex;
    @BindView(R.id.user_info_edit_tv_balse_age)
    TextView mUserInfoEditTvBalseAge;
    @BindView(R.id.user_info_edit_base_rl_age)
    RelativeLayout mUserInfoEditBaseRlAge;
    @BindView(R.id.user_info_edit_tv_balse_addr)
    TextView mUserInfoEditTvBalseAddr;
    @BindView(R.id.user_info_edit_base_rl_addr)
    RelativeLayout mUserInfoEditBaseRlAddr;
    @BindView(R.id.user_info_edit_tv_balse_occupation_lable)
    TextView mUserInfoEditTvBalseOccupationLable;
    @BindView(R.id.user_info_edit_tv_balse_occupation)
    TextView mUserInfoEditTvBalseOccupation;
    @BindView(R.id.user_info_edit_base_rl_occupation)
    RelativeLayout mUserInfoEditBaseRlOccupation;
    @BindView(R.id.user_info_edit_base_ll)
    LinearLayout mUserInfoEditBaseLl;
    @BindView(R.id.user_info_edit_ll_video)
    RelativeLayout mUserIfoEditLLVideo;
    @BindView(R.id.user_info_edit_rv_video)
    RecyclerView mUserInfoEditRvVideos;
    @BindView(R.id.user_info_edit_tv_video_enter)
    TextView mUserInfoEditTvVideoEnter;

    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;
    private BaseQuickAdapter<PhotosEntity, BaseViewHolder> mPhotosAdapter;
    private BaseQuickAdapter<VideoEntity, BaseViewHolder> mVideosAdapter;
    private HashMap<String, Object> modifyUserData;
    private final String KEY_MODIFY_HEADER = "userHeader";
    private boolean isPhotoChange;//照片墙发生改变
    private boolean isAuth;
    private String mAuthMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backLastActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回之前的界面
     */
    private void backLastActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPhoto(Intent action) {
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_USER_PHOTOS_CHANGE:
                List<PhotosEntity> photos = action.getParcelableArrayListExtra("data");
                if (photos != null) {
                    showPhotos(photos);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isPhotoChange) {
//            isPhotoChange = false;
//            getBasePresenter().getUserInfo();
//        }
        mPhotosAdapter.getData().clear();
        mVideosAdapter.getData().clear();
        getBasePresenter().getUserInfo();
    }

    public static Intent getCallingIntent(Context context, boolean isAuth, String authMsg) {
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        intent.putExtra("isAuth", isAuth);
        intent.putExtra("authMsg", authMsg);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_edit;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAuth)
            getMenuInflater().inflate(R.menu.user_info_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_info_edit_menu_next://时间筛选
                getBasePresenter().checkCompleteness();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        modifyUserData = new HashMap<String, Object>();
        initToolbar();
        UserEntity.UserinfoEntity userInfo = UserManager.getInstance()
                .getCurrentUser()
                .getUserinfo();
        GlideUtils.getInstance()
                .loadBitmapNoAnim(this, userInfo.getUserphoto_thumb(), mUserInfoEditIvHeader);
//        mUserInfoEditRvPhotos
//                .addItemDecoration(new RecyclerViewSpaceItemDecoration
//                        .Builder(context())
//                        .marginHorizontal(DisplayUtil.dip2px(context(), 4))
//                        .marginHorizontal(DisplayUtil.dip2px(context(), 4))
//                        .create());
        mPhotosAdapter = new BaseQuickAdapter<PhotosEntity, BaseViewHolder>(R.layout.item_user_eidt_photo) {
            @Override
            public int getItemCount() {
                if (mData.size() > 0 && mData.size() <= 3) {
                    return mData.size();
                } else if (mData.size() > 3) {
                    return 3;//多显示3张
                }
                return mData.size();
            }

            @Override
            protected void convert(BaseViewHolder helper, PhotosEntity item) {
                GlideUtils.getInstance()
                        .loadBitmapNoAnim(
                                context(),
                                item.getPhoto_thumb(),
                                helper.getView(R.id.item_user_info_edit_iv_photo));
            }
        };
        mUserInfoEditRvPhotos.setAdapter(mPhotosAdapter);
        mVideosAdapter = new BaseQuickAdapter<VideoEntity, BaseViewHolder>(R.layout.item_user_eidt_photo) {
            @Override
            public int getItemCount() {
                if (mData.size() > 0 && mData.size() <= 3) {
                    return mData.size();
                } else if (mData.size() > 3) {
                    return 3;//多显示3张
                }
                return mData.size();
            }

            @Override
            protected void convert(BaseViewHolder helper, VideoEntity item) {
                GlideUtils.getInstance()
                        .loadBitmapNoAnim(
                                context(),
                                item.getImgurl(),
                                helper.getView(R.id.item_user_info_edit_iv_photo));
            }
        };
        mUserInfoEditRvVideos.setAdapter(mVideosAdapter);

        mUserInfoEditTvBalseName.setText(Check.checkReplace(userInfo.getNickname(), "未设置"));
        mUserInfoEditTvBalseAge.setText(Check.checkReplace(userInfo.getBirthday(), "未设置"));
        mUserInfoEditTvBalseSex.setText(Check.checkReplace(userInfo.getSex() == 1 ? "男" : "女"));
        ItemMenuEntity occup = UserManager.getInstance().getCurrentUser().getUserinfo().getOccupation();
        if (occup != null)
            mUserInfoEditTvBalseOccupation.setText(occup.getName());
        else
            mUserInfoEditTvBalseOccupation.setText("未设置");
        //所在地
        setAddrStr(userInfo.getProvince(), userInfo.getCity());
    }

    @Override
    protected void initData() {
        isAuth = getIntent().getBooleanExtra("isAuth", false);
        mAuthMsg = getIntent().getStringExtra("authMsg");
//        getBasePresenter().getUserInfo();
//        if (isAuth && mAuthMsg!=null){
//            showError(0,mAuthMsg);
//        }
    }


    private void setAddrStr(ItemMenuEntity province,
                            ItemMenuEntity.SubEntity city) {
        String addrStr = null;
        if (province != null)
            addrStr = province.getName();
        if (city != null) {
            if (addrStr != null) {
                addrStr = addrStr + "-" + city.getName();
            } else {
                addrStr = city.getName();
            }
        }
        mUserInfoEditTvBalseAddr.setText(Check.checkReplace(Check.checkReplace(addrStr, "未设置")));
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> backLastActivity());
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
    public void showNext() {
        getNavigator().navtoRedMianAuth(this);
        finish();
    }

    @Override
    public void showRetry() {
    }

    @Override
    public void hideRetry() {
    }

    @Override
    public void showError(int flag, String message) {
        showHint(HintDialog.HintType.WARN, message);
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showPhotos(List<PhotosEntity> phpots) {
        if (phpots == null || phpots.size() == 0) {
            mUserInfoEditTvPhotosEnter.setText("你还没有上传照片");
            return;
        } else {
            mUserInfoEditTvPhotosEnter.setText("");
        }
        GridLayoutManager layoutManager = (GridLayoutManager) mUserInfoEditRvPhotos.getLayoutManager();
        int spanCount = phpots.size() == 0 ? 1 : phpots.size() > 3 ? 3 : phpots.size();
        layoutManager.setSpanCount(spanCount);
        mPhotosAdapter.setNewData(phpots);
    }

    @Override
    public void showVideos(List<VideoEntity> videos) {
        if (videos == null || videos.size() == 0) {
            mUserInfoEditTvVideoEnter.setText("你还没有上传视频");
            return;
        } else {
            mUserInfoEditTvVideoEnter.setText("");
        }
        GridLayoutManager layoutManager = (GridLayoutManager) mUserInfoEditRvVideos.getLayoutManager();
        int spanCount = videos.size() == 0 ? 1 : videos.size() > 3 ? 3 : videos.size();
        layoutManager.setSpanCount(spanCount);
        mVideosAdapter.setNewData(videos);
    }

    @Override
    public void showCitys(List<ItemMenuEntity> citys) {
        showAddrDialog();
    }

    @Override
    public void showCoinfgData(OccupationsEntity userConfigData) {
        showOccpDialog();
    }

    @OnClick({R.id.user_info_edit_rl_head, R.id.user_info_edit_rl_photos, R.id.user_info_edit_base_rl_addr, R.id.user_info_edit_base_rl_occupation, R.id.user_info_edit_ll_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_info_edit_base_rl_addr:
                showAddrDialog();
                break;
            case R.id.user_info_edit_base_rl_occupation:
                showOccpDialog();
                break;
            case R.id.user_info_edit_rl_photos:
                if (getBasePresenter().isCanNavToPhotos()) {
                    getNavigator().navtoPhotos(this, (ArrayList<PhotosEntity>) mPhotosAdapter.getData());
                }
                break;
            case R.id.user_info_edit_rl_head:
                openAlbum();
                break;
            case R.id.user_info_edit_ll_video:
                getNavigator().navtoMediaUpLoad(this, (ArrayList<VideoEntity>) mVideosAdapter.getData(),true);
                break;
        }
    }

    private void showOccpDialog() {
        DialogPick dialog = new DialogPick(this);
        dialog.setOnOccupationSelectListener((date, position) -> {
            try {
                if (UserManager.getInstance().getCurrentUser().getUserinfo().getOccupation().getId() ==
                        date.getId()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getBasePresenter().setDefaultOccp(position);
            mUserInfoEditTvBalseOccupation.setText(date.getName());
            getBasePresenter().upBaseInfo(0, 0, date.getId());
        });
        if (getBasePresenter().getUserConfigData() != null)
            dialog.showMajorPickerDialog(getBasePresenter().getUserConfigData().getOccupation(), getBasePresenter().getDefaultOccp());
    }

    private void showAddrDialog() {
        DialogPick dialog = new DialogPick(this);
        dialog.setOnAreaPickSelectListener((areaentity, choosePositions, chooseids) -> {
            try {
                if (UserManager.getInstance().getCurrentUser().getUserinfo().getProvince().getId()
                        == chooseids[0]) {
                    if (UserManager.getInstance().getCurrentUser().getUserinfo().getCity().getId()
                            == chooseids[1])
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getBasePresenter().setDefaultAddr(choosePositions);
            setAddrStr(areaentity, areaentity.getSub().get(choosePositions[1]));
            getBasePresenter().upBaseInfo(chooseids[0], chooseids[1], 0);
        });
        dialog.showAreaPickerDialog(getBasePresenter().getCitys(), getBasePresenter().getDefaultAddr());
    }

    @AfterPermissionGranted(REQUEST_CAMERA_WRITE_READ_PERM)
    private void openAlbum() {
        if (DoubleClickUtils.isFastDoubleClick()) return;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context(), perms)) {
            new HM_StartAlbum
                    .Bulide(context(), new HM_GlideEngine())
                    .setMaxChoose(1)
                    .setCrop(true)
                    .setMaxOutPutH(400)
                    .setMaxOutPutW(400)
                    .setRequestCode(REQUEST_CODE_ALBUM)
                    .setTargetPath(FilePathManager.getUpImage(context()))
                    .setCropMode(HM_ExtarCropImageView.CropMode.SQUARE)
                    .bulide();
        } else {
            // Ask for one permission
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
                openAlbum();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openAlbum();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ALBUM && data != null) {//头像设置
                ArrayList<HM_ImgData> chooseImages = data.getParcelableArrayListExtra("chooseImages");
                if (chooseImages != null && chooseImages.size() > 0) {
                    modifyUserData.put(KEY_MODIFY_HEADER, new File(chooseImages.get(0).getImage()).getAbsolutePath());
                    GlideUtils.getInstance()
                            .loadBitmapNoAnim(
                                    context(),
                                    modifyUserData.get(KEY_MODIFY_HEADER),
                                    mUserInfoEditIvHeader
                            );
                    getBasePresenter().upImage("/index/userext/updateface", modifyUserData.get(KEY_MODIFY_HEADER).toString());
                }
            }
        }

    }


}

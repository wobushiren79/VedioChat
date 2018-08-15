package com.huanmedia.videochat.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.mvp.presenter.photo.IPhotoListPresenter;
import com.huanmedia.videochat.mvp.presenter.photo.PhotoListPresenterImpl;
import com.huanmedia.videochat.mvp.view.photo.IPhotoListView;
import com.huanmedia.videochat.my.adapter.PhotosAdapter;
import com.huanmedia.videochat.my.adapter.PhotosItemDragAndSwipeCallback;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.FilePathManager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressWarnings("ButterKnifeInjectNotCalled")
public class PhotosActivity extends BaseMVPActivity<PhotosPrestener> implements PhotosView, EasyPermissions.PermissionCallbacks, IPhotoListView {
    private static final int REQUEST_CAMERA_WRITE_READ_PERM = 1;//权限标识符
    private final int REQUEST_CODE_IMAGES = 2;//相册请求码
    private final int REQUEST_CODE_SECRET_IMAGES = 3;//相册请求码
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photos_rv)
    RecyclerView mPhotosRv;
    @BindView(R.id.photos_btn_ok)
    Button mPhotosBtnOk;
    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;

    private PhotosAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private IPhotoListPresenter mPhotoListPresenter;

    private @UpLoadType
    int mUploadType;


    @Retention(RetentionPolicy.SOURCE)
    public @interface UpLoadType {
        int ALL = 0;
        int NORMAL = 1;
        int SECRET = 2;
    }

    public static Intent getCallingIntent(Context context, @UpLoadType int uploadType, ArrayList<PhotosEntity> data) {
        Intent intent = new Intent(context, PhotosActivity.class);
        intent.putParcelableArrayListExtra("data", data);
        intent.putExtra("uploadType", uploadType);
        return intent;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photos;
    }

    @Override
    protected void initView() {
        mUploadType = getIntent().getIntExtra("uploadType", 1);
        mPhotoListPresenter = new PhotoListPresenterImpl(this);
        ArrayList<PhotosEntity> mData = getIntent().getParcelableArrayListExtra("data");
        initToolbar();
        //照片墙
        RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewItemDecoration.Builder(context())
                .color(ContextCompat.getColor(context(), R.color.white))
                .thickness(DisplayUtil.dip2px(context(), 4))
                .gridHorizontalSpacing(DisplayUtil.dip2px(context(), 4))
                .gridVerticalSpacing(DisplayUtil.dip2px(context(), 4))
                .gridLeftVisible(true)
                .gridRightVisible(true)
                .gridTopVisible(true)
                .gridBottomVisible(true)
                .create();
        int count = ((GridLayoutManager) mPhotosRv.getLayoutManager()).getSpanCount();
        mPhotosRv.addItemDecoration(mCurrentItemDecoration);
        OnItemDragListener onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                if (pos == 0) {//移动到第一张图片更换封面标识
                    mAdapter.notifyItemRangeChanged(0, 2);
                }
                getBasePresenter().updateUserPhotosOrder(mAdapter.getData());
            }
        };

        mPhotosRv.addOnItemTouchListener(new OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.isEdit()) {//编辑模式点击选中相片
                    PhotosEntity data = ((PhotosEntity) adapter.getData().get(position));
                    if (mAdapter.getCheckMap().get(position) == null) {
                        mAdapter.getCheckMap().put(position, data);
                        adapter.notifyItemChanged(position);
                    } else {
                        mAdapter.getCheckMap().remove(position);
                        adapter.notifyItemChanged(position);
                    }
                } else {
                    if (position == adapter.getItemCount() - 1) {//打开相册
                        openAlbum();
                    } else {//查看大图
                        new HM_StartSeePhoto.Bulide(context(), new HM_GlideEngine())
                                .setList(adapter.getData())
                                .setCurrentSelect(position)
                                .bulide();
                    }
                }
            }
        });
        if (mUploadType == UpLoadType.SECRET) {
            mToolbar.setTitle("私照墙");
            mPhotoListPresenter.getSecretPhoto();
        } else {
            mAdapter = new PhotosAdapter(R.layout.item_photos, mData, count, mUploadType);
            mAdapter.bindToRecyclerView(mPhotosRv);
            PhotosItemDragAndSwipeCallback itemDragAndSwipeCallback = new PhotosItemDragAndSwipeCallback(mAdapter);
            mItemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            mItemTouchHelper.attachToRecyclerView(mPhotosRv);
            // 开启拖拽
            mAdapter.setOnItemDragListener(onItemDragListener);
            mAdapter.enableDragItem(mItemTouchHelper, R.id.item_photos_iv, true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photos_menu_edit:
                if (item.getTitle().toString().equals("编辑")) {
                    if (mAdapter.getData().size() == 0) return true;
                    item.setTitle("取消");
                    mAdapter.setEdit(true);
                    mPhotosBtnOk.setText("删除");
                    mAdapter.enableDragItem(null);
                    mPhotosBtnOk.setVisibility(View.VISIBLE);
//                    mPhotosBtnOk.setBackground(ContextCompat.getDrawable(context(), R.drawable.base_btn_solid_disable));
                    mPhotosRv.getAdapter().notifyDataSetChanged();
                } else {
                    mAdapter.setEdit(false);
                    mAdapter.getCheckMap().clear();
                    item.setTitle("编辑");
                    mAdapter.enableDragItem(mItemTouchHelper, R.id.item_photos_iv, true);
//                    mPhotosBtnOk.setText("上传照片");
                    mPhotosBtnOk.setVisibility(View.GONE);
//                    mPhotosBtnOk.setBackground(ContextCompat.getDrawable(context(), R.drawable.base_btn_solid));
                    mPhotosRv.getAdapter().notifyDataSetChanged();
                }
                break;
        }
        return true;
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


    @OnClick({R.id.photos_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photos_btn_ok:
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < mAdapter.getCheckMap().size(); i++) {
                    HM_PhotoEntity photo = mAdapter.getCheckMap().valueAt(i);
                    buffer.append(((PhotosEntity) photo).getId());
                    if (i < mAdapter.getCheckMap().size() - 1) {
                        buffer.append(",");
                    }
                }
                getBasePresenter().deletePhotos(buffer.toString());
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_CAMERA_WRITE_READ_PERM)
    private void openAlbum() {
        if (DoubleClickUtils.isFastDoubleClick()) return;
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context(), perms)) {
//            int choosSize = 8 - mAdapter.getData().size();
//            if (choosSize == 0) {
//                showHint(HintDialog.HintType.WARN, "最多上传8张照片");
//                return;
//            }
            int choosSize = 5;
            if (mUploadType == UpLoadType.SECRET) {
                choosSize = 1;
            }
            new HM_StartAlbum
                    .Bulide(context(), new HM_GlideEngine())
                    .setMaxChoose(choosSize)
                    .setCrop(false)
                    .setShowCamera(true)
                    .setRequestCode(REQUEST_CODE_IMAGES)
                    .setTargetPath(FilePathManager.getUpImage(context()))
                    .bulide();
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.rationale_camera_write_read),
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
            new AppSettingsDialog.Builder((Activity) context()).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openAlbum();
        }
        if (resultCode == Activity.RESULT_OK) {
            if ((requestCode == REQUEST_CODE_IMAGES) && data != null) {//头像设置
                ArrayList<HM_PhotoEntity> chooseImages = data.getParcelableArrayListExtra("chooseImages");
                if (chooseImages != null && chooseImages.size() > 0) {
                    if (mUploadType == UpLoadType.SECRET) {
                        getNavigator().navtoFileInfoEdit(this, 1, chooseImages.get(0).getImage(), REQUEST_CODE_SECRET_IMAGES);
                    } else {
                        List<String> upDatas = new ArrayList<>();
                        for (int i = 0; i < chooseImages.size(); i++) {
                            upDatas.add(chooseImages.get(i).getImage());
                        }
                        getBasePresenter().upImages("/index/userext/addphotos", upDatas);
                    }
                }
            } else if (requestCode == REQUEST_CODE_SECRET_IMAGES && data != null) {

            }
        }
    }

    @Override
    public void deleteSuccess() {
        for (int i = 0; i < mAdapter.getCheckMap().size(); i++) {
            HM_PhotoEntity photo = mAdapter.getCheckMap().valueAt(i);
            mAdapter.remove(mAdapter.getData().indexOf(photo));
        }
        mAdapter.getCheckMap().clear();
        Intent intent = new Intent(EventBusAction.ACTION_USER_PHOTOS_CHANGE);
        List<PhotosEntity> oldData = mAdapter.getData();
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) oldData);
        EventBus.getDefault().post(intent);
    }

    @Override
    public void upPhotoSuccess(List<PhotosEntity> phpotsEntities) {
        mAdapter.getData().addAll(phpotsEntities);

        mAdapter.notifyDataSetChanged();
        Intent intent = new Intent(EventBusAction.ACTION_USER_PHOTOS_CHANGE);
        List<PhotosEntity> oldData = mAdapter.getData();
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) oldData);
        EventBus.getDefault().post(intent);
    }

    @Override
    public void updateUserPhotosOrderSuccess() {
        Intent intent = new Intent(EventBusAction.ACTION_USER_PHOTOS_CHANGE);
        List<PhotosEntity> oldData = mAdapter.getData();
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) oldData);
        EventBus.getDefault().post(intent);
    }


    //----------------照片列表获取---------------------
    @Override
    public void getPhotoListSuccess(List<PhotosEntity> listData) {
        int count = ((GridLayoutManager) mPhotosRv.getLayoutManager()).getSpanCount();
        mAdapter = new PhotosAdapter(R.layout.item_photos, listData, count, mUploadType);
        mAdapter.bindToRecyclerView(mPhotosRv);
    }

    @Override
    public void getPhotoListFail(String msg) {
        showToast(msg);
    }

    @Override
    public void setAllPhotoList(List<PhotosEntity> listData) {

    }

    @Override
    public void setOpenPhotoList(List<PhotosEntity> listData) {

    }

    @Override
    public void setSecretList(List<PhotosEntity> listData) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }
}

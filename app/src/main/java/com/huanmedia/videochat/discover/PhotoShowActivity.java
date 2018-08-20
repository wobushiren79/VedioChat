package com.huanmedia.videochat.discover;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.github.chrisbanes.photoview.PhotoView;
import com.huanmedia.hmalbumlib.extar.HM_DisplayUtil;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.discover.adapter.PhotoShowPagerAdapter;
import com.huanmedia.videochat.media.MediaPlayActivity;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.presenter.file.FileManagePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileManagePresenter;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.transform.BlurTransformation;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

public class PhotoShowActivity extends BaseActivity implements ViewPager.OnPageChangeListener, IFileManageCheckView, IFileManagePayView {

    @BindView(R.id.photo_tv_page)
    TextView mTVPage;
    @BindView(R.id.iv_exit)
    ImageView mIVExit;
    @BindView(R.id.view_pager)
    ViewPager mVP;

    private List<View> mListImage;
    private PhotoShowPagerAdapter mPhotoShowPagerAdapter;
    private List<PhotosEntity> mPhotoList;
    private int mPhotoPosition;
    private boolean mIsShowMask;
    private IFileManagePresenter mFileManagePresenter;
    private int mResize;

    public static Intent getCallingIntent(Context context, List<PhotosEntity> photoList, int position, boolean isShowMask) {
        Intent intent = new Intent(context, PhotoShowActivity.class);
        intent.putExtra("photoList", (ArrayList) photoList);
        intent.putExtra("position", position);
        intent.putExtra("isShowMask", isShowMask);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_show;
    }

    @Override
    protected void initView() {
        super.initView();
        ((RelativeLayout.LayoutParams) mIVExit.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mFileManagePresenter = new FileManagePresenterImpl(this, this);
        mPhotoList = getIntent().getParcelableArrayListExtra("photoList");
        mPhotoPosition = getIntent().getIntExtra("position", 0);
        mIsShowMask = getIntent().getBooleanExtra("isShowMask", false);
        mResize = HM_DisplayUtil.getDisplayWidth(this);

        if (mPhotoList == null || mPhotoList.size() == 0) {
            finish();
            return;
        }

        mListImage = new ArrayList<>();

        for (PhotosEntity item : mPhotoList) {
            PhotoView photoView = new PhotoView(this);
            photoViewHandler(item, photoView);
            mListImage.add(photoView);
        }
        mVP.addOnPageChangeListener(this);
        mPhotoShowPagerAdapter = new PhotoShowPagerAdapter(this, mListImage);
        mVP.setAdapter(mPhotoShowPagerAdapter);
        mVP.setOffscreenPageLimit(4);
        setPageText();
        mVP.setCurrentItem(mPhotoPosition);
    }

    @OnClick({R.id.iv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_exit:
                finish();
                break;
        }
    }

    private void photoViewHandler(PhotosEntity itemData, PhotoView photoView) {
        if (mIsShowMask) {
            if (itemData.getPlevel() == 1) {
                changeMask(false, itemData, photoView);
            } else {
                changeMask(true, itemData, photoView);
            }
        } else {
            changeMask(false, itemData, photoView);
        }

    }

    private void changeMask(boolean hasMask, PhotosEntity itemData, PhotoView photoView) {
        if (hasMask) {
            GlideApp.with(this)
                    .asBitmap()
                    .override(100, 100)
                    .load(itemData.getPhoto())
                    .transform(new BlurTransformation(this, 25))
                    .into(photoView);
        } else {
            itemData.setPlevel(1);
            GlideApp.with(this)
                    .load(itemData.getPhoto())
                    .override(mResize, mResize)
                    .priority(Priority.HIGH)
                    .into(photoView);
        }
    }

    public void setPageText() {
        if (mPhotoList != null)
            mTVPage.setText((mPhotoPosition + 1) + "/" + mPhotoList.size());
    }

    //------------------pager转换---------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPhotoPosition = position;
        setPageText();
        if (mIsShowMask) {
            PhotosEntity itemData = mPhotoList.get(position);
            if (itemData.getPlevel() == 2)
                mFileManagePresenter.checkHasPhotoFile(itemData.getId());
        }
    }

    //------------------文件处理-------------
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void checkHasFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (results.getHasread() == 1) {
            changeMask(false, mPhotoList.get(mPhotoPosition), (PhotoView) mListImage.get(mPhotoPosition));
        } else {
            GeneralDialog dialog = new GeneralDialog(this);
            dialog
                    .setContent("将消耗您" + results.getVcoin() + "钻石")
                    .setTitle("您确定要查看私照吗？")
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < results.getVcoin()) {
                                NoMoreMoneyDialog(getContext(), "抱歉，无法查看，没有更多钻石了。");
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

    }

    @Override
    public void payFileSuccess(int fileID, int fileType, FileManageResults results) {
        changeMask(false, mPhotoList.get(mPhotoPosition), (PhotoView) mListImage.get(mPhotoPosition));
    }

    @Override
    public void payFileFail(String msg) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(this, toast);
    }
}

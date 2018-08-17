package com.huanmedia.videochat.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.widget.video.EmptyVideoPlayer;
import com.huanmedia.videochat.my.fragment.FileInfoEditPirceFragment;
import com.huanmedia.videochat.my.fragment.FileInfoEditTagFragment;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.File;

import butterknife.BindView;

public class FileInfoEditActivity extends BaseActivity {

    @BindView(R.id.fl_layout)
    FrameLayout mLayoutFragment;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.view_photo)
    PhotoView mPhotoView;
    @BindView(R.id.view_video)
    EmptyVideoPlayer mVideoView;

    private MenuItem menuItem;

    private String mFilePath;
    private int mFileType;

    private int mCurrentPosition;

    private FileInfoEditPirceFragment mPriceFragment;
    private FileInfoEditTagFragment mTagFragment;

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @SuppressLint("RestrictedApi")
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> {
            if (mCurrentPosition == 0) {
                finish();
            } else {
                mCurrentPosition--;
                menuItem.setTitle("下一步");
                addFragmentAndShow(R.id.fl_layout, mPriceFragment, mPriceFragment.TAG);
            }
        });
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_info_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_next:
                this.menuItem = item;
                if (mCurrentPosition == 0) {
                    mCurrentPosition++;
                    item.setTitle("完成");
                    addFragmentAndShow(R.id.fl_layout, mTagFragment, mTagFragment.TAG);
                } else {
                    int price = mPriceFragment.getPrice();
                    String tag = mTagFragment.getTagStr();
                    if (tag == null || tag.equals("")) {
                        ToastUtils.showToastShortInCenter(this, "还没有设置标签");
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("FilePrice", price);
                    intent.putExtra("FileTag", tag);
                    intent.putExtra("FilePath", mFilePath);
                    intent.putExtra("FileType", mFileType);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_info_edit;
    }

    public static Intent getCallingIntent(Context context, int fileType, String fileurl, int resultCode) {
        Intent intent = new Intent(context, FileInfoEditActivity.class);
        intent.putExtra("FilePath", fileurl);
        intent.putExtra("FileType", fileType);
        return intent;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initToolbar();
        mFilePath = getIntent().getStringExtra("FilePath");
        mFileType = getIntent().getIntExtra("FileType", 0);

        mPriceFragment = (FileInfoEditPirceFragment) FileInfoEditPirceFragment.newInstance(mFilePath, mFileType);
        mTagFragment = (FileInfoEditTagFragment) FileInfoEditTagFragment.newInstance(mFilePath, mFileType);

        mCurrentPosition = 0;
        addFragmentAndShow(R.id.fl_layout, mPriceFragment, mPriceFragment.TAG);


        if (mFileType == 1) {
            mVideoView.setVisibility(View.GONE);
            mPhotoView.setVisibility(View.VISIBLE);
            mPhotoView.setImageURI(Uri.parse(mFilePath));
        } else if (mFileType == 2) {
            mVideoView.setVisibility(View.VISIBLE);
            mPhotoView.setVisibility(View.GONE);
            mVideoView.setUp(mFilePath, true, new File(mFilePath), "");
            mVideoView.setLooping(true);
            mVideoView.startPlayLogic();
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}

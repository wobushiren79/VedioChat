package com.huanmedia.videochat.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.presenter.file.FileHandlerPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileHandlerPresenter;
import com.huanmedia.videochat.mvp.presenter.video.IUserVideoListPresenter;
import com.huanmedia.videochat.mvp.presenter.video.UserVideoListPresenterImpl;
import com.huanmedia.videochat.mvp.view.file.IFileHandlerView;
import com.huanmedia.videochat.mvp.view.video.IUserVideoListView;
import com.huanmedia.videochat.my.UserInfoEditActivity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class MediaUpLoadActivity extends BaseActivity implements IFileHandlerView, IUserVideoListView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.media_upload_rv)
    RecyclerView mMediaUpLoadRV;
    @BindView(R.id.tv_delete)
    TextView mTVDelete;

    private MediaUpLoadAdapter mUpLoadAdapter;
    private List<VideoEntity> mListVideoData;

    private IFileHandlerPresenter mFileHandlerPresenter;
    private IUserVideoListPresenter mVideoListPresenter;

    @UpLoadType
    private int mUpLoadType;


    @Retention(RetentionPolicy.SOURCE)
    public @interface UpLoadType {
        int ALL = 0;
        int NORMAL = 1;
        int SECRET = 2;
    }

    public static Intent getCallingIntent(Context context, @UpLoadType int uploadType, ArrayList<VideoEntity> videos, boolean isOpenUserEdit) {
        Intent intent = new Intent(context, MediaUpLoadActivity.class);
        intent.putParcelableArrayListExtra("videos", videos);
        intent.putExtra("uploadType", uploadType);
        intent.putExtra("isOpenUserEdit", isOpenUserEdit);
        intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        if (getIntent().getBooleanExtra("isOpenUserEdit", false)) {
            if (mUpLoadAdapter.isHasUpLoadTask()) {
                Intent intent = new Intent(this, UserInfoEditActivity.class);
                intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar();
        mUpLoadType = getIntent().getIntExtra("uploadType", 0);
        mMediaUpLoadRV.setLayoutManager(new GridLayoutManager(this, 3));
        mMediaUpLoadRV.setItemAnimator(null);
    }

    @Override
    protected void initData() {
        super.initData();
        mFileHandlerPresenter = new FileHandlerPresenterImpl(this);
        mVideoListPresenter = new UserVideoListPresenterImpl(this);
        mUpLoadAdapter = new MediaUpLoadAdapter(this);
        mUpLoadAdapter.setUploadType(mUpLoadType);
        mMediaUpLoadRV.setAdapter(mUpLoadAdapter);

        if (mUpLoadType == UpLoadType.SECRET) {
            mToolbar.setTitle("私密视频");
            mVideoListPresenter.getSecretVideo();
        } else {
            mListVideoData = getIntent().getParcelableArrayListExtra("videos");
            if (mListVideoData == null)
                mListVideoData = new ArrayList<>();

            VideoEntity addItem = new VideoEntity();
            addItem.setUploadStatus(-1);

            mListVideoData.add(addItem);
            mUpLoadAdapter.setData(mListVideoData);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_up_load;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.video_menu_edit:
                if (item.getTitle().toString().equals("编辑")) {
                    item.setTitle("取消");
                    mTVDelete.setVisibility(View.VISIBLE);
                } else {
                    item.setTitle("编辑");
                    mTVDelete.setVisibility(View.GONE);
                }
                mUpLoadAdapter.changeItemMode();
                break;
        }
        return true;
    }

    private VideoInfoRequest tempVideoInfo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == 1 || requestCode == 2) {
                Uri uri = data.getData();
                tempVideoInfo = mFileHandlerPresenter.getVideoInfoByUri(uri, this.getContentResolver());
                if (tempVideoInfo.getImagePath() == null) {
                    ToastUtils.showToastLong(this, "请选择视频文件！");
                    return;
                }
                if (requestCode == 1)
                    mUpLoadAdapter.setUpLoadVideoFile(tempVideoInfo);
                else if (requestCode == 2)
                    getNavigator().navtoFileInfoEdit(this, 2, tempVideoInfo.getVideoPath(), 3);
            } else if (requestCode == 3) {
                String fileTag = data.getStringExtra("FileTag");
                String filePath = data.getStringExtra("FilePath");
                int price = data.getIntExtra("FilePrice", 0);
                mUpLoadAdapter.setSercetData(price, fileTag);
                mUpLoadAdapter.setUpLoadVideoFile(tempVideoInfo);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLong(this, toast);
    }

    @OnClick({R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                mUpLoadAdapter.deleteUserVideo();
                break;
        }
    }

    //---------------------用户视频列表----------------------
    @Override
    public void getUserVideoListSuccess(List<VideoEntity> listData) {
        mListVideoData = listData;
        if (mListVideoData == null)
            mListVideoData = new ArrayList<>();

        VideoEntity addItem = new VideoEntity();
        addItem.setUploadStatus(-1);

        mListVideoData.add(addItem);
        mUpLoadAdapter.setData(mListVideoData);
    }

    @Override
    public void getUserVideoListFail(String msg) {

    }
}

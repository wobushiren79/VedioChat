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
import com.huanmedia.videochat.mvp.view.file.IFileHandlerView;
import com.huanmedia.videochat.my.UserInfoEditActivity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class MediaUpLoadActivity extends BaseActivity implements IFileHandlerView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.media_upload_rv)
    RecyclerView mMediaUpLoadRV;
    @BindView(R.id.tv_delete)
    TextView mTVDelete;

    private MediaUpLoadAdapter mUpLoadAdapter;
    private List<VideoEntity> mListVideoData;

    private IFileHandlerPresenter mFileHandlerPresenter;


    public static Intent getCallingIntent(Context context, ArrayList<VideoEntity> videos) {
        Intent intent = new Intent(context, MediaUpLoadActivity.class);
        intent.putParcelableArrayListExtra("videos", videos);
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
        if (mUpLoadAdapter.isHasUpLoadTask()) {
            Intent intent = new Intent(this, UserInfoEditActivity.class);
            intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else {
            finish();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar();
        mMediaUpLoadRV.setLayoutManager(new GridLayoutManager(this, 3));
        mMediaUpLoadRV.setItemAnimator(null);
    }

    @Override
    protected void initData() {
        super.initData();
        mFileHandlerPresenter = new FileHandlerPresenterImpl(this);
        mUpLoadAdapter = new MediaUpLoadAdapter(this);
        mMediaUpLoadRV.setAdapter(mUpLoadAdapter);

        mListVideoData = getIntent().getParcelableArrayListExtra("videos");
        if (mListVideoData == null)
            mListVideoData = new ArrayList<>();

        VideoEntity addItem = new VideoEntity();
        addItem.setUploadStatus(-1);

        mListVideoData.add(addItem);

        mUpLoadAdapter.setData(mListVideoData);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            VideoInfoRequest videoInfoRequest = mFileHandlerPresenter.getVideoInfoByUri(uri, this.getContentResolver());
            if(videoInfoRequest.getImagePath()==null)
            {
                ToastUtils.showToastLong(this,"请选择视频文件！");
                return;
            }
            mUpLoadAdapter.setUpLoadVideoFile(videoInfoRequest);
            super.onActivityResult(requestCode, resultCode, data);
        }
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
}

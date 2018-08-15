package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.my.fragment.FileInfoEditPirceFragment;

import butterknife.BindView;

public class FileInfoEditActivity extends BaseActivity {

    @BindView(R.id.fl_layout)
    FrameLayout mLayoutFragment;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Fragment[] mListFragment;
    private String mFilePath;
    private int mFileType;

    private int mCurrentPosition;

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> {
            if (mCurrentPosition == 0) {
                finish();
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
                break;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_info_edit;
    }

    public static Intent getCallingIntent(Context context, int fileType, String fileurl) {
        Intent intent = new Intent(context, FileInfoEditActivity.class);
        intent.putExtra("fileUrl", fileurl);
        intent.putExtra("fileType", fileType);
        return intent;
    }

    @Override
    protected void initView() {
        initToolbar();
        mFilePath = getIntent().getStringExtra("fileUrl");
        mFileType = getIntent().getIntExtra("fileType", 0);
        mListFragment = new Fragment[]{
                FileInfoEditPirceFragment.newInstance(mFilePath, mFileType)};
        mCurrentPosition = 0;
        addFragmentAndShow(R.id.fl_layout, mListFragment[0], ((FileInfoEditPirceFragment) mListFragment[0]).TAG);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}

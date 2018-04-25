/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.huanmedia.hmalbumlib.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.hmalbumlib.R;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.hmalbumlib.ui.adapter.HM_PhotoSeeAdapter;
import com.huanmedia.hmalbumlib.ui.widget.HM_HackyViewPager;

import java.util.ArrayList;
import java.util.Locale;

public class HM_PhotoSeePagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener, HM_PhotoSeeAdapter.OnPhotoListener, View.OnClickListener {

    HM_HackyViewPager photoViewPager;
    TextView photoViewTvIndicator;
    RelativeLayout photoViewRlBottomContent;
    private HM_PhotoSeeAdapter adapter;
    private ArrayList<HM_PhotoEntity> list;
    private int currentSelect;


    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        photoViewPager = (HM_HackyViewPager) findViewById(R.id.photo_view_pager);
        photoViewTvIndicator = (TextView)findViewById(R.id.photo_view_tv_indicator);
        photoViewRlBottomContent= (RelativeLayout) findViewById(R.id.photo_view_rl_bottom_content);
        photoViewRlBottomContent.setOnClickListener(this);
        setToolbar();
        currentSelect = getIntent().getIntExtra(HM_StartSeePhoto.EXTRA_PHOTOSEE_CURRENTSELECT, 0);
        if (getIntent().hasExtra(HM_StartSeePhoto.EXTRA_PHOTOSEE_DATA)) {
            list = getIntent().getParcelableArrayListExtra(HM_StartSeePhoto.EXTRA_PHOTOSEE_DATA);
            photoViewTvIndicator.setText(String.format(Locale.getDefault(), "%d/%d", currentSelect + 1, list.size()));
            adapter = new HM_PhotoSeeAdapter(this, list);
            adapter.setOnPhotoListener(this);
            photoViewPager.setAdapter(adapter);
            photoViewPager.addOnPageChangeListener(this);
            photoViewPager.setCurrentItem(currentSelect);
            //如果选择的是第一张照片可能会无法选中CheckBox 手动设置
        }
        animTitleBar(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.hm_album_photosee;
    }
    @Override
    protected View getTitlebarView() {
        return findViewById(R.id.toolbar);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onPageSelected(int position) {
        photoViewTvIndicator.setText(String.format("%d/%d", position + 1, adapter.getCount()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void photoClick(View v) {
            finish();
    }

    @SuppressLint("RestrictedApi")
    private void animTitleBar(boolean isShow) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            ActionBar actionbar = getSupportActionBar();
            assert actionbar != null;
            actionbar.setShowHideAnimationEnabled(true);
            if (isShow) {
                actionbar.show();
            } else {
                actionbar.hide();
            }
        } else {
//            mActionLayout.animate().alpha(isShow ? 0.8f : 0).setDuration(200);
            if (isShow) {
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
//        photoViewRlBottomContent.animate().alpha(isShow ? 1 : 0).setDuration(200);
    }


    public int getCurrentPage() {
        return photoViewPager.getCurrentItem();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

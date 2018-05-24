package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentFragmentAdapter;
import com.huanmedia.videochat.appointment.fragment.AppointmentListFragment;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;

import butterknife.BindView;

public class AppointmentListActivity extends BaseActivity implements OnTabSelectListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout mTitleTab;
    @BindView(R.id.view_pager)
    ViewPager mVPList;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AppointmentListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initView() {
        initToolbar();
        mVPList.addOnPageChangeListener(this);
        mTitleTab.setOnTabSelectListener(this);
    }

    @Override
    protected void initData() {
        int starButton = UserManager.getInstance().getCurrentUser().getUserinfo().getStarbutton();
        int isStarauth = UserManager.getInstance().getCurrentUser().getUserinfo().getIsstarauth();
        String[] titles;
        Fragment[] fragments;
        if (isStarauth == 1 && starButton == 1) {
            titles = new String[]{ "被预约","预约"};
            AppointmentListFragment readmanAppointment = new AppointmentListFragment();
            readmanAppointment.setUserType(AppointmentListFragment.UserType.READMAN);
            AppointmentListFragment normalAppointment = new AppointmentListFragment();
            normalAppointment.setUserType(AppointmentListFragment.UserType.NORMAL);
            fragments = new Fragment[]{readmanAppointment,normalAppointment};
        } else {
            titles = new String[]{"预约"};
            AppointmentListFragment normalAppointment = new AppointmentListFragment();
            normalAppointment.setUserType(AppointmentListFragment.UserType.NORMAL);
            fragments = new Fragment[]{normalAppointment};
        }

        mVPList.setAdapter(new AppointmentFragmentAdapter(getSupportFragmentManager(), fragments));
        mTitleTab.setViewPager(mVPList, titles);
        this.onTabSelect(0);
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_list;
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

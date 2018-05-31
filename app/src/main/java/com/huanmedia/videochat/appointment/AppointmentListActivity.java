package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.pay.MyWalletFragment;

import java.util.Calendar;

import butterknife.BindView;

public class AppointmentListActivity extends BaseActivity implements OnTabSelectListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout mTitleTab;
    @BindView(R.id.view_pager)
    ViewPager mVPList;

    AppointmentListFragment[] fragments;
    private String mCurrentDate = "";

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AppointmentListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentDate = getDefDay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_particulars_menu, menu);
        return true;
    }

    /**
     * 获取默认日期
     *
     * @return
     */
    private String getDefDay() {
        //默认值
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);
        return currentYear + "-" + (currentMonth + 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_particulars_menu_time_filter://时间筛选
                DialogPick dialogPick = new DialogPick(this);
                dialogPick.setDatelistener(obj -> {
                    mCurrentDate = obj;
                    String[] data = mCurrentDate.split("-");
                    if (data != null && data.length == 2) {
                        for (AppointmentListFragment itemFragment : fragments) {
                            itemFragment.setfiltrateData(Integer.valueOf(data[0]), Integer.valueOf(data[1]));
                            itemFragment.refreshData();
                        }
                    }
                });
                dialogPick.showDateFilter(mCurrentDate, "%d-%d");
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (isStarauth == 1 && starButton == 1) {
            titles = new String[]{"预约订单", "我的预约"};
            AppointmentListFragment mReadManAppointment = new AppointmentListFragment();
            mReadManAppointment.setUserType(AppointmentListFragment.UserType.READMAN);
            AppointmentListFragment mNormalAppointment = new AppointmentListFragment();
            mNormalAppointment.setUserType(AppointmentListFragment.UserType.NORMAL);
            fragments = new AppointmentListFragment[]{mReadManAppointment, mNormalAppointment};
        } else {
            titles = new String[]{"我的预约"};
            AppointmentListFragment mNormalAppointment = new AppointmentListFragment();
            mNormalAppointment.setUserType(AppointmentListFragment.UserType.NORMAL);
            fragments = new AppointmentListFragment[]{mNormalAppointment};
        }

        mVPList.setAdapter(new AppointmentFragmentAdapter(getSupportFragmentManager(), fragments));
        mVPList.setOffscreenPageLimit(2);
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

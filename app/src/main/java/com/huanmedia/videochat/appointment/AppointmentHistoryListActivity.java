package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentHistoryListAdapter;
import com.huanmedia.videochat.appointment.fragment.AppointmentListFragment;
import com.huanmedia.videochat.chat.ChatActivity;
import com.huanmedia.videochat.chat.bean.ChatIntentBean;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListOpResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentListPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListOpView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class AppointmentHistoryListActivity extends BaseActivity implements IAppointmentListOpView, PtrLayout.PtrHandleCallBack {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ptr_layout)
    PtrLayout mPtrLayout;

    private String mCurrentDate = "";
    private IAppointmentListPresenter mListPresenter;
    private AppointmentHistoryListAdapter mAdapter;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AppointmentHistoryListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_history_list;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_particulars_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_particulars_menu_time_filter://时间筛选
                DialogPick dialogPick = new DialogPick(this);
                dialogPick.setDatelistener(obj -> {
                    mCurrentDate = obj;
                    mPtrLayout.setRefresh();
                });
                dialogPick.showDateFilter(mCurrentDate, "%d-%d");
                break;
        }
        return super.onOptionsItemSelected(item);
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
    protected void initView() {
        initToolbar();
        mPtrLayout.setCallBack(this);
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListPresenter = new AppointmentListPresenterImpl(this);
        mAdapter = new AppointmentHistoryListAdapter(this, layoutManager);
        mPtrLayout.setAdapter(mAdapter);
        mPtrLayout.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mPtrLayout.setLayoutManager(layoutManager);
        RxCountDown.delay2(500).subscribe(integer -> {
            mPtrLayout.setRefresh();
            mCurrentDate = getDefDay();
        });
    }

    //--------------------列表处理----------------------------------
    @Override
    public void getAppointmentListSuccess(AppointmentListOpResults results) {

    }

    @Override
    public void getAppointmentListFail(String msg) {
        mPtrLayout.setRefreshComplete();
    }

    @Override
    public int getAppointmentListPage() {
        return mPtrLayout.getPage();
    }

    @Override
    public int getAppointmentListPageSize() {
        return mPtrLayout.getPageSize();
    }

    @Override
    public int getAppointmentListYear() {
        if (mCurrentDate == null || mCurrentDate.length() == 0)
            return 0;
        String[] data = mCurrentDate.split("-");
        return Integer.valueOf(data[0]);
    }

    @Override
    public int getAppointmentListMonth() {
        if (mCurrentDate == null || mCurrentDate.length() == 0)
            return 0;
        String[] data = mCurrentDate.split("-");
        return Integer.valueOf(data[1]);
    }

    @Override
    public String getAppointmentListStatus() {
        return "0,1,2,-1,-2,-3";
    }

    @Override
    public void setAppointmentListData(AppointmentListOpResults results, List<AppointmentDataOpResults> listData) {
        mPtrLayout.setListDataResults(results, listData);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(this, toast);
    }

    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
        mListPresenter.getAppointmentListOp();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mListPresenter.getAppointmentListOp();
    }
}

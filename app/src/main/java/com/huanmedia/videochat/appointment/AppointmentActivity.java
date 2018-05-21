package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentListApdater;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.ScrollLinearLayoutManager;
import com.huanmedia.videochat.common.widget.ScrollRecyclerView;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.presenter.user.AppointmentUserInfoPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.user.IAppointmentUserInfoPresenter;
import com.huanmedia.videochat.mvp.view.user.IAppointmentUserInfoView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.transform.BlurTransformation;

public class AppointmentActivity extends BaseActivity implements IAppointmentUserInfoView {
    @BindView(R.id.rl_title_layout)
    RelativeLayout mTitleLayout;
    @BindView(R.id.iv_title_back)
    ImageView mIVTitleBack;
    @BindView(R.id.tv_name)
    TextView mTVName;
    @BindView(R.id.tv_charge)
    TextView mTVCharge;
    @BindView(R.id.tv_online_time)
    TextView mTVOnlineTime;
    @BindView(R.id.tv_deposit)
    TextView mTVDeposit;
    @BindView(R.id.tv_timeselect_day)
    TextView mTVTimeSelectDay;
    @BindView(R.id.iv_icon)
    RoundedImageView mIVIcon;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingBar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_appointment_layout)
    LinearLayout mLLAppointmentLayout;
    @BindView(R.id.ll_select_min_layout)
    LinearLayout mLLSelectMinLayout;
    @BindView(R.id.ll_select_day_layout)
    LinearLayout mLLSelectDayLayout;

    private int mUid;
    private AppointmentUserInfoResults mUserData;
    private AppointmentListApdater mAppointmentApdater;
    private IAppointmentUserInfoPresenter mAppointmentUserInfoPresenter;

    public static Intent getCallingIntent(Context context, int uid) {
        Intent intent = new Intent(context, AppointmentActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment;
    }

    @Override
    protected void initView() {
        initToolbar();
        mTitleLayout.setPadding(0, DisplayUtil.getStatusBarHeight(this), 0, 0);
    }

    @Override
    protected void initData() {
        super.initData();
        mUid = getIntent().getIntExtra("uid", 0);
        mAppointmentUserInfoPresenter = new AppointmentUserInfoPresenterImpl(this);
        mAppointmentUserInfoPresenter.getAppointmentInfo(mUid);

        mAppointmentApdater = new AppointmentListApdater(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppointmentApdater);

        mTVTimeSelectDay.setText(getDefDay());

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //展开状态
                    mCollapsingBar.setTitle("");
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //折叠状态
                    if (mUserData != null) {
                        mCollapsingBar.setTitle(mUserData.getBase().getNickname());
                    } else {
                        mCollapsingBar.setTitle("");
                    }
                } else {
                    //中间状态
                }
            }
        });
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void getAppointmentUserInfoSuccess(AppointmentUserInfoResults results) {
        mUserData = results;
    }

    @Override
    public void getAppointmentUserInfoFail(String msg) {
        showToast(msg);
    }

    @Override
    public void setAppointmentUserIcon(String photoUrl) {
        GlideApp.with(this).asBitmap()
                .load(photoUrl)
                .transform(new BlurTransformation(this, 23))// “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(mIVTitleBack);

        GlideApp.with(this).asBitmap()
                .load(photoUrl)
                .into(mIVIcon);
    }

    @Override
    public void setAppointmentUserName(String name) {
        mTVName.setText(name);
    }

    @Override
    public void setAppointmentUserCharge(String charge) {
        mTVCharge.setVisibility(View.VISIBLE);
        mTVCharge.setText(charge + "钻/分钟");
    }

    @Override
    public void setAppointmentUserOnlineTime(String timeStatus, String time) {
        mTVOnlineTime.setText(timeStatus + " " + time);
    }

    @Override
    public void setAppointmentDeposit(String deposit) {
        mTVDeposit.setText(deposit + "钻石");
    }

    @Override
    public void setAppointmentListData(List<AppointmentDataResults> listData) {
        if (listData == null || listData.size() == 0) {
            mLLAppointmentLayout.setVisibility(View.GONE);
        } else {
            mAppointmentApdater.setData(listData);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dimen_78dp) * listData.size());
            mRecyclerView.setLayoutParams(layoutParams);
            mAppointmentApdater.notifyDataSetChanged();
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

    @OnClick({R.id.ll_select_min_layout, R.id.ll_select_day_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_min_layout:
                showDialogPickForMin();
                break;
            case R.id.ll_select_day_layout:
                showDialogPickForDay();
                break;
        }
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
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        return currentYear + "-" + (currentMonth + 1) + "-" + currentDay;
    }

    /**
     * 展示天数选择框
     */
    private void showDialogPickForDay() {
        DialogPick pick = new DialogPick(this.getContext());
        pick.setDatelistener(obj -> {
            mTVTimeSelectDay.setText(obj);
        });
        pick.showPickerDate("日期选择", getDefDay(), "%s-%s-%s");
    }

    /**
     * 展示小时选择框
     */
    private void showDialogPickForMin() {
        DialogPick pick = new DialogPick(this.getContext());
        pick.setDatelistener(obj -> {
            mTVTimeSelectDay.setText(obj);
        });
        pick.showPickerDate("日期选择", getDefDay(), "%s-%s-%s");
    }
}

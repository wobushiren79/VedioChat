package com.huanmedia.videochat.appointment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentListAdapter;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentSubmitPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentUserInfoPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentSubmitPresenter;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentUserInfoPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentSubmitView;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentUserInfoView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.transform.BlurTransformation;

public class AppointmentOpActivity extends BaseActivity implements IAppointmentUserInfoView, IAppointmentSubmitView, TextWatcher {
    @BindView(R.id.rl_title_layout)
    RelativeLayout mTitleLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_appointment_layout)
    LinearLayout mLLAppointmentLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingBar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBar;
    @BindView(R.id.iv_title_back)
    ImageView mIVTitleBack;
    @BindView(R.id.iv_icon)
    RoundedImageView mIVIcon;
    @BindView(R.id.tv_name)
    TextView mTVName;
    @BindView(R.id.tv_charge)
    TextView mTVCharge;
    @BindView(R.id.tv_online_time)
    TextView mTVOnlineTime;
    @BindView(R.id.tv_appointment_valid_time)
    TextView mTVValidTime;
    @BindView(R.id.iv_number_add)
    ImageView mIVNumberAdd;
    @BindView(R.id.iv_number_remove)
    ImageView mIVNumberRemove;
    @BindView(R.id.et_min_appointment_call_time)
    EditText mETCallTime;
    @BindView(R.id.et_appointment_msg)
    EditText mETAppointmentMsg;
    @BindView(R.id.tv_price_unit)
    TextView mTVPriceUnit;
    @BindView(R.id.tv_price_total)
    TextView mTVPriceTotal;
    @BindView(R.id.tv_submit)
    TextView mTVSubmit;

    private int mUid;
    private int mMinCallTime;
    private int mUnitPrice;
    private String mHintMsg;
    private IAppointmentUserInfoPresenter mUserInfoPresenter;
    private IAppointmentSubmitPresenter mSubmitPresenter;
    private AppointmentUserInfoResults mUserData;
    private AppointmentListAdapter mAppointmentApdater;

    public static Intent getCallingIntent(Context context, int uid) {
        Intent intent = new Intent(context, AppointmentOpActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected void initView() {
        initToolbar();
        mTitleLayout.setPadding(0, DisplayUtil.getStatusBarHeight(this), 0, 0);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected void initData() {
        super.initData();
        mUid = getIntent().getIntExtra("uid", 0);
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
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
        });
        mETCallTime.addTextChangedListener(this);
        mAppointmentApdater = new AppointmentListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppointmentApdater);

        mSubmitPresenter = new AppointmentSubmitPresenterImpl(this);
        mUserInfoPresenter = new AppointmentUserInfoPresenterImpl(this);
        mUserInfoPresenter.getAppointmentInfoOp(mUid);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_op;
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
    public void setAppointmentUserCharge(int charge) {
        mUnitPrice = charge;
        mTVCharge.setVisibility(View.VISIBLE);
        mTVCharge.setText(charge + "钻/分钟");
        mTVPriceUnit.setText(charge + "钻/分钟");
    }

    @Override
    public void setAppointmentUserOnlineTime(String timeStatus, String time) {
        mTVOnlineTime.setText(timeStatus + " " + time);
    }

    @Override
    public void setAppointmentDeposit(String deposit) {

    }


    @Override
    public void setAppointmentListData(List<AppointmentDataResults> listData) {
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) mAppBar.getChildAt(0).getLayoutParams();
        mParams.setScrollFlags
                (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
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
    public void setValidAppointmentTime(int hours) {
        mTVValidTime.setText(hours + "小时以内");
    }

    @Override
    public void setMinAppointmentCallTime(int min) {
        mMinCallTime = min;
        mETCallTime.setText(min + "");
    }

    @Override
    public void setDefHintMsg(String msg) {
        mHintMsg = msg;
        mETAppointmentMsg.setHint(msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(this, toast);
    }

    @OnClick({R.id.iv_number_remove, R.id.iv_number_add, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_number_remove:
                numberChange(-1);
                break;
            case R.id.iv_number_add:
                numberChange(1);
                break;
            case R.id.tv_submit:
                mSubmitPresenter.submitAppointmentOp();
                break;
        }
    }


    private void numberChange(int number) {
        String numberStr = mETCallTime.getText().toString();
        int nowNumber;
        try {
            nowNumber = Integer.valueOf(numberStr);
        } catch (Exception e) {
            showToast("时长格式错误");
            return;
        }
        if ((nowNumber + number) < mMinCallTime) {
            number = 0;
            nowNumber = mMinCallTime;
        }
        mETCallTime.setText((number + nowNumber) + "");
    }

    //-------------------价格变化监听--------------------
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String number = editable.toString();
        int nowNumber;
        try {
            nowNumber = Integer.valueOf(number);
        } catch (Exception e) {
            mIVNumberRemove.setImageResource(R.drawable.btn_number_remove_style_1);
            return;
        }
        if (nowNumber == mMinCallTime) {
            mIVNumberRemove.setImageResource(R.drawable.btn_number_remove_style_1);
        } else if (nowNumber > mMinCallTime) {
            mIVNumberRemove.setImageResource(R.drawable.btn_number_remove_style_2);
        } else {
            mIVNumberRemove.setImageResource(R.drawable.btn_number_remove_style_1);
        }
        mTVPriceTotal.setText(nowNumber * mUnitPrice + "钻");
        if (nowNumber > 9999) {
            mETCallTime.setText("9999");
        }
    }

    //-----------------提交预约----------------
    @Override
    public void submitAppointmentSuccess(AppointmentUserInfoResults data) {
        showToast("预约成功");
        finish();
    }

    @Override
    public void submitAppointmentFail(String msg) {
        showToast(msg);
    }

    @Override
    public int getAppointmentUid() {
        return mUid;
    }

    @Override
    public String getAppointmentDate() {
        return null;
    }

    @Override
    public String getAppointmentTime() {
        return null;
    }

    @Override
    public int getAppointmentCallTime() {
        int callTime;
        try {
            callTime = Integer.valueOf(mETCallTime.getText().toString());
        } catch (Exception e) {
            return 0;
        }
        return callTime;
    }

    @Override
    public int getAppointmentPrice() {
        return getAppointmentCallTime() * mUnitPrice;
    }

    @Override
    public String getAppointmentMsg() {
        String msg = mETAppointmentMsg.getText().toString();
        if (msg == null || msg.length() == 0) {
            msg = mHintMsg;
        }
        return msg;
    }

    @Override
    public int getMinCallTime() {
        return mMinCallTime;
    }
}

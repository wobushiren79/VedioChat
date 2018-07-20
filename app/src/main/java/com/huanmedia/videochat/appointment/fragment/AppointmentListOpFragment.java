package com.huanmedia.videochat.appointment.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentListOpAdapter;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.AppointmentDataOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListOpResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentListPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListOpView;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class AppointmentListOpFragment extends BaseFragment implements IAppointmentListOpView, PtrLayout.PtrHandleCallBack {
    @BindView(R.id.ptr_layout)
    PtrLayout mPtrLayout;

    private AppointmentListOpAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private IAppointmentListPresenter mListPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_appointment_list_op;
    }

    public static AppointmentListOpFragment newInstance() {
        AppointmentListOpFragment fragment = new AppointmentListOpFragment();
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void initData() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AppointmentListOpAdapter(getContext(), mLayoutManager);
        mPtrLayout.setAdapter(mAdapter);
        mPtrLayout.setLayoutManager(mLayoutManager);
        mPtrLayout.setCallBack(this);
        mListPresenter = new AppointmentListPresenterImpl(this);
        RxCountDown.delay2(500).subscribe(integer -> {
            mPtrLayout.setRefresh();
        });
    }

    @Override
    public void getAppointmentListSuccess(AppointmentListOpResults results) {
        mPtrLayout.setRefreshComplete();
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
        return 0;
    }

    @Override
    public int getAppointmentListMonth() {
        return 0;
    }

    @Override
    public String getAppointmentListStatus() {
        return "0,1";
    }

    @Override
    public void setAppointmentListData(AppointmentListOpResults results, List<AppointmentDataOpResults> listData) {
        mPtrLayout.setListDataResults(results, listData);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
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

package com.huanmedia.videochat.appointment.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.appointment.adapter.AppointmentListPtrAdapter;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.ptr.PtrLayout;
import com.huanmedia.videochat.mvp.entity.results.AppointmentListResults;
import com.huanmedia.videochat.mvp.presenter.appointment.AppointmentListPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.appointment.IAppointmentListPresenter;
import com.huanmedia.videochat.mvp.view.appointment.IAppointmentListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class AppointmentListFragment extends BaseFragment implements IAppointmentListView {
    @BindView(R.id.ptr_layout)
    PtrLayout mPtrLayout;

    @Retention(RetentionPolicy.SOURCE)
    public @interface UserType {
        int NORMAL = 0;
        int READMAN = 1;
    }

    private @UserType
    int mUserType;
    private int page = 1;
    private int pageSize = 10;
    private int year = 0;
    private int month = 0;

    public void setUserType(int mUserType) {
        this.mUserType = mUserType;
    }

    private IAppointmentListPresenter mAppointmentListPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_appointment_list;
    }

    @Override
    protected void initView(View view) {
        mPtrLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mPtrLayout.setCallBack(new PtrLayout.PtrHandleCallBack() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (mAppointmentListPresenter == null)
                    return;
                if (mUserType == UserType.NORMAL) {
                    mAppointmentListPresenter.getAppointmentListForNormal();
                } else if (mUserType == UserType.READMAN) {
                    mAppointmentListPresenter.getAppointmentListForReadMan();
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mAppointmentListPresenter == null)
                    return;
                if (mUserType == UserType.NORMAL) {
                    mAppointmentListPresenter.getAppointmentListForNormal();
                } else if (mUserType == UserType.READMAN) {
                    mAppointmentListPresenter.getAppointmentListForReadMan();
                }
            }
        });
    }

    @Override
    protected void initData() {
        mAppointmentListPresenter = new AppointmentListPresenterImpl(this);
        AppointmentListPtrAdapter adapter = new AppointmentListPtrAdapter(getContext(), mUserType);
        mPtrLayout.setAdapter(adapter);

        RxCountDown.delay2(500).subscribe(integer -> {
            mPtrLayout.setRefresh();
        });
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mPtrLayout.setRefresh();
    }

    /**
     * 设置筛选条件
     *
     * @param year
     * @param month
     */
    public void setfiltrateData(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public void getAppointmentListForReadManSuccess(List<AppointmentListResults.Item> listData) {

    }

    @Override
    public void getAppointmentListForReadManFail(String msg) {
        showToast(msg);
    }

    @Override
    public void getAppointmentListForNormalSuccess(List<AppointmentListResults.Item> listData) {

    }

    @Override
    public void getAppointmentListForNormalFail(String msg) {
        showToast(msg);
    }

    @Override
    public int getPageForReadMan() {
        return page;
    }

    @Override
    public int getPageSizeForReadMan() {
        return pageSize;
    }

    @Override
    public int getPageForNormal() {
        return page;
    }

    @Override
    public int getPageSizeForNormal() {
        return pageSize;
    }

    @Override
    public int getFiltrateYear() {
        return year;
    }

    @Override
    public int getFiltrateMonth() {
        return month;
    }

    @Override
    public PtrLayout getPtrLayoutForReadMan() {
        return mPtrLayout;
    }

    @Override
    public PtrLayout getPtrLayoutForNormal() {
        return mPtrLayout;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShort(getContext(), toast);
    }
}

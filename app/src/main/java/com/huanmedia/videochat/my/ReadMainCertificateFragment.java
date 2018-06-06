package com.huanmedia.videochat.my;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.net.HostManager;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadMainCertificateFragment extends BaseMVPFragment<ReadMainCertificatePresenter> implements ReadMainCertificateView {


    private static String ARG_MSG = "msg";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rmc_btn_start)
    Button mRmcBtnStart;
    @BindView(R.id.rmc_tv_protocol_name)
    TextView mTVProtocol;
    private HintDialog mHintDialog;
    private HintDialog mLoadingDialog;

    public ReadMainCertificateFragment() {
        // Required empty public constructor
    }

    public static ReadMainCertificateFragment newInstance() {
        ReadMainCertificateFragment fragment = new ReadMainCertificateFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_MSG,msg);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
//        if (!Check.isEmpty(getArguments().getString(ARG_MSG))){
//            new MaterialDialog.Builder(context()).title("提示")
//                    .content(getArguments().getString(ARG_MSG))
//                    .positiveText("知道了")
//                    .positiveColorRes(R.color.base_yellow)
//                    .onPositive((dialog, which) ->{
//                        if (getActivity() == null) {
//                            getActivity().finish();
//                        }
//                    }).show();
//
//        }
        mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void statusBarConfig() {
        mImmersionBar = ImmersionBar.with(this);
        if (getTitlebarView() != null) {
            mImmersionBar.titleBar(getTitlebarView());
        }
        mImmersionBar
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .navigationBarWithKitkatEnable(false)
                .init();
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read_main_certificate;
    }


    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mLoadingDialog.show();
            mLoadingDialog.setTitleText(getString(R.string.loading));
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        flag = flag == 0 ? HintDialog.HintType.WARN : flag;
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(context(), flag);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(message);
        } else {
            if (mHintDialog.getType() != flag) {
                mHintDialog.setType(flag);
            }
            mHintDialog.setTitleText(message);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

    @OnClick({R.id.rmc_btn_start,R.id.rmc_tv_protocol_name})
    public void onClickView(View view) {
        switch (view.getId()){
            case R.id.rmc_btn_start:
                getBasePresenter().checkCompleteness();
                break;
            case R.id.rmc_tv_protocol_name:
                getNavigator().navtoWebActivity(getActivity(), HostManager.getHtmlUrl() + "wordh5/commitment.html", "用户协议");

                break;
        }

    }

    @Override
    public void showNext(String msg, boolean isCompleteness) {
        if (isCompleteness) {
            getNavigator().navtoRedMianAuth(getActivity());
        } else {
            getNavigator().navtoUserInfoEdit(getActivity(), true, msg);
        }
    }

}

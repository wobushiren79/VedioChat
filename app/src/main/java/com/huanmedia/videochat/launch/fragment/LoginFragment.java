package com.huanmedia.videochat.launch.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.ilibray.utils.InputMethodUtils;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.launch.StartActivity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.net.HostManager;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.net.DataResponse;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginFragment extends BaseMVPFragment<LoginPresenter> implements LoginView {
    public static final String TAG = "/LoginFragment";
    @BindView(R.id.login_et_phone)
    EditText mLoginEtPhone;
    @BindView(R.id.login_et_smscode)
    EditText mLoginEtSmscode;
    @BindView(R.id.login_tv_smscode)
    TextView mLoginTvSmscode;
    @BindView(R.id.login_tv_errorhint_layout)
    LinearLayout mLoginTvErrorhintLayout;
    @BindView(R.id.login_tv_errorhint)
    TextView mLoginTvErrorhint;
    @BindView(R.id.login_tv_loginbtn)
    Button mLoginTvLoginbtn;
    @BindView(R.id.login_tv_firsttosee)
    TextView mLoginTvFirsttosee;
    @BindView(R.id.login_tv_protocol)
    TextView mLoginTvProtocol;
    private final int RC_LOCATION = 1;
    private HintDialog mHintDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view) {
        if (BuildConfig.netWorkType != 3) {
            mLoginTvLoginbtn.setOnLongClickListener(v -> {
                new MaterialDialog.Builder(context())
                        .title("网络选择")
                        .items(R.array.debug_network)
                        .itemsCallback((dialog, itemView, position, text) -> {
                            int netType = 0;
                            switch (text.toString()) {
                                case "本地测试":
                                    netType = 1;
                                    break;
                                case "开发环境":
                                    netType = 2;
                                    break;
                                case "正式环境":
                                    netType = 3;
                                    break;
                            }
                            if (netType != 0)
                                HostManager.setNetWorkType(netType);
                        })
                        .build().show();
                return true;
            });
        }
        mLoginTvFirsttosee.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mLoginEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    if (getBasePresenter().getTimeDown() == null || getBasePresenter().getTimeDown().isDisposed()) {
                        mLoginTvSmscode.setEnabled(true);
                    }
                } else {
                    mLoginTvSmscode.setEnabled(false);
                }
            }
        });
        Spanny spanny = new Spanny(mLoginTvProtocol.getText());
        spanny.findAndSpan("蒙面服务和隐私条款", new Spanny.GetSpan() {
            @Override
            public Object getSpan() {
                return new UnderlineSpan();
            }
        });
        mLoginTvProtocol.setText(spanny);
//        mLoginTvProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        mLoginTvProtocol.getPaint().setAntiAlias(true);//抗锯齿
        requestLocation();
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void requestLocation() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            EasyPermissions.requestPermissions(this, "1.萌面需要访问你的存储器，用于保存数据缓存\n2.萌面需要获取你的位置信息，为你推荐附近的好友",
                    RC_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onDestroyView() {
        Logger.i("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.i("onDetach");
    }

    @Override
    public void onDestroy() {
        Logger.i("onDestroy");
        super.onDestroy();
    }

    @OnClick({R.id.login_tv_smscode, R.id.login_tv_loginbtn, R.id.login_tv_firsttosee, R.id.login_tv_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_smscode:
                getBasePresenter().getSmsCode(mLoginTvSmscode, mLoginEtPhone.getText().toString());
                break;
            case R.id.login_tv_loginbtn:
                InputMethodUtils.hideSoftInput(getActivity());
                Map<String, String> prams = new HashMap<>();
                prams.put("mobile", mLoginEtPhone.getText().toString());
                prams.put("code", mLoginEtSmscode.getText().toString());
                getBasePresenter().login(prams);
                break;
            case R.id.login_tv_firsttosee:
                getNavigator().navToMain(getContext(), false);
                ActivitManager.getAppManager().finishAlikeActivity(StartActivity.class);
//                EventBus.getDefault().post(new Intent(EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+CompleteInformationFragment.TAG));
                break;
            case R.id.login_tv_protocol:
                getNavigator().navtoWebActiviyt(getActivity(), HostManager.getHtmlUrl() + "wordh5/agreement.html", "用户协议");
                break;
        }
    }


    @Override
    public void showLoading(String msg) {
        if (msg == null) msg = getString(R.string.loading);
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(context(), HintDialog.HintType.LOADING);
            mHintDialog.show();
            mHintDialog.setTitleText(msg);
        } else if (!mHintDialog.isShowing()) {
            mHintDialog.show();
            mHintDialog.setTitleText(msg);
        }
    }

    @Override
    public void hideLoading() {
        if (mHintDialog != null) {
            mHintDialog.dismiss();
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
        mLoginTvErrorhint.setText(Check.checkReplace(message));
        getBasePresenter().watiingGone(mLoginTvErrorhintLayout);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void loginSuccess(UserEntity userModle) {
        getNavigator().navToMain(getContext(), false);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void completeInfo(UserEntity userModle) {
        Intent intent = new Intent(EventBusAction.SCHEME_ACTION + "://" + EventBusAction.LUANCH_HOST + CompleteInformationFragment.TAG);
        intent.putExtra("user", (Parcelable) userModle);
        EventBus.getDefault().post(intent);
    }

    @Override
    public void smsCodeReturn(DataResponse dataResponse) {
        showError(0, "验证码已发送");
    }
}

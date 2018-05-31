package com.huanmedia.videochat.main2.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.BuildConfig;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import mvp.data.store.glide.GlideUtils;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MyFragment extends BaseMVPFragment<MyPresenter> implements MyView {
    @BindView(R.id.my_fm_iv_mail)
    ImageView mMyFmIvMail;
    @BindView(R.id.my_fm_iv_header)
    RoundedImageView mMyFmIvHeader;
    @BindView(R.id.my_fm_iv_sex)
    ImageView mMyFmIvSex;
    @BindView(R.id.my_fm_tv_name)
    TextView mMyFmTvName;
    @BindView(R.id.my_fm_iv_level)
    ImageView mMyFmIvLevel;
    @BindView(R.id.my_fm_tv_btn_data_editor)
    TextView mMyFmTvBtnDataEditor;
    @BindView(R.id.my_fm_iv_myAccount)
    ImageView mMyFmIvMyAccount;
    @BindView(R.id.my_fm_tv_lable_myAccount)
    TextView mMyFmTvLableMyAccount;
    @BindView(R.id.my_fm_tv_myMoney)
    TextView mMyFmTvMyMoney;
    @BindView(R.id.my_fm_rl_account)
    RelativeLayout mMyFmRlAccount;
    @BindView(R.id.my_fm_iv_readman)
    ImageView mMyFmIvReadman;
    @BindView(R.id.my_fm_tv_lable_readman)
    TextView mMyFmTvLableReadman;
    @BindView(R.id.my_fm_tv_readman)
    TextView mMyFmTvReadman;
    @BindView(R.id.my_fm_rl_readman)
    RelativeLayout mMyFmRlReadman;
    @BindView(R.id.my_fm_iv_trust)
    ImageView mMyFmIvTrust;
    @BindView(R.id.my_fm_tv_lable_trust)
    TextView mMyFmTvLableTrust;
    @BindView(R.id.my_fm_tv_trust)
    TextView mMyFmTvTrust;
    @BindView(R.id.my_fm_rl_trust)
    RelativeLayout mMyFmRlTrust;
    @BindView(R.id.my_fm_iv_generalize)
    ImageView mMyFmIvGeneralize;
    @BindView(R.id.my_fm_tv_lable_generalize)
    TextView mMyFmTvLableGeneralize;
    @BindView(R.id.my_fm_tv_generalize)
    TextView mMyFmTvGeneralize;
    @BindView(R.id.my_fm_rl_generalize)
    RelativeLayout mMyFmRlGeneralize;
    @BindView(R.id.my_fm_iv_setting)
    ImageView mMyFmIvSetting;
    @BindView(R.id.my_fm_tv_lable_setting)
    TextView mMyFmTvLableSetting;
    @BindView(R.id.my_fm_tv_setting)
    TextView mMyFmTvSetting;
    @BindView(R.id.my_fm_rl_setting)
    RelativeLayout mMyFmRlSetting;
    @BindView(R.id.my_fm_iv_help)
    ImageView mMyFmIvHelp;
    @BindView(R.id.my_fm_tv_lable_help)
    TextView mMyFmTvLableHelp;
    @BindView(R.id.my_fm_tv_help)
    TextView mMyFmTvHelp;
    @BindView(R.id.my_fm_rl_help)
    RelativeLayout mMyFmRlHelp;
    @BindView(R.id.my_fm_rl_appointment)
    RelativeLayout mAppointmentLayout;
    private MainInteractionListener mListener;
    private Badge mMsgBadeg;
//    private Badge mMyBadeg;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
//        setUserData(UserManager.getInstance().getCurrentUser().getUserinfo());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my2;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    protected void statusBarConfig() {
    }

//    @Override
//    protected void onVisible() {
//        super.onVisible();
//        if (mImmersionBar == null) {
//            mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(true);
//            mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
//        } else {
//            mImmersionBar.statusBarDarkFont(true).init();
//        }
//    }
//
//    @Override
//    protected void onInvisible() {
//        super.onInvisible();
//        if (mImmersionBar != null) {
//            mImmersionBar.statusBarDarkFont(false).init();
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainInteractionListener) {
            mListener = (MainInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upSysMsgNoRed(Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        switch (intent.getAction()) {
            case EventBusAction.ACTION_SYSTEM_MESSAGE:
                int msgCount = 0;
                msgCount = intent.getIntExtra("msgCount", 0);

                if (mMsgBadeg == null) {
                    mMsgBadeg = new QBadgeView(getContext())
                            .bindTarget(mMyFmIvMail)
                            .setBadgeGravity(Gravity.END | Gravity.TOP)
                            .setBadgePadding(0, false)
                            .setBadgeTextSize(8, true);
                }
                if (msgCount == 0) {
                    UserManager.getInstance().setNoRedMsg(0);
                    mMsgBadeg.setBadgeText("");
                } else {
                    mMsgBadeg.setBadgeText(" ");
                }
                break;
            case EventBusAction.ACTION_USERDATA_CHANGE://用户数据更新后更新主菜单数据
                setUserData(UserManager.getInstance().getCurrentUser().getUserinfo());
                break;
        }

    }


    @Override
    protected void initData() {
        if (UserManager.getInstance().islogin()) {
            UserEntity.UserinfoEntity userInfo = UserManager.getInstance().getCurrentUser().getUserinfo();
            setUserData(userInfo);
        }
    }

    /**
     * 本地用户数据发送改变
     *
     * @param userInfo
     */
    public void setUserData(UserEntity.UserinfoEntity userInfo) {
        Logger.i("用户数据更变:" + userInfo.toString());
        //初始化数据
        mMyFmTvName.setText(Check.checkReplace(userInfo.getNickname()));
        mMyFmIvLevel.getDrawable().setLevel(userInfo.getLevel());
        mMyFmIvLevel.setOnClickListener(v -> getNavigator().navtoLevel(getActivity()));

        GlideUtils.getInstance().loadBitmapNoAnim(this, userInfo.getUserphoto_thumb(), mMyFmIvHeader);
        int sexRes = userInfo.getSex() == 1 ? R.drawable.icon_focus_boy : R.drawable.icon_focus_girl;
        mMyFmIvSex.setImageDrawable(ContextCompat.getDrawable(context(), sexRes));

        if (userInfo.getIsstarauth() == 1 && userInfo.getStarbutton() == 1) {
            mMyFmRlTrust.setVisibility(View.VISIBLE);
        } else {
            mMyFmRlTrust.setVisibility(View.GONE);
        }


    }


    @OnClick({R.id.my_fm_iv_mail, R.id.my_fm_rl_readman,
            R.id.my_fm_tv_btn_data_editor, R.id.my_fm_iv_header,
            R.id.my_fm_rl_account, R.id.my_fm_rl_trust,
            R.id.my_fm_rl_generalize, R.id.my_fm_rl_setting,
            R.id.my_fm_rl_help, R.id.my_fm_rl_appointment})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_fm_iv_mail://系统消息
                intent = new Intent(EventBusAction.ACTION_SYSTEM_MESSAGE);
                intent.putExtra("msgCount", 0);
                upSysMsgNoRed(intent);
                EventBus.getDefault().post(intent);
                getNavigator().navtoSystemMessage(getActivity());
                break;
            case R.id.my_fm_rl_account://我的账户
                getNavigator().navtoMyWallet(getActivity());
                break;
            case R.id.my_fm_rl_readman://红人模式
                getNavigator().navtoReadMainCertificate(getActivity());
                break;
            case R.id.my_fm_rl_trust://信任值
                getNavigator().navtoTrustValue(getActivity());
                break;
            case R.id.my_fm_rl_generalize://推广
                intent = new Intent(EventBusAction.ACTION_SYSTEM_MESSAGE);
                intent.putExtra("msgCount", 0);
                EventBus.getDefault().post(intent);
                ToastUtils.showToastShortInCenter(getContext(), "功能开发中");
                break;
            case R.id.my_fm_rl_setting://设置
                getNavigator().navtoSetting(getActivity());
                break;
            case R.id.my_fm_rl_help://帮助
                getNavigator().navtoHelp(getActivity());
                break;
            case R.id.my_fm_iv_header://头像大图
                break;
            case R.id.my_fm_tv_btn_data_editor:
                getNavigator().navtoUserInfoEdit(getActivity(), false, null);
                break;
            case R.id.my_fm_rl_appointment:
                getNavigator().navtoAppointmentList(getActivity(),0);
                break;
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

}

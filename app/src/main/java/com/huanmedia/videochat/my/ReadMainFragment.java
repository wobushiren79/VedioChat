package com.huanmedia.videochat.my;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.InputMethodUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPFragment;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.UserEntity;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadMainFragment extends BaseMVPFragment<ReadMainPresenter> implements ReadMainView {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frm_tv_icon)
    TextView mFrmTvIcon;
    @BindView(R.id.frm_tv_callingUnit_enter)
    TextView mFrmTvCallingUnitEnter;
    @BindView(R.id.frm_cl_callingUnit)
    ConstraintLayout mFrmClCallingUnit;
    @BindView(R.id.frm_tv_otherAccount_enter)
    TextView mFrmTvOtherAccountEnter;
    @BindView(R.id.frm_cl_otherAccountUnit)
    ConstraintLayout mFrmClOtherAccountUnit;
    @BindView(R.id.frm_tv_QQAccount_enter)
    TextView mFrmTvQQAccountEnter;
    @BindView(R.id.frm_cl_QQAccount)
    ConstraintLayout mFrmClQQAccount;
    @BindView(R.id.frm_tv_WXAccount_enter)
    TextView mFrmTvWXAccountEnter;
    @BindView(R.id.frm_cl_WXAccount)
    ConstraintLayout mFrmClWXAccount;
    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;
    private DialogPick dialogPack;

    public ReadMainFragment() {
        // Required empty public constructor
    }

    public static ReadMainFragment newInstance() {
        ReadMainFragment fragment = new ReadMainFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_TYPE,position);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read_main;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void initView(View view) {
        mIsVisible = true;//控制数据加载 如果是false 将不会调用initData方法
        mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    protected void initData() {
        UserEntity.UserinfoEntity userInfo = UserManager.getInstance().getCurrentUser().getUserinfo();
        int price = userInfo.getStarcoin() / (userInfo.getStartime() == 0 ? 1 : userInfo.getStartime());
        mFrmTvIcon.setText(price + "");
        getBasePresenter().getReadMainConfig();
    }

    @Override
    protected void statusBarConfig() {
        mImmersionBar = ImmersionBar.with(this);
        if (getTitlebarView() != null) {
            mImmersionBar.titleBar(getTitlebarView());
        }
        mImmersionBar
                .statusBarDarkFont(false)
                .keyboardEnable(true)
                .navigationBarWithKitkatEnable(false)
                .init();
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
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
        return getActivity();
    }

    @OnClick({R.id.frm_cl_callingUnit, R.id.frm_cl_WXAccount, R.id.frm_cl_QQAccount, R.id.frm_cl_otherAccountUnit})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.frm_cl_callingUnit://聊天价格
                if (dialogPack == null)
                    dialogPack = new DialogPick(context());

                dialogPack.showPickerTimeMoney(getBasePresenter().getDiscoverDefaultMoney(), "设置发现聊天单价", "钻石/分钟", mFrmTvIcon.getText().toString());
                dialogPack.setReadmainListener((showStr, money) -> {
                    if (!showStr.equals(mFrmTvIcon.getText().toString())) {
                        getBasePresenter().setReadMain(1, money, 1);
                    }
                });
                break;
            case R.id.frm_cl_otherAccountUnit://私人账户查看费用
                if (dialogPack == null)
                    dialogPack = new DialogPick(context());

                dialogPack.showPickerTimeMoney(getBasePresenter().getAccoundDefaultMoney(), "设置查看私人账户单价", "钻石", mFrmTvOtherAccountEnter.getText().toString());
                dialogPack.setReadmainListener((showStr, money) -> {
                    if (!showStr.equals(mFrmTvOtherAccountEnter.getText().toString())) {
                        mFrmTvOtherAccountEnter.setText(showStr);
                        getBasePresenter().configPrice(null, null, money);
                    }
                });

                break;
            case R.id.frm_cl_QQAccount://QQ号码设置
                showQQDialog();
                break;
            case R.id.frm_cl_WXAccount://微信号码设置
                showWxDialog();
                break;
        }
    }

    private void showQQDialog() {
        View contentView = getLayoutInflater().inflate(R.layout.dialog_readmain_account, null, false);
        MaterialDialog dialog = new MaterialDialog.Builder(context())
                .customView(contentView, false)
                .show();
        TextView title = contentView.findViewById(R.id.dialog_readmain_account_tv_title);
        EditText editText = contentView.findViewById(R.id.dialog_readmain_account_et);
        TextView ok = contentView.findViewById(R.id.dialog_readmain_account_tv_ok);
        TextView cancle = contentView.findViewById(R.id.dialog_readmain_account_tv_cancle);
        title.setText("请输入QQ号");
        dialog.setOnDismissListener(dialog1 -> InputMethodUtils.hideSoftInput(getActivity()));
        ok.setOnClickListener(v -> {
            String price = editText.getText().toString();
            if (price.length() == 0) {
                return;
            } else {
                dialog.dismiss();
                if (!price.equals(mFrmTvQQAccountEnter.getText().toString())) {
                    mFrmTvQQAccountEnter.setText(price);
                    getBasePresenter().configPrice(null, price, 0);
                }
            }
        });
        cancle.setOnClickListener(v -> dialog.dismiss());
    }

    private void showWxDialog() {
        View contentView = getLayoutInflater().inflate(R.layout.dialog_readmain_account, null, false);
        MaterialDialog dialog = new MaterialDialog.Builder(context())
                .customView(contentView, false)
                .show();
        TextView title = contentView.findViewById(R.id.dialog_readmain_account_tv_title);
        EditText editText = contentView.findViewById(R.id.dialog_readmain_account_et);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        TextView ok = contentView.findViewById(R.id.dialog_readmain_account_tv_ok);
        TextView cancle = contentView.findViewById(R.id.dialog_readmain_account_tv_cancle);
        title.setText("请输入微信号");
        dialog.setOnDismissListener(dialog1 -> InputMethodUtils.hideSoftInput(getActivity()));
        ok.setOnClickListener(v -> {
            String price = editText.getText().toString();
            if (price.length() == 0) {
                return;
            } else {
                dialog.dismiss();
                if (!price.equals(mFrmTvWXAccountEnter.getText().toString())) {
                    mFrmTvWXAccountEnter.setText(price);
                    getBasePresenter().configPrice(price, null, 0);
                }
            }
        });
        cancle.setOnClickListener(v -> dialog.dismiss());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showReadMainConfig(Map<String, String> result) {
        mFrmTvOtherAccountEnter.setText(Check.checkReplace(result.get("price")));
        mFrmTvQQAccountEnter.setText(Check.checkReplace(result.get("qq")));
        mFrmTvWXAccountEnter.setText(Check.checkReplace(result.get("wechat")));
    }

    @Override
    public void setReadManChatPrice(String price) {
        mFrmTvIcon.setText(price);
        try {
            UserManager.getInstance().getCurrentUser().getUserinfo().setStarcoin(Integer.valueOf(price));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.main2.weight.MaskDialog;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.presenter.user.ContactUnLockInfoPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.user.IContactUnLockInfoPresenter;
import com.huanmedia.videochat.mvp.view.user.IContactUnLockView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CheckContactDialog extends Dialog implements View.OnClickListener, IContactUnLockView {
    private TextView mTVHintTitle;
    private TextView mTVShowQQ;
    private TextView mTVShowWechat;
    private TextView mTVSubmit;
    private TextView mTVCopyQQ;
    private TextView mTVCopyWechat;
    private ImageView mIVCancel;

    private long mReadManId;//红人ID

    private IContactUnLockInfoPresenter contactUnLockInfoPresenter;

    //解锁信息
    private ContactUnLockInfoResults contactUnLockInfo;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogStyle {
        //解锁           //锁住
        int UNLOCK = 1, LOCK = 0;
    }

    public CheckContactDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkcontact);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.CENTER;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }

    public void setReadManId(long mReadManId) {
        this.mReadManId = mReadManId;
    }

    private void initView() {
        mTVHintTitle = findViewById(R.id.tv_hint_title);
        mIVCancel = findViewById(R.id.iv_cancel);
        mTVShowQQ = findViewById(R.id.tv_show_qq);
        mTVShowWechat = findViewById(R.id.tv_show_wechat);
        mTVSubmit = findViewById(R.id.tv_submit);
        mTVCopyQQ = findViewById(R.id.tv_copy_qq);
        mTVCopyWechat = findViewById(R.id.tv_copy_wechat);

        mIVCancel.setOnClickListener(this);
        mTVCopyWechat.setOnClickListener(this);
        mTVCopyQQ.setOnClickListener(this);
        mTVSubmit.setOnClickListener(this);
    }

    private void initData() {
        contactUnLockInfoPresenter = new ContactUnLockInfoPresenterImpl(this);
        contactUnLockInfoPresenter.getContactUnLockInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == mIVCancel) {
            cancel();
        } else if (v == mTVCopyQQ || v == mTVCopyWechat) {
            clipText(v);
        } else if (v == mTVSubmit) {
            submit();
        }
    }

    /**
     * 提交
     */
    private void submit() {
        if (contactUnLockInfo == null) {
            mTVSubmit.setVisibility(View.GONE);
            return;
        }
        switch (contactUnLockInfo.getIspay()) {
            case DialogStyle.UNLOCK:
                cancel();
                break;
            case DialogStyle.LOCK:
                unlockContact();
                break;
        }
    }

    /**
     * 解锁
     */
    private void unlockContact() {
        if (contactUnLockInfo == null)
            return;
        if ((contactUnLockInfo.getQq() == null || contactUnLockInfo.getQq().length() == 0)
                && (contactUnLockInfo.getWechat() == null || contactUnLockInfo.getWechat().length() == 0)) {
            ToastUtils.showToastShort(getContext(), "对方未设置微信或QQ");
            this.cancel();
        } else {
            new MaterialDialog.Builder(getContext())
                    .backgroundColorRes(R.color.white)
                    .title("提示")
                    .titleColorRes(R.color.base_black)
                    .content("您即将花费" + contactUnLockInfo.getPrice() + "钻石获取对方的联系方式")
                    .contentColorRes(R.color.base_gray)
                    .negativeColorRes(R.color.base_gray)
                    .negativeText("再想想")
                    .positiveText("我是土豪")
                    .positiveColorRes(R.color.color_f65aa0)
                    .onPositive((dialog, which) -> {
                        contactUnLockInfoPresenter.unLockContact();
                    }).show();
        }

    }

    /**
     * 复制文字
     *
     * @param v
     */
    private void clipText(View v) {
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        String clipText = "";
        if (v == mTVCopyQQ) {
            clipText = mTVShowQQ.getText().toString();
        } else if (v == mTVCopyWechat) {
            clipText = mTVShowWechat.getText().toString();
        }
        ClipData mClipData = ClipData.newPlainText("Contact", clipText);
        cm.setPrimaryClip(mClipData);
        showToast("已复制到粘贴板");
    }

    /**
     * 设置样式
     */
    private void setStyle(@DialogStyle int style) {
        if (contactUnLockInfo == null)
            return;
        String qqStr = "未设置";
        String wechatStr = "未设置";
        String hintTitle = "";
        String submitText = "";
        switch (style) {
            case DialogStyle.UNLOCK:
                if (contactUnLockInfo.getQq() != null && contactUnLockInfo.getQq().length() > 0)
                    qqStr = contactUnLockInfo.getQq();
                if (contactUnLockInfo.getWechat() != null && contactUnLockInfo.getWechat().length() > 0)
                    wechatStr = contactUnLockInfo.getWechat();
                hintTitle = getContext().getString(R.string.dialog_checkcontact_hint_2);
                submitText = getContext().getString(R.string.dialog_checkcontact_button_1);

                mTVCopyQQ.setVisibility(View.VISIBLE);
                mTVCopyWechat.setVisibility(View.VISIBLE);
                break;
            case DialogStyle.LOCK:
                qqStr = getSecretWord(contactUnLockInfo.getQq());
                wechatStr = getSecretWord(contactUnLockInfo.getWechat());
                hintTitle = getContext().getString(R.string.dialog_checkcontact_hint_1);
                submitText = getContext().getString(R.string.dialog_checkcontact_button_2);

                mTVCopyQQ.setVisibility(View.GONE);
                mTVCopyWechat.setVisibility(View.GONE);
                break;
        }
        mTVHintTitle.setText(hintTitle);
        mTVShowQQ.setText(qqStr);
        mTVShowWechat.setText(wechatStr);
        mTVSubmit.setText(submitText);
        mTVSubmit.setVisibility(View.VISIBLE);
    }

    private String getSecretWord(String data) {
        if (data == null || data.length() == 0) {
            return "****";
        }
        int halfPostion = data.length() / 2;
        return data.substring(0, halfPostion) + "****";
    }
//------------------------获取解锁信息------------------------------

    @Override
    public void getContactUnLockInfoSuccess(ContactUnLockInfoResults results) {
        this.contactUnLockInfo = results;
        setStyle(results.getIspay());
    }

    @Override
    public void getContactUnLockInfoFail(String msg) {
        showToast(msg);
        this.cancel();
    }

    @Override
    public void unlockSuccess() {
        contactUnLockInfo.setIspay(1);
        setStyle(DialogStyle.UNLOCK);
    }

    @Override
    public void unlockFail(String msg) {
        showToast(msg);
    }

    @Override
    public long getReadManId() {
        return mReadManId;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShort(getContext(), toast);
    }
}

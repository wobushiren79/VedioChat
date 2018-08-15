package com.huanmedia.videochat.my.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.dialog.DialogPick;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class FileInfoEditPirceFragment extends BaseFragment {
    public final String TAG = "FileInfoEditPirceFragment";

    private DialogPick mDialogPack;
    private CallBack mCallBack;

    @BindView(R.id.tv_price)
    TextView mTVPrice;
    @BindView(R.id.tv_price_title)
    TextView mTVPriceTitle;
    @BindView(R.id.ll_price)
    LinearLayout mLLPrice;
    @BindView(R.id.view_photo)
    PhotoView mPhotoView;

    private String[] mPirceList;
    private String mFilePath;
    private int mFileType;

    public FileInfoEditPirceFragment(String mFilePath, int mFileType) {
        this.mFilePath = mFilePath;
        this.mFileType = mFileType;
    }

    public static Fragment newInstance(String mFilePath, int mFileType) {
        FileInfoEditPirceFragment fragment = new FileInfoEditPirceFragment(mFilePath, mFileType);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPirceList = new String[9];
        for (int i = 0; i < mPirceList.length; i++) {
            mPirceList[i] = (100 + 100 * i) + "";
        }
        if (mFileType == 1) {
            mPhotoView.setVisibility(View.VISIBLE);
            mPhotoView.setImageURI(Uri.parse(mFilePath));
        } else if (mFileType == 2) {
            mPhotoView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file_info_edit_price;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @OnClick({R.id.ll_price})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_price:
                if (mDialogPack == null)
                    mDialogPack = new DialogPick(getContext());
                mDialogPack.showPickerTimeMoney(mPirceList, "设置查看价格", "钻石", mTVPrice.getText().toString());
                mDialogPack.setReadmainListener((showStr, money) -> {
                    mTVPrice.setText(showStr);
                    if (mCallBack != null)
                        mCallBack.changePirce(showStr, money);
                });
                break;
        }
    }

    public int getPrice() {
        String priceStr = mTVPrice.getText().toString();
        return Integer.valueOf(priceStr);
    }

    public interface CallBack {
        void changePirce(String showStr, int money);
    }
}

package com.huanmedia.videochat.my;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;

public class ReadMainCertificateActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, ReadMainCertificateActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_main_certificate;
    }

    @Override
    protected void initView() {
        if (UserManager.getInstance().getCurrentUser().getUserinfo() == null) {
            ToastUtils.showToastLongInCenter(this, "账号数据有误，请重新登录");
            finish();
        }
        int isStarauth = UserManager.getInstance().getCurrentUser().getUserinfo().getIsstarauth();
        if (isStarauth == 1) {
            addFragmentAndShow(R.id.rmc_fl_content, ReadMainFragment.newInstance()
                    , ReadMainFragment.class.getName());
        } else {
            addFragmentAndShow(R.id.rmc_fl_content, ReadMainCertificateFragment.newInstance()
                    , ReadMainCertificateFragment.class.getName());
        }
    }
}

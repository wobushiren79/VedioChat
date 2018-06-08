package com.huanmedia.videochat.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.launch.fragment.CompleteInformationFragment;
import com.huanmedia.videochat.launch.fragment.FirstGuideFragment;
import com.huanmedia.videochat.launch.fragment.LoginFragment;
import com.huanmedia.videochat.launch.fragment.LuanchFragment;
import com.huanmedia.videochat.repository.entity.UserEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class StartActivity extends BaseActivity {

    @BindView(R.id.start_fragment)
    FrameLayout mStartFragment;
    private Intent mCurrentFragmentIntent;
    private boolean isPause;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        addFragmentAndShow(R.id.start_fragment, new LuanchFragment(), LuanchFragment.TAG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            lunach(mCurrentFragmentIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lunach(Intent intent) {
        if (isPause) {
            mCurrentFragmentIntent = intent;
            return;
        }
        if (intent == null || (intent.getAction() != null && !Uri.parse(intent.getAction()).getScheme().equals(EventBusAction.SCHEME_ACTION)))
            return;
        switch (Uri.parse(intent.getAction()).getPath()) {
            case FirstGuideFragment.TAG:
                addFragmentAndShow(R.id.start_fragment, new FirstGuideFragment(),


                        FirstGuideFragment.TAG, false, true);
                break;
            case LoginFragment.TAG:
                addFragmentAndShow(R.id.start_fragment, new LoginFragment(), LoginFragment.TAG);
                break;
            case CompleteInformationFragment.TAG:
                addFragmentAndShow(R.id.start_fragment,
                        CompleteInformationFragment.newInstance((UserEntity) intent.getParcelableExtra("user")),
                        CompleteInformationFragment.TAG,
                        intent.getBooleanExtra("isAddBack", true),
                        intent.getBooleanExtra("isRemoveCurrent", false),
                        new int[]{R.anim.fragment_enter_in, 0, 0, R.anim.fragment_pop_out}
                );
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getCurrentFragment() != null)
            getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null)
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment.getTag() != null && fragment.getTag().equals("/CompleteInformationFragment")) {
                    addFragmentAndShow(R.id.start_fragment, new LoginFragment(), LoginFragment.TAG, false, true);
                    return;
                }
            }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

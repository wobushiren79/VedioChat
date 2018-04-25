package com.huanmedia.videochat.launch.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.launch.adapter.GuideAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mvp.data.store.DataKeeper;

public class FirstGuideFragment extends BaseFragment implements ViewPager.OnPageChangeListener, GuideAdapter.GuideListener {
    public static final String TAG = "/FirstGuideFragment";
    @BindView(R.id.first_guide_viewPage)
    ViewPager firstGuideViewPage;
    @BindView(R.id.first_guide_dot0)
    TextView firstGuideDot0;
    @BindView(R.id.first_guide_dot1)
    TextView firstGuideDot1;
    @BindView(R.id.first_guide_dot2)
    TextView firstGuideDot2;
    @BindView(R.id.first_guide_dot3)
    TextView firstGuideDot3;
    @BindView(R.id.first_guide_ll_dotGroup)
    LinearLayout firstGuideLlDotGroup;
    private ArrayList<View> dots;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_first_guide;
    }

    @Override
    protected void initView(View view) {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.glide_p1);
        list.add(R.drawable.glide_p2);
        list.add(R.drawable.glide_p3);
        list.add(R.drawable.glide_p4);
        dots = new ArrayList<>();
        dots.add(firstGuideDot0);
        dots.add(firstGuideDot1);
        dots.add(firstGuideDot2);
        dots.add(firstGuideDot3);
        firstGuideViewPage.setAdapter(new GuideAdapter(list, this));
        firstGuideViewPage.addOnPageChangeListener(this);
        setDotLevel(0);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setDotLevel(position);
    }

    private void setDotLevel(int position) {
        for (int i = 0; i < dots.size(); i++) {
            if (position==i){
                dots.get(i).getBackground().setLevel(1);
            }else {
                dots.get(i).getBackground().setLevel(2);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void startApp(View view) {
        if (getContext()!=null) {
            new DataKeeper(getContext(), DataKeeper.DEFULTFILE).put("showFirstGuide", false);
            if(UserManager.getInstance().getCurrentUser()!=null){
                getNavigator().navToMain(getContext(),true);
                ActivitManager.getAppManager().finishActivity(getActivity());
            }else {
                EventBus.getDefault().post(new Intent(EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+LoginFragment.TAG));
            }
        }

    }
}

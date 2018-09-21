package com.huanmedia.videochat.discover.fragment;

import android.widget.TextView;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;

import butterknife.BindView;

public class ArtistUserInfoFragment extends BaseFragment {

    @BindView(R.id.tv_userinfo_name)
    TextView mTVName;
    @BindView(R.id.tv_userinfo_birth)
    TextView mTVBirth;
    @BindView(R.id.tv_userinfo_blood)
    TextView mTVBlood;
    @BindView(R.id.tv_userinfo_high)
    TextView mTVHigh;
    @BindView(R.id.tv_userinfo_like)
    TextView mTVLike;

    private BusinessCardInfoResults mUserData;

    public static ArtistUserInfoFragment newInstance() {
        ArtistUserInfoFragment fragment = new ArtistUserInfoFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist_userinfo;
    }

    public void setUserData(BusinessCardInfoResults results) {
        mUserData = results;
        BusinessCardInfoResults.BaseInfo baseInfo = mUserData.getBase();
        if (baseInfo.getNickname() != null)
            mTVName.setText(baseInfo.getNickname());
        if (baseInfo.getBirthday() != null)
            mTVBirth.setText(baseInfo.getBirthday());
        if (baseInfo.getBloodtype() != null)
            mTVBlood.setText(baseInfo.getBloodtype());
        if (baseInfo.getHeight() != 0)
            mTVHigh.setText(baseInfo.getHeight() + "");
        if(baseInfo.getHobby()!=null){
            String hobby="";
            for (String item: baseInfo.getHobby()) {
                hobby+=" "+item;
            }
            mTVLike.setText(hobby);
        }

    }
}

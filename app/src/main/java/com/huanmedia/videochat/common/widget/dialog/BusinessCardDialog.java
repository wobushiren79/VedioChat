package com.huanmedia.videochat.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.presenter.user.BusinessCardInfoPresenterlmpl;
import com.huanmedia.videochat.mvp.presenter.user.IBusinessCardInfoPresenter;
import com.huanmedia.videochat.mvp.view.user.IBusinessCardInfoView;
import com.huanmedia.videochat.repository.entity.VideoEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideUtils;

public class BusinessCardDialog extends Dialog implements IBusinessCardInfoView, View.OnClickListener {

    private View mView;
    private int mUid;//用户ID
    private String mDistance;//距离

    private RelativeLayout mRLVideo;
    private ImageView mIVCancel;
    private TextView mTVName;
    private TextView mTVAge;
    private TextView mTVDistance;
    private TextView mTVLevel;
    private RoundedImageView mIVVideo;
    private ViewPager mVPImage;
    private RadioGroup mRGContent;

    private BusinessCardInfoResults mUserInfoData;
    private IBusinessCardInfoPresenter mBussinessCardInfoPresenter;

    public BusinessCardDialog(@NonNull Context context) {
        this(context, R.style.customDialog);
    }

    public BusinessCardDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mBussinessCardInfoPresenter = new BusinessCardInfoPresenterlmpl(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(getContext(), R.layout.dialog_buiness_card, null);
        setContentView(mView);
        initView();
        initData();

    }

    private void initView() {
        mRLVideo = mView.findViewById(R.id.rl_video);
        mIVCancel = mView.findViewById(R.id.iv_cancel);
        mTVName = mView.findViewById(R.id.tv_name);
        mTVAge = mView.findViewById(R.id.tv_age);
        mTVLevel = mView.findViewById(R.id.tv_level);
        mTVDistance = mView.findViewById(R.id.tv_distance);
        mIVVideo = mView.findViewById(R.id.iv_video);
        mVPImage = mView.findViewById(R.id.buiness_vp);
        mRGContent = mView.findViewById(R.id.rg_content);

        mIVCancel.setOnClickListener(this);
        initViewPagerImage();
    }

    private void initData() {
        if (mUserInfoData == null)
            return;

        String nickName = mUserInfoData.getBase().getNickname();
        int sex = mUserInfoData.getBase().getSex();
        int age = mUserInfoData.getBase().getAge();
        int level = mUserInfoData.getBase().getLevel();
        String videoImage = "";
        if (mUserInfoData.getBase().getVoides() != null && mUserInfoData.getBase().getVoides().size() > 0) {
            videoImage = mUserInfoData.getBase().getVoides().get(0).getImgurl();
            GlideUtils.getInstance().loadBitmapNoAnim(getContext(), videoImage, mIVVideo);
            mIVVideo.setOnClickListener(this);
        } else {
            mRLVideo.setVisibility(View.GONE);
        }

        mTVName.setText(nickName);
        mTVAge.setText(age + "岁");
        mTVLevel.setText("Lv." + level);
        mTVDistance.setText(Check.checkReplace(mDistance, "未知"));

        Drawable sexDrawable = null;
        switch (sex) {
            case 1:
                sexDrawable = getContext().getResources().getDrawable(R.drawable.icon_focus_boy);
                break;
            case 2:
                sexDrawable = getContext().getResources().getDrawable(R.drawable.icon_focus_girl);
                break;
        }
        if (sexDrawable != null)
            sexDrawable.setBounds(
                    0,
                    0,
                    getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_30dp),
                    getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_30dp));

        mTVName.setCompoundDrawables(
                null,
                null,
                sexDrawable,
                null);
    }

    /**
     * 初始化图片切换
     */
    private void initViewPagerImage() {
        if (mUserInfoData == null)
            return;
        List<ImageView> viewList = new ArrayList<>();
        if (mUserInfoData.getBase().getPhpots() != null && mUserInfoData.getBase().getPhpots().size() > 0) {
            addImageView(viewList, mUserInfoData.getBase().getUserphoto_thumb());
            for (int i = 0; i < mUserInfoData.getBase().getPhpots().size(); i++) {
                addImageView(viewList, mUserInfoData.getBase().getPhpots().get(i).getPhoto_thumb());
            }
        }
        addImageView(viewList, mUserInfoData.getBase().getUserphoto_thumb());
        mVPImage.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }
        });

        mVPImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton) mRGContent.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mRGContent.getChildCount() != 0) {
            RadioButton firstRB = (RadioButton) mRGContent.getChildAt(0);
            firstRB.setChecked(true);
        }
    }

    /**
     * 添加一个图片
     */
    private void addImageView(List<ImageView> listView, String imageUrl) {
        RoundedImageView imageView = new RoundedImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCornerRadius(
                getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_24dp),
                getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_24dp),
                0,
                0);
        GlideUtils.getInstance().loadBitmapNoAnim(getContext(), imageUrl, imageView);

        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_50dp),
                getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_8dp));
        layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_8dp);
        layoutParams.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.dimen_8dp);

        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setButtonDrawable(null);
        radioButton.setBackgroundResource(R.drawable.rb_style_2);
        radioButton.setLayoutParams(layoutParams);
        mRGContent.addView(radioButton);

        listView.add(imageView);
    }

    /**
     * 设置用户ID
     *
     * @param uid
     */
    public void setUid(int uid) {
        mUid = uid;
    }

    /**
     * 设置距离
     *
     * @param distance
     */
    public void setDistance(String distance) {
        mDistance = distance;
    }

    @Override
    public void show() {
        mBussinessCardInfoPresenter.getBusinessCardInfo(mUid);
    }

    @Override
    public void getBusinessCardInfoSuccess(BusinessCardInfoResults results) {
        mUserInfoData = results;
        super.show();
    }

    @Override
    public void getBusinessCardInfoFail(String msg) {
        showToast(msg);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastLong(getContext(), toast);
    }

    @Override
    public void onClick(View view) {
        if (view == mIVCancel) {
            this.cancel();
        } else if (view == mIVVideo) {
            Navigator navigator = new Navigator();
            navigator.navtoMediaPlay(DevUtils.scanForActivity(getContext()), (ArrayList<VideoEntity>) mUserInfoData.getBase().getVoides(), 0);
        }
    }
}

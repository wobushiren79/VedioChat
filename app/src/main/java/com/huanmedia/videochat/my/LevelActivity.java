package com.huanmedia.videochat.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseMVPActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.main2.weight.GoodProgressView;
import com.huanmedia.videochat.repository.entity.MyLevelEntity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import mvp.data.store.glide.GlideUtils;

public class LevelActivity extends BaseMVPActivity<LevelPresenter> implements LevelView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.level_iv_header)
    RoundedImageView mLevelIvHeader;
    @BindView(R.id.level_iv_lv)
    ImageView mLevelIvLv;
    @BindView(R.id.level_tv_low)
    TextView mLevelTvLow;
    @BindView(R.id.level_tv_low_desc)
    TextView mLevelTvLowDesc;
    @BindView(R.id.level_gpv)
    GoodProgressView mLevelGpv;
    @BindView(R.id.level_tv_high)
    TextView mLevelTvHigh;
    @BindView(R.id.level_tv_high_desc)
    TextView mLevelTvHighDesc;
    @BindView(R.id.level_tv_currentLevel)
    TextView mLevelTvCurrentLevel;
    @BindView(R.id.level_tv_levelDesc)
    TextView mLevelTvLevelDesc;
    @BindView(R.id.level_upgrade_rv)
    RecyclerView mLevelUpgradeRv;
    private HintDialog mLoadingDialog;
    private HintDialog mHintDialog;
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, LevelActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_level;
    }

    @Override
    protected void initView() {
        initToolbar();
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_level_upgrade) {
            public Spanny mSpanny = new Spanny();
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                String[] data = item.split("-");
                mSpanny.clear();
                mSpanny.append(data[1]);
                mSpanny.findAndSpan(data[1].substring(data[1].indexOf(" "), data[1].length())
                        , () -> new ForegroundColorSpan(ContextCompat.getColor(context(), R.color.base_black)));
                helper.setText(R.id.item_level_tv_name, data[0])
                        .setText(R.id.item_level_tv_value, mSpanny);
            }
        };
        mAdapter.bindToRecyclerView(mLevelUpgradeRv);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initData() {
       getBasePresenter().getMyLevelData();
        UserEntity.UserinfoEntity userinfo = UserManager.getInstance().getCurrentUser().getUserinfo();
        GlideUtils.getInstance().loadBitmapNoAnim(this, userinfo.getUserphoto_thumb(), mLevelIvHeader);
        mAdapter.setNewData(getBasePresenter().getLeveDiscs());
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    @Override
    protected ImmersionBar defaultBarConfig() {
        return super.defaultBarConfig().statusBarDarkFont(true);
    }
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    @Override
    public Context context() {
        return this;
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

    @SuppressLint("DefaultLocale")
    @Override
    public void showLevelData(MyLevelEntity myLevelEntity) {
        mLevelIvLv.getDrawable().setLevel(myLevelEntity.getLevel());
        String lvDesc = getResources().getStringArray(R.array.level_value)[myLevelEntity.getLevel()-1];
        String[] lvRange = lvDesc.split("–");
        String lvLowDesc = lvRange[0];
        String lvHighDesc = lvRange[1];
        int low = Integer.parseInt(lvLowDesc);
        int hight = Integer.parseInt(lvHighDesc);
        float ratio;
        if (hight == Integer.MAX_VALUE) {
            lvHighDesc = "MAX";
            ratio = 100;
        } else {
            ratio = ((myLevelEntity.getExperience() - low)*1.0f) / hight;
        }
        mLevelTvLow.setText(String.format("Lv.%d", myLevelEntity.getLevel()));
        mLevelTvLowDesc.setText(String.format("(%s)", lvLowDesc));
        mLevelGpv.setProgressValue((int) (ratio*100));
        mLevelTvHigh.setText(String.format("Lv.%s", (myLevelEntity.getLevel()+1) > 10 ? myLevelEntity.getLevel()+"" : myLevelEntity.getLevel()+1 + ""));
        mLevelTvHighDesc.setText(String.format("(%s)", lvHighDesc));
        Spanny spanny = new Spanny(String.format("当前经验值：%d",myLevelEntity.getExperience()));
        spanny.findAndSpan(myLevelEntity.getExperience() + "", () -> {
            int leveColor = Color.parseColor(getBasePresenter().getLeveExperienceColor(myLevelEntity.getLevel()));
            return new ForegroundColorSpan(leveColor);
        });
        mLevelTvCurrentLevel.setText(spanny);
        mLevelTvLevelDesc.setText(String.format("特权：%s", Check.checkReplace(myLevelEntity.getPrivilege())));
    }
}

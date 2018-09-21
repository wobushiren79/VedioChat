package com.huanmedia.videochat.main2.fragment;

import android.view.View;

import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseFragment;
import com.huanmedia.videochat.common.widget.artists.ArtistsGroupShowLayout;
import com.huanmedia.videochat.mvp.entity.results.ArtistsGroupShowResults;
import com.huanmedia.videochat.mvp.presenter.info.ArtistsGroupShowPresenterImpl;
import com.huanmedia.videochat.mvp.presenter.info.IArtistsGroupShowPresenter;
import com.huanmedia.videochat.mvp.view.info.IArtistsGroupShowView;

import java.util.List;

import butterknife.BindView;

public class ArtistsGroupFragment extends BaseFragment implements IArtistsGroupShowView {

    private int mGroupId;
    private IArtistsGroupShowPresenter mShowPresenter;
    @BindView(R.id.layout_show)
    ArtistsGroupShowLayout mShowLayout;

    /**
     * 设置组合ID
     *
     * @param mGroupId
     */
    public void setGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artists_group;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void initData() {
        super.initData();
        mShowPresenter = new ArtistsGroupShowPresenterImpl(this);
        mShowPresenter.getArtistsGroupShowData();
    }

    @Override
    public void getArtistsGroupShowDataSuccess(ArtistsGroupShowResults results) {
        mShowLayout.setBaseData(results);
    }

    @Override
    public void getArtistsGroupShowDataFail(String msg) {

    }

    @Override
    public int getArtistsGroupId() {
        return mGroupId;
    }

    @Override
    public void setArtistsGroupBackGround(String backGroundUrl) {
        mShowLayout.setBackGround(backGroundUrl);
    }

    @Override
    public void setArtistsGroupTitle(String url) {
        mShowLayout.setTitleImage(url);
    }

    @Override
    public void setArtistsList(List<ArtistsGroupShowResults.Items> listData) {
        mShowLayout.setArtistsList(listData);
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }
}

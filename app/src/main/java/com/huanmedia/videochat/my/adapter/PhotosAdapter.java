package com.huanmedia.videochat.my.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.my.PhotosActivity;
import com.huanmedia.videochat.repository.entity.PhotosEntity;

import java.util.List;

import mvp.data.store.glide.GlideUtils;

/**
 * Create by Administrator
 * time: 2018/3/2.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class PhotosAdapter extends BaseItemDraggableAdapter<PhotosEntity, BaseViewHolder> {
    private int mGridCount;
    @PhotosActivity.UpLoadType
    private int mUpLoadType;

    private boolean mIsEdit;
    private SparseArray<HM_PhotoEntity> mCheckMap = new SparseArray<>();

    public boolean isEdit() {
        return mIsEdit;
    }

    public SparseArray<HM_PhotoEntity> getCheckMap() {
        return mCheckMap;
    }

    public void setEdit(boolean edit) {
        mIsEdit = edit;
        notifyDataSetChanged();
    }

    public PhotosAdapter(int layoutResId, @Nullable List<PhotosEntity> data, int count, @PhotosActivity.UpLoadType int mUpLoadType) {
        super(layoutResId, data);
        this.mGridCount = count;
        this.mUpLoadType = mUpLoadType;
    }

    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        View rootview = mLayoutInflater.inflate(layoutResId, parent, false);
        int value = DisplayUtil.getDisplayWidth(mContext) - (DisplayUtil.dip2px(mContext, 4) * mGridCount - 1);
        rootview.getLayoutParams().height = value / mGridCount;
        return rootview;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + ((!isEdit()) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (!isEdit() && position == getItemCount() - 1) {
            return 1742;//用于上传照片View占位
        }
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotosEntity item) {
        ImageView iv = helper.getView(R.id.item_photos_iv);
        TextView tvMask = helper.getView(R.id.tv_mask);
        tvMask.setVisibility(View.GONE);
        Object url;

        if (!mIsEdit && helper.getAdapterPosition() == getItemCount() - 1) {//非编辑状态下，显示上传按钮
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.item_photos_bg_solid));
            url = R.drawable.bg_photo_plus;
        } else {
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackground(null);
            url = item.thumbnail();
            if (mUpLoadType == PhotosActivity.UpLoadType.SECRET) {
                tvMask.setVisibility(View.VISIBLE);
                if (item.getStatus() == -1) {
                    tvMask.setText("未审核");
                } else if (item.getStatus() == -9) {
                    tvMask.setText("非法");
                } else if (item.getStatus() == 0) {
                    tvMask.setText("审核未通过");
                } else {
                    tvMask.setVisibility(View.GONE);
                }
            } else {
                tvMask.setVisibility(View.GONE);
            }
        }


        if (mIsEdit) {
            boolean isChecked = mCheckMap.indexOfKey(helper.getAdapterPosition()) >= 0;
            if (isChecked) {
                iv.setColorFilter(Color.parseColor("#66ffffff"));
            } else {
                iv.clearColorFilter();
            }
            helper.setVisible(R.id.item_photos_tv_hint, false)
                    .setVisible(R.id.item_photos_stv_cover, false).
                    setVisible(R.id.item_photos_rb, true)
                    .setChecked(R.id.item_photos_rb, isChecked);
        } else {
            iv.clearColorFilter();
            helper.setVisible(R.id.item_photos_rb, false);
            helper.setVisible(R.id.item_photos_tv_hint, helper.getAdapterPosition() == getItemCount() - 1);
            if (helper.getAdapterPosition() == 0 && mUpLoadType != PhotosActivity.UpLoadType.SECRET) {
                helper.setVisible(R.id.item_photos_stv_cover, true);
            } else {
                helper.setVisible(R.id.item_photos_stv_cover, false);
            }
        }
        GlideUtils.getInstance().loadBitmapNoAnim(mContext, url, iv);
    }
}

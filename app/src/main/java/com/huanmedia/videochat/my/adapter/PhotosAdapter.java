package com.huanmedia.videochat.my.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;

import java.util.List;

import mvp.data.store.glide.GlideUtils;

/**
 * Create by Administrator
 * time: 2018/3/2.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class PhotosAdapter extends BaseItemDraggableAdapter<HM_PhotoEntity, BaseViewHolder> {
    private  int mGridCount;
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

    public PhotosAdapter(int layoutResId, @Nullable List<HM_PhotoEntity> data, int count) {
        super(layoutResId, data);
        this.mGridCount =count;
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
        if (!isEdit() && position==getItemCount()-1){
            return 1742;//用于上传照片View占位
        }
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, HM_PhotoEntity item) {
        ImageView iv=helper.getView(R.id.item_photos_iv);
        Object url;

        if (!mIsEdit && helper.getAdapterPosition()==getItemCount()-1){//非编辑状态下，显示上传按钮
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setBackground(ContextCompat.getDrawable(mContext,R.drawable.item_photos_bg_solid));
            url = R.drawable.bg_photo_plus;
        }else {
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackground(null);
            url = item.thumbnail();
        }

        if (mIsEdit) {
            boolean isChecked = mCheckMap.indexOfKey(helper.getAdapterPosition()) >= 0;
            if (isChecked){
                iv.setColorFilter( Color.parseColor("#66ffffff"));
            }else {
                iv.clearColorFilter();
            }
            helper.setVisible(R.id.item_photos_tv_hint,false)
                    .setVisible(R.id.item_photos_stv_cover,false).
                    setVisible(R.id.item_photos_rb, true)
                    .setChecked(R.id.item_photos_rb, isChecked);
        } else {
            iv.clearColorFilter();
            helper.setVisible(R.id.item_photos_rb, false)
                    .setVisible(R.id.item_photos_stv_cover,helper.getAdapterPosition()==0)
                    .setVisible(R.id.item_photos_tv_hint,helper.getAdapterPosition()==getItemCount()-1);
        }
        GlideUtils.getInstance().loadBitmapNoAnim(mContext, url,iv);
    }
}

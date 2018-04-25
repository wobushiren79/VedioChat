package com.huanmedia.hmalbumlib.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.huanmedia.hmalbumlib.HM_AlbumConifg;
import com.huanmedia.hmalbumlib.extar.HM_DisplayUtil;
import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;

import java.util.ArrayList;

public class HM_PhotoSeeAdapter extends PagerAdapter {
    private Context mContext;
    ArrayList<HM_PhotoEntity> photoEntities;
    long waiting=0;

    private OnPhotoListener onPhotoListener;



    public HM_PhotoSeeAdapter(Context c, ArrayList<HM_PhotoEntity> photoEntities) {
        this.photoEntities = photoEntities;
        this.mContext = c;
    }

    public OnPhotoListener getOnPhotoListener() {
        return onPhotoListener;
    }

    public void setOnPhotoListener(OnPhotoListener onPhotoListener) {
        this.onPhotoListener = onPhotoListener;
    }

    @Override
    public int getCount() {
        return photoEntities.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        HM_PhotoEntity photodata = photoEntities.get(position);
        PhotoView photoView = new PhotoView(container.getContext());
        if (onPhotoListener != null) {
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    onPhotoListener.photoClick(view);
                }
            });
        }
        int resize = HM_DisplayUtil.getDisplayWidth(container.getContext());
        if (HM_AlbumConifg.getInstance().getEngine().supportAnimatedGif()){
            HM_AlbumConifg.getInstance().getEngine()
                    .loadImage(mContext, resize,resize,photoView,Uri.parse(photodata.getImage()));
        }else {
            HM_AlbumConifg.getInstance().getEngine()
                    .loadGifImage(mContext, resize,resize,photoView, Uri.parse(photodata.getImage()));
        }

        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
       return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public interface OnPhotoListener {
        void photoClick(View v);
    }
}
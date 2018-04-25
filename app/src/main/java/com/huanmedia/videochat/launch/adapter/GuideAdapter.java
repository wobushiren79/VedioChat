package com.huanmedia.videochat.launch.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huanmedia.videochat.R;

import java.util.List;

import mvp.data.store.glide.GlideApp;

public  class  GuideAdapter extends PagerAdapter {
        private final List<Integer> mList;
    private final GuideListener mListener;

    public GuideAdapter(List<Integer> list , GuideListener listener) {
            this.mList=list;
            this.mListener=listener;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View containerView = LayoutInflater.from(container.getContext()).inflate(R.layout.viewpage_first_guide, container, false);
            ImageView imgTop= (ImageView) containerView.findViewById(R.id.page_first_guide_iv_buttom);
            View openYourchum=containerView.findViewById(R.id.page_first_guide_btn);
          /*  ImageView imgButtom= (ImageView) containerView.findViewById(R.id.page_first_guide_iv_buttom);
            View openYourchum=containerView.findViewById(R.id.page_first_guide_btn);
*/
            GlideApp.with(container.getContext())
                    .load(mList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgTop);
 /*           Glide.with(container.getContext())
                    .load(mList.get(position).second)
                    .override(container.getMeasuredWidth(),container.getMeasuredHeight())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgButtom);
            if (position==mList.size()-1){
                openYourchum.setVisibility(View.VISIBLE);
                openYourchum.setOnClickListener(v -> {
                    if (mListener!=null){
                        mListener.startApp(v);
                    }
                });
            }*/
            if(position==3)
            {
                openYourchum.setVisibility(View.VISIBLE);
                openYourchum.setOnClickListener(v -> {
                    if (mListener!=null){
                        mListener.startApp(v);
                    }
                });
            }else {
                openYourchum.setVisibility(View.INVISIBLE);
            }
            container.addView(containerView);
            return containerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
       public  interface GuideListener{
            void startApp(View view);
        }
    }
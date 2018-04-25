package com.huanmedia.videochat.my.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huanmedia.hmalbumlib.HM_StartAlbumPhoto;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideUtils;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ImageViewHolder>{

    private final Context mContext;
    private final int layout;
    private  int imageSize;
    private List<HM_ImgData> datas = new ArrayList<>();
    private OnCloseListener listener;
    public final int ADDBTN=0;
    public final int CONTENT=1;

    //是否加入添加按钮
    private boolean isAddShow=false;
    private int maxChoose =3;

    public FeedbackAdapter(Context context, @LayoutRes int layout, List<HM_ImgData> data) {
       this.mContext =context;
        this.layout=layout;
        if (data!=null)
        this.datas=data;
    }
    public List<HM_ImgData> getData(){
        return datas;
    }
    @Override
    public int getItemViewType(int position) {
        if (isAddShow && position==getItemCount()-1){
            return ADDBTN;//添加按钮
        }else {
            return CONTENT;//正常类容
        }
    }

    public void setListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<HM_ImgData> data) {
        datas = data;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,imageViewremove;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.itme_opinion_iv);
            imageViewremove= (ImageView) itemView.findViewById(R.id.itme_opinion_iv_remove);
        }

        public String getName() {
            return "类容";
        }
    }
    public class ImageAddHolder extends ImageViewHolder{
        public ImageView imageView;
        public ImageAddHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.itme_layout_plus_iv);
            imageView.setOnClickListener(v -> {
                if (listener!=null){
                    listener.plusClick();
                }
            });
        }
        public String getName() {
            return "按钮";
        }
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==ADDBTN && isAddShow){
            return new ImageAddHolder(LayoutInflater.from(mContext).inflate(R.layout.itme_layout_plus,parent,false));
        }else {
            return new ImageViewHolder(LayoutInflater.from(mContext).inflate(layout,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
            switch (getItemViewType(position)){
                case ADDBTN:

                    break;
                case CONTENT:
                    holder.imageView.setOnClickListener(v -> {
                        if (!DoubleClickUtils.isFastDoubleClick()){
                            new HM_StartAlbumPhoto.Bulide(mContext,new HM_GlideEngine())
                                    .setCurrentSelect(position)
                                    .setMode(HM_StartAlbumPhoto.AlbumPhotoMode.EDITCHOOSEIMAGE)
                                    .setList(datas)
                                    .setRequestCode(204)
                                    .bulide();
                        }
                    });
                    GlideUtils.getInstance().loadBitmapNoAnim(mContext,datas.get(position).
                            getImage(),holder.imageView);
                    holder.imageViewremove.setOnClickListener(v -> {
                        if (listener!=null){
                            datas.remove(position);
                            listener.close(position);
                            if (isAddShow){
                                setAddShow(true);
                            }
                            notifyDataSetChanged();
                        }
                    });
                    break;
            }
    }
    public  interface   OnCloseListener {
        void close(int position);
        void plusClick();
    }

    public void setAddShow(boolean addShow) {
        int position;
       if ((isAddShow&&addShow)||(!isAddShow && !addShow)){
           return ;
       }
        if (isAddShow){
            position= datas.size()-1;
        }else {
            position = datas.size();
        }
        isAddShow = addShow;
        if (isAddShow){
            notifyItemInserted(position);
//            datas.add(position,new ChooseImageEntity(isAddShow));
        }else {
            notifyItemRemoved(position);
//            datas.remove(position);
        }
    }

    public boolean isAddShow() {
        return isAddShow;
    }

    @Override
    public int getItemCount() {
        if (isAddShow){
            return datas.size()+1;
        }else {
            return datas.size();
        }
    }
}

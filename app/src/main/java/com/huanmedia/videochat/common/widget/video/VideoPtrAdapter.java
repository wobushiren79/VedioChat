package com.huanmedia.videochat.common.widget.video;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.TextView;

import com.applecoffee.devtools.base.adapter.BaseRCAdapter;
import com.applecoffee.devtools.base.adapter.BaseViewHolder;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.mvp.entity.results.ShortVideoResults;
import com.shuyu.gsyvideoplayer.render.effect.BlackAndWhiteEffect;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPtrAdapter extends BaseRCAdapter<ShortVideoResults> {


    private int mPlayPosition;

    public VideoPtrAdapter(Context context) {
        super(context, R.layout.layout_video_ptr_item);
        mPlayPosition = -1;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, ShortVideoResults shortVideoResults, int i) {

        StandardGSYVideoPlayer videoPlayer = baseViewHolder.getView(R.id.detail_player);
        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        String source1 = "https://mmianvideo.oss-cn-beijing.aliyuncs.com/prod_voides_20180529_123637_43344";
        videoPlayer.setUp(source1, true, shortVideoResults.getName());
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        videoPlayer.setLooping(true);
        if (i == mPlayPosition) {
//            videoPlayer.getStartButton().performClick();
            videoPlayer.startPlayLogic();
        }
    }


    public void playVideo(int position) {
        mPlayPosition = position;
        notifyItemChanged(position);
    }
}

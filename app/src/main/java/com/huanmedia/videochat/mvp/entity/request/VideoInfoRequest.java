package com.huanmedia.videochat.mvp.entity.request;

import android.graphics.Bitmap;

public class VideoInfoRequest {

    private int videoId;//视频ID
    private String title;//视频名称
    private String videoPath;//视频路径
    private long duration;//视频时长
    private long size;//视频大小
    private String imagePath;//视频缩略图路径
    private int imageId;//图片路径
    private String imageVideo;//视频路径
    private Bitmap bitmap;//缩略图

    public String getImageVideo() {
        return imageVideo;
    }

    public void setImageVideo(String imageVideo) {
        this.imageVideo = imageVideo;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

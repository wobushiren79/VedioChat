package com.huanmedia.videochat.mvp.presenter.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.huanmedia.ilibray.utils.FileUtils;
import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.view.file.IFileHandlerView;

import java.io.File;

public class FileHandlerPresenterImpl extends BaseMVPPresenter<IFileHandlerView, BaseMVPModel> implements IFileHandlerPresenter {

    public FileHandlerPresenterImpl(IFileHandlerView mMvpView) {
        super(mMvpView, BaseMVPModel.class);
    }

    @Override
    public VideoInfoRequest getVideoInfoByUri(Uri uri, ContentResolver contentResolver) {
        /** 数据库查询操作。
         * 第一个参数 uri：为要查询的数据库+表的名称。
         * 第二个参数 projection ： 要查询的列。
         * 第三个参数 selection ： 查询的条件，相当于SQL where。
         * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
         * 第四个参数 sortOrder ： 结果排序。
         */
        VideoInfoRequest videoInfoRequest = new VideoInfoRequest();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {


                // 视频ID:MediaStore.Audio.Media._ID
                int videoIdIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                int videoId = -1;
                if (videoIdIndex != -1)
                    videoId = cursor.getInt(videoIdIndex);

                // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
                if (videoId != -1) {
                    String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID};
                    Cursor thumbCursor = contentResolver.query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                    + "=" + videoId, null, null);
                    if (thumbCursor.moveToFirst()) {
                        videoInfoRequest.setImagePath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                    }
                }

                // 视频名称：MediaStore.Audio.Media.TITLE
                int titleIndex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
                String title = null;
                if (titleIndex != -1)
                    title = cursor.getString(titleIndex);
                // 视频路径：MediaStore.Audio.Media.DATA
                int videoPathIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                String videoPath = null;
                if (videoPathIndex != -1)
                    videoPath = cursor.getString(videoPathIndex);
                // 视频时长：MediaStore.Audio.Media.DURATION
                int durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                long duration = -1;
                if (durationIndex != -1)
                    duration = cursor.getLong(durationIndex);
                // 视频大小：MediaStore.Audio.Media.SIZE
                int sizeIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
                long size = -1;
                if (sizeIndex != -1)
                    size = cursor.getLong(sizeIndex);
                // 视频缩略图路径：MediaStore.Images.Media.DATA
                int imageVideoPathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String imageVideoPath = null;
                if (imageVideoPathIndex != -1)
                    imageVideoPath = cursor.getString(imageVideoPathIndex);
                // 缩略图ID:MediaStore.Audio.Media._ID
                int imageIdIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int imageId = -1;
                if (imageIdIndex != -1)
                    imageId = cursor.getInt(imageIdIndex);
                // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。


                // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
//                    Bitmap bitmap3=    ThumbnailUtils.extractThumbnail(bitmap, width,height ,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//                    setText(tv_VideoPath, R.string.path, videoPath);
////                    setText(tv_VideoDuration, R.string.duration, String.valueOf(duration));
////                    setText(tv_VideoSize, R.string.size, String.valueOf(size));
////                    setText(tv_VideoTitle, R.string.title, title);
////                    iv_VideoImage.setImageBitmap(bitmap1);

                videoInfoRequest.setVideoId(videoId);
                videoInfoRequest.setTitle(title);
                videoInfoRequest.setVideoPath(videoPath);
                videoInfoRequest.setDuration(duration);
                videoInfoRequest.setSize(size);
                videoInfoRequest.setImageId(imageId);
                videoInfoRequest.setImageVideo(imageVideoPath);

                Bitmap bitmap = null;
                if (imageId != -1)
//                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
                    // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                    // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
                    bitmap = ThumbnailUtils.createVideoThumbnail(imageVideoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
                if (bitmap == null && videoInfoRequest.getImagePath() == null && videoPath != null) {
                    bitmap = getVideoThumb(videoPath);
                }
                File file = FileUtils.saveBitMapToSDCard(bitmap);
                if (file != null)
                    videoInfoRequest.setImagePath(file.getPath());
                videoInfoRequest.setBitmap(bitmap);
            }
            cursor.close();
        }
        return videoInfoRequest;
    }

    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }
}

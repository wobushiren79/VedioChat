package com.huanmedia.videochat.mvp.presenter.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.huanmedia.videochat.mvp.base.BaseMVPModel;
import com.huanmedia.videochat.mvp.base.BaseMVPPresenter;
import com.huanmedia.videochat.mvp.entity.request.VideoInfoRequest;
import com.huanmedia.videochat.mvp.view.file.IFileHandlerView;

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
                int imagePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String imagePath = null;
                if (imagePathIndex != -1)
                    imagePath = cursor.getString(imagePathIndex);
                // 缩略图ID:MediaStore.Audio.Media._ID
                int imageIdIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int imageId = -1;
                if (imageIdIndex != -1)
                    imageId = cursor.getInt(imageIdIndex);
                // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
                Bitmap bitmap = null;
                if (imageId != -1)
                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
//                    Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);


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
                videoInfoRequest.setImagePath(imagePath);
                videoInfoRequest.setBitmap(bitmap);
            }
            cursor.close();
        }
        return videoInfoRequest;
    }
}

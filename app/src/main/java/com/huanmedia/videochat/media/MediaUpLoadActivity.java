package com.huanmedia.videochat.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MediaUpLoadActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.media_upload_rv)
    RecyclerView mMediaUpLoadRV;

    private MediaUpLoadAdapter mUpLoadAdapter;
    private List<VideoEntity> mListVideoData;

    public static Intent getCallingIntent(Context context, ArrayList<VideoEntity> videos) {
        Intent intent = new Intent(context, MediaUpLoadActivity.class);
        intent.putParcelableArrayListExtra("videos", videos);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar();
        mMediaUpLoadRV.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    protected void initData() {
        super.initData();
        mUpLoadAdapter = new MediaUpLoadAdapter(this);
        mMediaUpLoadRV.setAdapter(mUpLoadAdapter);

        mListVideoData = getIntent().getParcelableArrayListExtra("videos");
        if (mListVideoData == null)
            mListVideoData = new ArrayList<>();

        VideoEntity addItem = new VideoEntity();
        addItem.setUploadStatus(-1);
        mListVideoData.add(addItem);

        mUpLoadAdapter.setData(mListVideoData);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_up_load;
    }

    @Override
    protected View getTitlebarView() {
        return mToolbar;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.video_menu_edit:
                if (item.getTitle().toString().equals("编辑")) {
                    item.setTitle("取消");
                } else {
                    item.setTitle("编辑");
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            /** 数据库查询操作。
             * 第一个参数 uri：为要查询的数据库+表的名称。
             * 第二个参数 projection ： 要查询的列。
             * 第三个参数 selection ： 查询的条件，相当于SQL where。
             * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
             * 第四个参数 sortOrder ： 结果排序。
             */
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    // 视频ID:MediaStore.Audio.Media._ID
                    int videoIdIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                    int videoId;
                    if (videoIdIndex != -1)
                        videoId = cursor.getInt(videoIdIndex);
                    // 视频名称：MediaStore.Audio.Media.TITLE
                    int titleIndex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
                    String title;
                    if (titleIndex != -1)
                        title = cursor.getString(titleIndex);
                    // 视频路径：MediaStore.Audio.Media.DATA
                    int videoPathIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                    String videoPath;
                    if (videoPathIndex != -1)
                        videoPath = cursor.getString(videoPathIndex);
                    // 视频时长：MediaStore.Audio.Media.DURATION
                    int durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                    int duration;
                    if (durationIndex != -1)
                        duration = cursor.getInt(durationIndex);
                    // 视频大小：MediaStore.Audio.Media.SIZE
                    int sizeIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
                    long size;
                    if (sizeIndex != -1)
                        size = cursor.getLong(sizeIndex);
                    // 视频缩略图路径：MediaStore.Images.Media.DATA
                    int imagePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imagePath;
                    if (imagePathIndex != -1)
                        imagePath = cursor.getString(imagePathIndex);
                    // 缩略图ID:MediaStore.Audio.Media._ID
                    int imageIdIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    int imageId = -1;
                    if (imageIdIndex != -1)
                        imageId = cursor.getInt(imageIdIndex);
                    // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                    // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
                    Bitmap bitmap;
                    if (imageId != -1)
                        bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

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
                }
                cursor.close();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

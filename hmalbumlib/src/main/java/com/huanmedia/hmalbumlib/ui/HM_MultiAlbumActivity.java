package com.huanmedia.hmalbumlib.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huanmedia.hmalbumlib.HM_AlbumConifg;
import com.huanmedia.hmalbumlib.HM_DividerGridItemDecoration;
import com.huanmedia.hmalbumlib.HM_MulitiAlbumLoader;
import com.huanmedia.hmalbumlib.HM_StartAlbum;
import com.huanmedia.hmalbumlib.HM_StartAlbumPhoto;
import com.huanmedia.hmalbumlib.HM_StartCorp;
import com.huanmedia.hmalbumlib.R;
import com.huanmedia.hmalbumlib.extar.HM_AlbumFileTraversal;
import com.huanmedia.hmalbumlib.extar.HM_DisplayUtil;
import com.huanmedia.hmalbumlib.extar.HM_ExtarCropImageView;
import com.huanmedia.hmalbumlib.extar.HM_ImgData;
import com.huanmedia.hmalbumlib.extar.HM_StorageUtil;
import com.huanmedia.hmalbumlib.ui.adapter.HM_AlbumAdapter;
import com.huanmedia.hmalbumlib.ui.adapter.HM_PhotoDirAdapter;
import com.huanmedia.hmalbumlib.ui.widget.ProgressDialog;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.orhanobut.logger.Logger;
import com.top.zibin.luban.Luban;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


//TODO　由于之前规划中未涉及到自定义相册因此如果需要后期版本中加入自定义相册功能
public class HM_MultiAlbumActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<HM_AlbumFileTraversal>> {

    RecyclerView albumRv;
    private int spaceCount = 3;//默认列数
    private List<HM_AlbumFileTraversal> mAlbumFileTraversals;
    private PopupWindow pop;
    private HM_PhotoDirAdapter albumItemAadpter;
    private RecyclerView pop_lv;
    private ImageView downArrow;
    private int maxChoose = 3;//最多选择
    private ArrayList<HM_ImgData> chooseImage;//已经选择的图片
    private boolean isCrop;//是否裁剪
    //裁剪
    private HM_ExtarCropImageView.CropMode cropMode;
    private Bitmap.CompressFormat compressFormat;
    private int compressquality;
    private int maxOutPutW;
    private int maxOutPutH;
    private boolean isShowCamera;
    private TextView mActionRight;
    private TextView mActionCenter;
    private TextView mActionLeft;
    private View mActionLayout;
    private HM_OpenUtils mOpenUtils;
    private File targetPath;
    private ProgressDialog mDialog;
    private Disposable imageDataProgress;


    @Override
    protected int getLayoutId() {
        return R.layout.hm_activity_multi_album;
    }
    @Override
    protected void initView() {
        albumRv = (RecyclerView) findViewById(R.id.album_rv);
        mActionRight = (TextView) findViewById(R.id.actionbar_right);
        mActionCenter = (TextView) findViewById(R.id.actionbar_center);
        mActionLeft = (TextView) findViewById(R.id.actionbar_lift);
        mActionLayout = findViewById(R.id.toolbar_layout);
        setToolbar();
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

        mOpenUtils = new HM_OpenUtils(getSharedPreferences("hmAlbum", MODE_PRIVATE));
        HM_AlbumAdapter adapter = new HM_AlbumAdapter(R.layout.hm_album_itme_imgs, null, spaceCount);
        isShowCamera = getIntent().getBooleanExtra(HM_StartAlbum.EXTRA_ALBUM_SHOWCAMERA, true);
        targetPath = (File) getIntent().getSerializableExtra(HM_StartAlbum.EXTRA_ALBUM_TARGETPATH);
        if (targetPath == null) {
            targetPath = HM_StorageUtil.getAlbumDir(this);
        }
        if (!isShowCamera) {//是否显示相机图标
            adapter.setCameraShow(false);
        }
        adapter.setOnCheckedPhoto(new HM_AlbumAdapter.OnCheckedPhoto() {
            @Override
            public void checkSize(int size) {
                countPhoto(size);
            }

            @Override
            public void openCamera() {
                mOpenUtils.openCamera(HM_MultiAlbumActivity.this, targetPath);
            }
        });
        albumRv.setLayoutManager(new GridLayoutManager(this, spaceCount));
        albumRv.setAdapter(adapter);
        albumRv.addItemDecoration(new HM_DividerGridItemDecoration(this, HM_DisplayUtil.dip2px(this, 2)));
        //已经选择的照片
        chooseImage = getIntent().getParcelableArrayListExtra(HM_StartAlbum.EXTRA_ALBUM_CHOOSE_DATA);
        if (chooseImage != null && chooseImage.size() > 0) {
            mActionRight.setText(String.format(Locale.getDefault(), "确定(%d)", chooseImage.size()));
            mActionRight.setTextColor(ContextCompat.getColor(this, R.color.hm_white));
        }
        //最多选择张数
        maxChoose = getIntent().getIntExtra(HM_StartAlbum.EXTRA_ALBUM_MAXCHOOSE, 1);
        //是否裁剪
        isCrop = getIntent().getBooleanExtra(HM_StartAlbum.EXTRA_ALBUM_ISCROP, false);
        if (isCrop) {
            initCrop(adapter);
        } else {
            initPhoto(adapter);
        }
    }

    private void initPhoto(HM_AlbumAdapter adapter) {
        if (chooseImage == null) {
            chooseImage = new ArrayList<>();
        }
        adapter.setChooseImage(chooseImage);
        adapter.setMaxChoose(maxChoose);
    }

    private void initCrop(HM_AlbumAdapter adapter) {
        cropMode = (HM_ExtarCropImageView.CropMode) getIntent().getSerializableExtra(HM_StartAlbum.EXTRA_CORP_CROPMODE);
        maxOutPutH = getIntent().getIntExtra(HM_StartAlbum.EXTRA_CORP_MAXOUTPUTH, 300);
        maxOutPutW = getIntent().getIntExtra(HM_StartAlbum.EXTRA_CORP_MAXOUTPUTW, 300);
        compressFormat = (Bitmap.CompressFormat) getIntent().getSerializableExtra(HM_StartAlbum.EXTRA_CORP_COMPRESSFORMAT);
        compressquality = getIntent().getIntExtra(HM_StartAlbum.EXTRA_CORP_COMPRESSQUALITY, 90);
        maxChoose = 1;
        chooseImage = null;
        adapter.setCrop(isCrop);
        adapter.setCropMode(cropMode);
        adapter.setMaxOutPutH(maxOutPutH);
        adapter.setMaxOutPutW(maxOutPutW);
        adapter.setComPressFromat(compressFormat);
        adapter.setCompressQuality(compressquality);
    }

    private void countPhoto(int size) {
        mActionRight.setText(String.format(Locale.getDefault(), "确定(%d)", size));
        if (size == 0) {
            mActionRight.setTextColor(ContextCompat.getColor(this, R.color.hm_text_hint));
            mActionRight.setClickable(false);
        } else {
            mActionRight.setTextColor(ContextCompat.getColor(this, R.color.hm_white));
            mActionRight.setClickable(true);
        }
    }

    @Override
    protected View getTitlebarView() {
        return findViewById(R.id.toolbar);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        downArrow = ((ImageView) findViewById(R.id.album_photo_page_down_arrow));
        mActionCenter.setText("所有照片");
        mActionRight.setText(String.format(Locale.getDefault(), "确定(%d)", 0));
        mActionRight.setTextColor(ContextCompat.getColor(this, R.color.hm_text_hint));
        mActionRight.setClickable(false);
        mActionRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseImage != null && chooseImage.size() > 0)
                    onBackPressed(chooseImage);
            }
        });
        mActionLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mActionCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pop == null) {// 初始化popwindow
                    getPopWindow();
                    if (pop == null) return;
                    pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (downArrow != null)
                                downArrow.animate().rotationX(0).setDuration(200);
                        }
                    });
                }

                if (pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();

                } else {
//                // 显示窗口
                    pop.showAsDropDown(mActionLayout, 0, 0);
                    if (downArrow != null)
                        downArrow.animate().rotationX(180f).setDuration(200);
                }


            }
        });

    }


    public void onBackPressed(final ArrayList<HM_ImgData> imgDatas) {
        if (imgDatas != null) {
            if (!isCrop) {
                List<String> pathList = new ArrayList<>();
                for (int i = 0; i < imgDatas.size(); i++) {
                    String path = imgDatas.get(i).getImage();
                    if (Check.isEmpty(path)) continue;
                    pathList.add(path);
                }

               imageDataProgress= Flowable.just(pathList)
                        .observeOn(Schedulers.io())
                        .map(new Function<List<String>, List<File>>() {
                            @Override
                            public List<File> apply(@NonNull List<String> list) throws Exception {
                                // 同步方法直接返回压缩后的文件
                                return Luban.with(HM_MultiAlbumActivity.this)
                                        .ignoreBy(100)
                                        .setTargetDir(targetPath.getAbsolutePath())
                                        .load(list)
                                        .get();
                            }
                        })
                       .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                if (mDialog == null){
                                    mDialog = new ProgressDialog(HM_MultiAlbumActivity.this);
                                    mDialog.setCancelable(false);
                                    mDialog.setCanceledOnTouchOutside(false);
                                }
                                if (!mDialog.isShowing()) {
                                    try {
                                        mDialog.show();
                                    }catch (Exception ignored){

                                    }
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (mDialog != null && !mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                            }
                        })
                        .subscribe(new Consumer<List<File>>() {
                            @Override
                            public void accept(List<File> o) throws Exception {
                                for (int i = 0; i < o.size(); i++) {
                                    Logger.i("File:%s", o.get(i).getAbsoluteFile());
                                }
                                for (int i = 0; i < imgDatas.size(); i++) {
                                    imgDatas.get(i).imgPath = o.get(i).getPath();
                                }
                                Intent intent = new Intent();
                                intent.putParcelableArrayListExtra("chooseImages", imgDatas);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (throwable instanceof OutOfMemoryError) {
                                    Toast.makeText(HM_MultiAlbumActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HM_MultiAlbumActivity.this, "图片处理错误:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("chooseImages", imgDatas);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != imageDataProgress && !imageDataProgress.isDisposed()){
            imageDataProgress.dispose();
            imageDataProgress=null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<HM_ImgData> chooseimage;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case HM_StartAlbumPhoto.REQUESTCODE:
                    chooseimage = data.getParcelableArrayListExtra("chooseImages");
                    boolean resultOk = data.getBooleanExtra("resultOk", false);
                    boolean imageForCamera = data.getBooleanExtra("imageForCamera", false);

                    if (chooseimage == null) return;

                    if (!resultOk) {
                        HM_AlbumAdapter adapter = ((HM_AlbumAdapter) albumRv.getAdapter());
                        adapter.getChooseImage().clear();
                        adapter.getChooseImage().addAll(chooseimage);
                        adapter.notifyDataSetChanged();
                        countPhoto(chooseimage.size());
                    } else {
                        if (imageForCamera) {//如果是拍照返回需要将已经选中照片加上拍照的照片一起返回
                            chooseimage.addAll(0, chooseImage);
                        }
                        onBackPressed(chooseimage);
                    }


                    break;
                case HM_OpenUtils.REQUEST_CODE_CAMERA:
                    String path = mOpenUtils.getReturnImagePath(requestCode, resultCode, data, this);
                    if (path == null && path.length() == 0) return;
                    //扫描图片
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
                    if (isCrop) {
                        new HM_StartCorp.Bulide(HM_MultiAlbumActivity.this, HM_AlbumConifg.getInstance().getEngine())
                                .setCorpMode(cropMode)
                                .setCompressQuality(compressquality)
                                .setCompressFormat(compressFormat)
                                .setMaxOutPutH(maxOutPutH)
                                .setMaxOutPutW(maxOutPutW)
                                .setTargetPaht(targetPath)
                                .setRequestCode(HM_StartCorp.REQUEST_CODE_CROP)
                                .setChooseImage(new HM_ImgData(path)).bulide();
                    } else {
                        showImageForCamera(path);
                    }
                    break;
                case HM_StartCorp.REQUEST_CODE_CROP:
                    HM_ImgData cropImage = data.getParcelableExtra("cropImage");
                    if (cropImage != null) {
                        chooseimage = new ArrayList<>();
                        chooseimage.add(cropImage);
                        onBackPressed(chooseimage);
                    }
                    break;
            }
        }
    }

    private void showImageForCamera(String imagePath) {
        HM_ImgData imgData = new HM_ImgData();
        imgData.imgPath = imagePath;
        List<HM_ImgData> datas = new ArrayList<>();
        datas.add(imgData);
        new HM_StartAlbumPhoto.Bulide(this, HM_AlbumConifg.getInstance().getEngine())
                .setList(datas)
                .setChooseImages(datas)
                .setCurrentSelect(0)
                .setRequestCode(HM_StartAlbumPhoto.REQUESTCODE)
                .setMode(HM_StartAlbumPhoto.AlbumPhotoMode.IMAGEFORCAMERA)
                .bulide();
    }

    public void getPopWindow() {
        if (mAlbumFileTraversals != null && mAlbumFileTraversals.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            // 引入窗口配置文件
            View view = inflater.inflate(R.layout.hm_album_popwindows, null);
            view.findViewById(R.id.album_itme_popwindows_rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });

            pop_lv = (RecyclerView) view.findViewById(R.id.lv_album);
            albumItemAadpter = new HM_PhotoDirAdapter(R.layout.hm_album_itme_popwindows, mAlbumFileTraversals);
            albumItemAadpter.setDirItemClickListener(new HM_PhotoDirAdapter.OnDirItemClickListener() {
                @Override
                public void dirClick(View view, int position) {
                    HM_AlbumAdapter albumAdapter = (HM_AlbumAdapter) albumRv.getAdapter();
                    HM_AlbumFileTraversal album = mAlbumFileTraversals.get(position);
                    if (isShowCamera) {
                        if (position != 0) {
                            albumAdapter.setCameraShow(false);
                        } else {
                            albumAdapter.setCameraShow(true);
                        }
                    }
                    albumAdapter.setNewData(album.filecontent);
                    albumItemAadpter.setmCurrentAlbum(album.filename);
                    albumItemAadpter.notifyDataSetChanged();
                    mActionCenter.setText(album.filename);
                    pop.dismiss();
                }
            });
            pop_lv.setAdapter(albumItemAadpter);
            // 创建PopupWindow对象
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

            int hight = (getResources().getDisplayMetrics().heightPixels - (mActionLayout.getHeight()));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                hight -= HM_DisplayUtil.getStatusBarHeight(getApplication());
            }
            pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, (hight), false);
            // 需要设置一下此参数，点击外边可消失
            pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));
            // 设置点击窗口外边窗口消失
            pop.setOutsideTouchable(true);
            // 设置此参数获得焦点，否则无法点击
            pop.setFocusable(true);
        }
    }


    @Override
    public Loader<List<HM_AlbumFileTraversal>> onCreateLoader(int id, Bundle args) {
        return new HM_MulitiAlbumLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<HM_AlbumFileTraversal>> loader, List<HM_AlbumFileTraversal> albumFileTraversals) {
        if (albumFileTraversals != null && albumFileTraversals.size() > 0) {
            HM_AlbumAdapter adapter = (HM_AlbumAdapter) albumRv.getAdapter();
            this.mAlbumFileTraversals = albumFileTraversals;
            HM_AlbumFileTraversal album = mAlbumFileTraversals.get(0);
            adapter.setNewData(album.filecontent);
            mActionCenter.setText(album.filename);
        } else {
            mActionCenter.setClickable(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<HM_AlbumFileTraversal>> loader) {
        HM_AlbumAdapter adapter = (HM_AlbumAdapter) albumRv.getAdapter();
        adapter.setNewData(null);
    }
}

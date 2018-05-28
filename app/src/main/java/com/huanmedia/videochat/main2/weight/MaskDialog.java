package com.huanmedia.videochat.main2.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.FUManager;
import com.huanmedia.ilibray.utils.DevUtils;
import com.huanmedia.ilibray.utils.FileUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.makeramen.roundedimageview.RoundedImageView;

import org.dync.giftlibrary.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.IDListener;
import io.reactivex.disposables.CompositeDisposable;
import mvp.data.store.glide.GlideUtils;

import static com.faceunity.FUManager.getmBeautyConfig;

/**
 * Create by Administrator
 * time: 2018/1/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MaskDialog extends Dialog {
    private MaskChooseListener mMaskChooseListener;
    private ViewPager mMaskVp;
    private RadioGroup mMaskRGP;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private MaskPagerAdapter mAdapter;
    private int mCurrentPage;
    private boolean mChooseEnable = true;

    public void setMaskChooseListener(MaskChooseListener maskChooseListener) {
        mMaskChooseListener = maskChooseListener;
        if (mAdapter != null) {
            mAdapter.setMaskChooseListener(mMaskChooseListener);
        }
    }


    public static int getCurrentMask() {
        if (getmBeautyConfig() == null) return 0;
        return Check.isEmpty(getmBeautyConfig().getMask()) ? 0 : 1;
    }


    public MaskDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MaskDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.customDialog_Gift);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mask);
        if (getWindow() != null) {
            getWindow().getAttributes().gravity = Gravity.BOTTOM;
            getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        initView();
        initData();
    }

    @Override
    public void show() {
        super.show();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCompositeDisposable != null)
            mCompositeDisposable.dispose();
    }


    private void initData() {
        List<List<SkinMode>> localData = MaskHandler.getMaskDatas();
        addCancelListData(localData);
        if (localData != null && localData.size() > 0) {//加载本地数据
            mAdapter.setData(localData);
            mAdapter.notifyDataSetChanged();
        }
        if (!MaskHandler.isInitData()) {
            mCompositeDisposable.add(new MainRepostiory().facelist()//从网络加载数据
                    .map(lists -> {
                        int changeCount = MaskHandler.saveFaceCaches(lists);
                        if (changeCount > 0) {
                            addCancelListData(lists);
                            return lists;
                        } else {
                            return new ArrayList<List<SkinMode>>();
                        }
                    })
                    .subscribe(response -> {
                        if (response.size() > 0) {
                            mAdapter.setData(response);
                            mAdapter.notifyDataSetChanged();
                        }
                        MaskHandler.setInitData(true);
                    }, Throwable::printStackTrace));
        }
    }

    /**
     * 增加第一个取消按钮
     */
    private void addCancelListData(List<List<SkinMode>> listData) {
        for (List<SkinMode> item : listData) {
            item.add(0, new SkinMode());
        }
    }


    private void initView() {
        mMaskVp = findViewById(R.id.dialog_mask_vp);
        mMaskRGP = findViewById(R.id.dialog_mask_rg);

        mMaskVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                ((RadioButton) mMaskRGP.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter = new MaskPagerAdapter(new ArrayList<>());
        mAdapter.setMaskChooseListener(mMaskChooseListener);
        mMaskVp.setAdapter(mAdapter);

        mMaskRGP.getChildAt(0).performClick();
        mMaskRGP.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mMaskVp.setCurrentItem(i, true);
                    break;
                }
            }

        });
    }

    public int clearMask(int maskLayout) {
        if (maskLayout == 0) {
            FUManager.clearMask();
            clearFilter();
            return 0;
        } else {
            FUManager.clearMaskByLayout(maskLayout);
            if (getmBeautyConfig().getMask() != null && getmBeautyConfig().getMask().size() > 0) {
                return 1;
            } else {
                clearFilter();
                return 0;
            }
        }
    }

    public void clearFilter() {
        FUManager.clearmPagePosition();
    }

    public void setChooseEnable(boolean chooseEnable) {
        mChooseEnable = chooseEnable;
        if (mAdapter != null) {
            mAdapter.setChooseEnable(mChooseEnable);
        }
    }


    public interface MaskChooseListener {
        /**
         * @param chooseMask true 选择面具 false 取消面具
         */
        void choose(boolean chooseMask);
    }

    private static class MaskPagerAdapter extends PagerAdapter {
        private List<List<SkinMode>> mSkinMode;
        private SparseArray<SparseArray<SkinMode>> mFilterSelect;
        private SparseArray<BaseQuickAdapter<SkinMode, BaseViewHolder>> mAdapters;
        private LayoutInflater mInft;
        private int pageIndex;
        private Handler mHandler = new Handler(Looper.getMainLooper()) {
        };
        private MaskChooseListener mMaskChooseListener;
        private boolean mChooseEnable = true;

        public void setMaskChooseListener(MaskChooseListener maskChooseListener) {
            mMaskChooseListener = maskChooseListener;
        }

        public MaskPagerAdapter(List<List<SkinMode>> skinMode) {
            if (skinMode == null) skinMode = new ArrayList<>();
            this.mSkinMode = skinMode;
            this.mAdapters = new SparseArray<>();
            this.mFilterSelect = new SparseArray<>();
            if (skinMode.size() > 0 && FUManager.getmPagePosition() != null) {//获取默认选择数据
                SparseArray<SkinMode> mode = new SparseArray<>();
                mode.put(FUManager.getmPagePosition()[1], mSkinMode.get(FUManager.getmPagePosition()[0]).get(FUManager.getmPagePosition()[1]));
                mFilterSelect.put(FUManager.getmPagePosition()[0], mode);
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mInft == null) {
                mInft = LayoutInflater.from(container.getContext());
            }
            View view = mInft.inflate(R.layout.dialog_item_mask, container, false);
            view.setTag(position);
            RecyclerView rv = view.findViewById(R.id.main_rv_select_filter);
            rv.setItemAnimator(null);
            BaseQuickAdapter<SkinMode, BaseViewHolder> adapter = mAdapters.get(position);
            if (adapter == null) {
                adapter = createRvAdapter(container, position);
                mAdapters.put(position, adapter);
            }
            rv.setAdapter(adapter);
            container.addView(view);
            return view;
        }


        /**
         * 创建Adapter 并设置监听
         *
         * @param container
         * @param position
         * @return
         */
        private BaseQuickAdapter<SkinMode, BaseViewHolder> createRvAdapter(@NonNull ViewGroup container, int position) {
            BaseQuickAdapter<SkinMode, BaseViewHolder> adapter = new BaseQuickAdapter<SkinMode, BaseViewHolder>(R.layout.item_main_select_filter, mSkinMode.get(position)) {
                @Override
                protected void convert(BaseViewHolder helper, SkinMode item) {
                    if (helper.getAdapterPosition() == 0) {
                        helper.setGone(R.id.item_main_fl_noraml, false);
                        helper.setGone(R.id.item_main_iv_cancel, true);
                    } else {
                        helper.setGone(R.id.item_main_fl_noraml, true);
                        helper.setGone(R.id.item_main_iv_cancel, false);
                    }
                    RoundedImageView riv = helper.getView(R.id.item_main_iv_filter_type);
                    helper.itemView.setTag(position);

//                    helper.setGone(R.id.item_main_select_iv_down,!new File(MaskHandler.getCacheDir(), item.getName()).exists())
                    helper.setGone(R.id.item_main_select_iv_down, !FileUtils.hasListFileExists(MaskHandler.getCacheDir(), item.getPackName()))
                            .setGone(R.id.item_main_select_pb, item.isDownload() == 1);
                    if (mFilterSelect.size() > 0) {
                        SparseArray<SkinMode> pagData = mFilterSelect.get(position);
                        SkinMode chooseFilter = null;
                        if (pagData != null) {
                            chooseFilter = pagData.get(helper.getAdapterPosition());
                        }
                        if (chooseFilter != null && chooseFilter.getId() == item.getId()) {
                            if (FUManager.getmPagePosition() == null)
                                riv.setBorderColor(ContextCompat.getColor(mContext, R.color.transparent));
                            else
                                riv.setBorderColor(ContextCompat.getColor(mContext, R.color.color_f65aa0));
                        } else {
                            riv.setBorderColor(ContextCompat.getColor(mContext, R.color.transparent));
                        }
                    } else {
                        helper.setChecked(R.id.item_main_iv_cancel, false);
                        riv.setBorderColor(ContextCompat.getColor(mContext, R.color.transparent));
                    }
                    GlideUtils.getInstance().loadBitmapNoAnim(container.getContext(), item.getImg_thumb(), riv);
                }
            };
            adapter.setOnItemClickListener((adapter1, view1, position1) -> {
                Integer pagePosition = (Integer) view1.getTag();
                boolean clearSelect = false;
                if (position1 == 0) {
                    if (mFilterSelect.size() > 0) {
                        int pageindex = mFilterSelect.keyAt(0);
                        int upPosition = mFilterSelect.get(pageindex).keyAt(0);
                        if (pagePosition != null && pageindex == pagePosition) {
                            mFilterSelect.clear();
                            adapter1.notifyItemChanged(upPosition);
                        } else {
                            mFilterSelect.clear();
                            notifyDataSetChanged(pageindex);
                        }
                    }
                    FUManager.setMaskByNameList(new ArrayList<>(), pagePosition, position);
                    return;
                }
                SkinMode maskMode = mSkinMode.get(pagePosition).get(position1);
//                if (!MaskHandler.checkFileFull(maskMode)) {//判断是否下载道具
                if (!FileUtils.hasListFileExists(MaskHandler.getCacheDir(), maskMode.getPackName())) {
                    downloadMask(pagePosition, position1);
                } else {//已下载加载道具
                    if (mFilterSelect.size() > 0) {
                        int pageindex = mFilterSelect.keyAt(0);
                        int upPosition = mFilterSelect.get(pageindex).keyAt(0);
                        if (pagePosition != null && pageindex == pagePosition) {
                            clearSelect = upPosition == position1;//如果相等就清除选中
                            mFilterSelect.clear();
                            adapter1.notifyItemChanged(upPosition);
                        } else {
                            mFilterSelect.clear();
                            notifyDataSetChanged(pageindex);
                        }
                    }
                    List<String> mask = new ArrayList<>();
                    if (mChooseEnable) {//被用户揭面后不可选择面具
                        if (!clearSelect) {//开启面具
                            SparseArray<SkinMode> item = new SparseArray<>();
                            item.put(position1, maskMode);
                            mFilterSelect.put(pagePosition, item);
                            for (String itemName : maskMode.getPackName()) {
                                mask.add(new File(MaskHandler.getCacheDir(), itemName).getAbsolutePath());
                            }
                            if (mMaskChooseListener != null) {
                                mMaskChooseListener.choose(true);
                            }
                        } else {//取消面具
                            if (mMaskChooseListener != null) {
                                mMaskChooseListener.choose(false);
                            }
                            mask = new ArrayList<>();
                        }
                        FUManager.setMaskByNameList(mask, pagePosition, position1);
                    } else {
                        FUManager.setMaskByNameList(new ArrayList<>(), pagePosition, position);
                        Toast.makeText(view1.getContext(), "对方用户揭面后无法选择面具", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyItemChanged(position1);
                }
            });
            return adapter;
        }

        public BaseQuickAdapter<SkinMode, BaseViewHolder> getAdapterForPage(int pageIndex) {
            return mAdapters.get(pageIndex);
        }

        //下载道具包
        private void downloadMask(int page, int position) {
            BaseQuickAdapter<SkinMode, BaseViewHolder> adapter = getAdapterForPage(page);
            SkinMode maskMode = adapter.getData().get(position);
            List<String> packUrlList = maskMode.getPackUrlList();
            List<String> packNameList = maskMode.getPackName();
            //下载并没有销毁会再后台下载
            if (maskMode.isDownload() != 1) {
                //取消面具下载
                for (String packUrl : packUrlList) {
                    DLManager.getInstance(FApplication.getApplication()).dlCancel(packUrl);
                }
                //删除下载了一半的面具
                for (String packName : packNameList) {
                    File oldFile = new File(MaskHandler.getCacheDir(), packName);//删除旧文件
                    oldFile.delete();
                }
            }
            final int[] dowanFinishPosition = {0};
            for (int i = 0; i < packUrlList.size(); i++) {
                downloadMaskItem(page, position, packUrlList.get(i), packNameList.get(i), new DownloadAllCallBack() {
                    @Override
                    public void downloadFinish() {
                        dowanFinishPosition[0]++;
                        if (dowanFinishPosition[0] == packUrlList.size()) {
                            mHandler.post(() -> {
                                BaseQuickAdapter<SkinMode, BaseViewHolder> upAdapter = getAdapterForPage(page);
                                if (upAdapter != null) {
                                    SkinMode upMaskMode = upAdapter.getItem(position);
                                    if (upMaskMode != null) {
                                        upMaskMode.setDownload(2);
                                        upAdapter.notifyItemChanged(position);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }


        private interface DownloadAllCallBack {
            void downloadFinish();
        }

        public void downloadMaskItem(int page, int position, String packUrl, String packName, DownloadAllCallBack downloadAllCallBack) {
            DLManager.getInstance(FApplication.getApplication()).dlStart(packUrl, MaskHandler.getCacheDir().getAbsolutePath(), packName, new IDListener() {
                @Override
                public void onPrepare() {
                }

                @Override
                public void onStart(String fileName, String realUrl, int fileLength) {
                    mHandler.post(() -> {
                        BaseQuickAdapter<SkinMode, BaseViewHolder> upAdapter = getAdapterForPage(page);
                        if (upAdapter != null) {
                            SkinMode upMaskMode = upAdapter.getItem(position);
                            if (upMaskMode != null) {
                                upMaskMode.setDownload(1);
                                upAdapter.notifyItemChanged(position);
                            }
                        }
                    });
                }

                @Override
                public void onProgress(int progress) {
                }

                @Override
                public void onStop(int progress) {
                    mHandler.post(() -> {
                        BaseQuickAdapter<SkinMode, BaseViewHolder> upAdapter = getAdapterForPage(page);
                        if (upAdapter != null) {
                            SkinMode upMaskMode = upAdapter.getItem(position);
                            if (upMaskMode != null) {
                                upMaskMode.setDownload(0);
                                upAdapter.notifyItemChanged(position);
                            }
                        }
                    });
                }

                @Override
                public void onFinish(File file) {
                    downloadAllCallBack.downloadFinish();
                }

                @Override
                public void onError(int status, String error) {
                    mHandler.post(() -> {
                        BaseQuickAdapter<SkinMode, BaseViewHolder> upAdapter = getAdapterForPage(page);
                        if (upAdapter != null) {
                            SkinMode upMaskMode = upAdapter.getItem(position);
                            if (upMaskMode != null) {
                                upMaskMode.setDownload(0);
                                upAdapter.notifyItemChanged(position);
                            }
                        }
                    });
                }
            });
        }

        private void notifyDataSetChanged(int pageindex) {
            pageIndex = pageindex;
            super.notifyDataSetChanged();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mSkinMode.size();
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            Integer integer = (Integer) ((View) object).getTag();
            if (pageIndex == integer) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setData(List<List<SkinMode>> data) {
            mSkinMode = data;
        }

        public void setChooseEnable(boolean chooseEnable) {
            mChooseEnable = chooseEnable;
        }
    }


}

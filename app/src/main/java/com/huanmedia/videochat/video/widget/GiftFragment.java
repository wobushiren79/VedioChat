package com.huanmedia.videochat.video.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mvp.data.store.glide.GlideUtils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GiftFragment extends Fragment {
    private static final String ARG_DATA = "data";
    private static final String ARG_PAGE = "page";
    RecyclerView mDialogFragmentGiftRv;
    private GiftClickListener mGiftClickListener;
    private ArrayList<GiftEntity> data;
    private View mRootView;
    private SparseArray<Integer> mGiftFilter;
    private int mPage;

    private @GiftFragmentDialog.GiftDialogStyle int giftDialogStyle;

    public static GiftFragment newInstance(ArrayList<GiftEntity> data, int page,@GiftFragmentDialog.GiftDialogStyle int giftDialogStyle) {
        GiftFragment fragment = new GiftFragment(giftDialogStyle);
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_DATA, data);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }


    public GiftFragment(@GiftFragmentDialog.GiftDialogStyle int giftDialogStyle) {
        // Required empty public constructor
        this.giftDialogStyle=giftDialogStyle;
    }

    public void setGiftClickListener(GiftClickListener giftClickListener) {
        mGiftClickListener = giftClickListener;
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_fragment_gift, container, false);
        }
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = getArguments().getParcelableArrayList(ARG_DATA);
            mPage = getArguments().getInt(ARG_PAGE, -1);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(getView());
    }

    @Override
    public void onDestroy() {
        Logger.i("onDestroy："+mPage);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Logger.i("onDetach："+mPage);
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        Logger.i("onAttach："+mPage);
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Logger.i("onDestroyView："+mPage);
        super.onDestroyView();
    }

    protected void initView(View view) {
        mDialogFragmentGiftRv = view.findViewById(R.id.dialog_fragment_gift_rv);
        int layoutId;
        if(giftDialogStyle==GiftFragmentDialog.GiftDialogStyle.AddTime){
            layoutId=R.layout.dialog_item_gift_addtime;
        }else{
            layoutId=R.layout.dialog_item_gift;
        }
        mDialogFragmentGiftRv.setAdapter(new BaseQuickAdapter<GiftEntity, BaseViewHolder>(layoutId, data) {
            @Override
            protected void convert(BaseViewHolder helper, GiftEntity item) {
                helper.setText(R.id.dialog_item_gift_tv_name, Check.checkReplace(item.getName()));
                Integer position = mGiftFilter.get(mPage);
                if (mGiftFilter != null && mGiftFilter.get(mPage) != null && position == helper.getAdapterPosition()) {
                    if(giftDialogStyle== GiftFragmentDialog.GiftDialogStyle.AddTime){
                        helper.setBackgroundRes(R.id.dialog_gift_cl, R.drawable.dialog_gift_selectbg_select);
                    }else{
                        helper.setBackgroundRes(R.id.dialog_gift_cl, R.drawable.dialog_overtime_selectbg_stroke);
                    }
                } else {
                    helper.setBackgroundRes(R.id.dialog_gift_cl, 0);
                }
                String path = "file:///android_asset/" + item.get_localMode().getFistImageAbsolute();
                GlideUtils.getInstance().loadBitmapNoAnim(GiftFragment.this, path, helper.getView(R.id.dialog_item_gift_iv_cover));

                //新增时长
                if(giftDialogStyle== GiftFragmentDialog.GiftDialogStyle.AddTime){
                    helper.setText(R.id.dialog_item_gift_tv_addTime,"时长+"+item.getAddtime()+"s");
                    helper.setText(R.id.dialog_item_gift_tv_price, String.valueOf(item.getCoin()));
                }else{
                    helper.setText(R.id.dialog_item_gift_tv_price, String.valueOf(item.getCoin()) + "钻石");
                }
            }
        });
        mDialogFragmentGiftRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mGiftFilter != null) {
                    if (mGiftFilter.size()!=0){
                        int oldPage = mGiftFilter.keyAt(0);
                        int oldPosition = mGiftFilter.get(oldPage);
                        mGiftFilter.clear();
                        if (oldPage != mPage && mGiftClickListener != null) {
                            mGiftClickListener.clearPageChoose(oldPage, oldPosition);
                        } else {
                            adapter.notifyItemChanged(oldPosition);
                        }
                    }

                    mGiftFilter.put(mPage, position);
                    adapter.notifyItemChanged(position);
                    if (mGiftClickListener != null) {
                        mGiftClickListener.onGiftClick(view, (GiftEntity) adapter.getData().get(position));
                    }
                }
            }
        });
    }

    public void setGiftFilter(SparseArray<Integer> giftFilter) {
        mGiftFilter = giftFilter;
    }

    public void notifDataChange(int position) {
        if (mDialogFragmentGiftRv != null && mDialogFragmentGiftRv.getAdapter() != null)
            mDialogFragmentGiftRv.getAdapter().notifyItemChanged(position);
    }

    public interface GiftClickListener {
        void onGiftClick(View view, GiftEntity entity);

        /**
         * 清除其他页面选中项目
         */
        void clearPageChoose(int page, int position);
    }
}

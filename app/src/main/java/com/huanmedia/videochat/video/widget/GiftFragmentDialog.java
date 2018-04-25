package com.huanmedia.videochat.video.widget;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ListPopupWindow;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.ViewFindUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.video.model.GiftCountMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Create by Administrator
 * time: 2017/12/14.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

@SuppressLint("ValidFragment")
public class GiftFragmentDialog extends DialogFragment implements View.OnClickListener, GiftFragment.GiftClickListener {

    @IntDef({GiftFragmentDialog.GiftDialogStyle.Normal,//支付
            GiftFragmentDialog.GiftDialogStyle.AddTime})//兑换
    @Retention(RetentionPolicy.SOURCE)
    public @interface GiftDialogStyle {
        int Normal = 1, AddTime = 2;
    }

    public static final String TAG = "GiftFragmentDialog";
    private ViewPager mDialogGiftVP;
    private LinearLayout mDialogGiftLlDot;
    private TextView mDialogGiftTvCoin;
    private TextView mDialogGiftTvPay;
    private Button mDialogGiftBtnOk;
    private GiftEventListener mGiftEventListener;
    private GiftEntity mCurrentItem;
    private List<ArrayList<GiftEntity>> mDatas = new ArrayList<>();
    private List<Fragment> mFragments;
    private SparseArray<Integer> mSelectFilter;
    private Button mDialogGiftBtnCount;
    private Disposable subscription;
    private List<GiftCountMode> data;
    private PopAdapter mAopAdapter;
    private ListPopupWindow mListPopupWindow;
    private View mView;

    private @GiftDialogStyle int dialogSytle;

    public GiftFragmentDialog(@GiftDialogStyle int dialogSytle){
         this.dialogSytle=dialogSytle;
    }

    public void setDatas(List<ArrayList<GiftEntity>> datas) {
        mDatas = datas;
    }


    public void setGiftEventListener(GiftEventListener giftEventListener) {
        mGiftEventListener = giftEventListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mView==null){
            switch (dialogSytle){
                case GiftDialogStyle.Normal:
                    mView = inflater.inflate(R.layout.dialog_gift, container, false);
                    break;
                case GiftDialogStyle.AddTime:
                    mView = inflater.inflate(R.layout.dialog_gift_addtime, container, false);
                    break;
            }
            initView(mView);
        }
        else {
            ViewGroup viewGroup= (ViewGroup) mView.getParent();
            initGifts();
            viewGroup.removeView(mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.customDialog_Gift);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.gravity = Gravity.BOTTOM;
            windowParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(windowParams);
        }
    }


    private void initView(View view) {
        mSelectFilter = new SparseArray<Integer>();
        mDialogGiftVP = view.findViewById(R.id.dialog_gift_vp);
        mDialogGiftBtnOk = view.findViewById(R.id.dialog_gift_btn_ok);
        mDialogGiftBtnCount = view.findViewById(R.id.dialog_gift_btn_count);
        mDialogGiftLlDot = view.findViewById(R.id.dialog_gift_ll_dot);
        mDialogGiftTvCoin = view.findViewById(R.id.dialog_gift_tv_coin);
        mDialogGiftTvPay = view.findViewById(R.id.dialog_gift_tv_pay);
        mDialogGiftBtnOk.setOnClickListener(this);
        mDialogGiftBtnCount.setOnClickListener(this);
        mDialogGiftTvPay.setOnClickListener(this);
        mFragments = new ArrayList<>();
        mDialogGiftVP.addOnPageChangeListener(new ViewPagerIndicator(getContext(), mDialogGiftVP, mDialogGiftLlDot, mFragments.size()));
        initGifts();
        setCoin();
    }

    private void initGifts() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            GiftFragment fragment = GiftFragment.newInstance(mDatas.get(i), i,dialogSytle);
            fragment.setGiftClickListener(this);
            fragment.setGiftFilter(mSelectFilter);
            mFragments.add(fragment);
        }
        mDialogGiftVP.setAdapter(new GiftPageAdapter(getChildFragmentManager(), mFragments));
    }

    private void setCoin() {
        if ((mGiftEventListener != null) && (mDialogGiftTvCoin != null)) {
            mDialogGiftTvCoin.setText(Check.checkReplace(mGiftEventListener.startCoin() + "", "0"));
        }
//        if (mListPopupWindow!=null){
//            if (mAopAdapter!=null&& mAopAdapter.mCurrentSelect!=null){
//                mDialogGiftBtnCount.setText(mAopAdapter.mCurrentSelect.valueAt(0).getPayCount());
//            }
//        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_gift_btn_ok:
                //确定
                if (mGiftEventListener != null && mCurrentItem != null) {
                        mCurrentItem.setPayCount(Integer.parseInt(mDialogGiftBtnCount.getText().toString()));
                        mGiftEventListener.okBtn(mCurrentItem);
                }
                break;
            case R.id.dialog_gift_tv_pay:
                //充值
                if (mGiftEventListener != null) {
                    mGiftEventListener.pay();
                }
                break;
            case R.id.dialog_gift_btn_count:
                showCountItem();
                break;
        }

    }

    @SuppressLint("RestrictedApi")
    private void showCountItem() {
        if (data == null)
            data = Arrays.asList(
                    new GiftCountMode("一生一世", 1314),
                    new GiftCountMode("我爱你", 520),
                    new GiftCountMode("要抱抱", 188),
                    new GiftCountMode("长长久久", 99),
                    new GiftCountMode("一切顺利", 66),
                    new GiftCountMode("十全十美", 10),
                    new GiftCountMode("一心一意", 1)
            );
        if (mListPopupWindow==null){
            mListPopupWindow = new ListPopupWindow(getContext());
//                        mListPopupWindow = new ListPopupWindow(getContext(),null,0);
            mListPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_list_item));
            mListPopupWindow.setWidth(mDialogGiftBtnOk.getWidth() + mDialogGiftBtnCount.getWidth());
            mListPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            mAopAdapter = new PopAdapter(data,Integer.parseInt(mDialogGiftBtnCount.getText().toString()));
            mAopAdapter.setPopSelectListener((position, mode) -> {
                mDialogGiftBtnCount.setText(mode.getCount() + "");
                mListPopupWindow.dismiss();
            });
            mListPopupWindow.setAdapter(mAopAdapter);
            mListPopupWindow.setDropDownAlwaysVisible(false);
            mListPopupWindow.setForceIgnoreOutsideTouch(false);
        }else {
            mAopAdapter.setPosDefault(Integer.parseInt(mDialogGiftBtnCount.getText().toString()));
        }

        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            subscription = null;
        }
        subscription = RxCountDown.delay2(200).subscribe(
                integer -> {
                    if (mListPopupWindow != null && mListPopupWindow.getListView() != null && mListPopupWindow.getListView().getAdapter() != null) {
                        PopAdapter adapter = (PopAdapter) mListPopupWindow.getListView().getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        mListPopupWindow.setAnchorView(mDialogGiftBtnCount);
        mListPopupWindow.show();
        if (mListPopupWindow.getListView() != null) {
            mListPopupWindow.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListPopupWindow.getListView().setCacheColorHint(Color.TRANSPARENT);
//            mListPopupWindow.getListView().setSelector(R.drawable.base_item_select);
            mListPopupWindow.getListView().setDividerHeight(DisplayUtil.dip2px(getContext(), 1));
            mListPopupWindow.getListView().setDivider(ContextCompat.getDrawable(getContext(), R.drawable.divider_ll));
        }
    }


    @Override
    public void onDetach() {
        if (subscription != null) {
            subscription.dispose();
        }
        super.onDetach();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        setCoin();
    }

    @Override
    public void onGiftClick(View view, GiftEntity entity) {
        this.mCurrentItem = entity;
        if (entity!=null){
            mDialogGiftBtnCount.setText(entity.getDefcount()+"");
        }
    }

    @Override
    public void clearPageChoose(int page, int position) {
        if (mFragments != null)
            ((GiftFragment) mFragments.get(page)).notifDataChange(position);
    }

    public interface GiftEventListener {
        void okBtn(GiftEntity gift);

        void pay();

        /**
         * 获得开始金币数
         *
         * @return
         */
        String startCoin();

        void cancel();
    }

    private static class PopAdapter extends BaseAdapter {
        private PopSelectListener mPopSelectListener;

        public void setPopSelectListener(PopSelectListener popSelectListener) {
            mPopSelectListener = popSelectListener;
        }

        private final List<GiftCountMode> mModes;
        SparseArray<GiftCountMode> mCurrentSelect= new SparseArray<>();
        public PopAdapter(List<GiftCountMode> modes,int defcount) {
            if (modes == null) modes = new ArrayList<>();
            this.mModes = modes;
            setPosDefault(defcount);//默认第三个188
        }
        public void setPosDefault(int count){
            if (mModes==null || mModes.size()==0)return;
            boolean isChanges = false;

            for (int i = 0; i < mModes.size(); i++) {
               if (mModes.get(i).getCount()==count){
                   isChanges = true;
                   changeSelect(i);
               }
            }
            if (!isChanges){
                changeSelect(mModes.size()-1);
            }
        }
        @Override
        public int getCount() {
            return mModes.size();
        }

        @Override
        public GiftCountMode getItem(int position) {
            return mModes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_gift_count, null);
            } else {
                LinearLayout rootView = ViewFindUtils.hold(convertView, R.id.dialog_item_gift_count_root);
                rootView.setOnClickListener(v -> {
                    changeSelect(position);
                });
                if (mCurrentSelect.get(position)!=null){
                    rootView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.base_yellow_alpha));
                }else {
                    rootView.setBackground(ContextCompat.getDrawable(parent.getContext(),R.drawable.base_item_select));
                }

                TextView count = ViewFindUtils.hold(convertView, R.id.dialog_item_tv_count);
                TextView desc = ViewFindUtils.hold(convertView, R.id.dialog_item_tv_description);
                GiftCountMode item = getItem(position);
                count.setText(item.getCount() + "");
                desc.setText(Check.checkReplace(item.getName()));
            }
            return convertView;
        }

        private void changeSelect(int position) {
            mCurrentSelect.clear();
            GiftCountMode item = getItem(position);
            mCurrentSelect.put(position,item);
            notifyDataSetChanged();
            if(mPopSelectListener!=null){
                mPopSelectListener.select(position,getItem(position));
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(mGiftEventListener!=null){
            mGiftEventListener.cancel();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mGiftEventListener!=null){
            mGiftEventListener.cancel();
        }
    }

    public  interface PopSelectListener{
        void select(int position,GiftCountMode mode);
    }
}

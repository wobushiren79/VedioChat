package com.huanmedia.videochat.common.widget.tablayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applecoffee.devtools.base.layout.BaseLinearLayout;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.main2.datamodel.TabMode;

import java.util.ArrayList;
import java.util.List;

public class TabSwitchView extends BaseLinearLayout {

    private LinearLayout mLLLayout;
    private List<TabSwitchItemView> listTabItem;
    private int position;
    private CallBack mCallBack;
    private ValueAnimator mMsgAnim;
    private int animPostion;

    public TabSwitchView(Context context) {
        this(context, null);
    }

    public TabSwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_tab_switch_view;
    }

    @Override
    protected void initView() {
        mLLLayout = mLayoutView.findViewById(R.id.ll_layout);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLLLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }


    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 设置Tab数据
     *
     * @param listData
     */
    public void setTabData(List<TabSwitchBean> listData) {
        listTabItem = new ArrayList<>();
        mLLLayout.removeAllViews();
        if (listData != null)
            for (int i = 0; i < listData.size(); i++) {
                TabSwitchBean itemData = listData.get(i);
                itemData.setPosition(i);
                createTabItem(itemData);
            }
    }

    public void setCurrentTab(int position) {
        if (position >= listTabItem.size())
            return;
        for (TabSwitchItemView item : listTabItem) {
            if (item.getItemData().getPosition() == position) {
                item.setSelect(1);
            } else {
                item.setSelect(0);
            }
        }
        if (animPostion == position && mMsgAnim != null) {
            mMsgAnim.end();
            listTabItem.get(position).initData();
        }
        if (this.position == position) {
            if (mCallBack != null)
                mCallBack.onItemDoubleClick(TabSwitchView.this, listTabItem.get(position).getItemData());
        } else {
            if (mCallBack != null)
                mCallBack.onItemClick(TabSwitchView.this, listTabItem.get(position).getItemData());
        }
        this.position = position;
    }

    /**
     * 创建Tabitem
     *
     * @param itemData
     */
    public void createTabItem(TabSwitchBean itemData) {
        TabSwitchItemView itemView = new TabSwitchItemView(getContext(), itemData);
        if (itemData.getPosition() == 0)
            itemView.setSelect(1);
        itemView.setOnClickListener(view -> {
            setCurrentTab(itemData.getPosition());
        });
        mLLLayout.addView(itemView);
        listTabItem.add(itemView);
    }

    private boolean hasZeroMsg;

    /**
     * 设置消息显示数量
     *
     * @param number
     */
    public void showMsgNumber(int position, int number) {
        if (position >= listTabItem.size())
            return;
        hasZeroMsg = false;
        TabSwitchItemView itemView = listTabItem.get(position);
        if (itemView.getMsgNumber() == number)
            return;
        itemView.clearAnimation();
        itemView.setMsgNumber(0);
        if (mMsgAnim != null) {
            mMsgAnim.end();
            itemView.initData();
        }
        if (number > 0) {
            showMsgAnim(itemView, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!hasZeroMsg) {
                        itemView.setMsgNumber(number);
                    }
                    if (TabSwitchView.this.position == position && mMsgAnim != null) {
                        mMsgAnim.end();
                        itemView.initData();
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            hasZeroMsg = true;
        }
    }

    /**
     * 消息出现动画
     *
     * @param itemView
     * @param listener
     */
    private void showMsgAnim(TabSwitchItemView itemView, Animation.AnimationListener listener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (1, 1.1f, 1, 1.1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatCount(3);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(listener);
        itemView.startAnimation(scaleAnimation);

        if (mMsgAnim != null)
            mMsgAnim.end();
        itemView.initData();
        animPostion = itemView.getItemData().getPosition();
        mMsgAnim = ValueAnimator.ofInt(0, 2);
        mMsgAnim.setDuration(1000);
        mMsgAnim.setRepeatMode(ValueAnimator.RESTART);
        mMsgAnim.setRepeatCount(ValueAnimator.INFINITE);
        mMsgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                ImageView icon = itemView.getIconView();
                if (currentValue == 0) {
                    icon.setImageResource(itemView.getItemData().getTabCheckPic());
                } else {
                    icon.setImageResource(itemView.getItemData().getTabUnCheckPic());
                }
            }
        });
        mMsgAnim.start();
    }

    /**
     * 设置文字未选中颜色
     *
     * @param textUnselectColor
     */
    public void setTextUnselectColor(int textUnselectColor) {
        for (TabSwitchItemView itemView : listTabItem) {
            itemView.getItemData().setTabTextUnCheckColor(textUnselectColor);
            itemView.initData();
        }
    }

    /**
     * 设置文字选中颜色
     *
     * @param textSelectColor
     */
    public void setTextSelectColor(int textSelectColor) {
        for (TabSwitchItemView itemView : listTabItem) {
            itemView.getItemData().setTabTextUnCheckColor(textSelectColor);
            itemView.initData();
        }
    }


    public interface CallBack {
        void onItemClick(View view, TabSwitchBean data);

        void onItemDoubleClick(View view, TabSwitchBean data);
    }
}

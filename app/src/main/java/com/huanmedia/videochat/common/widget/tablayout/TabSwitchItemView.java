package com.huanmedia.videochat.common.widget.tablayout;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applecoffee.devtools.base.layout.BaseLinearLayout;
import com.huanmedia.videochat.R;

public class TabSwitchItemView extends BaseLinearLayout {
    private RelativeLayout mRLLayout;
    private ImageView mIVIcon;
    private TextView mTVName;
    private TextView mTVMsgNumber;

    private TabSwitchBean itemData;
    private int selectStatus;
    private int msgNumber;

    public TabSwitchItemView(Context context, TabSwitchBean itemData) {
        super(context);
        this.itemData = itemData;
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_tab_switch_item;
    }

    @Override
    protected void initView() {
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        setLayoutParams(layoutParams);

        mRLLayout = getView(R.id.rl_layout);
        mIVIcon = getView(R.id.iv_icon);
        mTVName = getView(R.id.tv_name);
        mTVMsgNumber = getView(R.id.tv_msg_number);
        LayoutParams layoutRL = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRLLayout.setLayoutParams(layoutRL);
    }

    @Override
    protected void initData() {
        if (itemData == null)
            return;
        mTVName.setText(itemData.getTabName());
        if (selectStatus == 0) {
            mIVIcon.setImageResource(itemData.getTabUnCheckPic());
            mTVName.setTextColor(getResources().getColor(itemData.getTabTextUnCheckColor()));
        } else {
            mIVIcon.setImageResource(itemData.getTabCheckPic());
            mTVName.setTextColor(getResources().getColor(itemData.getTabTextCheckColor()));
        }
    }


    /**
     * 获取图标
     *
     * @return
     */
    public ImageView getIconView() {
        return mIVIcon;
    }

    /**
     * 获取单独一项数据
     *
     * @return
     */
    public TabSwitchBean getItemData() {
        return itemData;
    }

    /**
     * 获取消息数量
     * @return
     */
    public int getMsgNumber(){
        return  msgNumber;
    }
    /**
     * 设置选中状态
     *
     * @param selectStatus
     */
    public void setSelect(int selectStatus) {
        if (selectStatus == this.selectStatus)
            return;
        this.selectStatus = selectStatus;
        initData();
    }


    /**
     * 设置消息提示数量
     *
     * @param number
     */
    public void setMsgNumber(int number) {
        msgNumber = number;
        if (number > 0) {
            if (number > 99)
                number = 99;
            mTVMsgNumber.setVisibility(VISIBLE);
            msgNumberShowAnim();
        } else {
            mTVMsgNumber.setVisibility(GONE);
        }
        mTVMsgNumber.setText(number + "");
    }

    /**
     * 消息出现动画
     */
    private void msgNumberShowAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation
                (0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        mTVMsgNumber.startAnimation(scaleAnimation);
    }
}

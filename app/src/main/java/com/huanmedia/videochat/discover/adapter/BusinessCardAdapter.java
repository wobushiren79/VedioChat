package com.huanmedia.videochat.discover.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.navigation.Navigator;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.discover.weight.androidtagview.TagContainerLayout;
import com.huanmedia.videochat.main2.weight.GoodProgressView;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;
import com.huanmedia.videochat.repository.entity.BusinessCardUserTags;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.UserEvaluateEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

/**
 * Create by Administrator
 * time: 2018/2/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BusinessCardAdapter extends BaseMultiItemQuickAdapter<BusinessMultiItem, BaseViewHolder> {

    private String mDistance;
    private BaseQuickAdapter<PhotosEntity, BaseViewHolder> mHeaderPhotosAdapter;
    private BaseQuickAdapter<VideoEntity, BaseViewHolder> mHeaderVideoAdapter;
    private Context context;
    private int mItemSize;
    private @BusinessCardFragment.ShowType
    int mShowType;
    private BusinessAdapterListener mBusinessAdapterListener;

    public void setBusinessAdapterListener(BusinessAdapterListener businessAdapterListener) {
        mBusinessAdapterListener = businessAdapterListener;
    }

    public BusinessCardAdapter(Context context, List<BusinessMultiItem> data, String distance, @BusinessCardFragment.ShowType int showType) {
        super(data);
        this.context = context;
        mDistance = distance;
        mShowType = showType;
        addItemType(BusinessMultiItem.BusinessType.HEADER, R.layout.item_business_card_baseinfo);
        addItemType(BusinessMultiItem.BusinessType.TAG, R.layout.item_business_card_user_tag);
        addItemType(BusinessMultiItem.BusinessType.EVALUATE, R.layout.item_business_card_evaluate);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessMultiItem item) {
        switch (helper.getItemViewType()) {
            case BusinessMultiItem.BusinessType.HEADER:
                if (item instanceof BusinessCardEntity.BaseInfo) {
                    showHeadData(helper, (BusinessCardEntity.BaseInfo) item);
                }
                break;
            case BusinessMultiItem.BusinessType.TAG:
                if (item instanceof BusinessCardUserTags) {
                    configTags(helper, item);
                }
                break;
            case BusinessMultiItem.BusinessType.EVALUATE:
                if (item instanceof UserEvaluateEntity.EvaluateEntity) {
                    configEvaluates(helper, (UserEvaluateEntity.EvaluateEntity) item);
                }
                break;
        }
    }

    /**
     * 处理用户评价数据
     */
    private void configEvaluates(BaseViewHolder holder, UserEvaluateEntity.EvaluateEntity evaluateEntity) {
        GlideUtils.getInstance().loadBitmapNoAnim(mContext, evaluateEntity.getUserphoto_thumb(), holder.getView(R.id.ibc_rv_header));
        TextView tv = holder.getView(R.id.ibc_tv_nameAndContent);
        Spanny spanny = new Spanny(evaluateEntity.getNickname());
        spanny.append(": ");
        spanny.findAndSpan(evaluateEntity.getNickname() + ": ",
                () -> new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.base_black)));
        spanny.append(evaluateEntity.getText());
        tv.setText(spanny);
    }

    /**
     * 用户评价标签处理
     *
     * @param helper
     * @param item
     */
    private void configTags(BaseViewHolder helper, BusinessMultiItem item) {
        TagContainerLayout tagGroup = helper.getView(R.id.business_card_tgp_evaluate);
        if (tagGroup.getTags().size() == 0) {
            List<BusinessCardUserTags.TagEntity> items = ((BusinessCardUserTags) item).getItems();
            if (items.size() == 0) {
                TextView tvNoData = helper.getView(R.id.tv_nodata);
                if (tvNoData != null)
                    tvNoData.setVisibility(View.VISIBLE);
            }
            String[] targsArray = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                BusinessCardUserTags.TagEntity tag = items.get(i);
                targsArray[i] = tag.getTag() + " +" + tag.getTagcount();
            }
            tagGroup.setTags(targsArray);
        }


    }

    public static String getTrustvalueStr(int trustvalue) {
        if (trustvalue == 100)
            return "信任值爆表";
        if (trustvalue >= 90 && trustvalue <= 99) {
            return "信任值极佳";
        }
        if (trustvalue >= 80 && trustvalue <= 89) {
            return "信任值优秀";
        }
        if (trustvalue >= 70 && trustvalue <= 79) {
            return "信任值良好";
        }
        if (trustvalue >= 60 && trustvalue <= 69) {
            return "信任值不佳";
        }
        if (trustvalue >= 50 && trustvalue <= 59) {
            return "信任值差";
        }
        if (trustvalue >= 0 && trustvalue <= 49) {
            return "信任值极差";
        }
        return "";
    }

    private void configAdapter(RecyclerView photoRv, RecyclerView videoRv) {
        if (mHeaderPhotosAdapter == null && mHeaderVideoAdapter == null) {
            //照片墙
            RecyclerViewItemDecoration mCurrentItemDecoration = new RecyclerViewItemDecoration.Builder(mContext)
                    .color(ContextCompat.getColor(mContext, R.color.white))
                    .thickness(DisplayUtil.dip2px(mContext, 4))
                    .gridHorizontalSpacing(DisplayUtil.dip2px(mContext, 4))
                    .gridVerticalSpacing(DisplayUtil.dip2px(mContext, 4))
                    .create();

            int count = ((GridLayoutManager) photoRv.getLayoutManager()).getSpanCount();
            mItemSize = (DisplayUtil.getDisplayWidth(mContext) - (DisplayUtil.dip2px(mContext, 4) * (count - 1))) / count;
//            photoRv.getLayoutParams().height = mItemSize * 2 + DisplayUtil.dip2px(mContext, 4) * (count - 1);

//            photoRv.addItemDecoration(mCurrentItemDecoration);
            mHeaderPhotosAdapter = new BaseQuickAdapter<PhotosEntity, BaseViewHolder>(R.layout.item_discoverinfo) {
                protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
                    View rootview = mLayoutInflater.inflate(layoutResId, parent, false);
                    rootview.getLayoutParams().height = mItemSize;
                    return rootview;
                }

                @Override
                protected void convert(BaseViewHolder helper, PhotosEntity item) {
                    ImageView iv = helper.getView(R.id.item_discoverinfo_iv);
                    GlideApp.with(mContext)
                            .asBitmap()
                            .load(item.getPhoto_thumb())
                            .override(mItemSize, mItemSize)
                            .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot)
                            .error(com.huanmedia.ilibray.R.drawable.bg_logot)
                            .into(iv);
                }
            };
            photoRv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    new HM_StartSeePhoto.Bulide(mContext, new HM_GlideEngine())
                            .setList(adapter.getData())
                            .setCurrentSelect(position)
                            .bulide();
                }
            });
            photoRv.setAdapter(mHeaderPhotosAdapter);
            //-----------------------------------------------------------------------------
            mHeaderVideoAdapter = new BaseQuickAdapter<VideoEntity, BaseViewHolder>(R.layout.item_discoverinfo_video) {
                protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
                    View rootview = mLayoutInflater.inflate(layoutResId, parent, false);
                    rootview.getLayoutParams().height = mItemSize;
                    return rootview;
                }

                @Override
                protected void convert(BaseViewHolder helper, VideoEntity item) {
                    ImageView iv = helper.getView(R.id.iv_icon);
                    GlideApp.with(mContext)
                            .asBitmap()
                            .load(item.getImgurl())
//                            .override(mItemSize, mItemSize)
                            .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot).error(com.huanmedia.ilibray.R.drawable.bg_logot)
                            .into(iv);
                }
            };
            videoRv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ((BaseActivity) context).getNavigator().navtoMediaPlay((Activity) context, (ArrayList<VideoEntity>) mHeaderVideoAdapter.getData(), position);
                }
            });
            videoRv.setAdapter(mHeaderVideoAdapter);
        }
    }

    @SuppressLint("DefaultLocale")
    public void showHeadData(BaseViewHolder headerHolder, BusinessCardEntity.BaseInfo businessCard) {
        int sexSrc;
        if (businessCard.getSex() == 1) {
            sexSrc = R.drawable.bg_icon_man_1;
        } else {
            sexSrc = R.drawable.bg_icon_woman_1;
        }
        int charge = businessCard.getStartime() == 0 ? 0 : businessCard.getStarcoin() / businessCard.getStartime();
        headerHolder
                .setVisible(R.id.business_card_tv_distance, businessCard.getIsstarauth() == 1)//非红人隐藏距离信息
                .setVisible(R.id.business_card_tv_charge, businessCard.getIsstarauth() == 1)//非红人隐藏价格信息
                .setVisible(R.id.business_card_cb_attention,businessCard.getIsstarauth() == 1)//非红人隐藏关注
                .setVisible(R.id.business_card_tv_cl_trustValue,businessCard.getIsstarauth() == 1)//非红人隐藏信任值
                //用户基础信息
                .setText(R.id.business_card_tv_nickName, Check.checkReplace(businessCard.getNickname()))
                .setText(R.id.business_card_tv_level, String.format("LV.%d", businessCard.getLevel()))
                .setText(R.id.business_card_tv_age, String.format("%s岁", String.valueOf(businessCard.getAge())))
                .setText(R.id.business_card_tv_occupation, Check.checkReplace(businessCard.getOccupation(), "未知"))
                .setText(R.id.business_card_tv_charge, String.format("%d钻/分钟", charge))
                .setText(R.id.business_card_tv_distance, String.format("%s", Check.checkReplace(mDistance, "未知")))
                .setText(R.id.business_card_tv_desc_trustValue, getTrustvalueStr(businessCard.getTrustvalue()))
                .setChecked(R.id.business_card_cb_attention, businessCard.getIsfavorite() == 1)
                .setText(R.id.business_card_cb_attention, businessCard.getIsfavorite() == 1 ? "已关注" : "关注")
                .setOnCheckedChangeListener(R.id.business_card_cb_attention, (buttonView, isChecked) -> {
                    if (mBusinessAdapterListener != null) {
                        if (businessCard.getIsfavorite() != (isChecked ? 1 : 0)) {
                            buttonView.setText(isChecked ? "已关注" : "关注");
                            businessCard.setIsfavorite(isChecked ? 1 : 0);
                            mBusinessAdapterListener.attention(businessCard, isChecked);
                        }
                    }
                });
        GoodProgressView goodProgressView = headerHolder.getView(R.id.business_card_gpv_desc_trustValue);
        goodProgressView.setMaxProgress(businessCard.getMaxtrustvalue());
        goodProgressView.setProgressValue(businessCard.getTrustvalue());

        TextViewDrawableUtils.setDrawable(headerHolder.getView(R.id.business_card_tv_age), null
                , null, ContextCompat.getDrawable(mContext, sexSrc), null);
        TextViewDrawableUtils.setDrawable(headerHolder.getView(R.id.business_card_tv_nickName),
                businessCard.getIsstarauth() == 1 ? ContextCompat.getDrawable(mContext, R.drawable.icon_hot)
                        : null, null, null, null);

        configAdapter(headerHolder.getView(R.id.business_card_rv_photos), headerHolder.getView(R.id.business_card_rv_video));
        mHeaderPhotosAdapter.setNewData(businessCard.getPhpots());
        mHeaderVideoAdapter.setNewData(businessCard.getVoides());
        if (businessCard.getVoides() == null || businessCard.getVoides().size() == 0) {
            headerHolder.setGone(R.id.business_card_ll_video, false);
        } else {
            headerHolder.setGone(R.id.business_card_ll_video, true);
        }

    }

    public static interface BusinessAdapterListener {
        void attention(BusinessCardEntity.BaseInfo businessCard, boolean checked);
    }

}



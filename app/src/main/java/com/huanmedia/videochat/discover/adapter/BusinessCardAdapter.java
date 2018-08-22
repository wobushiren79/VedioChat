package com.huanmedia.videochat.discover.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huanmedia.hmalbumlib.HM_StartSeePhoto;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.ImageUtils;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.ToastUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.recycledecoration.RecyclerViewItemDecoration;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.album.HM_GlideEngine;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.discover.BusinessCardFragment;
import com.huanmedia.videochat.discover.weight.androidtagview.TagContainerLayout;
import com.huanmedia.videochat.main2.weight.GoodProgressView;
import com.huanmedia.videochat.mvp.entity.results.FileManageResults;
import com.huanmedia.videochat.mvp.presenter.file.FileManagePresenterImpl;
import com.huanmedia.videochat.mvp.presenter.file.IFileManagePresenter;
import com.huanmedia.videochat.mvp.view.file.IFileManageCheckView;
import com.huanmedia.videochat.mvp.view.file.IFileManagePayView;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;
import com.huanmedia.videochat.repository.entity.BusinessCardUserTags;
import com.huanmedia.videochat.repository.entity.PhotosEntity;
import com.huanmedia.videochat.repository.entity.UserEvaluateEntity;
import com.huanmedia.videochat.repository.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;
import mvp.data.store.glide.transform.BlurTransformation;

import static com.huanmedia.videochat.common.utils.VideoChatUtils.NoMoreMoneyDialog;

/**
 * Create by Administrator
 * time: 2018/2/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BusinessCardAdapter extends BaseMultiItemQuickAdapter<BusinessMultiItem, BaseViewHolder> implements IFileManagePayView, IFileManageCheckView {

    private String mDistance;
    private BaseQuickAdapter<PhotosEntity, BaseViewHolder> mHeaderPhotosAdapter;
    private BaseQuickAdapter<VideoEntity, BaseViewHolder> mHeaderVideoAdapter;
    private Context context;
    private int mItemSize;
    private @BusinessCardFragment.ShowType
    int mShowType;
    private BusinessAdapterListener mBusinessAdapterListener;
    private List<VideoEntity> listAllVideo;
    private List<PhotosEntity> listAllPhoto;

    private IFileManagePresenter mFileManagePresenter;

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
        mFileManagePresenter = new FileManagePresenterImpl(this, this);
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
                    TextView tvTag = helper.getView(R.id.tv_tag);
                    if (item.getPlevel() == 1) {
                        tvTag.setVisibility(View.GONE);
                        GlideApp.with(mContext)
                                .asBitmap()
                                .load(item.getPhoto_thumb())
                                .override(mItemSize, mItemSize)
                                .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot)
                                .error(com.huanmedia.ilibray.R.drawable.bg_logot)
                                .into(iv);

                    } else {
                        if (item.getTag() != null && !item.getTag().equals("")) {
                            tvTag.setVisibility(View.VISIBLE);
                            tvTag.setText(item.getTag());
                        } else {
                            tvTag.setVisibility(View.GONE);
                        }

                        GlideApp.with(context)
                                .asBitmap()
                                .override(100, 100)
                                .load(item.getPhoto_thumb())
                                .transform(new BlurTransformation(context, 25))
                                .into(iv);
                    }

                }
            };
            photoRv.addItemDecoration(mCurrentItemDecoration);
            photoRv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PhotosEntity item = mHeaderPhotosAdapter.getData().get(position);
                    if (item.getPlevel() == 2) {
                        mFileManagePresenter.checkHasPhotoFile(item.getId());
                    } else {
                        seePublicPhoto(listAllPhoto, position);
                    }
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
                    TextView tvTag = helper.getView(R.id.tv_tag);
                    if (item.getPlevel() == 1) {
                        tvTag.setVisibility(View.GONE);
                        GlideApp.with(mContext)
                                .asBitmap()
                                .load(item.getImgurl())
//                            .override(mItemSize, mItemSize)
                                .placeholder(com.huanmedia.ilibray.R.drawable.bg_logot).error(com.huanmedia.ilibray.R.drawable.bg_logot)
                                .into(iv);
                    } else {
                        if (item.getTag() != null && !item.getTag().equals("")) {
                            tvTag.setVisibility(View.VISIBLE);
                            tvTag.setText(item.getTag());
                        } else {
                            tvTag.setVisibility(View.GONE);
                        }

                        GlideApp.with(context)
                                .asBitmap()
                                .override(100, 100)
                                .load(item.getImgurl())
                                .transform(new BlurTransformation(context, 25))
                                .into(iv);
                    }

                }
            };
            videoRv.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    VideoEntity item = mHeaderVideoAdapter.getData().get(position);
                    if (item.getPlevel() == 2) {
                        mFileManagePresenter.checkHasVideoFile(item.getId());
                    } else {
                        seePublicVideo(listAllVideo, position);
                    }
                }
            });
            videoRv.addItemDecoration(mCurrentItemDecoration);
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
                .setVisible(R.id.business_card_cb_attention, businessCard.getIsstarauth() == 1)//非红人隐藏关注
                .setVisible(R.id.business_card_tv_cl_trustValue, businessCard.getIsstarauth() == 1)//非红人隐藏信任值
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

        RecyclerView rvPhotos = headerHolder.getView(R.id.business_card_rv_photos);
        RecyclerView rvVideo = headerHolder.getView(R.id.business_card_rv_video);

        listAllVideo = businessCard.getVoides();
        listAllPhoto = businessCard.getPhpots();

        configAdapter(rvPhotos, rvVideo);

        TextView tvVideoMore = headerHolder.getView(R.id.business_card_video_more);
        TextView tvPhotoMore = headerHolder.getView(R.id.business_card_photo_more);
        setVideoList(false, tvVideoMore, listAllVideo);
        setPhotoList(false, tvPhotoMore, listAllPhoto);
//        mHeaderPhotosAdapter.setNewData(businessCard.getPhpots());
//        mHeaderVideoAdapter.setNewData(businessCard.getVoides());
        if (businessCard.getVoides() == null || businessCard.getVoides().size() == 0) {
            headerHolder.setGone(R.id.business_card_ll_video, false);
        } else {
            headerHolder.setGone(R.id.business_card_ll_video, true);
        }

        if (businessCard.getVoides().size() <= 6)
            tvVideoMore.setVisibility(View.GONE);
        if (businessCard.getPhpots().size() <= 6)
            tvPhotoMore.setVisibility(View.GONE);
        tvVideoMore.setOnClickListener(view -> {
            setVideoList(true, tvVideoMore, businessCard.getVoides());
        });
        tvPhotoMore.setOnClickListener(view -> {
            setPhotoList(true, tvPhotoMore, businessCard.getPhpots());
        });
    }


    private void setVideoList(boolean isChange, TextView title, List<VideoEntity> videoList) {
        if (isChange) {
            if (isVideoListShow) {
                isVideoListShow = false;
                title.setText("更多");
                if (videoList.size() <= 6) {
                    mHeaderVideoAdapter.setNewData(videoList);
                } else {
                    List newList = videoList.subList(0, 6);
                    mHeaderVideoAdapter.setNewData(newList);
                }
            } else {
                isVideoListShow = true;
                title.setText("收起");
                mHeaderVideoAdapter.setNewData(videoList);
            }
        } else {
            if (isVideoListShow) {
                title.setText("收起");
                mHeaderVideoAdapter.setNewData(videoList);
            } else {
                title.setText("更多");
                if (videoList.size() <= 6) {
                    mHeaderVideoAdapter.setNewData(videoList);
                } else {
                    List newList = videoList.subList(0, 6);
                    mHeaderVideoAdapter.setNewData(newList);
                }
            }
        }

    }

    private void setPhotoList(boolean isChange, TextView title, List<PhotosEntity> photoList) {
        if (isChange) {
            if (isPhotoListShow) {
                isPhotoListShow = false;
                title.setText("更多");
                if (photoList.size() <= 6) {
                    mHeaderPhotosAdapter.setNewData(photoList);
                } else {
                    List newList = photoList.subList(0, 6);
                    mHeaderPhotosAdapter.setNewData(newList);
                }
            } else {
                isPhotoListShow = true;
                title.setText("收起");
                mHeaderPhotosAdapter.setNewData(photoList);
            }
        } else {
            if (isPhotoListShow) {
                title.setText("收起");
                mHeaderPhotosAdapter.setNewData(photoList);
            } else {
                title.setText("更多");
                if (photoList.size() <= 6) {
                    mHeaderPhotosAdapter.setNewData(photoList);
                } else {
                    List newList = photoList.subList(0, 6);
                    mHeaderPhotosAdapter.setNewData(newList);
                }
            }
        }

    }

    boolean isPhotoListShow = false;
    boolean isVideoListShow = false;

    /**
     * 查看公开照片
     *
     * @param listPhoto
     * @param position
     */
    private void seePublicPhoto(List<PhotosEntity> listPhoto, int position) {
//        List<PhotosEntity> publicPhotoList = new ArrayList<>();
//        int removePosition = 0;
//        for (int i = 0; i < listPhoto.size(); i++) {
//            PhotosEntity item = listPhoto.get(i);
//            if (item.getPlevel() == 1) {
//                publicPhotoList.add(item);
//            } else {
//                if (position > i)
//                    removePosition++;
//            }
//        }
//        position -= removePosition;
//        new HM_StartSeePhoto.Bulide(mContext, new HM_GlideEngine())
//                .setList(listPhoto)
//                .setCurrentSelect(position)
//                .bulide();
        ((BaseActivity) mContext)
                .getNavigator()
                .navtoPhotoShow((Activity) mContext, listPhoto, position, true);
    }


    /**
     * 查看公开视频
     *
     * @param listVideo
     * @param position
     */
    private void seePublicVideo(List<VideoEntity> listVideo, int position) {
//        ArrayList<VideoEntity> publicVideoList = new ArrayList<>();
//        int removePosition = 0;
//        for (int i = 0; i < listVideo.size(); i++) {
//            VideoEntity item = listVideo.get(i);
//            if (item.getPlevel() == 1) {
//                publicVideoList.add(item);
//            } else {
//                if (position > i)
//                    removePosition++;
//            }
//        }
//        position -= removePosition;
        ((BaseActivity) mContext)
                .getNavigator()
                .navtoMediaPlay((Activity) mContext, listVideo, position, true);
    }


    //-----------文件处理------------------------
    @Override
    public void checkHasFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (results.getHasread() == 1) {
            payFileSuccess(fileID, fileType, results);
        } else {
            String dialogTitle = "";
            if (fileType == 1) {
                dialogTitle = "您确定要查看私照吗？";
            } else if (fileType == 2) {
                dialogTitle = "您确定要查看视频吗？";
            }
            GeneralDialog dialog = new GeneralDialog(mContext);
            dialog
                    .setContent("将消耗您" + results.getVcoin() + "钻石")
                    .setTitle(dialogTitle)
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < results.getVcoin()) {
                                NoMoreMoneyDialog(mContext, "抱歉，无法查看，没有更多钻石了。");
                            } else {
                                if (fileType == 1) {
                                    mFileManagePresenter.payPhotoFile(fileID);
                                } else if (fileType == 2) {
                                    mFileManagePresenter.payVideoFile(fileID);
                                }
                            }
                        }

                        @Override
                        public void cancelClick(Dialog dialog) {

                        }
                    })
                    .show();
        }

    }

    @Override
    public void checkHasFileFail(String msg) {
        showToast(msg);
    }

    @Override
    public void payFileSuccess(int fileID, int fileType, FileManageResults results) {
        if (fileType == 1) {
            for (int i = 0; i < listAllPhoto.size(); i++) {
                PhotosEntity item = listAllPhoto.get(i);
                if (item.getId() == fileID) {
                    item.setPlevel(1);
                    mHeaderPhotosAdapter.notifyItemChanged(i);
                    seePublicPhoto(listAllPhoto, i);
                    break;
                }
            }
        } else if (fileType == 2) {
            for (int i = 0; i < listAllVideo.size(); i++) {
                VideoEntity item = listAllVideo.get(i);
                if (item.getId() == fileID) {
                    item.setPlevel(1);
                    mHeaderVideoAdapter.notifyItemChanged(i);
                    seePublicVideo(listAllVideo, i);
                    break;
                }
            }
        }
    }

    @Override
    public void payFileFail(String msg) {
        showToast(msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showToast(String toast) {
        ToastUtils.showToastShortInCenter(getContext(), toast);
    }

    public static interface BusinessAdapterListener {
        void attention(BusinessCardEntity.BaseInfo businessCard, boolean checked);
    }

}



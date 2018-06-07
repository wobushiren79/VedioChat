package com.huanmedia.videochat.main2.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.faceunity.FUManager;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseVideoFragment;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.utils.DoubleClickUtils;
import com.huanmedia.videochat.common.widget.dialog.CommDialogUtils;
import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.main2.weight.BeautyDialog;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.ConditionFilterDialog;
import com.huanmedia.videochat.main2.weight.MaskDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.propeller.preprocessing.VideoPreProcessing;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.reactivex.disposables.Disposable;

public class MatchFragment extends BaseVideoFragment<MatchPresenter> implements MatchView {
    @BindView(R.id.main_video_view_container)
    FrameLayout mVideoViewContainer;
    @BindView(R.id.main_iv_blur)
    ImageView mMainIvBlur;
    @BindView(R.id.main_iv_back)
    ImageView mMainIvBack;
    //    @BindView(R.id.content)
//    FrameLayout mMainContent;
    @BindView(R.id.main_rootview)
    LinearLayout mMainRootView;
    @BindView(R.id.main_match_buttom_fl)
    FrameLayout mMainMathButtomFl;
    @BindView(R.id.main_windowView)
    FrameLayout mMainWindowView;
    @BindView(R.id.main_iv_mask)
    ImageView mMainIvMask;
    @BindView(R.id.main_tv_conditionFilter)
    ImageView mMainTvConditionFilter;
    @BindView(R.id.main_iv_beauty)
    ImageView mMainIvBeauty;
    private MatchInteractionListener mListener;
    private MaskDialog mMaskDialog;
    private ConditionFilterDialog mConditionDialog;
    private ConditionEntity mConditionEntity = new ConditionEntity();
    private SurfaceView surfaceV;
    private VideoPreProcessing videoPreProcessing;
    private boolean isResetPreVeiw;
    private boolean isInitEvent;
    private boolean isStopPlaying;//暂停界面数据
    private Disposable mInvisibleScript;

    public static boolean hasMatchFragment;//是否加载matchFragment

    public MatchFragment() {
    }

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainAction(Intent action) {//用于相机重新启动
        if (action == null || action.getAction() == null) return;
        switch (action.getAction()) {
            case EventBusAction.ACTION_MAINOPENCAMERA://通知相机重新采集数据
                isResetPreVeiw = true;
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (worker() != null && isResetPreVeiw) {
            isResetPreVeiw = false;
            worker().preview(true, surfaceV, 0);
        }
        enableProcess();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hasMatchFragment = true;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasMatchFragment = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_match;
    }


    @Override
    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    protected void initData() {
        //初始化数据
        enableProcess();
    }

    @Override
    protected void initView(View view) {
        mMainRootView.setPadding(
                mMainRootView.getLeft(),
                mMainRootView.getTop() +
                        DisplayUtil.getStatusBarHeight(getContext())
                , mMainRootView.getRight(), mMainRootView.getBottom()
        );
        initSelectFilter();
    }

    /**
     * 过滤器设置
     */
    private void initSelectFilter() {
        List<SkinMode> data = getBasePresenter().getSkinModes();
        if (mMaskDialog == null) {
            mMaskDialog = new MaskDialog(context());
            mMaskDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mMainMathButtomFl.animate().alpha(1).setDuration(300).start();
                }
            });
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        //将要隐藏
        deInitUIandEvent();
        deInitWorkerThread();
        if (surfaceV != null) {
            surfaceV.destroyDrawingCache();
            mVideoViewContainer.removeView(surfaceV);
            surfaceV = null;
        }
    }

    @Override
    protected void onVisible() {
        checkPhonePermission();
        super.onVisible();
    }

    @Override
    protected void initUIandEvent() {
        isInitEvent = true;
        //初始化视频UI和事件
        if (surfaceV == null) {
//            int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];
//            int vProfile = Constants.VIDEO_PROFILE_360P_11;
            int vProfile = Constants.VIDEO_PROFILE_360P_8;
            worker().configEngine(vProfile, null, null);
//            worker().getRtcEngine().disableAudio();//关闭声音
            worker().getRtcEngine().muteLocalVideoStream(true);//不发送本地视频数据
            surfaceV = RtcEngine.CreateRendererView(FApplication.getApplication());
            surfaceV.setZOrderOnTop(false);
            surfaceV.setZOrderMediaOverlay(false);
            mVideoViewContainer.addView(surfaceV);
            enableProcess();
        }
        worker().preview(true, surfaceV, 0);
        enableProcess();
    }

    @Override
    protected void deInitUIandEvent() {
        if (getBasePresenter() == null || !FUManager.hasInstance()) return;
        FUManager.getInstance(context()).destroyItems();
        //释放视频资源
        if (isInitEvent) {
            isInitEvent = false;
            if (worker() != null) {
                worker().preview(false, null, 0);
            }
        }
    }

    /**
     * 初始化美颜
     */
    protected void initAiYa() {
        if (context() == null) return;
        FUManager.getInstance(context()).loadInitData();
    }


    @Override
    protected String[] getReqPremiss() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};
    }

    public void setListener(MatchInteractionListener listener) {
        mListener = listener;
    }

    @OnClick({
            R.id.main_tv_conditionFilter,
            R.id.main_iv_random_matching,
            R.id.main_iv_beauty,
            R.id.main_iv_back,
            R.id.main_iv_mask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_tv_conditionFilter://筛选
                if (!UserManager.getInstance().islogin()) {
                    CommDialogUtils.showNavToLogin(context(), (dialog, which) ->
                    {
                        UserManager.getInstance().outLogin(null);
                        UserManager.getInstance().exit();
                    });
                    return;
                }
                if (mConditionDialog == null) {
                    mConditionDialog = new ConditionFilterDialog(context()).setConditionLisenter(conditionEntity -> {
                        if (UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() < 2 && conditionEntity.getMatchConfig().getType() == ConditionEntity.ConditionTtype.FILTER) {
                            new MaterialDialog
                                    .Builder(context())
                                    .title("余额不足")
                                    .content("条件匹配需要花费20钻，是否需要充值？")
                                    .negativeText("前往充值")
                                    .negativeColorRes(R.color.base_yellow)
                                    .positiveColorRes(R.color.base_gray)
                                    .positiveText("取消")
                                    .onNegative((dialog, which) -> getNavigator().navtoCoinPay(getActivity(), null))
                                    .show();
                        } else {
                            this.mConditionEntity = conditionEntity;
                            mConditionEntity.setVideoType(ConditionEntity.VideoType.MATCH);
                            mConditionEntity.getMatchConfig().setMask(MaskDialog.getCurrentMask());
                            getNavigator().navtoCalling(getActivity(), mConditionEntity, null);
                        }
                    });
                    mConditionDialog.setOnDismissListener(dialog -> mMainTvConditionFilter.clearColorFilter());
                }
                mConditionDialog.show();
                mMainTvConditionFilter.setColorFilter(ContextCompat.getColor(context(), R.color.color_f64d99), PorterDuff.Mode.SRC_IN);
                break;
            case R.id.main_iv_random_matching://匹配
                if (!UserManager.getInstance().islogin()) {
                    CommDialogUtils.showNavToLogin(context(), (dialog, which) ->
                    {
                        UserManager.getInstance().outLogin(null);
                        UserManager.getInstance().exit();
                    });
                    return;
                }
                mConditionEntity.setVideoType(ConditionEntity.VideoType.MATCH);
                mConditionEntity.getMatchConfig().setType(ConditionEntity.ConditionTtype.ALL);
                mConditionEntity.getMatchConfig().setMask(MaskDialog.getCurrentMask());
                mConditionEntity.setNeedCloseFU(0);
                getNavigator().navtoCalling(getActivity(), mConditionEntity, null);
                break;
            case R.id.main_iv_mask://展开面具
                mMainMathButtomFl.animate().alpha(0).setDuration(300).start();
                mMaskDialog.show();
                break;
            case R.id.main_iv_beauty://展开美化
                if (!DoubleClickUtils.isFastDoubleClick()) {
                    mMainMathButtomFl.animate().alpha(0).setDuration(300).start();
                    BeautyDialog dialog = new BeautyDialog(getContext());
                    dialog.setOnDismissListener(dialog1 -> mMainMathButtomFl.animate().alpha(1).setDuration(300).start());
                    dialog.show();
                }
                break;
            case R.id.main_iv_back:
                backMatch();
                break;
        }
    }


    @Override
    public Context context() {
        return getContext();
    }

    public void stopCarmer() {
//        if (worker()!=null && !isStopPlaying){
//            isStopPlaying=true;
//            worker().getRtcEngine().muteLocalVideoStream(true);
//        }
    }

    public void startCarmer() {
//        if (worker()!=null && isStopPlaying){
//            isStopPlaying=false;
//            worker().getRtcEngine().muteLocalVideoStream(false);
//        }
    }

    @Override
    public void enableProcess() {
        if (videoPreProcessing == null)
            videoPreProcessing = new VideoPreProcessing();
        videoPreProcessing.enablePreProcessing(true);
    }

    @Override
    protected void backMatch() {
        if (mListener != null) {
            mListener.matchBack();
        }
    }

    /**
     * 匹配界面回调接口
     */
    public interface MatchInteractionListener {
        void matchBack();
    }


}

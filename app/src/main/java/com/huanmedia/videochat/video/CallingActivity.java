package com.huanmedia.videochat.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.faceunity.FUManager;
import com.huanmedia.ilibray.utils.DisplayUtil;
import com.huanmedia.ilibray.utils.RxCountDown;
import com.huanmedia.ilibray.utils.Spanny;
import com.huanmedia.ilibray.utils.TextViewDrawableUtils;
import com.huanmedia.ilibray.utils.TimeUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.BaseVideoActivity;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.service.notifserver.ringtone.Constants;
import com.huanmedia.videochat.common.service.notifserver.ringtone.NotificationHandler;
import com.huanmedia.videochat.common.service.notifserver.ringtone.RingtoneManager;
import com.huanmedia.videochat.common.widget.dialog.CheckContactDialog;
import com.huanmedia.videochat.common.widget.dialog.CommDialogUtils;
import com.huanmedia.videochat.common.widget.dialog.EvaluationDialog;
import com.huanmedia.videochat.common.widget.dialog.GeneralDialog;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.main2.weight.BeautyDialog;
import com.huanmedia.videochat.main2.weight.ConditionEntity;
import com.huanmedia.videochat.main2.weight.ConditionEntity.VideoType;
import com.huanmedia.videochat.main2.weight.MaskDialog;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.repository.net.NetConstants;
import com.huanmedia.videochat.video.adapter.ChatTextAdapter;
import com.huanmedia.videochat.video.model.GiftLocalMode;
import com.huanmedia.videochat.video.model.TextChatMode;
import com.huanmedia.videochat.video.widget.CallButtomBtns;
import com.huanmedia.videochat.video.widget.GiftFragmentDialog;
import com.huanmedia.videochat.video.widget.OvertimeMode;
import com.huanmedia.videochat.video.widget.RippleBackground;
import com.huanmedia.videochat.video.widget.TimerTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.orhanobut.logger.Logger;

import org.dync.giftlibrary.GiftControl;
import org.dync.giftlibrary.widget.CustormAnim;
import org.dync.giftlibrary.widget.GiftModel;
import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.OnClick;
import ch.halcyon.squareprogressbar.SquareProgressView;
import io.agora.propeller.model.AGEventHandler;
import io.agora.propeller.preprocessing.VideoPreProcessing;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.reactivex.disposables.Disposable;
import mvp.data.store.glide.GlideApp;
import mvp.data.store.glide.GlideUtils;

public class CallingActivity extends BaseVideoActivity<CallingPresenter> implements CallingView, AGEventHandler {

    //    @BindView(R.id.video_call_iv_blur_big)
//    ImageView mVideoCallIvBlurBig;
//    @BindView(R.id.video_call_iv_blur_small)
//    ImageView mVideoCallIvBlurSmall;
    @BindView(R.id.calling_fl_video_big)
    FrameLayout mCallingFlVideoBig;
    @BindView(R.id.video_call_fl_calling)
    LinearLayout mVideoCallFlCalling;
    @BindView(R.id.video_call_fl_rootView)
    FrameLayout mVideoCallFlRootView;
    @BindView(R.id.video_calling_rbg)//头像的波纹动画VIew
            RippleBackground mVideoCallingWwvBG;
    @BindView(R.id.video_call_fl_cotorll)
    FrameLayout mVideoCallFlCotorll;
    @BindView(R.id.calling_fl_video_small)
    FrameLayout mCallingFlVideoSmall;
    //    @BindView(R.id.calling_fl_video_small_content)
//    FrameLayout mCallingFlVideoSmallContent;
    @BindView(R.id.video_call_tv_hint)
    TextView mVideoCallTvHint;
    @BindView(R.id.video_call_tv_hint_small)
    TextView mVideoCallTvHintSmall;
    @BindView(R.id.calling_tv_small_calling)
    TextView mVideoCallTvSmallCalling;
    @BindView(R.id.video_call_ll_hint)
    LinearLayout mVideoCallLlHint;
    @BindView(R.id.video_call_tv_countdown)
    TextView mVideoCallTvCountDown;
    @BindView(R.id.video_call_iv_header)
    RoundedImageView mVideoCallIvHeader;
    @BindView(R.id.video_calling_tv_name)
    TextView mVideoCallingTvName;
    @BindView(R.id.video_calling_tv_location)
    TextView mVideoCallingTvLocation;
    @BindView(R.id.video_calling_iv_header)
    RoundedImageView mVideoCallingIvHeader;//匹配中或者拨号时候的大头像
    @BindView(R.id.video_call_tv_nickName)
    TextView mVideoCallTvNickName;
    @BindView(R.id.video_call_tv_location)
    TextView mVideoCallTvLocation;
    @BindView(R.id.video_call_rl_userinfo)
    RelativeLayout mVideoCallRlUserinfo;
    @BindView(R.id.video_call_tv_hint_anim)
    TextView mVideoCallTvHintAnim;//加时动画
    @BindView(R.id.tv_check_contact)
    TextView mCheckContact;//查看微信QQ按钮

    //底部控制按钮
//    @BindView(R.id.video_call_lav_endCallanim)TODO
//    LottieAnimationView mVideoCallLavEndCallanim;//挂断波纹
//    @BindView(R.id.video_call_svg_anim)
//    SVGAImageView mVideoCallSvgaAnim;//挂断动画
//    @BindView(R.id.video_call_iv_endCallbtn)
//    Button mVideoCallIvEndCallbtn;//挂断
    @BindView(R.id.video_call_rl_callBtns)
    RelativeLayout mVideoCallRlEndCallbtn;//挂断和接听按钮布局
    @BindView(R.id.video_call_tv_time)
    TextView mVideoCallTvTime;//倒计时数字
    @BindView(R.id.video_call_progress_bar)
    ProgressBar mVideoCallProgressBar;//倒计时进度条
    @BindView(R.id.video_call_ll_timeDown)
    LinearLayout mVideoCallLlTimeDown;//倒计时根布局
    @BindView(R.id.video_call_cbb_controlBtns)
    CallButtomBtns mVideoCallCbbControlBtns;//控制按钮布局
    @BindView(R.id.video_call_rv_chatText)
    RecyclerView mVideoCallRvChatText;
    @BindView(R.id.video_call_ll_gift)
    LinearLayout mVideoCallLlGift;
    @BindView(R.id.video_call_iv_answer)
    ImageView mVideoCallIvAnswer;
    @BindView(R.id.video_call_lav)//礼物动画View
            SVGAImageView mVideoCallLav;

    @BindView(R.id.view_time_progress)
    SquareProgressView mTimeProgressView;
    @BindView(R.id.view_time)
    TimerTextView mTimeText;
    @BindView(R.id.calling_fl_video_small_layout)
    FrameLayout mCallingFlVideoSmallLayout;
    //    private int mDefaultAutioVolume;
    private volatile SparseArray<SurfaceView> mUidsList = new SparseArray<>();

    public static final int LAYOUT_TYPE_DEFAULT = 0;//视频全屏大小
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    public static final int LAYOUT_TYPE_SMALL = 1; //视频小
    private Disposable mTimeDownSub;//聊天计时器（倒计时）
    private OvertimeMode mOverTimeValue;
    private ChatTextAdapter mAdapter;

    public static @CallingType
    int mCallingState = CallingType.LEISURE;//通话状态
    private GiftFragmentDialog mGiftDialog;//礼物对话框
    private boolean isVideoCalling;
    private HintDialog mHintDialog;
    private GiftControl mGiftControl;
    private String mDescFormat;
    private boolean localVideoStarting;//本地视频是否打开
    private SVGAParser mParser;
    private Dialog mBeautyDialog;
    private MaskDialog mMaskDialog;
    private boolean isEndCall;//判断是否是结束通话
    private boolean isCallSuccess;//判断是否连接成功过
    private boolean isConnection;//是否连接中
    private boolean isShowTimer;//是否展示倒计时
    private int mRemoteVideoUid;
    private EvaluationDialog mEvaluationDialog;
    private VideoPreProcessing videoPreProcessing;


    @Retention(RetentionPolicy.SOURCE)
    public @interface CallingType {
        //空闲            //通话中
        int LEISURE = 1, BUSY = 2;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calling;
    }

    @Override
    public void onBackPressed() {//返回按钮不能点击
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeDownSub != null)
            mTimeDownSub.dispose();
    }


    public static Intent getCallingIntent(Context context, ConditionEntity conditionEntity, String hint) {
        Intent intent = new Intent(context, CallingActivity.class);
        intent.putExtra("condition", conditionEntity);
        if (!Check.isEmpty(hint))
            intent.putExtra("hint", hint);
        return intent;
    }

    public static @CallingType
    int getmCallingState() {
        return mCallingState;
    }

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mParser = new SVGAParser(this);
        ConditionEntity conditionEntigy = getIntent().getParcelableExtra("condition");

        if (getIntent().hasExtra("hint")) {
            String[] strings = getIntent().getStringExtra("hint").split(";");
            mVideoCallTvHint.setText(Check.checkReplace(strings[0]));
            if (strings.length > 1) {//收到对方电话后显示提示信息
                Spanny spanny = new Spanny(strings[1]);
                spanny.findAndSpan("{钻石}", () -> new ImageSpan(getApplicationContext(), R.drawable.tab_icon_jewel));
                mVideoCallTvHintSmall.setText(spanny);
            }
        }
        getBasePresenter().setCondition(conditionEntigy);
        if (conditionEntigy != null) {
            if (conditionEntigy.getVideoType() == VideoType.NONE) {//发起视频聊天时候需要传入视频类型
                Toast.makeText(this, "未定义Calling类型", Toast.LENGTH_SHORT).show();
                endCall();
                return;
            }
            mCallingState = CallingType.BUSY;

            if (conditionEntigy.getVideoType() == VideoType.REDMAN) {
                if (conditionEntigy.getReadMainConfig().getRequestType() == ConditionEntity.RequestType.PERSON) {
                    VideoChatEntity mvideoConfig = conditionEntigy.getReadMainConfig().getVideoChatConfig();
                    mvideoConfig.set_location_VideoType(conditionEntigy.getVideoType());
                    getBasePresenter().setVideoChatEntity(mvideoConfig);
                    setCallingUserData(mvideoConfig);
                } else {
                    getBasePresenter().setSendEndCallMsg(true);
                    getBasePresenter().chatBegininfo();
                }
                getBasePresenter().addDisposable(RxCountDown.countdown(10).subscribe(//匹配成功后等待10秒执行连接
                        integer -> {
                            if (integer == 0) {
                                mVideoCallTvCountDown.setVisibility(View.GONE);
                                if (conditionEntigy.getReadMainConfig().getRequestType() == ConditionEntity.RequestType.PERSON) {
                                    //对方拨打电话
                                    getBasePresenter().callTimerStart(CallingPresenter.TIMER_CREATERECEIVE);//超时计时器
                                    mVideoCallIvAnswer.setVisibility(View.VISIBLE);
                                } else {//呼叫对方
                                    getBasePresenter().callTimerStart(CallingPresenter.TIMER_CREATECALL);//超时计时器
                                    startCallAnim();
                                }
                            } else {
                                mVideoCallTvCountDown.setVisibility(View.VISIBLE);
                                mVideoCallTvCountDown.setText(integer + "秒后即将开始...");
                            }
                        }));

            } else {
                if (conditionEntigy.getMatchConfig().getRequestType() == ConditionEntity.RequestType.PERSON) {
                    //对方拨打电话
                    getBasePresenter().callTimerStart(CallingPresenter.TIMER_CREATERECEIVE);//超时计时器
                    VideoChatEntity mvideoConfig = conditionEntigy.getMatchConfig().getVideoChatConfig();
                    mvideoConfig.set_location_VideoType(conditionEntigy.getVideoType());
                    getBasePresenter().setVideoChatEntity(mvideoConfig);
                    if (mvideoConfig.getTomask() != 0) {
                        mVideoCallTvHint.setTextColor(ContextCompat.getColor(context(), R.color.white));
                        mVideoCallTvHintSmall.setTextColor(ContextCompat.getColor(context(), R.color.block_gray));
                    }
                    setCallingUserData(mvideoConfig);
                    mVideoCallIvAnswer.setVisibility(View.VISIBLE);
                } else if (conditionEntigy.getMatchConfig().getRequestType() == ConditionEntity.RequestType.SELF) {
                    getBasePresenter().setSendEndCallMsg(true);
                    getBasePresenter().callTimerStart(CallingPresenter.TIMER_CREATECALL);//超时计时器
                    getBasePresenter().chatDefault(conditionEntigy.getMatchConfig().getMask());
                    startCallAnim();
                } else {
                    //默认匹配的时候要显示自己的视频端
//                    if (getBasePresenter().getCondition().getMatchConfig().getMask()!=0){
//                        mVideoCallIvBlurSmall.setVisibility(View.VISIBLE);
//                     GlideUtils.getInstance().loadContextBitmap(context(), MaskHandler.
//                                getSkinModes().get(getBasePresenter().getCondition().getMatchConfig().getMask())
//                                .background, mVideoCallIvBlurSmall, 0, 0, false);
//                    }
                    startCallAnim();
                    getBasePresenter().beginSearch();
                }
            }
        } else {
            Toast.makeText(this, "参数 conditionEntity 不能为空", Toast.LENGTH_SHORT).show();
            endCall();
        }
        mVideoCallCbbControlBtns.inflate(VideoType.NONE);
        setControlbtn(VideoType.NONE);
        enableProcess();
    }

    @Override
    protected void initWorkThread() {
        super.initWorkThread();
        openLocalVideo();
    }


    /**
     * 挂断动画
     */
    private void startCallAnim() {
        mVideoCallingWwvBG.setVisibility(View.VISIBLE);
        mVideoCallingWwvBG.startRippleAnimation();
//        mParser.parse("svga/callanim.svga", new SVGAParser.ParseCompletion() {//小浣熊动画 TODO
//            @Override
//            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
//                Drawable drawable=new SVGADrawable(svgaVideoEntity);
//                if (mVideoCallSvgaAnim==null)return;
//                mVideoCallSvgaAnim.setImageDrawable(drawable);
//                mVideoCallSvgaAnim.setVisibility(View.VISIBLE);
//                mVideoCallSvgaAnim.startAnimation();
//            }
//
//            @Override
//            public void onError() {
//            }
//        });
//
//        mVideoCallLavEndCallanim.playAnimation();
//        mVideoCallLavEndCallanim.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isVideoCalling) {
                worker().getRtcEngine().muteLocalVideoStream(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (isVideoCalling) {
                worker().getRtcEngine().muteLocalVideoStream(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initView() {
        ((FrameLayout.LayoutParams) mCallingFlVideoSmallLayout.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(context());
        ((FrameLayout.LayoutParams) mVideoCallRlUserinfo.getLayoutParams()).topMargin += DisplayUtil.getStatusBarHeight(context());
        //小屏与大屏幕的比例 5.8:1 4.56:1
        ((FrameLayout.LayoutParams) mCallingFlVideoSmallLayout.getLayoutParams()).width = (int) (DisplayUtil.getDisplayWidth(context()) / 3.89f);
        ((FrameLayout.LayoutParams) mCallingFlVideoSmallLayout.getLayoutParams()).height = (int) (DisplayUtil.getDisplayHeight(context()) / 4f);
        mAdapter = new ChatTextAdapter(R.layout.item_video_call_chat_text, null);
        mVideoCallRvChatText.setAdapter(mAdapter);

    }

    @Override
    protected void initUIandEvent() {
        //初始化视频UI和事件
        if (event() != null)
            event().addEventHandler(this);

        final String encryptionKey = NetConstants.S;
    /*
    “aes-128-xts”: 128 位 AES 加密， XTS 模式
    “aes-128-ecb”: 128 位 AES 加密， ECB 模式
    “aes-256-xts”: 256 位 AES 加密，XTS 模式
    “”: 设置为空字符串时，使用默认加密方式 “aes-128-xts”
     */
        final String encryptionMode = "aes-128-xts";

//        int vProfile = ConstantApp.VIDEO_PROFILES[getVideoProfileIndex()];
//        int vProfile = io.agora.rtc.Constants.VIDEO_PROFILE_360P_10;
        int vProfile = io.agora.rtc.Constants.VIDEO_PROFILE_360P_8;
        if (worker() != null) {
            worker().getRtcEngine().enableAudio();//开启声音
            worker().configEngine(vProfile, encryptionKey, encryptionMode);
            //设置视频音量
//            worker().getRtcEngine().setEnableSpeakerphone(true);
//            worker().getRtcEngine().setSpeakerphoneVolume(50);//0-255
            worker().getRtcEngine().muteLocalVideoStream(false);
        }//发送本地视频数据


    }

    /**
     * 开启本地视频预览
     */
    private void openLocalVideo() {
        if (!localVideoStarting) {//防止重复初始化本地视频
            localVideoStarting = true;
            EventBus.getDefault().post(new Intent(EventBusAction.ACTION_MAINOPENCAMERA));
            SurfaceView surfaceV = RtcEngine.CreateRendererView(FApplication.getApplication());
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, 0));
            mUidsList.put((int) UserManager.getInstance().getId(), surfaceV);
            mCallingFlVideoSmall.addView(surfaceV);
            surfaceV.setZOrderOnTop(true);
            surfaceV.setZOrderMediaOverlay(true);
            if (worker() != null)
                worker().preview(true, surfaceV, (int) UserManager.getInstance().getId());
        }
        enableProcess();
    }

    @Override
    public void deInitUIandEvent() {
        if (getBasePresenter().getCondition().getMatchConfig() != null && getBasePresenter().getCondition().getNeedCloseFU() == 0) {
        } else {
            FUManager.getInstance(context()).destroyItems();
        }

        //如果是匹配模式 停止搜索
        if (getBasePresenter().getCondition().getVideoType() == VideoType.MATCH) {
            getBasePresenter().forceOutSearch();

        }
        //释放视频资源
        optionalDestroy();
        doLeaveChannel();
        if (event() != null)
            event().removeEventHandler(this);
    }

    @Override
    protected void enableProcess() {
        if (videoPreProcessing == null)
            videoPreProcessing = new VideoPreProcessing();
        videoPreProcessing.enablePreProcessing(true);
    }

    public void doLeaveChannel() {
        isConnection = false;
        isVideoCalling = false;
        if (worker() != null)
            worker().leaveChannel(config().mChannel);
        initUIandEvent();//重新初始化视频数据
        NotificationHandler.cancle(Constants.NOTIFICATION_ID_VIDEO_CALL);//关闭通知
        RingtoneManager.getInstance().stopSoundVibrate();//关闭声音提示
        mVideoCallLav.stopAnimation(true);//停止礼物动画
        doLeaveChannelUI();
        if (mCallingFlVideoSmall.getTag().equals("1")) {
            switchVideo(0);
        }
    }

    public void doLeaveChannelUI() {
        //离开会话 1.如果是对方挂断并且是匹配模式 需要显示匹配界面
        if (getBasePresenter().getCondition().getVideoType() == VideoType.MATCH && !isEndCall) {
            mVideoCallFlCalling.animate().alpha(1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mVideoCallFlCalling != null)
                        mVideoCallFlCalling.setVisibility(View.VISIBLE);
                }
            }).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
            mVideoCallCbbControlBtns.inflate(VideoType.NONE);
            startCallAnim();
            doRemoveRemoteUi(mRemoteVideoUid);
            mVideoCallRlUserinfo.setVisibility(View.GONE);
            mCallingFlVideoBig.setOnClickListener(null);
            mVideoCallingTvLocation.setVisibility(View.GONE);
            mVideoCallingTvName.setVisibility(View.GONE);
            mVideoCallingIvHeader.setImageResource(R.drawable.video_calling_logo);
            setSystemUIVisible(true);
            stopTimeDown();
            mVideoCallTvHint.setText("匹配中...");
            mVideoCallTvSmallCalling.setText("匹配中..");
            getBasePresenter().beginSearch();//开启搜索
        }
    }


    private void optionalDestroy() {
//        worker().getRtcEngine().disableAudio();//关闭声音
        if (worker() != null) {
            worker().getRtcEngine().muteLocalVideoStream(true);//不发送本地视频数据
//            worker().getRtcEngine().setEnableSpeakerphone(false);
        }
//        setVolumeControlStream(mDefaultAutioVolume);
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        //视频接通
        getBasePresenter().callTimerStop();
        doRenderRemoteUi(uid);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        //连接成功
        getBasePresenter().callTimerStop();
        getBasePresenter().callTimerStart(CallingPresenter.TIMER_JOINSUCCESS);//加入通道成功后开始计时防止一直未连接
        isVideoCalling = true;
        SurfaceView local = mUidsList.get(0);
        mUidsList.remove(0);
        if (local == null) {
            return;
        }
        mUidsList.put(uid, local);
        NotificationHandler.createChannelNoSound(NotificationHandler.channelId_NoticeNoSound, "通话中");
        NotificationHandler.videoNotificationNoSound(getBasePresenter().getVideoChatEntity(), 2);
    }

    @Override
    public void onUserOffline(int uid, int reason) {
//
        isVideoCalling = false;
        doRemoveRemoteUi(uid);
        if (getBasePresenter().getCondition().getVideoType() != VideoType.MATCH) {
            //对方挂断或离线  如果是匹配状态需要重新进入匹配 如果是通话状态需要直接挂断
            //内部Socket方法中已经回调doLeaveChannel方法这里不需要调用 {@link socket 命令中 "endChatnotice"命令}
            endCall();
        }
    }

    @Override
    public void onExtraCallback(int type, Object... data) {
        //其他状态
        doHandleExtraCallback(type, data);
    }


    /**
     * 远程视频连接
     *
     * @param uid
     */
    private void doRenderRemoteUi(int uid) {
        mRemoteVideoUid = uid;
        runOnUiThread(() -> {
            if (isFinishing()) {
                return;
            }
            if (mUidsList.get(mRemoteVideoUid) != null) {
                return;
            }
            setSystemUIVisible(false);
            mCallingFlVideoBig.setOnClickListener(v -> {
                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                uiFlags |= 0x00001000;
                if (getWindow().getDecorView().getSystemUiVisibility() == uiFlags) {
                    setSystemUIVisible(false);
                } else {
                    setSystemUIVisible(true);
                }
            });
            remoteUiForVideoType();
            SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
            mUidsList.put(mRemoteVideoUid, surfaceV);
//            boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 2;
            rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, mRemoteVideoUid));
            if (Integer.parseInt(mCallingFlVideoSmall.getTag().toString()) == 0) {
                mCallingFlVideoBig.addView(surfaceV);
                surfaceV.setZOrderMediaOverlay(true);
                surfaceV.setZOrderOnTop(false);
            } else {
                mCallingFlVideoSmall.addView(surfaceV);
                surfaceV.setZOrderMediaOverlay(true);
                surfaceV.setZOrderOnTop(true);
            }
            if (getBasePresenter().getVideoChatEntity() != null) {//启动计时器
                ConditionEntity condition = getBasePresenter().getCondition();
                if (condition.getVideoType() == VideoType.REDMAN) {//红人模式扣费
                    if (!isShowTimer)
                        setTimeUp();
                } else {
                    if (!isShowTimer)
                        setTimeDown((getBasePresenter().getVideoChatEntity().getEndtime() - getBasePresenter().getVideoChatEntity().getBegintime()));
                }
                //如果是红人模式。就打开查询微信号功能
                if (condition.getVideoType() == VideoType.REDMAN
                        && getBasePresenter().getCondition().getReadMainConfig().getRedManId() != UserManager.getInstance().getId()
                        && getBasePresenter().getCondition().getReadMainConfig().getRedManId() != 0
                        && isVideoCalling) {
                    mCheckContact.setVisibility(View.VISIBLE);
                }
            }
            setControlbtn(getBasePresenter().getCondition().getVideoType());
            isCallSuccess = true;
        });
        enableProcess();
    }

    /**
     * 根据不同的视频类型设置不同的UI显示状态
     */
    public void remoteUiForVideoType() {
        if (getBasePresenter().getCondition() == null) return;
//        mVideoCallLlHint.setVisibility(View.GONE);
        mVideoCallRlEndCallbtn.setVisibility(View.GONE);
        mVideoCallFlCalling.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mVideoCallFlCalling.setVisibility(View.GONE);
            }
        }).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        mVideoCallCbbControlBtns.setVisibility(View.VISIBLE);// 底部控制按钮显示
        mVideoCallCbbControlBtns.initButionStatus(getBasePresenter().getVideoChatEntity());
        ConditionEntity condition = getBasePresenter().getCondition();
        mVideoCallCbbControlBtns.showNumberCountDown();

        if (condition.getVideoType() == VideoType.REDMAN
                && condition.getReadMainConfig().getRequestType() == ConditionEntity.RequestType.SELF
                && condition.getReadMainConfig().getRedManId() != UserManager.getInstance().getId()) {//红人模式扣费
            getBasePresenter().chatCoinConsumption(Integer.parseInt(getBasePresenter().getVideoChatEntity().getCallid()), 4, 0);
        }
        if (condition.getVideoType() == VideoType.MATCH) {
            if (condition.getMatchConfig().getRequestType() == ConditionEntity.RequestType.SELF) {//直聊扣费
                getBasePresenter().chatCoinConsumption(Integer.parseInt(getBasePresenter().getVideoChatEntity().getCallid()), 9, 0);
            } else {//匹配扣费
                if (condition.getMatchConfig().getType() == ConditionEntity.ConditionTtype.FILTER)
                    getBasePresenter().chatCoinConsumption(Integer.parseInt(getBasePresenter().getVideoChatEntity().getCallid()), 5, 0);
            }
        }
    }

    private void doRemoveRemoteUi(final int uid) {
        if (mRemoteVideoUid != 0) mRemoteVideoUid = 0;
        runOnUiThread(() -> {
            if (isFinishing()) {
                return;
            }

            SurfaceView target = mUidsList.get(uid);
            if (target == null) {
                return;
            }
            mUidsList.remove(uid);
            ViewGroup parentView = (ViewGroup) target.getParent();
            parentView.removeView(target);
        });
    }


    private void doHandleExtraCallback(int type, Object... data) {
        switch (type) {

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG://收到声网Socket消息
                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {//消息发送错误 ERR_SIZE_TOO_LARGE/ERR_TOO_OFTEN/ERR_BITRATE_LIMIT
                break;
            }
            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:

                break;

        }
    }


    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(int flag, String message) {
        switch (flag) {
//            case -8409://对方正在聊天中
//                showError(message);
//                endCall();
//                break;
            case -8405://余额不足
                CommDialogUtils.showInsufficientBalance(this, new GeneralDialog.CallBack() {
                    @Override
                    public void submitClick(Dialog dialog) {
                        coinPay();
                        dialog.dismiss();
                    }

                    @Override
                    public void cancelClick(Dialog dialog) {

                    }
                });
                break;
            case -1:
                new MaterialDialog.Builder(context())
                        .canceledOnTouchOutside(false)
                        .cancelable(false)
                        .content(message)
                        .negativeText("好的")
                        .negativeColorRes(R.color.base_yellow)
                        .onNegative((dialog, which) -> {
                            finish();
                        })
                        .show();
                break;
            case -2:
                getBasePresenter().setEndFlag(2);
                finish();
                break;
            case -3:
                showHint(HintDialog.HintType.WARN, message);
                break;
            default:
                if (!Check.isEmpty(message) && !isVideoCalling) {
                    if (message.startsWith("SearchTimeOut::")) {
                        new MaterialDialog.Builder(context()).title("非常抱歉")
                                .content(message.split("::")[1])
                                .negativeText("知道了")
                                .negativeColor(ContextCompat.getColor(context(), R.color.base_yellow))
                                .onNegative((dialog, which) -> {
                                    dialog.dismiss();
                                    endCall();
                                })
                                .show();
                    } else {
                        showHint(HintDialog.HintType.WARN, message);
                    }
                }
                break;
        }

    }

    public void showHint(int type, String hint) {
        if (mHintDialog == null) {
            mHintDialog = new HintDialog(context(), HintDialog.HintType.WARN);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
            mHintDialog.setTitleText(hint);
        } else {
            if (mHintDialog.getType() != type) {
                mHintDialog.setType(type);
            }
            mHintDialog.setTitleText(hint);
            mHintDialog.showToast(HintDialog.LENGTH_SHORT);
        }
    }


    @Override
    public void endCall() {
        Logger.i("关闭视频");
        mCallingState = CallingType.LEISURE;
        stopTimeDown();
        deInitData();
        if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN &&
                getBasePresenter().getCondition().getReadMainConfig().getRedManId() != UserManager.getInstance().getId()
                && getBasePresenter().getCondition().getReadMainConfig().getRedManId() != 0
                && isCallSuccess) {
            //如果是红人模式 并且红人不是自己。需要评价
            getBasePresenter().destroy();
            if (mEvaluationDialog == null) {
                mEvaluationDialog = new EvaluationDialog(this);
                mEvaluationDialog.setEvalutaionUserId(getBasePresenter().getCondition().getReadMainConfig().getRedManId());
                mEvaluationDialog.setCallBack(new EvaluationDialog.CallBack() {
                    @Override
                    public void cancel() {
                        finish();
                    }

                    @Override
                    public void submitSuccess() {
                        finish();
                    }
                });
                mEvaluationDialog.show();
            }
        } else {
            if (mHintDialog != null && mHintDialog.isShowing()) {
                RxCountDown.delay(3).subscribe(
                        integer -> {
                            finish();
                        }
                        , Throwable::printStackTrace
                );
            } else {
                finish();
            }
        }
    }


    @Override
    public void showGift(GiftEntity entity) {
        if (entity == null || entity.get_localMode() == null) return;
        GiftLocalMode mode = entity.get_localMode();
        if (entity.get_localMode().getType() == GiftLocalMode.GiftType.SVGA) {
            mParser.parse(mode.getJsonAbsolute(), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mVideoCallLav.setImageDrawable(drawable);
                    mVideoCallLav.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } else {
            if (mGiftControl == null) {
                mGiftControl = new GiftControl(this);
                mGiftControl.setGiftLayout(mVideoCallLlGift, 3)
                        .setHideMode(false)
                        .setCustormAnim(new CustormAnim());//这里可以自定义礼物动画
                mDescFormat = "%s \n送出 %s";
            }
            GiftModel giftModel = new GiftModel();//如果是1对多的直播情况向不能在此创建对象
            String path = "file:///android_asset/" + mode.getFistImageAbsolute();
            String desc = String.format(mDescFormat, entity.getSendUserName(), entity.getName());
            Spanny spanny = new Spanny(desc);
            spanny.findAndSpan(entity.getSendUserName() + " ", () ->
                    new ForegroundColorSpan(ContextCompat.getColor(context(), R.color.base_yellow)));
            spanny.findAndSpan(entity.getName(), () ->
                    new ForegroundColorSpan(ContextCompat.getColor(context(), R.color.base_yellow)));
            giftModel
                    .setDescription(spanny)
                    .setGiftId(entity.getId() + "")
                    .setGiftName(entity.getName())
                    .setGiftCount(entity.getPayCount())
                    .setGiftPic(path)
                    .setSendUserId(entity.getSendUserId())
                    .setSendUserName(entity.getSendUserName())
                    .setSendGiftTime(System.currentTimeMillis());
            mGiftControl.loadGift(giftModel);
        }


//        mVideoCallLav.setImageAssetsFolder(mode.getImagesAbsolute());
//        mVideoCallLav.setAnimation(mode.getJsonAbsolute());
//        mVideoCallLav.loop(false);
//        mVideoCallLav.playAnimation();
    }

    @Override
    public void addMaskOther() {
        mVideoCallCbbControlBtns.initButionStatus(getBasePresenter().getVideoChatEntity());
    }

    @Override
    public void chatMaskEnable(boolean enabled) {
        mVideoCallCbbControlBtns.setEnabledMask(enabled);//对方揭面后不可在点击面具
        getMaskDialog(false).setChooseEnable(enabled);
    }


    /**
     * 控制条功能设置
     *
     * @param type
     */
    private void setControlbtn(@VideoType int type) {
        mVideoCallLav.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                Logger.i("动画控件:onPause");
//                mVideoCallLav.setVisibility(View.VISIBLE);
//                mVideoCallLav.animate().scaleX(1).scaleY(1).setDuration(200).start();
            }

            @Override
            public void onFinished() {
                Logger.i("动画控件:onFinished");
//                mVideoCallLav.setVisibility(View.GONE);
//                mVideoCallLav.animate().scaleX(0f).scaleY(0f).setDuration(200).start();
            }

            @Override
            public void onRepeat() {
//                Logger.i("动画控件:onRepeat");
            }

            @Override
            public void onStep(int i, double v) {
                Logger.i("动画控件:onStep");
            }
        });

        mVideoCallCbbControlBtns.inflate(type);//控制条配置
        if (type == VideoType.MATCH) {
            mVideoCallCbbControlBtns.setMatchListener(new CallButtomBtns.MatchListener() {
                @Override
                public void onMaskTa(View view) {
                    if (getBasePresenter().getVideoChatEntity().getTomask() != 0) {
                        GeneralDialog dialog = new GeneralDialog(context());
                        dialog
                                .setContent("本次揭面需花费您 50钻")
                                .setCallBack(new GeneralDialog.CallBack() {
                                    @Override
                                    public void submitClick(Dialog dialog) {
                                        getBasePresenter().chatCoinConsumption(
                                                Integer.parseInt(getBasePresenter().getVideoChatEntity().getCallid())
                                                , 6
                                                , 1
                                        );
                                    }

                                    @Override
                                    public void cancelClick(Dialog dialog) {

                                    }
                                }).show();

                    }
                }

                @Override
                public void onMaskWo(View view) {
                    getMaskDialog(true).show();
                }

                @Override
                public void onGift(View view) {
                    if (getBasePresenter().getGifts() == null) return;
                    GiftFragmentDialog giftDialog = getGiftDialog(GiftFragmentDialog.GiftDialogStyle.AddTime);
                    if (!giftDialog.isVisible())
                        giftDialog.show(getSupportFragmentManager(), GiftFragmentDialog.TAG);
                    mVideoCallCbbControlBtns.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onBeauty() {
                    getBeautyDialog().show();
                }

                @Override
                public void close() {
                    endCallBtn();
                }
            });
        } else if (type == VideoType.REDMAN) {
            if (getBasePresenter().getCondition().getReadMainConfig().getRedManId() != UserManager.getInstance().getId()
                    && getBasePresenter().getCondition().getReadMainConfig().getRedManId() != 0) {
                mVideoCallCbbControlBtns.setShowAttention(View.VISIBLE);
            } else {
                mVideoCallCbbControlBtns.setShowAttention(View.INVISIBLE);
            }

            mVideoCallCbbControlBtns.setRedmanListener(new CallButtomBtns.RedmanListener() {
                @Override
                public void onAttention(boolean isChecked) {
                    getBasePresenter().favorite(isChecked ? 1 : 0);
                }

                @Override
                public void onGift(View view) {
                    if (getBasePresenter().getGifts() == null) return;
                    GiftFragmentDialog giftDialog = getGiftDialog(GiftFragmentDialog.GiftDialogStyle.Normal);
                    if (!giftDialog.isVisible())
                        giftDialog.show(getSupportFragmentManager(), GiftFragmentDialog.TAG);
                    mVideoCallCbbControlBtns.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onBeauty() {
                    getBeautyDialog().show();
                }

                @Override
                public void onMaskWo(View view) {
                    getMaskDialog(true).show();
                }

                @Override
                public void close() {
                    endCallBtn();
                }
            });
        } else if (type == VideoType.NONE) {
            mVideoCallCbbControlBtns.setRedmanListener(new CallButtomBtns.RedmanListener() {
                @Override
                public void onAttention(boolean isChecked) {
                }

                @Override
                public void onGift(View view) {
                }

                @Override
                public void onBeauty() {
                }

                @Override
                public void onMaskWo(View view) {
                }

                @Override
                public void close() {
                    endCallBtn();
                }
            });
        }

    }

    /**
     * 获取礼物对话框
     */
    private GiftFragmentDialog getGiftDialog(int gitDialogStyle) {
        if (mGiftDialog == null) {
            mGiftDialog = new GiftFragmentDialog(gitDialogStyle);
            mGiftDialog.setDatas(getBasePresenter().getGifts());
            mGiftDialog.setGiftEventListener(new GiftFragmentDialog.GiftEventListener() {
                @Override
                public void okBtn(GiftEntity gift) {
                    if (gitDialogStyle == GiftFragmentDialog.GiftDialogStyle.AddTime) {
                        getBasePresenter().postAddTime(gift, gift.getAddtime(), 8);
                    } else {
                        getBasePresenter().postGiftData(gift);
                    }
                }

                @Override
                public void pay() {
                    coinPay();
                }

                @Override
                public String startCoin() {
                    String coin;
                    if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN &&
                            getBasePresenter().getCondition().getReadMainConfig().getRequestType() == ConditionEntity.RequestType.SELF) {
                        coin = String.valueOf(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() - getBasePresenter().getCondition().getReadMainConfig().getRedMainStartCoin());
                        return coin;
                    } else
                        return UserManager.getInstance().getCurrentUser().getUserinfo().getCoin() + "";
                }

                @Override
                public void cancel() {
                    if (mVideoCallCbbControlBtns != null)
                        mVideoCallCbbControlBtns.setVisibility(View.VISIBLE);
                }
            });
        }
        return mGiftDialog;
    }

    private MaskDialog getMaskDialog(boolean hideControlBtns) {
        if (hideControlBtns) {
            mVideoCallCbbControlBtns.animate().alpha(0).setDuration(300).start();
        }
        if (mMaskDialog == null) {
            mMaskDialog = new MaskDialog(context());
            mMaskDialog.setMaskChooseListener(chooseMask -> {
                if (chooseMask) {
                    getBasePresenter().videoCommond("USER_SELF_ADD_MASK", 0);//用户带上面具
                } else {
                    getBasePresenter().videoCommond("USER_SELF_MASK", 0);//用户自己揭面
                }
            });
        }
        if (!mMaskDialog.isShowing()) {//防止当对话框打开时候对方揭面无法显示控制条问题
            mMaskDialog.setOnDismissListener(dialog -> {
                if (hideControlBtns) {
                    mVideoCallCbbControlBtns.animate().alpha(1).setDuration(300).start();
                }
            });
        }

        return mMaskDialog;
    }

    public Dialog getBeautyDialog() {
        mVideoCallCbbControlBtns.animate().alpha(0).setDuration(300).start();
        if (mBeautyDialog == null) {
            mBeautyDialog = new BeautyDialog(this);
            mBeautyDialog.setOnDismissListener(dialog1 -> mVideoCallCbbControlBtns.animate().alpha(1).setDuration(300).start());
        }
        return mBeautyDialog;
    }

    private void coinPay() {
        if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN &&
                getBasePresenter().getCondition().getReadMainConfig().getRequestType() == ConditionEntity.RequestType.SELF) {
            getNavigator().navtoCoinPay(CallingActivity.this,
                    String.valueOf(UserManager.getInstance().getCurrentUser().getUserinfo().getCoin()
                            -
                            getBasePresenter().getCondition().getReadMainConfig().getRedMainStartCoin()));
        } else {
            getNavigator().navtoCoinPay(CallingActivity.this, null);
        }
    }


    //    @OnClick({R.id.video_call_iv_endCallbtn, R.id.video_call_iv_answer, R.id.calling_fl_video_small})TODO
    @OnClick({R.id.video_call_iv_answer, R.id.calling_fl_video_small, R.id.tv_check_contact})
    public void onClickView(View view) {
        switch (view.getId()) {
//            case R.id.video_call_iv_endCallbtn://挂断 TODO
//                endCallBtn();
//                break;
            case R.id.video_call_iv_answer://接听
                view.setVisibility(View.GONE);
                getBasePresenter().answer();
                RingtoneManager.getInstance().stopSoundVibrate();
                break;
            case R.id.calling_fl_video_small://画面切换
                int videoTag = Integer.parseInt(mCallingFlVideoSmall.getTag().toString());
                switchVideo(videoTag);
                break;
            case R.id.tv_check_contact:
                showContactDialog();
                break;
        }
    }

    /**
     * 展示联系信息弹窗
     */
    private void showContactDialog() {
        CheckContactDialog dialog = new CheckContactDialog(this);
        dialog.setReadManId(getBasePresenter().getCondition().getReadMainConfig().getRedManId());

        dialog.show();
    }

    private void switchVideo(int videoTag) {
        if (videoTag == 0) {
            mCallingFlVideoSmall.setTag(1);
            mVideoCallTvSmallCalling.setVisibility(View.VISIBLE);
            if (!isVideoCalling) {
                mVideoCallTvSmallCalling.setText(mVideoCallTvHint.getText().toString());
            } else {
                mVideoCallTvSmallCalling.setText("");
            }
            SurfaceView localVideo = mUidsList.get((int) UserManager.getInstance().getId());
            SurfaceView remoteVideo = mUidsList.get(mRemoteVideoUid);
            mCallingFlVideoSmall.removeView(localVideo);
            mCallingFlVideoBig.addView(localVideo);
            localVideo.setZOrderOnTop(false);
//            localVideo.setZOrderMediaOverlay(true);
            if (remoteVideo != null) {
                mCallingFlVideoBig.removeView(remoteVideo);
                mCallingFlVideoSmall.addView(remoteVideo);
                remoteVideo.setZOrderOnTop(true);
                remoteVideo.setZOrderMediaOverlay(true);
            }
        } else {
            mCallingFlVideoSmall.setTag(0);
            mVideoCallTvSmallCalling.setVisibility(View.GONE);
            SurfaceView localVideo = mUidsList.get((int) UserManager.getInstance().getId());
            SurfaceView remoteVideo = mUidsList.get(mRemoteVideoUid);
            mCallingFlVideoBig.removeView(localVideo);
            if (remoteVideo != null) {
                mCallingFlVideoSmall.removeView(remoteVideo);
                mCallingFlVideoBig.addView(remoteVideo);
                remoteVideo.setZOrderOnTop(false);
//                remoteVideo.setZOrderMediaOverlay(true);
            }
            mCallingFlVideoSmall.addView(localVideo);
            localVideo.setZOrderOnTop(true);
            localVideo.setZOrderMediaOverlay(true);
        }
    }

    private void endCallBtn() {
        if (isVideoCalling) {
            GeneralDialog dialog = new GeneralDialog(this);
            dialog
                    .setContent("是否挂断当前视频聊天？")
                    .setCallBack(new GeneralDialog.CallBack() {
                        @Override
                        public void submitClick(Dialog dialog) {
                            if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN) {
                                getBasePresenter().setEndFlag(1);
                            }
                            RingtoneManager.getInstance().stopSoundVibrate();
                            isEndCall = true;
                            endCall();
                        }

                        @Override
                        public void cancelClick(Dialog dialog) {

                        }
                    }).show();
        } else {
            if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN
                    && getBasePresenter().getCondition().getReadMainConfig().getRequestType() == ConditionEntity.RequestType.PERSON) {
                getBasePresenter().setEndFlag(1);
            }
            RingtoneManager.getInstance().stopSoundVibrate();
            isEndCall = true;
            endCall();
        }
    }

    /**
     * 是否开始动画
     */
    private boolean isTimeAnimStart = false;

    /**
     * 倒计时动画
     *
     * @return
     */
    private AnimationSet timeAnima() {
        isTimeAnimStart = true;
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    /**
     * 倒计时用户聊天用户时长
     */
    private void stopTimeDown() {
        isShowTimer = false;
        mVideoCallLlTimeDown.setVisibility(View.GONE);
        mVideoCallCbbControlBtns.setAddTimeShow(View.GONE);
        mTimeText.setVisibility(View.GONE);
        mTimeProgressView.setVisibility(View.GONE);
        if (mTimeDownSub != null && !mTimeDownSub.isDisposed()) {
            mTimeDownSub.dispose();
        }
        mTimeDownSub = null;
    }

    /**
     * 记时用户聊天时长
     */
    private void setTimeUp() {
        isShowTimer = true;
        //设置进度条样式
        mTimeProgressView.setVisibility(View.VISIBLE);
        mTimeProgressView.clearAnimation();
        mTimeProgressView.setProgress(100);
        mTimeProgressView.setColor(getResources().getColor(R.color.progress_green));
        mTimeText.setVisibility(View.VISIBLE);

        if (mTimeDownSub != null && !mTimeDownSub.isDisposed()) {
            mTimeDownSub.dispose();
        }
        mTimeText.setTimeUpText("00:00");
        mTimeDownSub = RxCountDown.interval(1).subscribe(totalTime -> {
            String time = TimeUtils.millisToMinStr((int) (totalTime * 1000));
            mTimeText.setTimeUpText(time);
        });
    }

    /**
     * 倒计时用户聊天用户时长
     */
    private void setTimeDown(int timeLength) {
        isShowTimer = true;
//        mVideoCallLlTimeDown.setVisibility(View.VISIBLE);
        mVideoCallProgressBar.setMax(timeLength);
        mVideoCallProgressBar.setProgress(timeLength);
        mTimeProgressView.setProgress(100);
        mTimeProgressView.setVisibility(View.VISIBLE);
        mTimeText.setVisibility(View.VISIBLE);
        mTimeText.setTimeDownText(timeLength);
        //设置进度条颜色
        mTimeProgressView.setColor(getResources().getColor(R.color.progress_green));
        if (mTimeDownSub != null && !mTimeDownSub.isDisposed()) {
            mTimeDownSub.dispose();
        }
        mTimeDownSub = RxCountDown.countdown(timeLength).subscribe(
                integer -> {
//                    mVideoCallTvTime.setText(String.format("%ss", integer));
                    mVideoCallProgressBar.setProgress(integer);
                    mTimeProgressView.setProgress(((float) integer / (float) timeLength) * 100);
                    mTimeText.setTimeDownText(integer);
                    if (integer <= 20) {//倒计时小于二十秒提示用户
//                        mVideoCallTvTime.setTextColor(ContextCompat.getColor(context(), R.color.base_red));
//                        mVideoCallProgressBar.setProgressDrawable(ContextCompat.getDrawable(context(), R.drawable.video_call_progress_bar_red));
                        if (!isTimeAnimStart)
                            mTimeProgressView.startAnimation(timeAnima());
//                        mVideoCallTvTime.setTag("red");
                        //设置进度条颜色
                        mTimeProgressView.setColor(getResources().getColor(R.color.progress_red));
                        mVideoCallCbbControlBtns.setAddTimeShow(View.VISIBLE);
                    } else if (integer > 20) {
//                        mVideoCallTvTime.setTag(null);
//                        mVideoCallTvTime.setTextColor(ContextCompat.getColor(context(), R.color.white));
//                        mVideoCallProgressBar.setProgressDrawable(ContextCompat.getDrawable(context(), R.drawable.video_call_progress_bar));
                        mTimeProgressView.clearAnimation();
                        isTimeAnimStart = false;
                        //设置进度条颜色
                        mTimeProgressView.setColor(getResources().getColor(R.color.progress_green));
                        mVideoCallCbbControlBtns.setAddTimeShow(View.GONE);
                    }
                    if (integer == 0) {
                        endCall();
                    }
                }
                , Throwable::printStackTrace
        );
    }


    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    @Override
    public void chatOpenSuccess(VideoChatEntity videoChatEntity) {
        if (videoChatEntity == null) {
            Toast.makeText(this, "无法开启视频聊天", Toast.LENGTH_SHORT).show();
            endCall();
            return;
        }
        isConnection = true;
        int delayTime = 1;
        if (getBasePresenter().getCondition().getVideoType() == VideoType.REDMAN) {
            if (getBasePresenter().getCondition().getReadMainConfig().getRequestType() == ConditionEntity.RequestType.SELF) {
                delayTime = 10;
            }
        }
        setCallingUserData(videoChatEntity);
        getBasePresenter().addDisposable(RxCountDown.delay(delayTime).subscribe(//匹配成功后等待10秒执行连接
                integer -> {
                    if (!isConnection)
                        return;
                    //设置聊天界面数据
                    mVideoCallingWwvBG.stopRippleAnimation();
                    setUserData(videoChatEntity);
                    mVideoCallingTvLocation.setVisibility(View.GONE);
                    mVideoCallingTvName.setVisibility(View.GONE);
                    mVideoCallingWwvBG.setVisibility(View.INVISIBLE);
                    mVideoCallingIvHeader.setImageResource(R.drawable.video_calling_logo);
                    openLocalVideo();
                    String channelName = videoChatEntity.getChannelName();
                    mVideoCallTvHint.setText("连接中...");
                    mVideoCallTvSmallCalling.setText("连接中...");
                    if (worker() != null)
                        worker().joinChannel(channelName, config().mUid);
                    isConnection = false;
                }
        ));
    }

    /**
     * 设置用户拨打电话进入时候的用户数据
     *
     * @param videoChatEntity
     */
    private void setCallingUserData(VideoChatEntity videoChatEntity) {
        mVideoCallingTvLocation.setVisibility(View.VISIBLE);
        mVideoCallingTvName.setVisibility(View.VISIBLE);
        mVideoCallingWwvBG.setVisibility(View.VISIBLE);
        //设置匹配界面数据 头像 名称 距离
        GlideUtils.getInstance().loadBitmapNoAnim(this, Check.checkReplace(videoChatEntity.getTouidinfo().getUserphoto_thumb()),
                mVideoCallingIvHeader);
        mVideoCallingTvName.setText(Check.checkReplace(videoChatEntity.getTouidinfo().getNickname()));
        String locationStr = "未知";
        if (!Check.isEmpty(videoChatEntity.getTouidinfo().getArea())) {
            locationStr = videoChatEntity.getTouidinfo().getArea();
        } else if (!Check.isEmpty(videoChatEntity.getTouidinfo().getDistince())) {
            locationStr = videoChatEntity.getTouidinfo().getDistince();
        }
        mVideoCallingTvLocation.setText(locationStr);
    }


    @Override
    public int chatMaskDisclose(boolean isSelf, int maskLayout) {//揭面
        mVideoCallCbbControlBtns.initButionStatus(getBasePresenter().getVideoChatEntity());
        if (!isSelf) {//对方的蒙面状态取消的时候需要显示头像
            mVideoCallIvHeader.setVisibility(View.VISIBLE);//蒙面状态不显示头像
            GlideUtils.getInstance().loadContextBitmap(context(), Check.checkReplace(getBasePresenter().getVideoChatEntity().getTouidinfo().getUserphoto_thumb()),
                    mVideoCallIvHeader, false);
            return 0;
        } else {
            return getMaskDialog(false).clearMask(maskLayout);
        }
    }

    @Override
    public void showBalanceDeficiency() {//余额不足
        CommDialogUtils.showInsufficientBalance(this, new GeneralDialog.CallBack() {
            @Override
            public void submitClick(Dialog dialog) {
                coinPay();
            }

            @Override
            public void cancelClick(Dialog dialog) {

            }
        });
    }

    @Override
    public void cancelGiftDialog() {
        if (mGiftDialog != null)
            mGiftDialog.dismiss();
    }

    @Override
    public void resultFavoriteSuccess() {//收藏成功

    }

    @Override
    public void setChatText(TextChatMode chatText) {
        if (chatText == null)
            return;
        mAdapter.addData(mAdapter.getData().size(), chatText);
        mVideoCallRvChatText.getLayoutManager().scrollToPosition(0);
        getBasePresenter().addDisposable(mAdapter.startTimeDown());
    }

    @Override
    public void animOvertime(int addtime, String animStr) {
        getBasePresenter().getVideoChatEntity().setEndtime(getBasePresenter().getVideoChatEntity().getEndtime() + addtime);
        int timelenght = mVideoCallProgressBar.getProgress() + addtime;
        setTimeDown(timelenght);
        mVideoCallTvHintAnim.setAlpha(1);
        mVideoCallTvHintAnim.setTranslationY(0);
        mVideoCallTvHintAnim.setText(animStr);
        mVideoCallTvHintAnim
                .animate()
                .alpha(0)
                .translationY(-DisplayUtil.getDisplayHeight(context()) / 2).setInterpolator(new AccelerateInterpolator())
                .setDuration(800)
                .start();
    }


    public void setUserData(VideoChatEntity videoChatEntity) {
        if (videoChatEntity != null && videoChatEntity.getTouidinfo() != null) {
            if (videoChatEntity.getTomask() != 0) {//蒙面状态判断
//                mVideoCallIvBlurBig.setVisibility(View.VISIBLE);
                mVideoCallIvHeader.setVisibility(View.GONE);//蒙面状态不显示头像
                // GlideUtils.getInstance().loadContextBitmap(context(), MaskHandler.getSkinModes().get(videoChatEntity.getTomask()).background, mVideoCallIvBlurBig, 0, 0, false);
            } else {
                GlideUtils.getInstance().loadContextBitmap(context(), Check.checkReplace(videoChatEntity.getTouidinfo().getUserphoto_thumb()),
                        mVideoCallIvHeader, false);
            }
//            if (videoChatEntity.getFrommask() != 0) {//蒙面状态判断
//                mVideoCallIvBlurSmall.setVisibility(View.VISIBLE);
            // GlideUtils.getInstance().loadContextBitmap(context(), MaskHandler.getSkinModes().get(videoChatEntity.getFrommask()).background, mVideoCallIvBlurSmall, 0, 0, false);
//            }

            mVideoCallTvNickName.setText(Check.checkReplace(videoChatEntity.getTouidinfo().getNickname()));
            int sex = videoChatEntity.getTouidinfo().getSex() == 1 ? R.drawable.bg_icon_man_1 : R.drawable.bg_icon_woman_1;
            TextViewDrawableUtils.setDrawable(mVideoCallTvNickName, null, null, ContextCompat.getDrawable(context(), sex), null);
            String locationStr = "未知";
            if (!Check.isEmpty(videoChatEntity.getTouidinfo().getDistince())) {
                locationStr = videoChatEntity.getTouidinfo().getDistince();
            } else if (!Check.isEmpty(videoChatEntity.getTouidinfo().getArea())) {
                locationStr = videoChatEntity.getTouidinfo().getArea();
            }
            mVideoCallTvLocation.setText(locationStr);
            mVideoCallRlUserinfo.setVisibility(View.VISIBLE);
        }
    }
}

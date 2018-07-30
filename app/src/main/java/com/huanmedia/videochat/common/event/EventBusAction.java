package com.huanmedia.videochat.common.event;

/**
 * Create by Administrator
 * time: 2017/11/15.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class EventBusAction {
    public final static String SCHEME_ACTION="action";

    public final static String LUANCH_HOST="lunach.huanmedia.com";
    /**
     * 更新主界面右滑菜单中的聊过的人和关注的人数据
     */
    public final static String ACTION_CHATPEOPLEUPATA=EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/updata_chatpeopledata";
    /**
     * 绑定账户变更
     */
    public final static String ACTION_BOUND_ACCOUND_CHANGE=EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/bound_accound_change";
    /**
     * 用户数据更改
     */
    public final static String ACTION_USERDATA_CHANGE=EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/userDataChange";
    /**
     * 重置主界面相机
     */
    public final static String ACTION_MAINOPENCAMERA=EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/MainOpenCamera";

    /**
     * 照片墙发生改变
     */
    public final static String ACTION_USER_PHOTOS_CHANGE =EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/UserPhotosChange";
    /**
     * 主动获取用户信息
     */
    public final static String ACTION_USERINFO_UPDATE =EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/userinfoUpdate";

    /**
     * 更新系统消息
     */
    public final static String ACTION_SYSTEM_MESSAGE =EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/upSystemMessageCount";
    /**
     * 聊天消息(预约)
     */
    public final static String ACTION_CHAT_MESSAGE_APPOINTMENT =EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/upChatMessageCountAppointment";
    /**
     * 金币更变
     */
    public final static String ACTION_COIN_CHANGED= EventBusAction.SCHEME_ACTION+"://"+EventBusAction.LUANCH_HOST+"/coin_changed";
}

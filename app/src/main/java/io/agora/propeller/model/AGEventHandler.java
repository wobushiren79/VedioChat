package io.agora.propeller.model;

public interface AGEventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onExtraCallback(int type, Object... data);

    /**
     * 接收到对方数据流消息的回调
     */
    int EVENT_TYPE_ON_DATA_CHANNEL_MSG = 3;

    /**
     * 提示有其他用户暂停/恢复了视频流的发送。
     */
    int EVENT_TYPE_ON_USER_VIDEO_MUTED = 6;

    /**
     * 提示谁在说话及其音量。默认禁用。可以通过 enableAudioVolumeIndication 方法设置。
     */
    int EVENT_TYPE_ON_SPEAKER_STATS = 8;
    /**
     * 表示 SDK 运行时出现了（网络或媒体相关的）错误。<br></br>
     * 通常情况下，SDK 上报的错误意味着 SDK 无法自动恢复，需要 APP 干预或提示用户。<br></br>
     * 例如启动通话失败时，SDK 会上报 ERR_START_CALL 错误。<br></br>
     * APP 可以提示用户启动通话失败，并调用 leaveChannel() 退出频道。
     */
    int EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9;
    /**
     * 报告更新远端视频统计信息，该回调函数每两秒触发一次。
     * <p></p>
     * RemoteVideoStats 定义如下:
     * <br></br>
     *  名称	                    描述<br></br>
     *   stats                      远端视频相关的统计信息，包含:<br></br>
     *   uid:                       用户ID，指定是哪个用户的视频流<br></br>
     *   delay:                     延迟（毫秒）<br></br>
     *   receivedBitrate:           接收码率(kbps)<br></br>
     *   receivedFrameRate:         接收帧率(fps)<br></br>
     *   rxStreamType:              视频流类型，大流或小流<br></br>
     *
     */
    int EVENT_TYPE_ON_USER_VIDEO_STATS = 10;
    /**
     * 该回调方法表示 SDK 和服务器失去了网络连接，<br></br>
     * 并且尝试自动重连一段时间（默认 10 秒）后仍未连上。<br></br>
     * 该回调触发后，SDK 仍然会尝试重连，<br></br>
     * 重连成功后会触发 onRejoinChannelSuccess 回调。
     */
    int EVENT_TYPE_ON_APP_ERROR = 13;
    /**
     * 当调用 setEnableSpeakerphone 成功时，<br></br>
     * SDK 会通过该回调通知 App 语音路由状态已发生变化。<br></br>
     * 该回调返回当前的语音路由已切换至听筒，外放(扬声器)，耳机或蓝牙。
     */
    int EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18;
}

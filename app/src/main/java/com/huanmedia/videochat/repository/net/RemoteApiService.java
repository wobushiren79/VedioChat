package com.huanmedia.videochat.repository.net;


import com.huanmedia.videochat.common.BaseActivity;
import com.huanmedia.videochat.common.manager.ActivitManager;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.mvp.entity.request.BusinessCardInfoRequest;
import com.huanmedia.videochat.mvp.entity.results.AppointmentSettingResults;
import com.huanmedia.videochat.mvp.entity.results.AppointmentUserInfoResults;
import com.huanmedia.videochat.mvp.entity.results.BusinessCardInfoResults;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.FileUpLoadResults;
import com.huanmedia.videochat.mvp.entity.results.ShufflingAdsResults;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.mvp.entity.results.UserVideoDataResults;
import com.huanmedia.videochat.repository.entity.BillDetialEntity;
import com.huanmedia.videochat.repository.entity.BusinessCardEntity;
import com.huanmedia.videochat.repository.entity.BusinessCardUserTags;
import com.huanmedia.videochat.repository.entity.CashListEntity;
import com.huanmedia.videochat.repository.entity.ChatPeopleEntity;
import com.huanmedia.videochat.repository.entity.DiscoverEntity;
import com.huanmedia.videochat.repository.entity.DiscoverPageEntity;
import com.huanmedia.videochat.repository.entity.GiftEntity;
import com.huanmedia.videochat.repository.entity.ItemMenuEntity;
import com.huanmedia.videochat.repository.entity.MyLevelEntity;
import com.huanmedia.videochat.repository.entity.OccupationsEntity;
import com.huanmedia.videochat.repository.entity.PayCoinTypeMode;
import com.huanmedia.videochat.repository.entity.PayOlderEntity;
import com.huanmedia.videochat.repository.entity.TrustValueEntity;
import com.huanmedia.videochat.repository.entity.UserAccountBoundEntity;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.entity.UserEvaluateEntity;
import com.huanmedia.videochat.repository.entity.UserInformationEntity;
import com.huanmedia.videochat.repository.entity.VideoChatEntity;
import com.huanmedia.videochat.video.CallingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.net.DataResponse;
import mvp.data.net.converter.GsonConverterFactory;
import mvp.data.net.converter.StringConverterFactory;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by ericYang on 2017/5/18.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public interface RemoteApiService {
    //登录
    @FormUrlEncoded
    @POST("/index/user/login")
    Observable<DataResponse<UserEntity>> login(@FieldMap Map<String, String> prams);

    @POST("/index/user/sendSms")
    @FormUrlEncoded
    Observable<DataResponse> sendSms(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/index/user/logout")
    Observable<DataResponse> outLign(@FieldMap Map<String, String> prams);

    //随机列表筛选
    @POST("/index/Chatpage/starlist")
    @FormUrlEncoded
    Observable<DataResponse<DiscoverPageEntity>> readManList(@FieldMap Map<String, String> prams);

    //随机列表未登录
    @POST("/index/findtouristpage/index")
    @FormUrlEncoded
    @Deprecated
    Observable<DataResponse<List<DiscoverEntity>>> findtouristpage(@FieldMap Map<String, String> prams);

    //时间同步
    @POST("index/standard/timer")
    Observable<DataResponse> syncTime();

    //收藏
    @POST("index/userext/favorite")
    @FormUrlEncoded
    Observable<DataResponse> favorite(@Field("favouid") String id, @Field("flag") int flag);

    //搜索
    @POST("index/chatpage/beginSearch")
    @FormUrlEncoded
    Observable<DataResponse> beginSearch(@FieldMap Map<String, String> prams);

    @POST("/index/chatpage/forceOutSearch")
    Observable<DataResponse> forceOutSearch();

    //结束视频聊天
    @POST("/index/Chatpage/chatEnd")
    @FormUrlEncoded
    Observable<DataResponse> chatEnd(@FieldMap Map<String, String> prams);

    //开启视频聊天
    @POST("/index/Chatpage/chatBegininfo")
    @FormUrlEncoded
    Observable<DataResponse<VideoChatEntity>> chatBegininfo(@FieldMap Map<String, String> prams);

    //用户消费金币扣取
    @POST("/index/Chatpage/chatCoinConsumption")
    @FormUrlEncoded
    Observable<DataResponse> chatCoinConsumption(@FieldMap Map<String, String> prams);

    //用户消费金币扣取
    @POST("/index/userext/giftlist")
    Observable<DataResponse<List<ArrayList<GiftEntity>>>> giftlist();

    //获取用户充值金币列表
    @POST("/index/recharge/getrechargeinfo")
    Observable<DataResponse<List<PayCoinTypeMode>>> getRechargeInfo();

    //获取用户充值金币列表
    @POST("/index/userext/facelist")
    Observable<DataResponse<List<List<SkinMode>>>> facelist();

    //创建订单
    @POST("/index/recharge/createorder")
    @FormUrlEncoded
    Observable<DataResponse> createorder(@FieldMap Map<String, String> prams);

    //支付订单
    @POST("/index/recharge/orderpay")
    @FormUrlEncoded
    Observable<DataResponse<PayOlderEntity>> payorder(@FieldMap Map<String, String> prams);

    //和我聊过的人
    @POST("/index/userext/calleachList")
    @FormUrlEncoded
    Observable<DataResponse<ChatPeopleEntity>> calleachList(@Field("page") int page, @Field("onlineflag") int onlineflag);

    //删除聊过的人
    @POST("/index/userext/deltalkeachuser")
    @FormUrlEncoded
    Observable<DataResponse> deltalkeachuser(@Field("ouid") int ouid);

    //关注列表
    @POST("/index/userext/myfavoriteList")
    @FormUrlEncoded
    Observable<DataResponse<ChatPeopleEntity>> myfavoriteList(@Field("page") int page, @Field("onlineflag") int onlineflag);

    //关注列表
    @POST("/index/userext/funsList")
    @FormUrlEncoded
    Observable<DataResponse<ChatPeopleEntity>> funsList(@Field("page") int page);

    //红人赚钱设置更改
    @POST("/index/userext/starchange")
    @FormUrlEncoded
    Observable<DataResponse> starchange(@Field("times") int times, @Field("coins") int coins, @Field("coinstatus") int coinstatus);

    //红人设置信息获取
    @POST("/index/userext/getuserwxqqcoin")
    Observable<DataResponse<Map<String, String>>> getuserwxqqcoin();

    //通过ID获取用户信息
    @POST("/index/userext/userinfo")
    @FormUrlEncoded
    Observable<DataResponse<UserInformationEntity>> userinfo(@Field("uid") int id);

    //获取用户名片
    @POST("/index/userext/userinfoall")
    @FormUrlEncoded
    Observable<DataResponse<BusinessCardInfoResults>> userinfoall(@Field("uid") int id);

    //用户账户提现绑定信息
    @POST("/index/Userconsumption/userbindCacheInfo")
    Observable<DataResponse<UserAccountBoundEntity>> userbindCacheInfo();

    //用户账户提现绑定信息
    @POST("/index/Userconsumption/userbeforeBindData")
    @FormUrlEncoded
    Observable<DataResponse<Object>> userbeforeBindData(@Field("type") int type);//1微信 2支付宝

    //用户消费明细
    @POST("/index/userext/consumptionList")
    @FormUrlEncoded
    Observable<DataResponse<BillDetialEntity>> consumptionList(@Field("type") int type, @Field("page") int page, @Field("date") String data);

    //提现列表
    @POST("/index/userext/cashList")
    @FormUrlEncoded
    Observable<DataResponse<CashListEntity>> cashList(@Field("page") int page, @Field("date") String data);

    //用户提现
    @POST("/index/Userconsumption/userdocash")
    @FormUrlEncoded
    Observable<DataResponse> userdocash(@Field("coin") int coin);

    //M币兑换
    @POST("/index/userconsumption/usercointorecharge")
    @FormUrlEncoded
    Observable<DataResponse> usercointorecharge(@Field("coins") int coin);

    //账户绑定
    @POST("/index/Userconsumption/UserdoBindwxali")
    @FormUrlEncoded
    Observable<DataResponse> userdoBindwxali(@Field("type") int type, @Field("account") String account, @Field("realname") String realname, @Field("idcode") String idcode);// 邦定类型  1微信  2支付宝

    //解除绑定
    @POST("/index/Userconsumption/UserUnBindwxali")
    Observable<DataResponse> userUnBindwxali();

    //职业更新 城市
    @POST("/index/userext/updateaddjob")
    @FormUrlEncoded
    Observable<DataResponse> upUserBaseInfo(@FieldMap Map<String, String> prams);

    //职业更新 城市
    @POST("/index/userext/provincecity")
    Observable<DataResponse<List<ItemMenuEntity>>> citys();

    //行业信息
    @POST("/index/userext/updateinfocfg")
    Observable<DataResponse<OccupationsEntity>> userConfigData();

    //行业信息
    @POST("/index/userext/delomorephoto")
    @FormUrlEncoded
    Observable<DataResponse> delomorephoto(@Field("ids") String photoid);


    //行业信息
    @POST("/index/standard/version")
    @FormUrlEncoded
    Observable<DataResponse> version(@Field("andVerNum") int andVerNum);


    //my详细信息
    @POST("/index/userext/myinfo")
    Observable<DataResponse<UserEntity.UserinfoEntity>> myinfo();


    //启动统计
    @POST("/index/count/countboot")
    @FormUrlEncoded
    Observable<DataResponse> countboot(@Field("deviceid") String deviceid);

    //举报
    @POST("/index/userext/reportother")
    @FormUrlEncoded
    Observable<DataResponse> reportother(@Field("ouid") String ouid, @Field("type") int type);

    //系统标签列表
    @POST("/index/taglib/deftags")
    Observable<DataResponse<SystemTagsResults>> systags();

    //用户标签提交
    @POST("/index/taglib/submittags")
    @FormUrlEncoded
    Observable<DataResponse> submittags(@Field("uid") int uid, @Field("txt") String txt, @Field("tagids") String tagids);

    //用户标签统计
    @POST("/index/taglib/usertagscount")
    @FormUrlEncoded
    Observable<DataResponse<List<BusinessCardUserTags.TagEntity>>> usertagscount(@Field("uid") String uid);

    //用户评价列表
    @POST("/index/taglib/usertagslist")
    @FormUrlEncoded
    Observable<DataResponse<UserEvaluateEntity>> usertagslist(@Field("page") String page, @Field("uid") String uid);

    //修改照片墙顺序
    @POST("/index/userext/updateuserphotosorder")
    @FormUrlEncoded
    Observable<DataResponse> updateuserphotosorder(@Field("imgsorder") String imgsorder);

    //信任值页面数据
    @POST("/index/taglib/userfulltags")
    Observable<DataResponse<TrustValueEntity>> userfulltags();

    //红人认证资料完整度检查
    @POST("/index/userext/starcintegrity")
    Observable<DataResponse> starcintegrity();

    //红人微信QQ费用设置
    @POST("/index/userext/userwxqqcoin")
    @FormUrlEncoded
    Observable<DataResponse> userwxqqcoin(@FieldMap Map<String, String> prams);

    //红人微信QQ费用设置
    @POST("/index/userext/userlevelprivilege")
    Observable<DataResponse<MyLevelEntity>> userlevelprivilege();

    //查询其他红人QQ信息
    @POST("/index/userext/getotheruserwxqq")
    @FormUrlEncoded
    Observable<DataResponse<ContactUnLockInfoResults>> getotheruserwxqq(@Field("ouid") int ouid);

    //红人QQ信息 扣费
    @POST("/index/chatpage/viewotherwxqqcoin")
    @FormUrlEncoded
    Observable<DataResponse> viewotherwxqqcoin(@Field("ouid") int ouid);

    //更新用户信息
    @POST("/index/userextv2/updatelatlng")
    @FormUrlEncoded
    Observable<DataResponse<Object>> uploaduserdata(@FieldMap Map<String, Object> params);

    //用户聊天列表
    @POST("/index/userextv2/talkroomlist")
    @FormUrlEncoded
    Observable<DataResponse<TalkRoomListResults>> talkroomlist(@FieldMap Map<String, Object> params);

    //阿里云文件上传信息
    @POST("/index/userextv2/ossinfo")
    @FormUrlEncoded
    Observable<DataResponse<FileUpLoadResults>> ossinfo(@FieldMap Map<String, Object> params);

    //上传用户视频
    @POST("/index/userextv2/ossvoidupload")
    @FormUrlEncoded
    Observable<DataResponse<UserVideoDataResults>> ossvoidupload(@FieldMap Map<String, Object> params);

    //删除用户视频
    @POST("/index/userextv2/ossdel")
    @FormUrlEncoded
    Observable<DataResponse<Object>> ossdel(@FieldMap Map<String, Object> params);

    //广告数据
    @POST("/index/userextv2/ads")
    @FormUrlEncoded
    Observable<DataResponse<List<ShufflingAdsResults>>> ads(@FieldMap Map<String, Object> params);

    //预约红人数据
    @POST("/index/userextv2/appointmentPageData")
    @FormUrlEncoded
    Observable<DataResponse<AppointmentUserInfoResults>> appointmentPageData(@FieldMap Map<String, Object> params);


    //预约红人
    @POST("/index/userextv2/guestyuye")
    @FormUrlEncoded
    Observable<DataResponse<Object>> appointmentSubmit(@FieldMap Map<String, Object> params);


    //红人预约设置信息
    @POST("/index/userextv2/appointvipseeself")
    Observable<DataResponse<AppointmentSettingResults>> appointmentSettingInfo();


    @Multipart
    @POST
    Observable<DataResponse> uploadFileWithPartMap(
            @Url() String url,
            @QueryMap Map<String, String> parms,
            @PartMap() Map<String, RequestBody> partMap
    );

    @Multipart
    @POST
    Observable<DataResponse<UserVideoDataResults>> uploadViedoData(
            @Url() String url,
            @QueryMap Map<String, String> parms,
            @PartMap() Map<String, RequestBody> partMap
    );

    /**
     * 创建一个API服务
     */
    class Factory {
        private Factory() {
        }

        public static RemoteApiService createService() {
//            builder.addInterceptor(new HeaderInterceptor());
            GsonConverterFactory converFactory = GsonConverterFactory.create();
            converFactory.setOutLoginListener(api -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", "异常登录");
                map.put("msg", api.getMessage());
                UserManager.getInstance().outLogin(map);
                if (ActivitManager.getAppManager().currentActivity() instanceof CallingActivity) {
                    ActivitManager.getAppManager().currentActivity().finish();
                } else {
                    ((BaseActivity) ActivitManager.getAppManager().currentActivity()).showFouceExitDialog();
                }
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HostManager.getServiceUrl())
                    .client(OkhttpManager.getInstance().getClient(OkhttpManager.HTTP))
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(converFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(RemoteApiService.class);
        }
    }
}

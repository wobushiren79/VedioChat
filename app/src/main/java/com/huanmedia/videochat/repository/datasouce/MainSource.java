package com.huanmedia.videochat.repository.datasouce;

import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.mvp.entity.results.AttentionResults;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.net.DataResponse;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * Created by ericYang on 2017/8/24.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public interface MainSource extends DataSource {
    /**
     * 用户登录
     *
     * @return
     */
    Observable<UserEntity> login(Map<String, String> prams);

    /**
     * 退出登录
     *
     * @return
     */
    Observable<DataResponse> outLogin(Map<String, String> prams);

    /**
     * 验证码获取
     *
     * @param mobile
     * @return
     */
    Observable<DataResponse> sendSms(@Query("mobile") String mobile);

    /**
     * 时间同步
     *
     * @return
     */
    Observable<DataResponse> syncTime();

    /**
     * 发现列表分页
     *
     * @param prams
     * @return
     */
    Observable<DiscoverPageEntity> readMainList(Map<String, String> prams);

    /**
     * 随机匹配列表（未登录）
     *
     * @param prams
     * @return
     */
    Observable<List<DiscoverEntity>> findtouristpage(Map<String, String> prams);

    /**
     * 收藏
     *
     * @param id
     * @param flag
     * @return
     */
    Observable<DataResponse<Object>> favorite(String id, int flag);//收藏

    /**
     * 搜索匹配
     *
     * @param prams
     * @return
     */
    Observable<DataResponse> beginSearch(Map<String, String> prams);

    Observable<DataResponse> forceOutSearch();

    /**
     * 多图上传
     *
     * @param url     路径
     * @param parms   参数
     * @param fileMap 图片数据
     */
    Observable<DataResponse> uploadFileWithPartMap(
            String url,
            Map<String, String> parms,
            Map<String, RequestBody> fileMap);

    /**
     * 开始会话
     *
     * @param prams
     * @return
     */
    Observable<VideoChatEntity> chatBegininfo(Map<String, String> prams);

    /**
     * 结束会话
     *
     * @param prams
     * @return
     */
    Observable<DataResponse> chatEnd(Map<String, String> prams);

    /**
     * 用户消费金币扣去
     * <p>
     * 参数名称<p>
     * 类型<p>
     * 是否必须<p>
     * 说明<p>
     * callid	 int	 Y	 会话ID<p>
     * type	int	Y<p>
     * 用户消费类型：<p>
     * 1 赠送礼物<p>
     * 2 增加发现聊天时间<p>
     * 3 聊天充值<p>
     * 4 红人陪聊消费<p>
     * 5 随机用主 查询消费<p>
     * 6 揭面扣金币<p>
     * typevalue	int	Y/n<p>
     * type==1 礼物的ID<p>
     * type==2 增加聊天时间分钟 [ 1分钟/10金币 3分钟/25金币 10分钟/90金币]<p>
     * 响应格式--JSON<p>
     *
     * @param prams
     * @return
     */
    Observable<DataResponse> chatCoinConsumption(Map<String, String> prams);

    /**
     * 获取礼物列表
     *
     * @return
     */
    Observable<List<ArrayList<GiftEntity>>> giftlist();

    /**
     * 获取礼物列表
     *
     * @return
     */
    Observable<List<PayCoinTypeMode>> getRechargeInfo();

    /**
     * 获取礼物列表
     *
     * @return
     */
    Observable<DataResponse> createorder(Map<String, String> prams);

    /**
     * 获取礼物列表
     *
     * @return
     */
    Observable<PayOlderEntity> payorder(Map<String, String> prams);

    /**
     * 和我聊过的人
     *
     * @param page
     * @param onlineflag
     * @return
     */
    Observable<ChatPeopleEntity> calleachList(int page, int onlineflag);

    /**
     * 删除聊过的人
     *
     * @param ouid
     * @return
     */
    Observable<DataResponse> deltalkeachuser(int ouid);

    /**
     * 粉丝列表
     *
     * @param page
     * @return
     */
    Observable<ChatPeopleEntity> funsList(int page);

    /**
     * 我关注的人
     *
     * @param page
     * @param onlineflag
     * @return
     */
    Observable<ChatPeopleEntity> myfavoriteList(int page, int onlineflag);

    /**
     * 洪洞人配置
     *
     * @param times      时间
     * @param coins      金币
     * @param coinstatus 开关0关闭 1打开
     * @return
     */
    Observable<DataResponse> starchange(int times, int coins, int coinstatus);

    /**
     * 获取用户名片资料
     *
     * @param uid
     * @return BusinessCardEntity
     */
    Observable<UserInformationEntity> getUserInfo(int uid);

    /**
     * 获取用户名片资料(个人名片)
     *
     * @param uid
     * @return BusinessCardEntity
     */
    Observable<BusinessCardEntity> getUserBusinessCard(int uid, int plevel);

    /**
     * 用户账户绑定信息
     *
     * @return UserAccountBoundEntity
     */
    Observable<UserAccountBoundEntity> userAccountBoundInfo();

    /**
     * 消费细明（账单）
     *
     * @param page 页数
     * @param data 要查询的日期 格式： 2017-02-05不写就传0或空
     * @param type 账单类型 1 消费明细 和 充值2 收入明细 和 提现
     * @return DataResponse<BillDetialEntity>
     */
    Observable<BillDetialEntity> getBill(int type, int page, String data);

    /**
     * 用于提现
     *
     * @param coin 提现数据金币数 10的倍数 2000起( 对应现金为除以10就等于人民币数，2000起提)
     * @return DataResponse
     */
    Observable<DataResponse> userdocash(int coin);

    /**
     * M币兑换
     *
     * @param coin 以转出的数量  100的整数倍
     * @return DataResponse
     */
    Observable<DataResponse> usercointorecharge(int coin);

    /**
     * 账户绑定
     *
     * @param type    邦定类型  1微信  2支付宝
     * @param account 邦定账号
     * @return DataResponse
     */
    Observable<DataResponse> boundUser(int type, String account, String realname, String idcode);

    /**
     * 绑定账户需要的信息
     *
     * @param type 1微信 2支付宝
     * @return
     */
    Observable<DataResponse<Object>> userbeforeBindData(int type);

    /**
     * 账户绑定
     *
     * @return DataResponse
     */
    Observable<DataResponse> unboundUser();

    /**
     * 提现列表
     *
     * @param page 页数
     * @param data 要查询的日期 格式： 2017-02-05不写就传0或空
     * @return DataResponse
     */
    Observable<CashListEntity> cashList(int page, String data);

    /**
     * 更新用户基础信息
     *
     * @param prams province   省份
     *              city       城市
     *              occupation 行业
     * @return DataResponse
     */
    Observable<DataResponse> upUserBaseInfo(Map<String, String> prams);

    /**
     * 获取城市信息
     *
     * @return
     */
    Observable<List<ItemMenuEntity>> citys();

    /**
     * 行业信息
     *
     * @return
     */
    Observable<OccupationsEntity> userConfigData();

    /**
     * 批量删除用户照片墙图片
     *
     * @return
     */
    Observable<DataResponse> delomorephoto(@Field("ids") String photoid);

    /**
     * 检查版本
     *
     * @param version
     * @return
     */
    Observable<DataResponse> version(int version);

    /**
     * 检查版本
     *
     * @return
     */
    Observable<UserEntity.UserinfoEntity> myinfo();

    /**
     * 检查版本
     *
     * @param deviceid
     * @return
     */
    Observable<DataResponse> countboot(String deviceid);

    /**
     * 举报用户
     *
     * @param ouid 用户ID
     * @param type {@link com.huanmedia.videochat.common.widget.dialog.ReportDialog}
     * @return
     */
    Observable<DataResponse> reportother(String ouid, int type);

    /**
     * 系统标签
     *
     * @return
     */
    Observable<SystemTagsResults> systag();

    /**
     * 用户标签统计
     *
     * @param uid
     * @return
     */
    Observable<DataResponse> submittag(@Field("uid") int uid, @Field("txt") String txt, @Field("tagids") String tagids);

    /**
     * 用户标签统计
     *
     * @param uid
     * @return
     */
    Observable<List<BusinessCardUserTags.TagEntity>> usertagscount(@Field("uid") String uid);

    /**
     * 用户评价列表
     *
     * @param uid
     * @return
     */
    Observable<UserEvaluateEntity> usertagslist(@Field("page") String page, @Field("uid") String uid);

    /**
     * 照片墙顺序修改
     *
     * @param imgsorder 图片id及排序字符串，如 12-3,24-3,23-1 12代表图片id   3为顺序 ，用-分隔多张图片间用逗号分割
     *                  按升序排列 顺序小的排前
     * @return
     */
    Observable<DataResponse> updateuserphotosorder(String imgsorder);

    /**
     * 信任值页面
     *
     * @return
     */
    Observable<TrustValueEntity> userfulltags();

    /**
     * 红人认证资料完整度判断
     *
     * @return
     */
    Observable<DataResponse> starcintegrity();

    /**
     * 红人微信QQ账户价格设置
     *
     * @return
     */
    Observable<DataResponse> userwxqqcoin(Map<String, String> prams);

    /**
     * 红人设置信息获取
     *
     * @return
     */
    Observable<Map<String, String>> getuserwxqqcoin();

    /**
     * 红人设置信息获取
     *
     * @return
     */
    Observable<MyLevelEntity> userlevelprivilege();


    /**
     * 查询其他红人QQ信息
     *
     * @return
     */
    Observable<ContactUnLockInfoResults> getotheruserwxqq(@Field("ouid") int ouid);

    /**
     * 红人QQ信息 扣费
     *
     * @return
     */
    Observable<DataResponse> viewotherwxqqcoin(@Field("ouid") int ouid);

    /**
     * 获取道具列表
     *
     * @return
     */
    Observable<List<List<SkinMode>>> facelist();


}

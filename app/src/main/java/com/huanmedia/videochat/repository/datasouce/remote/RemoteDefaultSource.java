package com.huanmedia.videochat.repository.datasouce.remote;

import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.repository.datasouce.MainSource;
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
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.net.ApiException;
import mvp.data.net.DataResponse;
import okhttp3.RequestBody;

/**
 * Created by ericYang on 2017/8/24.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class RemoteDefaultSource implements MainSource {
    private final RemoteApiService mRemoteApiService;

    public RemoteDefaultSource(RemoteApiService remoteApiService) {
        mRemoteApiService = remoteApiService;
    }

    @Override
    public Observable<UserEntity> login(Map<String, String> prams) {
        return mRemoteApiService.login(prams).map(listDataResponse -> {
            if (listDataResponse == null || listDataResponse.getResult() == null) {
                return new UserEntity();
            } else {
                return listDataResponse.getResult();
            }
        });
    }

    @Override
    public Observable<DataResponse> outLogin(Map<String, String> prams) {
        return mRemoteApiService.outLign(prams);
    }

    @Override
    public Observable<DataResponse> sendSms(String mobile) {
        return mRemoteApiService.sendSms(mobile);
    }

    @Override
    public Observable<DataResponse> syncTime() {
        return mRemoteApiService.syncTime();
    }

    @Override
    public Observable<DiscoverPageEntity> readMainList(Map<String, String> prams) {
        return mRemoteApiService.readManList(prams).map(listDataResponse -> {
            if (listDataResponse.getResult() == null) {
                throw new ApiException(ApiException.NULL, "没有获取到数据");
            } else {
                return listDataResponse.getResult();
            }
        });
    }

    @Override
    @Deprecated
    public Observable<List<DiscoverEntity>> findtouristpage(Map<String, String> prams) {
        return mRemoteApiService.findtouristpage(prams).map(listDataResponse ->
                listDataResponse.getResult() == null ? new ArrayList<>() : listDataResponse.getResult());
    }

    @Override
    public Observable<DataResponse> favorite(String id, int flag) {
        return mRemoteApiService.favorite(id, flag);
    }

    @Override
    public Observable<DataResponse> beginSearch(Map<String, String> prams) {
        return mRemoteApiService.beginSearch(prams);
    }

    @Override
    public Observable<DataResponse> forceOutSearch() {
        return mRemoteApiService.forceOutSearch();
    }

    @Override
    public Observable<DataResponse> uploadFileWithPartMap(String url, Map<String, String> parms, Map<String, RequestBody> filemap) {
        return mRemoteApiService.uploadFileWithPartMap(url, parms, filemap);
    }

    @Override
    public Observable<VideoChatEntity> chatBegininfo(Map<String, String> prams) {
        return mRemoteApiService.chatBegininfo(prams).map(DataResponse::getResult);
    }

    @Override
    public Observable<DataResponse> chatEnd(Map<String, String> prams) {
        return mRemoteApiService.chatEnd(prams);
    }

    @Override
    public Observable<DataResponse> chatCoinConsumption(Map<String, String> prams) {
        return mRemoteApiService.chatCoinConsumption(prams);
    }

    @Override
    public Observable<List<ArrayList<GiftEntity>>> giftlist() {
        return mRemoteApiService.giftlist().map(listDataResponse ->
                listDataResponse.getResult() == null ? new ArrayList<>() : listDataResponse.getResult());
    }

    @Override
    public Observable<List<PayCoinTypeMode>> getRechargeInfo() {
        return mRemoteApiService.getRechargeInfo().map(DataResponse::getResult);
    }

    @Override
    public Observable<DataResponse> createorder(Map<String, String> prams) {
        return mRemoteApiService.createorder(prams);
    }

    @Override
    public Observable<PayOlderEntity> payorder(Map<String, String> prams) {
        return mRemoteApiService.payorder(prams).map(DataResponse::getResult);
    }

    @Override
    public Observable<ChatPeopleEntity> calleachList(int page, int onlineflag) {
        return mRemoteApiService.calleachList(page, onlineflag).map(chatPeopleEntityDataResponse -> {
                    if (chatPeopleEntityDataResponse.getResult() == null) {
                        throw new ApiException(ApiException.NULL, "没有获取到数据");
                    } else {
                        return chatPeopleEntityDataResponse.getResult();
                    }
                }
        );
    }

    @Override
    public Observable<DataResponse> deltalkeachuser(int ouid) {
        return mRemoteApiService.deltalkeachuser(ouid);
    }

    @Override
    public Observable<ChatPeopleEntity> funsList(int page) {
        return mRemoteApiService.funsList(page).map(chatPeopleEntityDataResponse -> {
            if (chatPeopleEntityDataResponse.getResult() == null)
                throw new ApiException(ApiException.NULL, "没有获取到数据");
            return chatPeopleEntityDataResponse.getResult();
        });
    }

    @Override
    public Observable<ChatPeopleEntity> myfavoriteList(int page, int onlineflag) {
        return mRemoteApiService.myfavoriteList(page, onlineflag).map(chatPeopleEntityDataResponse -> {
            if (chatPeopleEntityDataResponse.getResult() == null)
                throw new ApiException(ApiException.NULL, "没有获取到数据");
            return chatPeopleEntityDataResponse.getResult();
        });
    }

    @Override
    public Observable<DataResponse> starchange(int times, int coins, int coinstatus) {
        return mRemoteApiService.starchange(times, coins, coinstatus);
    }

    @Override
    public Observable<UserInformationEntity> getUserInfo(int uid) {
        return mRemoteApiService.userinfo(uid).map(businessCard -> businessCard.getResult() == null ? new UserInformationEntity() : businessCard.getResult());
    }

    @Override
    public Observable<BusinessCardEntity> getUserBusinessCard(int uid) {
        return mRemoteApiService.userinfoall(uid).map(businessCard -> businessCard.getResult() == null ? new BusinessCardEntity() : businessCard.getResult());
    }

    @Override
    public Observable<UserAccountBoundEntity> userAccountBoundInfo() {
        return mRemoteApiService.userbindCacheInfo().map(resp -> resp.getResult() == null ? new UserAccountBoundEntity() : resp.getResult());
    }

    @Override
    public Observable<BillDetialEntity> getBill(int type, int page, String data) {
        return mRemoteApiService.consumptionList(type, page, data).map(resp -> resp.getResult() == null ? new BillDetialEntity() : resp.getResult());
    }

    @Override
    public Observable<DataResponse> userdocash(int coin) {
        return mRemoteApiService.userdocash(coin);
    }

    @Override
    public Observable<DataResponse> usercointorecharge(int coin) {
        return mRemoteApiService.usercointorecharge(coin);
    }

    @Override
    public Observable<DataResponse> boundUser(int type, String account, String realname, String idcode) {
        return mRemoteApiService.userdoBindwxali(type, account, realname, idcode);
    }

    @Override
    public Observable<DataResponse<Object>> userbeforeBindData(int type) {
        return mRemoteApiService.userbeforeBindData(type);
    }

    @Override
    public Observable<DataResponse> unboundUser() {
        return mRemoteApiService.userUnBindwxali();
    }

    @Override
    public Observable<CashListEntity> cashList(int page, String data) {
        return mRemoteApiService.cashList(page, data).map(resp -> resp.getResult() == null ? new CashListEntity() : resp.getResult());
    }

    @Override
    public Observable<DataResponse> upUserBaseInfo(Map<String, String> prams) {
        return mRemoteApiService.upUserBaseInfo(prams);
    }

    @Override
    public Observable<List<ItemMenuEntity>> citys() {
        return mRemoteApiService.citys().map(citysEntityDataResponse ->
                citysEntityDataResponse.getResult() == null
                        ? new ArrayList<>() : citysEntityDataResponse.getResult());
    }

    @Override
    public Observable<OccupationsEntity> userConfigData() {
        return mRemoteApiService.userConfigData().map(citysEntityDataResponse ->
                citysEntityDataResponse.getResult() == null
                        ? new OccupationsEntity() : citysEntityDataResponse.getResult());
    }

    @Override
    public Observable<DataResponse> delomorephoto(String photoid) {
        return mRemoteApiService.delomorephoto(photoid);
    }

    @Override
    public Observable<DataResponse> version(int version) {
        return mRemoteApiService.version(version);
    }

    @Override
    public Observable<UserEntity.UserinfoEntity> myinfo() {
        return mRemoteApiService.myinfo().map(userinfoEntityDataResponse ->
                userinfoEntityDataResponse.getResult() == null
                        ? new UserEntity.UserinfoEntity() : userinfoEntityDataResponse.getResult());
    }

    @Override
    public Observable<DataResponse> countboot(String deviceid) {
        return mRemoteApiService.countboot(deviceid);
    }

    @Override
    public Observable<DataResponse> reportother(String ouid, int type) {
        return mRemoteApiService.reportother(ouid, type);
    }

    @Override
    public Observable<SystemTagsResults> systag() {
        return mRemoteApiService.systags()
                .map(response -> {
                    if (response.getResult() == null)
                        throw new ApiException(ApiException.NULL, "没有获取到数据");
                    return response.getResult();
                });
    }

    @Override
    public Observable<DataResponse> submittag(int uid, String txt, String tagids) {
        return mRemoteApiService.submittags(uid, txt, tagids);
    }

    @Override
    public Observable<List<BusinessCardUserTags.TagEntity>> usertagscount(String uid) {
        return mRemoteApiService.usertagscount(uid).map(response -> response.getResult() == null ? new ArrayList<>() : response.getResult());
    }

    @Override
    public Observable<UserEvaluateEntity> usertagslist(String page, String uid) {
        return mRemoteApiService.usertagslist(page, uid)
                .map(response -> {
                    if (response.getResult() == null)
                        throw new ApiException(ApiException.NULL, "没有获取到数据");
                    return response.getResult();
                });
    }

    @Override
    public Observable<DataResponse> updateuserphotosorder(String imgsorder) {
        return mRemoteApiService.updateuserphotosorder(imgsorder);
    }

    @Override
    public Observable<TrustValueEntity> userfulltags() {
        return mRemoteApiService.userfulltags()
                .map(response -> {
                    if (response.getResult() == null)
                        throw new ApiException(ApiException.NULL, "没有获取到数据");
                    return response.getResult();
                });
    }

    @Override
    public Observable<DataResponse> starcintegrity() {
        return mRemoteApiService.starcintegrity();
    }

    @Override
    public Observable<DataResponse> userwxqqcoin(Map<String, String> prams) {
        return mRemoteApiService.userwxqqcoin(prams);
    }

    @Override
    public Observable<Map<String, String>> getuserwxqqcoin() {
        return mRemoteApiService.getuserwxqqcoin().map(response -> {
            if (response.getResult() == null)
                throw new ApiException(ApiException.NULL, "没有获取到数据");
            return response.getResult();
        });
    }

    @Override
    public Observable<MyLevelEntity> userlevelprivilege() {
        return mRemoteApiService.userlevelprivilege().map(response -> {
            if (response.getResult() == null)
                throw new ApiException(ApiException.NULL, "没有获取到数据");
            return response.getResult();
        });
    }

    @Override
    public Observable<ContactUnLockInfoResults> getotheruserwxqq(int ouid) {
        return mRemoteApiService.getotheruserwxqq(ouid)
                .map(response -> {
                    if (response.getResult() == null)
                        throw new ApiException(ApiException.NULL, "没有获取到数据");
                    return response.getResult();
                });
    }

    @Override
    public Observable<DataResponse> viewotherwxqqcoin(int ouid) {
        return mRemoteApiService.viewotherwxqqcoin(ouid);
    }

    @Override
    public Observable<List<List<SkinMode>>> facelist() {
        return mRemoteApiService.facelist().map(response -> {
            if (response.getResult() == null)
                return new ArrayList<>();
            else {
                return response.getResult();
            }
        });
    }

}

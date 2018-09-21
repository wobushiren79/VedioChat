package com.huanmedia.videochat.repository.datasouce.impl;

import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.main2.datamodel.SkinMode;
import com.huanmedia.videochat.mvp.entity.results.AttentionResults;
import com.huanmedia.videochat.mvp.entity.results.ContactUnLockInfoResults;
import com.huanmedia.videochat.mvp.entity.results.SystemTagsResults;
import com.huanmedia.videochat.mvp.entity.results.TalkRoomListResults;
import com.huanmedia.videochat.repository.datasouce.DataSource;
import com.huanmedia.videochat.repository.datasouce.MainSource;
import com.huanmedia.videochat.repository.datasouce.remote.RemoteDefaultSource;
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
import com.huanmedia.videochat.repository.net.CacheProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import mvp.data.dispose.interactor.DefaultThreadProvider;
import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.data.net.DataResponse;
import okhttp3.RequestBody;


/**
 * Created by ericYang on 2017/8/24.
 * Email:eric.yang@huanmedia.com
 * 默认资源库没有数据库缓存
 */

public class MainRepostiory implements DataSource, MainSource {
    private final DefaultThreadProvider mThreadProvider;
    private final CacheProviders mCacheProviders;
    private RemoteDefaultSource mRemoteDataSource;

    /**
     * create user repository
     */
    public MainRepostiory() {
        this.mThreadProvider = ResourceManager.getInstance().getDefaultThreadProvider();
        this.mRemoteDataSource = new RemoteDefaultSource(ResourceManager.getInstance().getDefaultApiService());
        this.mCacheProviders = ResourceManager.getInstance().getCacheProviders();
    }


    @Override
    public Observable<UserEntity> login(Map<String, String> prams) {
        return mRemoteDataSource.login(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> outLogin(Map<String, String> prams) {
        return mRemoteDataSource.outLogin(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> syncTime() {
        return mRemoteDataSource.syncTime().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> sendSms(String mobile) {
        return mRemoteDataSource.sendSms(mobile).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DiscoverPageEntity> readMainList(Map<String, String> prams) {
        return mRemoteDataSource.readMainList(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    @Deprecated
    public Observable<List<DiscoverEntity>> findtouristpage(Map<String, String> prams) {
        return mRemoteDataSource.findtouristpage(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse<Object>> favorite(String id, int flag) {
        return mRemoteDataSource.favorite(id, flag).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> beginSearch(Map<String, String> prams) {
        return mRemoteDataSource.beginSearch(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> forceOutSearch() {
        return mRemoteDataSource.forceOutSearch().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> uploadFileWithPartMap(String url, Map<String, String> parms, Map<String, RequestBody> fileMap) {
        return mRemoteDataSource.uploadFileWithPartMap(url, parms, fileMap)
                .compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<VideoChatEntity> chatBegininfo(Map<String, String> prams) {
        return mRemoteDataSource.chatBegininfo(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> chatEnd(Map<String, String> prams) {
        return mRemoteDataSource.chatEnd(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> chatCoinConsumption(Map<String, String> prams) {
        return mRemoteDataSource.chatCoinConsumption(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<List<ArrayList<GiftEntity>>> giftlist() {
        return mRemoteDataSource.giftlist().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<List<PayCoinTypeMode>> getRechargeInfo() {
        return mRemoteDataSource.getRechargeInfo().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> createorder(Map<String, String> prams) {
        return mRemoteDataSource.createorder(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<PayOlderEntity> payorder(Map<String, String> prams) {
        return mRemoteDataSource.payorder(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<ChatPeopleEntity> calleachList(int page, int onlineflag) {
        return mRemoteDataSource.calleachList(page, onlineflag).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> deltalkeachuser(int ouid) {
        return mRemoteDataSource.deltalkeachuser(ouid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<ChatPeopleEntity> funsList(int page) {
        return mRemoteDataSource.funsList(page).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<ChatPeopleEntity> myfavoriteList(int page, int onlineflag) {
        return mRemoteDataSource.myfavoriteList(page, onlineflag).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> starchange(int times, int coins, int coinstatus) {
        return mRemoteDataSource.starchange(times, coins, coinstatus).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<UserInformationEntity> getUserInfo(int uid) {
        return mRemoteDataSource.getUserInfo(uid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<BusinessCardEntity> getUserBusinessCard(int uid,int plevel) {
        return mRemoteDataSource.getUserBusinessCard(uid,plevel).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<UserAccountBoundEntity> userAccountBoundInfo() {
        return mRemoteDataSource.userAccountBoundInfo().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<BillDetialEntity> getBill(int type, int page, String data) {
        return mRemoteDataSource.getBill(type, page, data).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> userdocash(int coin) {
        return mRemoteDataSource.userdocash(coin).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> usercointorecharge(int coin) {
        return mRemoteDataSource.usercointorecharge(coin).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> boundUser(int type, String account, String realname, String idcode) {
        return mRemoteDataSource.boundUser(type, account, realname, idcode).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse<Object>> userbeforeBindData(int type) {
        return mRemoteDataSource.userbeforeBindData(type).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> unboundUser() {
        return mRemoteDataSource.unboundUser().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<CashListEntity> cashList(int page, String data) {
        return mRemoteDataSource.cashList(page, data).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> upUserBaseInfo(Map<String, String> map) {
        return mRemoteDataSource.upUserBaseInfo(map).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<List<ItemMenuEntity>> citys() {//带缓存
        return mCacheProviders.getCitys(mRemoteDataSource.citys().compose(ThreadExecutorHandler.toMain(mThreadProvider)), new DynamicKey("citys"));
    }

    @Override
    public Observable<OccupationsEntity> userConfigData() {
        return mRemoteDataSource.userConfigData().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> delomorephoto(String photoid) {
        return mRemoteDataSource.delomorephoto(photoid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> version(int version) {
        return mRemoteDataSource.version(version).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<UserEntity.UserinfoEntity> myinfo() {
        return mRemoteDataSource.myinfo().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> countboot(String deviceid) {
        return mRemoteDataSource.countboot(deviceid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> reportother(String ouid, int type) {
        return mRemoteDataSource.reportother(ouid, type).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<SystemTagsResults> systag() {
        return mRemoteDataSource.systag().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> submittag(int uid, String txt, String tagids) {
        return mRemoteDataSource.submittag(uid, txt, tagids).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<List<BusinessCardUserTags.TagEntity>> usertagscount(String uid) {
        return mRemoteDataSource.usertagscount(uid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<UserEvaluateEntity> usertagslist(String page, String uid) {
        return mRemoteDataSource.usertagslist(page, uid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> updateuserphotosorder(String imgsorder) {
        return mRemoteDataSource.updateuserphotosorder(imgsorder).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<TrustValueEntity> userfulltags() {
        return mRemoteDataSource.userfulltags().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> starcintegrity() {
        return mRemoteDataSource.starcintegrity().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> userwxqqcoin(Map<String, String> prams) {
        return mRemoteDataSource.userwxqqcoin(prams).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<Map<String, String>> getuserwxqqcoin() {
        return mRemoteDataSource.getuserwxqqcoin().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<ContactUnLockInfoResults> getotheruserwxqq(int ouid) {
        return mRemoteDataSource.getotheruserwxqq(ouid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<DataResponse> viewotherwxqqcoin(int ouid) {
        return mRemoteDataSource.viewotherwxqqcoin(ouid).compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<MyLevelEntity> userlevelprivilege() {
        return mRemoteDataSource.userlevelprivilege().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

    @Override
    public Observable<List<List<SkinMode>>> facelist() {
        return mRemoteDataSource.facelist().compose(ThreadExecutorHandler.toMain(mThreadProvider));
    }

}

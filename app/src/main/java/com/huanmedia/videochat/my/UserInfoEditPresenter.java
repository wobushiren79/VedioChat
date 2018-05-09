package com.huanmedia.videochat.my;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.huanmedia.videochat.common.event.EventBusAction;
import com.huanmedia.videochat.common.manager.UserManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.ItemMenuEntity;
import com.huanmedia.videochat.repository.entity.OccupationsEntity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import mvp.presenter.Presenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Create by Administrator
 * time: 2017/12/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class UserInfoEditPresenter extends Presenter<UserInfoEditView> {
    private final MainRepostiory mMainRepostiry;
    private List<ItemMenuEntity> mCitys;
    private Disposable mCitysReq;//请求城市信息
    private OccupationsEntity mUserConfigData;//行业信息
    private Disposable mOccReq;//请求行业信息
    private int mDefaultOccp;
    private int[] mDefaultAddr = new int[2];
    private boolean mCanNavToPhotos;

    public UserInfoEditPresenter() {
        mMainRepostiry = new MainRepostiory();
    }

    @Override
    public void setView(@NonNull UserInfoEditView view) {
        super.setView(view);
        getCitys(false);
        getOccData(false);
    }

    public void checkCompleteness() {
        getView().showLoading(null);
        addDisposable(mMainRepostiry.starcintegrity().subscribe(
                response -> {
                    getView().hideLoading();
                    getView().showNext();
                }, throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));
    }

    public void getUserInfo() {

        addDisposable(mMainRepostiry.getUserInfo((int) UserManager.getInstance().getId()).subscribe(
                businessCard -> {
                    mCanNavToPhotos = true;
                    getView().showPhotos(businessCard.getPhpots());
                    getView().showVideos(businessCard.getVoides());
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));

    }

    public void upImage(String url, String fileName) {

        HashMap<String, RequestBody> fileMap = new HashMap<>();
        File file = new File(fileName);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        fileMap.put("img\"; filename=\"" + file.getName() + ".png", fileBody);
        getView().showLoading(null);
        addDisposable(mMainRepostiry.uploadFileWithPartMap(url, new HashMap<>(), fileMap).subscribe(
                response -> {
                    getView().hideLoading();
                    Map<String, String> map = (Map<String, String>) response.getResult();
                    String userPhoto = map.get("userphoto");
                    String userphoto_thumb = map.get("userphoto_thumb");
                    UserManager.getInstance().getCurrentUser().getUserinfo().setUserphoto(userPhoto);
                    UserManager.getInstance().getCurrentUser().getUserinfo().setUserphoto_thumb(userphoto_thumb);
                    UserManager.getInstance().saveUser();
                    EventBus.getDefault().post(new Intent(EventBusAction.ACTION_USERDATA_CHANGE));
                    getView().showError(0, "头像更新成功");
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }
        ));

    }

    public void upBaseInfo(int province, int city, int occupation) {
        Map<String, String> map = new HashMap<>();
        if (province != 0)
            map.put("province", province + "");
        if (city != 0)
            map.put("city", city + "");
        if (occupation != 0)
            map.put("occupation", occupation + "");
        if (map.size() == 0) return;
        getView().showLoading(null);
        addDisposable(mMainRepostiry.upUserBaseInfo(map).subscribe(response -> {
                    String str;
                    if (map.size() >= 2) {
                        str = "城市信息更新成功";
                        setCityUpData(getDefaultAddr()[0], getDefaultAddr()[1]);
                    } else {
                        setOccpUpData(getDefaultOccp());
                        str = "职业信息更新成功";
                    }
                    getView().showHint(HintDialog.HintType.WARN, str);
                    getView().hideLoading();
                },
                throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                }));
    }

    /**
     * 城市数据更新后更新本地数据
     *
     * @param provinceP
     * @param cityP
     */
    private void setCityUpData(int provinceP, int cityP) {
        ItemMenuEntity province = UserManager.getInstance()
                .getCurrentUser().getUserinfo()
                .getProvince();
        ItemMenuEntity.SubEntity city = UserManager.getInstance()
                .getCurrentUser().getUserinfo()
                .getCity();
        if (city == null) {
            city = new ItemMenuEntity.SubEntity();
            UserManager.getInstance()
                    .getCurrentUser().getUserinfo()
                    .setCity(city);
        }
        if (province == null) {
            province = new ItemMenuEntity();
            UserManager.getInstance()
                    .getCurrentUser().getUserinfo()
                    .setProvince(province);
        }
        province.setId(getCitys().get(provinceP).getId());
        province.setName(getCitys().get(provinceP).getName());

        city.setId(getCitys()
                .get(provinceP)
                .getSub().get(cityP)
                .getId());
        city.setName(getCitys()
                .get(provinceP)
                .getSub().get(cityP)
                .getName());
        UserManager.getInstance().saveUser();
    }

    /**
     * 职业信息更新后更新本地数据
     *
     * @param occpUpData
     */
    public void setOccpUpData(int occpUpData) {
        ItemMenuEntity occp = UserManager.getInstance()
                .getCurrentUser().getUserinfo()
                .getOccupation();
        if (occp == null) {
            occp = new ItemMenuEntity();
            UserManager.getInstance()
                    .getCurrentUser().getUserinfo()
                    .setOccupation(occp);
        }
        occp.setName(getUserConfigData().getOccupation().get(occpUpData).getName());
        occp.setId(getUserConfigData().getOccupation().get(occpUpData).getId());
        UserManager.getInstance().saveUser();
    }


    /**
     * 获取城市信息 如果不存在则从网络获取并设置到对应的对话框中
     *
     * @return
     */
    public List<ItemMenuEntity> getCitys() {
        if (mCitys == null) {
            getView().showLoading(null);
            getCitys(true);
        }
        return mCitys;
    }

    /**
     * 获取城市数据
     *
     * @param isNeedSet
     */
    public void getCitys(boolean isNeedSet) {
        if (mCitysReq != null && !mCitysReq.isDisposed()) {
            mCitysReq.dispose();
            remove(mCitysReq);
        }
        mCitysReq = mMainRepostiry.citys().subscribe(
                citysEntity -> {
                    this.mCitys = citysEntity;
                    ItemMenuEntity province = UserManager.getInstance().getCurrentUser().getUserinfo().getProvince();
                    ItemMenuEntity.SubEntity city = UserManager.getInstance().getCurrentUser().getUserinfo().getCity();
                    if (province == null || city == null) return;
                    for (int i = 0; i < citysEntity.size(); i++) {
                        ItemMenuEntity currCity = citysEntity.get(i);
                        if (currCity.getId() == province.getId()) {
                            mDefaultAddr[0] = i;
                            for (int j = 0; j < currCity.getSub().size(); j++) {
                                if (currCity.getSub().get(j).getId() == city.getId()) {
                                    mDefaultAddr[1] = j;
                                }
                            }
                        }
                    }
                    if (isNeedSet) {
                        getView().hideLoading();
                        getView().showCitys(mCitys);
                    }
                }
                , throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                });
        addDisposable(mCitysReq);
    }

    /**
     * 获取职业信息 如果不存在则从网络获取并设置到对应的对话框中
     *
     * @return
     */
    public OccupationsEntity getUserConfigData() {
        if (mUserConfigData == null) {
            getView().showLoading(null);
            getOccData(true);
        }
        return mUserConfigData;
    }

    /**
     * 获取行业数据
     *
     * @param isNeedSet
     */
    public void getOccData(boolean isNeedSet) {
        if (mOccReq != null && !mOccReq.isDisposed()) {
            mOccReq.dispose();
            remove(mOccReq);
        }
        mOccReq = mMainRepostiry.userConfigData().subscribe(
                citysEntity -> {
                    this.mUserConfigData = citysEntity;
                    ItemMenuEntity occp = UserManager.getInstance().getCurrentUser().getUserinfo().getOccupation();
                    if (occp == null) return;
                    for (int i = 0; i < citysEntity.getOccupation().size(); i++) {
                        if (citysEntity.getOccupation().get(i).getId() == occp.getId()) {
                            mDefaultOccp = i;
                        }
                    }
                    if (isNeedSet) {
                        getView().hideLoading();
                        getView().showCoinfgData(mUserConfigData);
                    }
                }
                , throwable -> {
                    if (!isNullView()) {
                        getView().hideLoading();
                        getView().showError(0, getGeneralErrorStr(throwable));
                    }
                });
        addDisposable(mOccReq);
    }

    public int getDefaultOccp() {
        return mDefaultOccp;
    }

    public int[] getDefaultAddr() {
        return mDefaultAddr;
    }

    public void setDefaultOccp(int defaultOccp) {
        mDefaultOccp = defaultOccp;
    }

    public void setDefaultAddr(int[] defaultAddr) {
        mDefaultAddr = defaultAddr;
    }

    public boolean isCanNavToPhotos() {
        return mCanNavToPhotos;
    }
}

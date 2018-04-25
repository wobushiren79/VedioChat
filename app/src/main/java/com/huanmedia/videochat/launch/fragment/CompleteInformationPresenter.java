package com.huanmedia.videochat.launch.fragment;

import com.huanmedia.ilibray.utils.AppValidationMgr;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.net.HostManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mvp.presenter.Presenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Create by Administrator
 * time: 2017/11/20.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class CompleteInformationPresenter extends Presenter<CompleteInformationView> {

    private final MainRepostiory mRepository;

    public CompleteInformationPresenter() {
        this.mRepository = new MainRepostiory();
    }

    /**
     * 用户登录
     *
     * @param userinfo
     */
    public void checkUploadUserInfo(UserEntity.UserinfoEntity userinfo) {
         if (!Check.isNull(userinfo)) {
             if (Check.isEmpty(userinfo.getUserphoto())) {
                 getView().showError("头像不能为空！");
             }else if (Check.isEmpty(userinfo.getNickname())) {
                getView().showError("昵称不能为空！");
            } else if (!AppValidationMgr.isUserName(userinfo.getNickname())) {
                getView().showError("用户名只能以中文、字母、数字、下划线组成！至少一个字符长度");
            } else if (userinfo.getSex() == 0) {
                getView().showError("性别不能为空！");
            } else if (Check.isEmpty(userinfo.getBirthday())) {
                getView().showError("选择你的年龄！");
            } else {
                 getView().confirmUpdate(userinfo);
            }
        }
    }


    @Override
    public void destroy() {
        super.destroy();
    }

    public void uploadUserInfo(UserEntity.UserinfoEntity userinfo) {
        if (userinfo==null)return;
        Map<String, String> prams = new HashMap<>();
        HashMap<String, RequestBody> fileMap = new HashMap<>();
        try {
            prams.put("nickname", userinfo.getNickname());
            prams.put("sex", userinfo.getSex() + "");
            prams.put("birthday", userinfo.getBirthday());
           File file = new File(userinfo.getUserphoto());
           RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
           fileMap.put("img\"; filename=\"" + file.getName() + ".png", fileBody);
       }catch (Exception e){
           e.printStackTrace();
           return;
       }
        mRepository.uploadFileWithPartMap(HostManager.getServiceUrl() + "/index/userext/updatebasereg", prams, fileMap)
                .subscribe(
                        response -> getView().upDateSuccess(response)
                        ,
                        throwable -> getView().showError(throwable.getMessage())
                );
    }
}

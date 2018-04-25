package com.huanmedia.videochat.my;

import com.huanmedia.ilibray.utils.AppValidationMgr;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import java.io.File;
import java.util.HashMap;

import mvp.presenter.Presenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Create by Administrator
 * time: 2017/12/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ReadMainAuthPresenter extends Presenter<ReadMainAuthView> {
    MainRepostiory mMainRepostiory;

    public ReadMainAuthPresenter() {
        mMainRepostiory = new MainRepostiory();
    }

    /**
     * @param cardnumber
     * @param cardName
     * @param phone
     * @param cardPhoto
     * @param cardHandPhoto
     */
    public void submitReadmain(String cardnumber, String cardName, String phone, String cardPhoto, String cardHandPhoto) {
        if (checkPrams(cardnumber,cardName,phone,cardPhoto,cardHandPhoto)){
            getView().showLoading(null);
            HashMap<String, RequestBody> fileMap = new HashMap<>();
            File file = new File(cardPhoto);
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            File file2 = new File(cardHandPhoto);
            RequestBody fileBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), cardName);
            RequestBody cardnumberbody = RequestBody.create(MediaType.parse("multipart/form-data"), cardnumber);
            RequestBody phonenumber = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
            fileMap.put("rname\";", name);
            fileMap.put("carid\";", cardnumberbody);
            fileMap.put("mobile\";", phonenumber);
            fileMap.put("img1\"; filename=\"" + file.getName() + ".png", fileBody);
            fileMap.put("img2\"; filename=\"" + file2.getName() + ".png", fileBody2);
            addDisposable(mMainRepostiory.uploadFileWithPartMap("/index/userext/starauthenupload", new HashMap<>(), fileMap).subscribe(
                    response -> {
                        getView().hideLoading();
                        getView().submitSuccess();
                    }
                    ,
                    throwable -> {
                        if (!isNullView()) {
                            getView().hideLoading();
                            getView().showError(0, getGeneralErrorStr(throwable));
                        }
                    }
            ));
        }

    }

    private boolean checkPrams(String cardnumber, String cardName, String phone, String cardPhoto, String cardHandPhoto) {
        if (!AppValidationMgr.isIDCard(cardnumber)){
            getView().showError(0, "请输入正确的身份证号码");
        }else
        if (!AppValidationMgr.isRealName(cardName)){
            getView().showError(0, "请输入正确的姓名");
        }else
        if (!AppValidationMgr.isPhone(phone)){
            getView().showError(0, "请输入正确的手机号码");
        }else
        if (Check.isEmpty(cardHandPhoto)){
            getView().showError(0, "请上传手持身份证照片");
        }else
        if (Check.isEmpty(cardPhoto)){
            getView().showError(0, "请上传身份证正面照片");
        }else {
            return true;
        }
        return false;
    }
}

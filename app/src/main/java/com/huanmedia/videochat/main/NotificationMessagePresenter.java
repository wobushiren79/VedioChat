package com.huanmedia.videochat.main;

import android.content.ContentValues;

import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.main.mode.SystemMessage;
import com.huanmedia.videochat.repository.datasouce.impl.MainRepostiory;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.presenter.Presenter;

/**
 * Create by Administrator
 * time: 2018/1/3.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class NotificationMessagePresenter extends Presenter<NotificationMessageView> {
    private final MainRepostiory mMainRepostiory;

    public NotificationMessagePresenter() {
        mMainRepostiory = new MainRepostiory();
    }

    public void getData(int page, int pageCount) {
        addDisposable(
                Observable
                        .create((ObservableOnSubscribe<List<SystemMessage>>) e -> {
                            List<SystemMessage> data = DataSupport.order("strtime desc").limit(pageCount).offset((page - 1) * pageCount).find(SystemMessage.class);
                            StringBuffer buffer = new StringBuffer();

                            for (int i = 0; i < data.size(); i++) {
                                SystemMessage message = data.get(i);
                                if (!message.getType().equals("SYSTEMNOTICE")) {
                                    buffer.append(data.get(i).getId());
                                    buffer.append(",");
                                }
                            }
                            ContentValues values = new ContentValues();
                            values.put("isRed", 1);
                            String condition = buffer.toString();
                            if (!Check.isEmpty(condition)) {
                                if (condition.endsWith(",")) {
                                    condition = condition.subSequence(0, condition.length() - 1).toString();
                                }
                                SystemMessage.updateAll(SystemMessage.class, values, "id in (" + condition + ")");
                            }
                            e.onNext(data);
                        })
                        .compose(ThreadExecutorHandler.toMain(ResourceManager.getInstance().getDefaultThreadProvider()))
                        .subscribe(
                                systemMessages -> {
                                    getView().showData(systemMessages);
                                },
                                throwable -> {
                                    if (!isNullView()) {
                                        getView().showError(0, getGeneralErrorStr(throwable));
                                    }
                                }

                        ));
    }
}

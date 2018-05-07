package com.huanmedia.videochat.repository.base;

import android.content.Context;

import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.dispose.interactor.DefaultThreadProvider;
import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.data.net.DataResponse;

public class BaseManagerImpl {

    private final DefaultThreadProvider mThreadProvider;

    public BaseManagerImpl() {
        this.mThreadProvider = ResourceManager.getInstance().getDefaultThreadProvider();
    }

    protected <T> void requestPost(Context context, Observable<DataResponse<T>> observable, HttpResponseHandler handler) {
        observable
                .map(response -> {
                    if (response.getCode() != 0) {
                        return new Exception("请求失败：" + response.getMessage());
                    } else {
                        return response.getResult();
                    }
                })
                .compose(ThreadExecutorHandler.toMain(mThreadProvider))
                .subscribe(response -> {
                            handler.onSuccess(response);
                        },
                        throwable -> {
                            handler.onError(throwable.getMessage());
                        });
    }


    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
                if (value == null)
                    continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
            map.put(fieldName, value);
        }
        return map;
    }
}

package com.huanmedia.videochat.repository.base;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.huanmedia.videochat.common.manager.ResourceManager;
import com.huanmedia.videochat.common.widget.dialog.HintDialog;
import com.huanmedia.videochat.repository.entity.UserEntity;
import com.huanmedia.videochat.repository.net.RemoteApiService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import mvp.data.dispose.interactor.DefaultThreadProvider;
import mvp.data.dispose.interactor.ThreadExecutorHandler;
import mvp.data.net.DataResponse;

public class BaseManagerImpl {

    protected final DefaultThreadProvider mThreadProvider;

    public BaseManagerImpl() {
        this.mThreadProvider = ResourceManager.getInstance().getDefaultThreadProvider();
    }

    private HintDialog mHintDialog;


    protected <T> void requestPost(Context context, Observable<DataResponse<T>> observable, HttpResponseHandler handler, boolean isShowDialog) {
        if (isShowDialog) {
            if (mHintDialog == null || !mHintDialog.isShowing()) {
                mHintDialog = new HintDialog(context, HintDialog.HintType.LOADING);
                mHintDialog.show();
                mHintDialog.setCanceledOnTouchOutside(false);
                mHintDialog.setTitleText("加载中...");
            }
        }

        observable
                .map(response -> {
                            if (response.getCode() != 0) {
                                return new Exception("请求失败：" + response.getMessage());
                            } else {
                                if (response.getResult() == null)
                                    return new Object();
                                return response.getResult();
                            }
                        }
                )
                .compose(ThreadExecutorHandler.toMain(mThreadProvider))
                .subscribe(response -> {
                            handler.onSuccess(response);
                            if (mHintDialog != null && mHintDialog.isShowing())
                                mHintDialog.cancel();
                        },
                        throwable -> {
                            handler.onError(throwable.getMessage());
                            if (mHintDialog != null && mHintDialog.isShowing())
                                mHintDialog.cancel();
                        });
    }

    protected <T> void requestPost(Context context, Observable<DataResponse<T>> observable, HttpResponseHandler handler) {
        requestPost(context, observable, handler, false);
    }


//    public static Map<String, String> mapObjectToString(Map<String, Object> map) {
//        Map<String, String> newMap = new HashMap<>();
//        for (Map.Entry<String, Object> itemData : map.entrySet()) {
//            Object itemObj = itemData.getValue();
//            try {
//                String itemStr = (String) itemObj;
//                newMap.put(itemData.getKey(), itemStr);
//            } catch (Exception e) {
//                continue;
//            }
//        }
//        return newMap;
//    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        String paramsJson = "";
        try {
            Gson gson = new Gson();
            paramsJson = gson.toJson(obj);
        } catch (Exception e) {

        } finally {
            Log.v("Post_Params", "");
            Log.v("Post_Params", "--------------------Post  Params--------------------");
            Log.v("Post_Params", "paramsJson:" + paramsJson);
            Log.v("Post_Params", "----------------------------------------------------");
        }
        Map<String, Object> map = new HashMap<>();
        if (obj == null)
            return map;
        List<Field> fieldList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            //得到父类,然后赋给自己
            clazz = clazz.getSuperclass();
        }
        for (Field field : fieldList) {
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

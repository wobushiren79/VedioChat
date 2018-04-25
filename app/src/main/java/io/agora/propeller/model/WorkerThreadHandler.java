package io.agora.propeller.model;

import android.app.Application;

import com.huanmedia.videochat.common.FApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频聊天线程管理器
 */

public class WorkerThreadHandler {
    private final Application mApplication;
    private static volatile WorkerThreadHandler INSTANCE;
    private volatile Map<Class,Integer> mQuotes=new HashMap<>();
    public static WorkerThreadHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (WorkerThreadHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WorkerThreadHandler(FApplication.getApplication());
                }
            }
        }
        return INSTANCE;
    }

    private WorkerThreadHandler(Application application) {
        this.mApplication = application;
    }

    private volatile WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(mApplication);
            mWorkerThread.start();
            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        if (mWorkerThread==null)return;
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
        INSTANCE= null;
    }

    public static final CurrentUserSettings mVideoSettings = new CurrentUserSettings();

    public void quote(Class<?> aClass) {
        if (!mQuotes.containsKey(aClass)){
            mQuotes.put(aClass,mQuotes.size());
        }
    }
    public void unquote(Class<?> aClass) {
        Integer index = mQuotes.get(aClass);
        if (index!=null){
            mQuotes.remove(aClass);
        }
    }
    public int getQuote(){
        return mQuotes.size();
    }
}

package com.huanmedia.videochat.repository.net;

import com.huanmedia.videochat.repository.base.MainManager;
import com.huanmedia.videochat.repository.base.MainManagerImpl;

public class MHttpManagerFactory {
    /**
     * 获取API管理
     * @return
     */
    public static MainManager getMainManager() {
        return MainManagerImpl.getInstance();
    }
}

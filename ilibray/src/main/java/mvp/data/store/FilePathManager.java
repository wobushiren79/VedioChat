package mvp.data.store;

import android.content.Context;

import java.io.File;

/**
 * Created by ericYang on 2017/5/26.
 * Email:eric.yang@huanmedia.com
 * what?
 */

public class FilePathManager {
    //--------------------------------App缓存路径
    public static File getImageCacheDir(Context context){
        return StorageUtil.getIndividualCacheDirectory(context,"cacheImg");
    }
    public static File getHttpCacheDir(Context context){
        return StorageUtil.getIndividualCacheDirectory(context,"httpCache");
    }
    public static File getCacheDir(Context context){
        return StorageUtil.getIndividualCacheDirectory(context,"cache");
    }
    public static File getApkDir(Context context){
        return StorageUtil.getIndividualCacheDirectory(context,"apk");
    }
    public static File getUpImage(Context context){
        return StorageUtil.getIndividualCacheDirectory(context,"upImage");
    }

    public static File getMaskDir(Context context) {
        return StorageUtil.getIndividualCacheDirectory(context,"mask",false);
    }
}

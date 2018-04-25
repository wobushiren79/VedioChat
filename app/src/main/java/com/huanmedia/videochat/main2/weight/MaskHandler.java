package com.huanmedia.videochat.main2.weight;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanmedia.ilibray.utils.GsonUtils;
import com.huanmedia.ilibray.utils.data.assist.Check;
import com.huanmedia.ilibray.utils.data.cipher.MD5Digest;
import com.huanmedia.ilibray.utils.io.IOUtils;
import com.huanmedia.videochat.common.FApplication;
import com.huanmedia.videochat.main2.datamodel.SkinMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mvp.data.store.FilePathManager;

/**
 * Create by Administrator
 * time: 2017/12/7.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class MaskHandler {

    private static java.lang.String CACHE_FACEDATA = "cache_data";
    private static boolean sInitData;

    public static File getCacheDir() {
        return FilePathManager.getMaskDir(FApplication.getApplication());
    }

    public static List<List<SkinMode>> getSkinModes() {
        try {
            return readCacheFace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static int saveFaceCaches(List<List<SkinMode>> lists) {
        if (lists.size() == 0) return 0;
        Object[] change = getCacheFaceChanges(lists);
        List<List<SkinMode>> caches = (List<List<SkinMode>>) change[0];
        int changeCount = (int) change[1];
        try {
            if (changeCount > 0)
                writerCacheFace(caches);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return changeCount;
    }

    /**
     * 对比数据并设置是否以改变，并返回改变数量
     * @param lists 【0】 设置是否以改变后的数据列表 【1】 改变数量
     * @return
     */
    private static Object[] getCacheFaceChanges(List<List<SkinMode>> lists) {
        int changecount = 0;
        List<List<SkinMode>> oldLists = new ArrayList<>();
        try {
            oldLists = readCacheFace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (oldLists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                List<SkinMode> modes = lists.get(i);
                for (int j = 0; j < modes.size(); j++) {
                    SkinMode mode = modes.get(j);
                    if (checkIsChange(oldLists,mode)) {
//                        mode.isChange(true);
//                        mode.setDownload(0);
                        changecount++;
                    }
                }
            }

        }else {
            for (int i = 0; i < lists.size(); i++) {
                changecount+=lists.get(i).size();
            }
        }
        return new Object[]{lists, changecount};
    }

    /**
     * 通过ID和md5确定数据是否以改变
     * @param oldLists
     * @return
     */
    private static boolean checkIsChange(List<List<SkinMode>> oldLists,SkinMode skinMode) {
        for (int i = 0; i < oldLists.size(); i++) {
            List<SkinMode> modes = oldLists.get(i);
            for (int j = 0; j < modes.size(); j++) {
                SkinMode mode = modes.get(j);
                if (mode.getId() == skinMode.getId() &&mode.getMd5().equals(skinMode.getMd5()) && mode.getVersion()==skinMode.getVersion()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 写入face数据到缓存
     * @param lists
     * @throws IOException
     */
    private static void writerCacheFace(List<List<SkinMode>> lists) throws IOException {
        if (Check.isEmpty(lists)) return;
            File newFile = new File(FilePathManager.getMaskDir(FApplication.getApplication()), CACHE_FACEDATA);
            FileOutputStream outputStream = new FileOutputStream(newFile);
            Gson gson = GsonUtils.getDefGsonBulder().create();
            String jsonstr = gson.toJson(lists);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonstr.getBytes("UTF-8"));
            try {
                byte[] buffer = new byte[1024];
                int readLength;
                while ((readLength=inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,readLength);
                }
            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
    }

    /**
     * 读取Face数据缓存
     * @return
     * @throws IOException
     */
    private static List<List<SkinMode>> readCacheFace() throws IOException {
        File olderFile = new File(FilePathManager.getMaskDir(FApplication.getApplication()), CACHE_FACEDATA);
        List<List<SkinMode>> localFace = new ArrayList<>();
        if (olderFile.exists()) {
            FileInputStream input = new FileInputStream(olderFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[1024];
                int readLength;
                while ((readLength=input.read(buffer))!=-1){
                    outputStream.write(buffer,0,readLength);
                }
                String liststr = new String(outputStream.toByteArray());
                if (!Check.isEmpty(liststr)) {
                    Gson gson = GsonUtils.getDefGsonBulder().create();
                    TypeToken<List<List<SkinMode>>> typeToken = new TypeToken<List<List<SkinMode>>>() {
                    };
                    localFace = gson.fromJson(liststr, typeToken.getType());
                }
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(outputStream);
            }
        }
        return localFace;
    }

    public static boolean isInitData() {
        return sInitData;
    }

    public static void setInitData(boolean initData) {
        sInitData = initData;
    }

    public static List<List<SkinMode>> getMaskDatas() {
        return getSkinModes();
    }

    /**
     * 检查文件完整性如果文件不完整或已过时删除旧文件
     * @param mode
     * @return
     */
    public static boolean checkFileFull(SkinMode mode){
        if (mode==null)return false;
        File localFile = new File(getCacheDir(), mode.getName());
        String fileMd5 = MD5Digest.getMD5(localFile);
        if (fileMd5==null || !mode.getMd5().equals(fileMd5)){
            return false;
        }
        return true;
    }
}

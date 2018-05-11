package com.huanmedia.ilibray.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileUtils {


    /**
     * 再指定文件夹下是否含有指定的所有文件
     *
     * @param parent
     * @param childList
     * @return
     */
    public static boolean hasListFileExists(File parent, List<String> childList) {
        if (parent == null || childList == null || childList.size() == 0)
            return false;
        for (int i = 0; i < childList.size(); i++) {
            String child = childList.get(i);
            boolean hasFile = hasFileExists(parent, child);
            if (!hasFile) {
                return false;
            }
        }
        return true;
    }

    /**
     * 再指定文件夹下是否有文件
     *
     * @param parent
     * @param child
     * @return
     */
    public static boolean hasFileExists(File parent, String child) {
        return new File(parent, child).exists();
    }


    /**
     * 保存bitmap到sd目录下
     * @param bitmap
     * @return
     */
    public static File saveBitMapToSDCard(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }
}

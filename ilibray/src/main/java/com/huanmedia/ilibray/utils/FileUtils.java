package com.huanmedia.ilibray.utils;

import java.io.File;
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
}

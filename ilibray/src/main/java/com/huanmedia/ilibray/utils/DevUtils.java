package com.huanmedia.ilibray.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevUtils {
    /**
     * String[] 转 List
     *
     * @param strings
     * @return
     */
    public static List ArraryToList(String[] strings) {
        if (strings == null || strings.length == 0) {
            return new ArrayList();
        }
        return Arrays.asList(strings);
    }

    /**
     * String  转List
     *
     * @param str
     * @return
     */
    public static List StrToList(String str, String mark) {
        if (str == null || str.length() == 0) {
            return new ArrayList();
        }
        String strs[] = str.split(mark);
        return ArraryToList(strs);
    }


    /**
     * List 用 mark分割成String
     *
     * @param list
     * @param mark
     * @return
     */
    public static String ListToStrByMark(List<String> list, String mark) {
        StringBuffer str = new StringBuffer();
        boolean isFirst = true;
        for (String item : list) {
            if (!isFirst)
                str.append(mark);
            str.append(item);
            isFirst = false;
        }
        return str.toString();
    }
}

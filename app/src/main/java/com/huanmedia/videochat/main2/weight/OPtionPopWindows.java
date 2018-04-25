package com.huanmedia.videochat.main2.weight;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.widget.SimpleAdapter;

import com.huanmedia.videochat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Administrator
 * time: 2018/3/5.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public final class OPtionPopWindows {
    public static ListPopupWindow getAttectionListOption(Context context){
        ListPopupWindow w = new ListPopupWindow(context);
        List<Map<String, String>> data = new ArrayList<>();
        String[] froms = new String[]{"title"};
        int[] tos = new int[]{android.R.id.text1};
        String[] datas = new String[]{"取消关注", "举报"};

        for (int i = 0; i < datas.length; i++) {
            Map<String, String> mapData = new HashMap<>();
            mapData.put(froms[0],datas[i]);
            data.add(mapData);
        }
        w.setAdapter(new SimpleAdapter(context,data, R.layout.simple_list_item1,froms,tos));

        return w;
    }
    public static ListPopupWindow getComeAcrossListOption(Context context){
        ListPopupWindow w = new ListPopupWindow(context);
        List<Map<String, String>> data = new ArrayList<>();
        String[] froms = new String[]{"title"};
        int[] tos = new int[]{android.R.id.text1};
        String[] datas = new String[]{"删除", "举报"};

        for (int i = 0; i < datas.length; i++) {
            Map<String, String> mapData = new HashMap<>();
            mapData.put(froms[0],datas[i]);
            data.add(mapData);
        }
        w.setAdapter(new SimpleAdapter(context,data, R.layout.simple_list_item1,froms,tos));

        return w;
    }
    public static ListPopupWindow getFansListOption(Context context){
        ListPopupWindow w = new ListPopupWindow(context);
        List<Map<String, String>> data = new ArrayList<>();
        String[] froms = new String[]{"title"};
        int[] tos = new int[]{android.R.id.text1};
        String[] datas = new String[]{"举报"};

        for (int i = 0; i < datas.length; i++) {
            Map<String, String> mapData = new HashMap<>();
            mapData.put(froms[0],datas[i]);
            data.add(mapData);
        }
        w.setAdapter(new SimpleAdapter(context,data, R.layout.simple_list_item1,froms,tos));

        return w;
    }
}

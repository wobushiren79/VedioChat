package com.huanmedia.videochat.video.model;

import android.util.SparseArray;

/**
 * Create by Administrator
 * time: 2017/12/14.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class GiftModeMapping {
    private  static SparseArray<GiftLocalMode> giftMapping;

    public static SparseArray<GiftLocalMode> getGiftMapping() {
        if (giftMapping==null){
           createMapping();
        }
        return giftMapping;
    }

    private static void createMapping() {
        giftMapping = new SparseArray<>();
        //        giftMapping.put(1,  new GiftLocalMode("bear/","images/","img_0.png","data.json"));
        giftMapping.put(12,  new GiftLocalMode(GiftLocalMode.GiftType.SVGA,"svga/","images/","跑车.png","跑车.svga"));
//        giftMapping.put(12,  new GiftLocalMode(GiftLocalMode.BusinessType.HEADER,"svga/","images/","yacht.png","游艇.svga"));

        giftMapping.put(4,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","点赞.png",null));
        giftMapping.put(5,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","鼓掌.png",null));
        giftMapping.put(6,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","亲吻.png",null));
        giftMapping.put(7,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","爱你.png",null));
        giftMapping.put(8,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","口红.png",null));
        giftMapping.put(9,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","戒指.png",null));
        giftMapping.put(10,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","香水.png",null));
        giftMapping.put(11,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","项链.png",null));
        giftMapping.put(13,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","拥抱.png",null));
        giftMapping.put(14,  new GiftLocalMode(GiftLocalMode.GiftType.ANIM,"gift/","images/","唱首歌呗.png",null));
    }
    /**
     * 使用后回收资源destroy中
     */
    public static void clearMapping(){
        if (giftMapping!=null){
            giftMapping.clear();
            giftMapping=null;
        }
    }
}

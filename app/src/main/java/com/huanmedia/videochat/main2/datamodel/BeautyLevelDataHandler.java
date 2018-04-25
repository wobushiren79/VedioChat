package com.huanmedia.videochat.main2.datamodel;

import android.support.annotation.IntDef;

import com.faceunity.FUManager;
import com.huanmedia.videochat.R;
import com.huanmedia.videochat.common.FApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType.BIGEYE;
import static com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType.BUFFING;
import static com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType.FILTER;
import static com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType.WHITENING;
import static com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType.YAD;

/**
 * Create by Administrator
 * time: 2018/3/15.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BeautyLevelDataHandler {
    private static final String packageName = FApplication.getApplication().getPackageName();
    public static final String RESOURCEURL= "android.resource://"+ packageName +"/";
    public static final String DRAWABLEURL= "android.resource://"+ packageName +"/drawable/";

    @IntDef({FILTER, BUFFING, WHITENING, YAD, BIGEYE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LeveType {
        /**
         * FILTER 滤镜
         * BUFFING 磨皮
         * WHITENING 美白
         * YAD 廋脸
         * BIGEYE 大眼
         */
        int FILTER = 1, BUFFING = 2, WHITENING = 3, YAD = 4, BIGEYE = 5;
    }

    public static List<BeautyLevleMode> getData() {
        List<BeautyLevleMode> modes = new ArrayList<>();
        //滤镜数据
        BeautyLevleMode filterMode = new BeautyLevleMode();
        filterMode.setTitle("滤镜");
        filterMode.setType(LeveType.FILTER);
        filterMode.setDefaultLevel(FUManager.getmBeautyConfig().getFilter_position());
        List<BeautyLevleMode.BeautyPageData> filterData = new ArrayList<>();
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "自然", DRAWABLEURL+ R.drawable.nature));
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "唯美", DRAWABLEURL+ R.drawable.delta));
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "冷调", DRAWABLEURL+ R.drawable.electric));
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "柔光", DRAWABLEURL+ R.drawable.slowlived));
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "日系", DRAWABLEURL+ R.drawable.tokyo));
        filterData.add(new BeautyLevleMode.BeautyPageData(1, "温暖", DRAWABLEURL+ R.drawable.warm));
        filterMode.setBeautyPageData(filterData);
        modes.add(filterMode);

        BeautyLevleMode buffingMode = new BeautyLevleMode();
        //磨皮数据
//        buffingMode
        buffingMode.setType(LeveType.BUFFING);
        buffingMode.setTitle("磨皮");
        buffingMode.setDefaultLevel(FUManager.getmBeautyConfig().getBlur_level());
        List<BeautyLevleMode.BeautyPageData> buffingData = new ArrayList<>();
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "0", null));//第一条数据需要设置为背景图片
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "1", null));
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "2", null));
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "3", null));
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "4", null));
        buffingData.add(new BeautyLevleMode.BeautyPageData(2, "5", null));
        buffingMode.setBeautyPageData(buffingData);
        modes.add(buffingMode);
        BeautyLevleMode whiteningMode = new BeautyLevleMode();
        //美白数据
        whiteningMode.setType(LeveType.WHITENING);
        whiteningMode.setTitle("美白");
        whiteningMode.setDefaultLevel((int) (FUManager.getmBeautyConfig().getColor_level()*10f)/2);
        List<BeautyLevleMode.BeautyPageData> whiteningData = new ArrayList<>();
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "0", null));//第一条数据需要设置为背景图片
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "1", null));
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "2", null));
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "3", null));
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "4", null));
        whiteningData.add(new BeautyLevleMode.BeautyPageData(2, "5", null));
        whiteningMode.setBeautyPageData(whiteningData);
        modes.add(whiteningMode);

        BeautyLevleMode yadMode = new BeautyLevleMode();
        //廋脸数据
        yadMode.setType(LeveType.YAD);
        yadMode.setTitle("廋脸");
        yadMode.setDefaultLevel((int) (FUManager.getmBeautyConfig().getCheek_thinning()*10f)/2);
        List<BeautyLevleMode.BeautyPageData> yadData = new ArrayList<>();
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "0", null));//第一条数据需要设置为背景图片
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "1", null));
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "2", null));
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "3", null));
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "4", null));
        yadData.add(new BeautyLevleMode.BeautyPageData(2, "5", null));
        yadMode.setBeautyPageData(yadData);
        modes.add(yadMode);

        BeautyLevleMode bigEyeMode = new BeautyLevleMode();
        //大眼数据
        bigEyeMode.setType(LeveType.BIGEYE);
        bigEyeMode.setTitle("大眼");
        bigEyeMode.setDefaultLevel((int) (FUManager.getmBeautyConfig().getEye_enlarging()*10)/2);
        List<BeautyLevleMode.BeautyPageData> bigEyeData = new ArrayList<>();
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "0", null));//第一条数据需要设置为背景图片
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "1", null));
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "2", null));
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "3", null));
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "4", null));
        bigEyeData.add(new BeautyLevleMode.BeautyPageData(2, "5", null));
        bigEyeMode.setBeautyPageData(bigEyeData);
        modes.add(bigEyeMode);
        return modes;
    }
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}

package com.huanmedia.videochat.main2.datamodel;

import com.huanmedia.videochat.main2.datamodel.BeautyLevelDataHandler.LeveType;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Administrator
 * time: 2018/3/15.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BeautyLevleMode {//美颜对话框数据模型
    @LeveType int type;//类别 1滤镜 2磨皮 3美颜 4廋脸 5大眼
    List<BeautyPageData> beautyPageData;
    int defaultLevel;
    private String mTitle;

    public @LeveType int getType() {
        return type;
    }

    public void setType(@LeveType int type) {
        this.type = type;
    }

    public List<BeautyPageData> getBeautyPageData() {
        return beautyPageData;
    }

    public void setBeautyPageData(List<BeautyPageData> beautyPageData) {
        this.beautyPageData = beautyPageData;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(int defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * 页面数据模型
     */
    public static class BeautyPageData implements Serializable {
        /**
         * @param type 类型：1图片文本类型 2 文本类型
         * @param name 标题名称
         * @param url 图片Url
         */
        public BeautyPageData(int type, String name, String url) {
            this.type = type;
            this.name = name;
            this.url = url;
        }

        private int type ;//类型：1图片文本类型 2 文本类型
        private String name;
        private String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

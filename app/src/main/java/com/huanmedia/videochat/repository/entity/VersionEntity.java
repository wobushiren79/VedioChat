package com.huanmedia.videochat.repository.entity;

/**
 * Create by Administrator
 * time: 2018/1/4.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class VersionEntity {

    /**
     * id : 3
     * version : 0.1.4
     * enforeUpdate : FALSE
     * apkUrl : http://mmimg.lzwifi.com/pack/1/xxx.apk
     * createTime : 1254878545
     * desc : 说明3
     * intro : 升级说明3
     * fileSize : 12312313
     * md5 : 42423424sfsfwerwer
     * andVerNum : 3
     * iosVerNum : 3
     */

    private int id;
    private String version;
    private String enforeUpdate;
    private String apkUrl;
    private int createTime;
    private String desc;
    private String intro;
    private int fileSize;
    private String md5;
    private int andVerNum;
    private int iosVerNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnforeUpdate() {
        return enforeUpdate;
    }

    public void setEnforeUpdate(String enforeUpdate) {
        this.enforeUpdate = enforeUpdate;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getAndVerNum() {
        return andVerNum;
    }

    public void setAndVerNum(int andVerNum) {
        this.andVerNum = andVerNum;
    }

    public int getIosVerNum() {
        return iosVerNum;
    }

    public void setIosVerNum(int iosVerNum) {
        this.iosVerNum = iosVerNum;
    }
}

package com.huanmedia.videochat.main2.datamodel;

import com.huanmedia.ilibray.utils.DevUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Administrator
 * time: 2017/11/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class SkinMode implements Serializable {
    /**
     * id : 2
     * name : 面具C
     * desc : 第三个面具
     * coin : 0
     * ftype : 1
     * packurl : http://mmimg-dev.lzwifi.com/facepack/gradient.bundle
     * img_thumb : http://mmimg-dev.lzwifi.com/facepack/2.jpg
     * createtime : 541248411
     * status : 1
     * fsort : 10
     * md5 : 820ab97ee303d45a77dc47c1a1c3ba7b
     */
    private int id;
    private String name;
    private String desc;
    private int coin;
    private int ftype;
    private String packurl;
    private String img_thumb;
    private int createtime;
    private int status;
    private int fsort;
    private int version;
    private String md5;

    private int isDownload;// 0 未下载 1 正在下载 2 下载完成

    //文件存储名字
    public List<String> getPackName() {
        List<String> packUrlList = DevUtils.StrToList(packurl, ",");
        List<String> packNameList = new ArrayList<>();
        for (int i = 0; i < packUrlList.size(); i++) {
            packNameList.add(i + "_" + name);
        }
        return packNameList;
    }

    //获取下载包路径列表
    public List<String> getPackUrlList() {
        return DevUtils.StrToList(packurl, ",");
    }

    public int getVersion() {
        return version;
    }

    public SkinMode setVersion(int version) {
        this.version = version;
        return this;
    }


    public int isDownload() {
        return isDownload;
    }

    public SkinMode setDownload(int download) {
        isDownload = download;
        return this;
    }
//
//    public boolean isChange() {
//        return isChange;
//    }
//
//    public void isChange(boolean download) {
//        isChange = download;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public String getPackurl() {
        return packurl;
    }

    public void setPackurl(String packurl) {
        this.packurl = packurl;
    }

    public String getImg_thumb() {
        return img_thumb;
    }

    public void setImg_thumb(String img_thumb) {
        this.img_thumb = img_thumb;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFsort() {
        return fsort;
    }

    public void setFsort(int fsort) {
        this.fsort = fsort;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkinMode mode = (SkinMode) o;

        if (getId() != mode.getId()) return false;
        if (getVersion() != mode.getVersion()) return false;
        if (getName() != null ? !getName().equals(mode.getName()) : mode.getName() != null)
            return false;
        if (getPackurl() != null ? !getPackurl().equals(mode.getPackurl()) : mode.getPackurl() != null)
            return false;
        if (getPackurl() != null ? !getPackurl().equals(mode.getPackurl()) : mode.getPackurl() != null)
            return false;
        return getMd5() != null ? getMd5().equals(mode.getMd5()) : mode.getMd5() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getVersion();
        result = 31 * result + (getPackurl() != null ? getPackurl().hashCode() : 0);
        result = 31 * result + (getMd5() != null ? getMd5().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SkinMode{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", coin=").append(coin);
        sb.append(", ftype=").append(ftype);
        sb.append(", packurl='").append(packurl).append('\'');
        sb.append(", img_thumb='").append(img_thumb).append('\'');
        sb.append(", createtime=").append(createtime);
        sb.append(", status=").append(status);
        sb.append(", fsort=").append(fsort);
        sb.append(", version=").append(version);
        sb.append(", md5='").append(md5).append('\'');
        sb.append(", isDownload=").append(isDownload);
        sb.append('}');
        return sb.toString();
    }
}

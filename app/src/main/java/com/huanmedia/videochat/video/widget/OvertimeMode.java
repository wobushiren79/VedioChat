package com.huanmedia.videochat.video.widget;

/**
 * Create by Administrator
 * time: 2017/12/11.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class OvertimeMode {
    private int src;
    private String hint;
    private int flag;
    private int times;
    private boolean select;
    private int type;//type ==0 为收费加时type ==1为免费加时
    public OvertimeMode() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    /**
     *加时数据模型
     * @param src 时间图标
     * @param hint 价格描述
     * @param flag 单价
     * @param times 最多使用次数(-1为不限次数)
     */
    public OvertimeMode(int src, String hint, int flag, int times) {
        this.src = src;
        this.hint = hint;
        this.flag = flag;
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OvertimeMode{");
        sb.append("src=").append(src);
        sb.append(", hint='").append(hint).append('\'');
        sb.append(", flag=").append(flag);
        sb.append(", times=").append(times);
        sb.append('}');
        return sb.toString();
    }
}

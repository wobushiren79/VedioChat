package com.huanmedia.videochat.common.widget.tablayout;

import com.huanmedia.videochat.R;

public class TabSwitchBean {
    private String tabName;
    private int tabCheckPic;
    private int tabUnCheckPic;
    private int tabTextCheckColor = R.color.base_black;
    private int tabTextUnCheckColor = R.color.base_hint;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TabSwitchBean(String tabName, int tabCheckPic, int tabUnCheckPic) {
        this.tabName = tabName;
        this.tabCheckPic = tabCheckPic;
        this.tabUnCheckPic = tabUnCheckPic;
    }


    public int getTabTextCheckColor() {
        return tabTextCheckColor;
    }

    public void setTabTextCheckColor(int tabTextCheckColor) {
        this.tabTextCheckColor = tabTextCheckColor;
    }

    public int getTabTextUnCheckColor() {
        return tabTextUnCheckColor;
    }

    public void setTabTextUnCheckColor(int tabTextUnCheckColor) {
        this.tabTextUnCheckColor = tabTextUnCheckColor;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getTabCheckPic() {
        return tabCheckPic;
    }

    public void setTabCheckPic(int tabCheckPic) {
        this.tabCheckPic = tabCheckPic;
    }

    public int getTabUnCheckPic() {
        return tabUnCheckPic;
    }

    public void setTabUnCheckPic(int tabUnCheckPic) {
        this.tabUnCheckPic = tabUnCheckPic;
    }
}

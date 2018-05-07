package com.huanmedia.videochat.mvp.entity.results;

import java.io.Serializable;
import java.util.List;

public class TalkRoomListResults extends ListDataResults implements Serializable {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String roomid;
        private int uid1;
        private int uid2;

        public String getRoomid() {
            return roomid;
        }

        public void setRoomid(String roomid) {
            this.roomid = roomid;
        }

        public int getUid1() {
            return uid1;
        }

        public void setUid1(int uid1) {
            this.uid1 = uid1;
        }

        public int getUid2() {
            return uid2;
        }

        public void setUid2(int uid2) {
            this.uid2 = uid2;
        }
    }

}

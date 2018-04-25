package com.huanmedia.videochat.repository.entity;

import com.huanmedia.videochat.discover.adapter.BusinessMultiItem;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Administrator
 * time: 2018/2/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class BusinessCardUserTags extends BusinessMultiItem {
    private List<TagEntity> items;

    public List<TagEntity> getItems() {
        return items;
    }

    public void setItems(List<TagEntity> items) {
        this.items = items;
    }

    public static class TagEntity  implements Serializable {
        /**
         * uid : 1
         * tagid : 1
         * tagcount : 3
         * type : GOOD
         * tag : 健谈
         */

        private int uid;
        private int tagid;
        private int tagcount;
        private String type;
        private String tag;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getTagid() {
            return tagid;
        }

        public void setTagid(int tagid) {
            this.tagid = tagid;
        }

        public int getTagcount() {
            return tagcount;
        }

        public void setTagcount(int tagcount) {
            this.tagcount = tagcount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TagEntity{");
            sb.append("uid=").append(uid);
            sb.append(", tagid=").append(tagid);
            sb.append(", tagcount=").append(tagcount);
            sb.append(", type='").append(type).append('\'');
            sb.append(", tag='").append(tag).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
    @Override
    public int getItemType() {
        return BusinessMultiItem.BusinessType.TAG;
    }

}

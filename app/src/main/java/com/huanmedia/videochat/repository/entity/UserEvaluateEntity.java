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

public class UserEvaluateEntity  implements Serializable  {


    /**
     * countlist : [{"uid":1,"tagid":1,"tagcount":3,"type":"GOOD","tag":"健谈"},{"uid":1,"tagid":2,"tagcount":3,"type":"GOOD","tag":"大长腿"},{"uid":1,"tagid":4,"tagcount":2,"type":"GOOD","tag":"高颜值"},{"uid":1,"tagid":3,"tagcount":1,"type":"GOOD","tag":"美人胚子"},{"uid":1,"tagid":5,"tagcount":1,"type":"GOOD","tag":"完美身材"}]
     * total : 3
     * page : 1
     * pagesize : 10
     * totalpage : 1
     * items : [{"text":"bb1212","uid":3,"nickname":"昵称3","userphoto_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg"},{"text":"aa","uid":1,"nickname":"昵称1","userphoto_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg"},{"text":"我好","uid":2,"nickname":"昵称2","userphoto_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg"}]
     */

    private int total;
    private int page;
    private int pagesize;
    private int totalpage;
//    private List<TagEntity> countlist;
    private List<EvaluateEntity> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }


    public List<EvaluateEntity> getItems() {
        return items;
    }

    public void setItems(List<EvaluateEntity> items) {
        this.items = items;
    }


    public static class EvaluateEntity extends BusinessMultiItem implements Serializable{
        /**
         * text : bb1212
         * uid : 3
         * nickname : 昵称3
         * userphoto_thumb : http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg
         */

        private String text;
        private int uid;
        private String nickname;
        private String userphoto_thumb;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserphoto_thumb() {
            return userphoto_thumb;
        }

        public void setUserphoto_thumb(String userphoto_thumb) {
            this.userphoto_thumb = userphoto_thumb;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("EvaluateEntity{");
            sb.append("text='").append(text).append('\'');
            sb.append(", uid=").append(uid);
            sb.append(", nickname='").append(nickname).append('\'');
            sb.append(", userphoto_thumb='").append(userphoto_thumb).append('\'');
            sb.append('}');
            return sb.toString();
        }

        @Override
        public int getItemType() {
             return BusinessMultiItem.BusinessType.EVALUATE;
        }
    }
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserEvaluateEntity{");
        sb.append("total=").append(total);
        sb.append(", page=").append(page);
        sb.append(", pagesize=").append(pagesize);
        sb.append(", totalpage=").append(totalpage);
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}

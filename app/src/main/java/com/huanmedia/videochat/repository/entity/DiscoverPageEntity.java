package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by Administrator
 * time: 2018/2/26.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class DiscoverPageEntity implements Parcelable {
    /**
     * totalpage :
     * total : 0
     * page : 1
     * pagesize : 10
     * items : [{"uid":1,"nickname":"昵称1","sex":2,"area":{"id":340800,"name":"安庆市"},"age":30,"userphoto_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","distance":"未知","occupation":null,"photos":[{"photo":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1},{"photo":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1}],"starbutton":1,"starcoin":100,"startime":10,"favorited":0},{"uid":2,"nickname":"昵称2","sex":1,"area":{"id":340800,"name":"安庆市"},"age":20,"userphoto_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/userphoto/5a0bbdd21c53e_thumb.jpg","distance":"15m","occupation":"公务员","photos":[{"photo":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1},{"photo":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg","photo_thumb":"http://mmimg-dev.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg","sort":1}],"starbutton":1,"starcoin":100,"startime":10,"favorited":0}]
     */

    private int total;
    private int page;
    private int pagesize;
    private int totalpage;
    private List<DiscoverEntity> items;

    public int getTotal() {
        return total;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
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

    public List<DiscoverEntity> getItems() {
        return items;
    }

    public void setItems(List<DiscoverEntity> items) {
        this.items = items;
    }

    public DiscoverPageEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeInt(this.page);
        dest.writeInt(this.pagesize);
        dest.writeInt(this.totalpage);
        dest.writeTypedList(this.items);
    }

    protected DiscoverPageEntity(Parcel in) {
        this.total = in.readInt();
        this.page = in.readInt();
        this.pagesize = in.readInt();
        this.totalpage = in.readInt();
        this.items = in.createTypedArrayList(DiscoverEntity.CREATOR);
    }

    public static final Creator<DiscoverPageEntity> CREATOR = new Creator<DiscoverPageEntity>() {
        @Override
        public DiscoverPageEntity createFromParcel(Parcel source) {
            return new DiscoverPageEntity(source);
        }

        @Override
        public DiscoverPageEntity[] newArray(int size) {
            return new DiscoverPageEntity[size];
        }
    };
}

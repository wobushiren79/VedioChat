package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Administrator
 * time: 2017/12/28.
 * Email:eric.yang@huanmedia.com
 * version: ${VERSION}
 */

public class ItemMenuEntity implements Parcelable ,Serializable{
    /**
     * id : 110000
     * name : 北京市
     * sub : [{"id":110101,"name":"东城区"},{"id":110102,"name":"西城区"},{"id":110105,"name":"朝阳区"},{"id":110106,"name":"丰台区"},{"id":110107,"name":"石景山区"},{"id":110108,"name":"海淀区"},{"id":110109,"name":"门头沟区"},{"id":110111,"name":"房山区"},{"id":110112,"name":"通州区"},{"id":110113,"name":"顺义区"},{"id":110114,"name":"昌平区"},{"id":110115,"name":"大兴区"},{"id":110116,"name":"怀柔区"},{"id":110117,"name":"平谷区"},{"id":110228,"name":"密云县"},{"id":110229,"name":"延庆县"}]
     */

    private int id;
    private String name;
    private List<SubEntity> sub;

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

    public List<SubEntity> getSub() {
        return sub;
    }

    public void setSub(List<SubEntity> sub) {
        this.sub = sub;
    }

    public static class SubEntity implements Parcelable ,Serializable {
        /**
         * id : 110101
         * name : 东城区
         */

        private int id;
        private String name;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public SubEntity() {
        }

        protected SubEntity(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<SubEntity> CREATOR = new Creator<SubEntity>() {
            @Override
            public SubEntity createFromParcel(Parcel source) {
                return new SubEntity(source);
            }

            @Override
            public SubEntity[] newArray(int size) {
                return new SubEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.sub);
    }

    public ItemMenuEntity() {
    }

    protected ItemMenuEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.sub = new ArrayList<SubEntity>();
        in.readList(this.sub, SubEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<ItemMenuEntity> CREATOR = new Parcelable.Creator<ItemMenuEntity>() {
        @Override
        public ItemMenuEntity createFromParcel(Parcel source) {
            return new ItemMenuEntity(source);
        }

        @Override
        public ItemMenuEntity[] newArray(int size) {
            return new ItemMenuEntity[size];
        }
    };
}

package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;

import com.huanmedia.hmalbumlib.extar.HM_PhotoEntity;

public  class PhpotsEntity implements HM_PhotoEntity {
        /**
         * photo : http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33.jpg
         * photo_thumb : http://mmimg.lzwifi.com/157/08/43/35/67/photos/73e976f4506543b616aa8c1ff5bd1f33_thumb.jpg
         * sort : 1
         */
        public int id;
        private String photo;
        private String photo_thumb;
        private int sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPhoto_thumb() {
            return photo_thumb;
        }

        public void setPhoto_thumb(String photo_thumb) {
            this.photo_thumb = photo_thumb;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("PhpotsEntity{");
            sb.append("id=").append(id);
            sb.append(", photo='").append(photo).append('\'');
            sb.append(", photo_thumb='").append(photo_thumb).append('\'');
            sb.append(", sort=").append(sort);
            sb.append('}');
            return sb.toString();
        }

        public PhpotsEntity() {
        }

        @Override
        public String getImage() {
            return photo;
        }

        @Override
        public String thumbnail() {
            return photo_thumb;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.photo);
            dest.writeString(this.photo_thumb);
            dest.writeInt(this.sort);
        }

        protected PhpotsEntity(Parcel in) {
            this.id = in.readInt();
            this.photo = in.readString();
            this.photo_thumb = in.readString();
            this.sort = in.readInt();
        }

        public static final Creator<PhpotsEntity> CREATOR = new Creator<PhpotsEntity>() {
            @Override
            public PhpotsEntity createFromParcel(Parcel source) {
                return new PhpotsEntity(source);
            }

            @Override
            public PhpotsEntity[] newArray(int size) {
                return new PhpotsEntity[size];
            }
        };
    }

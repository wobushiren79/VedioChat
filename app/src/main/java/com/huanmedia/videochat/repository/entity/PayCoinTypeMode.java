package com.huanmedia.videochat.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

public  class PayCoinTypeMode implements Parcelable {
        private String name;
        private String price;
        private int coin;
        private int extra;
        private String id;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("PayCoinTypeMode{");
            sb.append("name='").append(name).append('\'');
            sb.append(", price='").append(price).append('\'');
            sb.append(", coin='").append(coin).append('\'');
            sb.append(", extra='").append(extra).append('\'');
            sb.append(", id='").append(id).append('\'');
            sb.append('}');
            return sb.toString();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PayCoinTypeMode mode = (PayCoinTypeMode) o;

            if (getCoin() != mode.getCoin()) return false;
            if (getExtra() != mode.getExtra()) return false;
            if (getName() != null ? !getName().equals(mode.getName()) : mode.getName() != null)
                return false;
            if (getPrice() != null ? !getPrice().equals(mode.getPrice()) : mode.getPrice() != null)
                return false;
            return getId() != null ? getId().equals(mode.getId()) : mode.getId() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
            result = 31 * result + getCoin();
            result = 31 * result + getExtra();
            result = 31 * result + (getId() != null ? getId().hashCode() : 0);
            return result;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getExtra() {
            return extra;
        }

        public void setExtra(int extra) {
            this.extra = extra;
        }

        public PayCoinTypeMode() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.price);
            dest.writeInt(this.coin);
            dest.writeInt(this.extra);
            dest.writeString(this.id);
        }

        protected PayCoinTypeMode(Parcel in) {
            this.name = in.readString();
            this.price = in.readString();
            this.coin = in.readInt();
            this.extra = in.readInt();
            this.id = in.readString();
        }

        public static final Creator<PayCoinTypeMode> CREATOR = new Creator<PayCoinTypeMode>() {
            @Override
            public PayCoinTypeMode createFromParcel(Parcel source) {
                return new PayCoinTypeMode(source);
            }

            @Override
            public PayCoinTypeMode[] newArray(int size) {
                return new PayCoinTypeMode[size];
            }
        };
    }
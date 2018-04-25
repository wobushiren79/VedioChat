package com.huanmedia.hmalbumlib.extar;

import android.os.Parcel;


/**
 * @Description:
 * @author  yb email:yb498869020@hotmail.com
 * @date 2015-4-3 下午6:13:12 
 */

public class HM_ImgData implements HM_PhotoEntity {
	public String imgPath;//目标路径
	public Integer _ID=0;//id

	public HM_ImgData(String imgPath) {
		this.imgPath = imgPath;
	}

	public HM_ImgData() {
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("HM_ImgData{");
		sb.append("imgPath='").append(imgPath).append('\'');
		sb.append(", _ID=").append(_ID);
		sb.append('}');
		return sb.toString();
	}


	@Override
	public boolean equals(Object o) {
		if (o instanceof HM_ImgData){
			HM_ImgData oEnt = (HM_ImgData) o;
			if (oEnt._ID!=null && _ID!=null){
				return oEnt._ID.equals(_ID);
			}else {
				return false;
			}
		}
		return super.equals(o);
	}

	@Override
	public String getImage() {
		return imgPath ;
	}

	@Override
	public String thumbnail() {
		return "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.imgPath);
		dest.writeValue(this._ID);
	}

	protected HM_ImgData(Parcel in) {
		this.imgPath = in.readString();
		this._ID = (Integer) in.readValue(Integer.class.getClassLoader());
	}

	public static final Creator<HM_ImgData> CREATOR = new Creator<HM_ImgData>() {
		@Override
		public HM_ImgData createFromParcel(Parcel source) {
			return new HM_ImgData(source);
		}

		@Override
		public HM_ImgData[] newArray(int size) {
			return new HM_ImgData[size];
		}
	};
}

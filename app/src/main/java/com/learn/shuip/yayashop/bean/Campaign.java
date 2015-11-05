package com.learn.shuip.yayashop.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 15-10-14.
 */
public class Campaign implements Parcelable{

    private long id;
    private String title;
    private String imgUrl;

    protected Campaign(Parcel in) {
        id = in.readLong();
        title = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Campaign> CREATOR = new Creator<Campaign>() {
        @Override
        public Campaign createFromParcel(Parcel in) {
            return new Campaign(in);
        }

        @Override
        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(imgUrl);
    }
}

package com.tws.trevon.customwork;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemLatest implements Parcelable {

    private String LatestId;
    private String LatestCategoryId;
    private String LatestCategoryName;
    private String LatestVideoUrl;
    private String LatestVideoPlayId;
    private String LatestVideoName;
    private String LatestDuration;
    private String LatestDescription;
    private String LatestImageUrl;
    private String LatestVideoType;
    private String LatestVideoImgBig;
    private String LatestVideoRate;
    private String LatestVideoView;
    private String LatestPremium;

    public ItemLatest()
    {

    }

    protected ItemLatest(Parcel in) {
        LatestId = in.readString();
        LatestCategoryId = in.readString();
        LatestCategoryName = in.readString();
        LatestVideoUrl = in.readString();
        LatestVideoPlayId = in.readString();
        LatestVideoName = in.readString();
        LatestDuration = in.readString();
        LatestDescription = in.readString();
        LatestImageUrl = in.readString();
        LatestVideoType = in.readString();
        LatestVideoImgBig = in.readString();
        LatestVideoRate = in.readString();
        LatestVideoView = in.readString();
        LatestPremium = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LatestId);
        dest.writeString(LatestCategoryId);
        dest.writeString(LatestCategoryName);
        dest.writeString(LatestVideoUrl);
        dest.writeString(LatestVideoPlayId);
        dest.writeString(LatestVideoName);
        dest.writeString(LatestDuration);
        dest.writeString(LatestDescription);
        dest.writeString(LatestImageUrl);
        dest.writeString(LatestVideoType);
        dest.writeString(LatestVideoImgBig);
        dest.writeString(LatestVideoRate);
        dest.writeString(LatestVideoView);
        dest.writeString(LatestPremium);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemLatest> CREATOR = new Creator<ItemLatest>() {
        @Override
        public ItemLatest createFromParcel(Parcel in) {
            return new ItemLatest(in);
        }

        @Override
        public ItemLatest[] newArray(int size) {
            return new ItemLatest[size];
        }
    };

    public String getLatestId() {
        return LatestId;
    }

    public void setLatestId(String LatestId) {
        this.LatestId = LatestId;
    }

    public String getLatestCategoryId() {
        return LatestCategoryId;
    }

    public void setLatestCategoryId(String LatestCategoryId) {
        this.LatestCategoryId = LatestCategoryId;
    }

    public String getLatestCategoryName() {
        return LatestCategoryName;
    }

    public void setLatestCategoryName(String LatestCategoryName) {
        this.LatestCategoryName = LatestCategoryName;
    }

    public String getLatestVideoUrl() {
        return LatestVideoUrl;
    }

    public void setLatestVideoUrl(String LatestVideoUrl) {
        this.LatestVideoUrl = LatestVideoUrl;
    }

    public String getLatestVideoPlayId() {
        return LatestVideoPlayId;
    }

    public void setLatestVideoPlayId(String LatestVideoPlayId) {
        this.LatestVideoPlayId = LatestVideoPlayId;
    }

    public String getLatestVideoName() {
        return LatestVideoName;
    }

    public void setLatestVideoName(String LatestVideoName) {
        this.LatestVideoName = LatestVideoName;
    }

    public String getLatestDuration() {
        return LatestDuration;
    }

    public void setLatestDuration(String LatestDuration) {
        this.LatestDuration = LatestDuration;
    }

    public String getLatestDescription() {
        return LatestDescription;
    }

    public void setLatestDescription(String LatestDescription) {
        this.LatestDescription = LatestDescription;
    }

    public String getLatestImageUrl() {
        return LatestImageUrl;
    }

    public void setLatestImageUrl(String LatestImageUrl) {
        this.LatestImageUrl = LatestImageUrl;
    }

    public String getLatestVideoType() {
        return LatestVideoType;
    }

    public void setLatestVideoType(String LatestVideoType) {
        this.LatestVideoType = LatestVideoType;
    }

    public String getLatestVideoImgBig() {
        return LatestVideoImgBig;
    }

    public void setLatestVideoImgBig(String LatestVideoImgBig) {
        this.LatestVideoImgBig = LatestVideoImgBig;
    }

    public String getLatestVideoRate() {
        return LatestVideoRate;
    }

    public void setLatestVideoRate(String LatestVideoRate) {
        this.LatestVideoRate = LatestVideoRate;
    }

    public String getLatestVideoView() {
        return LatestVideoView;
    }

    public void setLatestVideoView(String LatestVideoView) {
        this.LatestVideoView = LatestVideoView;
    }

    public String getLatestPremium() {
        return LatestPremium;
    }

    public void setLatestPremium(String latestPremium) {
        LatestPremium = latestPremium;
    }

}


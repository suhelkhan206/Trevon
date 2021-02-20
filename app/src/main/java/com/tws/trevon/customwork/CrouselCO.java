package com.tws.trevon.customwork;

import android.os.Parcel;
import android.os.Parcelable;

public class CrouselCO implements Parcelable {
    public String id;

    protected CrouselCO(Parcel in) {
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CrouselCO> CREATOR = new Creator<CrouselCO>() {
        @Override
        public CrouselCO createFromParcel(Parcel in) {
            return new CrouselCO(in);
        }

        @Override
        public CrouselCO[] newArray(int size) {
            return new CrouselCO[size];
        }
    };
}

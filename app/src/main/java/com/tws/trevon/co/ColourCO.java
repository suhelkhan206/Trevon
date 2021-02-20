package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class ColourCO implements Parcelable {
    public String colour;
    public String isChecked;


    protected ColourCO(Parcel in) {
        colour = in.readString();
        isChecked = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(colour);
        dest.writeString(isChecked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColourCO> CREATOR = new Creator<ColourCO>() {
        @Override
        public ColourCO createFromParcel(Parcel in) {
            return new ColourCO(in);
        }

        @Override
        public ColourCO[] newArray(int size) {
            return new ColourCO[size];
        }
    };
}

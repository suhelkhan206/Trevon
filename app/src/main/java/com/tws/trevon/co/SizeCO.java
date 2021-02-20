package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class SizeCO implements Parcelable
{
    public String product_size;
    public String isChecked;


    protected SizeCO(Parcel in) {
        product_size = in.readString();
        isChecked = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_size);
        dest.writeString(isChecked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SizeCO> CREATOR = new Creator<SizeCO>() {
        @Override
        public SizeCO createFromParcel(Parcel in) {
            return new SizeCO(in);
        }

        @Override
        public SizeCO[] newArray(int size) {
            return new SizeCO[size];
        }
    };
}

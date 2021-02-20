package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandsCO implements Parcelable
{
    public String brand_name;
    public String brand_id;
    public String isChecked;


    protected BrandsCO(Parcel in) {
        brand_name = in.readString();
        brand_id = in.readString();
        isChecked = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand_name);
        dest.writeString(brand_id);
        dest.writeString(isChecked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandsCO> CREATOR = new Creator<BrandsCO>() {
        @Override
        public BrandsCO createFromParcel(Parcel in) {
            return new BrandsCO(in);
        }

        @Override
        public BrandsCO[] newArray(int size) {
            return new BrandsCO[size];
        }
    };
}

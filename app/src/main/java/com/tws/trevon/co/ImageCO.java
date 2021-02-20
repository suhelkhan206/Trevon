package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageCO implements Parcelable
{
    public String url;
    public String type;

    public ImageCO()
    {

    }

    public ImageCO(Parcel in) {
        url = in.readString();
        type = in.readString();
    }

    public static final Creator<ImageCO> CREATOR = new Creator<ImageCO>() {
        @Override
        public ImageCO createFromParcel(Parcel in) {
            return new ImageCO(in);
        }

        @Override
        public ImageCO[] newArray(int size) {
            return new ImageCO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(type);
    }
}

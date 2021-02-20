package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class RatngCO implements Parcelable
{
    public String id;
    public String star;
    public String comment;
    public String image;
    public String image1;
    public String image2;
    public String user_id;
    public String product_id;
    public String status;
    public String created_at;
    public String updated_at;
    public String options;

    protected RatngCO(Parcel in) {
        id = in.readString();
        star = in.readString();
        comment = in.readString();
        image = in.readString();
        image1 = in.readString();
        image2 = in.readString();
        user_id = in.readString();
        product_id = in.readString();
        status = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        options = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(star);
        dest.writeString(comment);
        dest.writeString(image);
        dest.writeString(image1);
        dest.writeString(image2);
        dest.writeString(user_id);
        dest.writeString(product_id);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(options);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RatngCO> CREATOR = new Creator<RatngCO>() {
        @Override
        public RatngCO createFromParcel(Parcel in) {
            return new RatngCO(in);
        }

        @Override
        public RatngCO[] newArray(int size) {
            return new RatngCO[size];
        }
    };
}

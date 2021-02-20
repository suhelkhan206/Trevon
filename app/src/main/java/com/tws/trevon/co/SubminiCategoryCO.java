package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class SubminiCategoryCO  implements Parcelable
{
    public String id;
    public String category_id;
    public String subcategory_id;
    public String subminicategory_name;
    public String image;
    public String status;
    public String is_featured;


    protected SubminiCategoryCO(Parcel in) {
        id = in.readString();
        category_id = in.readString();
        subcategory_id = in.readString();
        subminicategory_name = in.readString();
        image = in.readString();
        status = in.readString();
        is_featured = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(category_id);
        dest.writeString(subcategory_id);
        dest.writeString(subminicategory_name);
        dest.writeString(image);
        dest.writeString(status);
        dest.writeString(is_featured);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubminiCategoryCO> CREATOR = new Creator<SubminiCategoryCO>() {
        @Override
        public SubminiCategoryCO createFromParcel(Parcel in) {
            return new SubminiCategoryCO(in);
        }

        @Override
        public SubminiCategoryCO[] newArray(int size) {
            return new SubminiCategoryCO[size];
        }
    };
}

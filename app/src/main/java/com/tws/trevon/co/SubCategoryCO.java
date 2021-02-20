package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryCO implements Parcelable {
    public String subcat_id;
    public String categoryid;
    public String sub_category;
    public String image;
    public String status;
    public String created_at;
    public String updated_at;
    public List<SubminiCategoryCO> mimi_category;
    public boolean isVisible = false;



    public  SubCategoryCO()
    {
        mimi_category = new ArrayList<>();
    }

    protected SubCategoryCO(Parcel in) {
        subcat_id = in.readString();
        categoryid = in.readString();
        sub_category = in.readString();
        image = in.readString();
        status = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        mimi_category = in.createTypedArrayList(SubminiCategoryCO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subcat_id);
        dest.writeString(categoryid);
        dest.writeString(sub_category);
        dest.writeString(image);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeTypedList(mimi_category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryCO> CREATOR = new Creator<SubCategoryCO>() {
        @Override
        public SubCategoryCO createFromParcel(Parcel in) {
            return new SubCategoryCO(in);
        }

        @Override
        public SubCategoryCO[] newArray(int size) {
            return new SubCategoryCO[size];
        }
    };
}

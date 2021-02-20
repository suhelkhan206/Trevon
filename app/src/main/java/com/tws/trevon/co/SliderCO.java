package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class SliderCO implements Parcelable
{
    public String id;
    public String title;
    public String type;
    public String category_id;
    public String description;
    public String image;
    public String redirect_link;
    public String location;
    public String status;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;
    public String product_id;



    public SliderCO()
    {

    }


    protected SliderCO(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
        category_id = in.readString();
        description = in.readString();
        image = in.readString();
        redirect_link = in.readString();
        location = in.readString();
        status = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(category_id);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(redirect_link);
        dest.writeString(location);
        dest.writeString(status);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SliderCO> CREATOR = new Creator<SliderCO>() {
        @Override
        public SliderCO createFromParcel(Parcel in) {
            return new SliderCO(in);
        }

        @Override
        public SliderCO[] newArray(int size) {
            return new SliderCO[size];
        }
    };
}

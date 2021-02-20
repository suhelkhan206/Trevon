package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CategoryCO  implements Parcelable
{
    public String id;
    public String title;
    public String  description;
    public String post_type;
    public String slug;
    public String parent_id;
    public String image;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;
    public String status;

    public List<CategoryCO> subcats;

    public boolean isVisible = false;
    public String bgColor;

   public CategoryCO()
   {
       subcats = new ArrayList<>();
   }


    protected CategoryCO(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        post_type = in.readString();
        slug = in.readString();
        parent_id = in.readString();
        image = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
        status = in.readString();
        subcats = in.createTypedArrayList(CategoryCO.CREATOR);
        isVisible = in.readByte() != 0;
        bgColor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(post_type);
        dest.writeString(slug);
        dest.writeString(parent_id);
        dest.writeString(image);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
        dest.writeString(status);
        dest.writeTypedList(subcats);
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeString(bgColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryCO> CREATOR = new Creator<CategoryCO>() {
        @Override
        public CategoryCO createFromParcel(Parcel in) {
            return new CategoryCO(in);
        }

        @Override
        public CategoryCO[] newArray(int size) {
            return new CategoryCO[size];
        }
    };
}

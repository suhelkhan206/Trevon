package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterValueCO  implements Parcelable
{
    public String id;
    public String title;
    public  boolean isChecked;

    public FilterValueCO()
    {

    }


    protected FilterValueCO(Parcel in) {
        id = in.readString();
        title = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterValueCO> CREATOR = new Creator<FilterValueCO>() {
        @Override
        public FilterValueCO createFromParcel(Parcel in) {
            return new FilterValueCO(in);
        }

        @Override
        public FilterValueCO[] newArray(int size) {
            return new FilterValueCO[size];
        }
    };
}

package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FilterCO<T> implements Parcelable
{
    public String filter_title;
    public ArrayList<T> FilterValueList;

    public FilterCO()
    {

    }

    public FilterCO(Parcel in) {
        filter_title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filter_title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterCO> CREATOR = new Creator<FilterCO>() {
        @Override
        public FilterCO createFromParcel(Parcel in) {
            return new FilterCO(in);
        }

        @Override
        public FilterCO[] newArray(int size) {
            return new FilterCO[size];
        }
    };
}


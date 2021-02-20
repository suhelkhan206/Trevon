package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ServerFillterCO implements Parcelable
{
    public String filter_id;
    public List<String> filter_value_id_list;

    public ServerFillterCO()
    {

        filter_value_id_list = new ArrayList<>();
    }

    protected ServerFillterCO(Parcel in) {
        filter_id = in.readString();
        filter_value_id_list = in.createStringArrayList();
    }

    public static final Creator<ServerFillterCO> CREATOR = new Creator<ServerFillterCO>() {
        @Override
        public ServerFillterCO createFromParcel(Parcel in) {
            return new ServerFillterCO(in);
        }

        @Override
        public ServerFillterCO[] newArray(int size) {
            return new ServerFillterCO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filter_id);
        dest.writeStringList(filter_value_id_list);
    }
}

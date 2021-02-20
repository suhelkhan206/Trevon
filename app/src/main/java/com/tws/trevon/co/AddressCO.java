package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressCO implements Parcelable {

    public String id;
    public String state_id;
    public String name;
    public String status;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String  modified_date;

    protected AddressCO(Parcel in) {
        id = in.readString();
        state_id = in.readString();
        name = in.readString();
        status = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(state_id);
        dest.writeString(name);
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

    public static final Creator<AddressCO> CREATOR = new Creator<AddressCO>() {
        @Override
        public AddressCO createFromParcel(Parcel in) {
            return new AddressCO(in);
        }

        @Override
        public AddressCO[] newArray(int size) {
            return new AddressCO[size];
        }
    };
}

package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class PinCodeResponseCO implements Parcelable {

    public String id;
    public String pincode;
    public String courier_company_name;
    public String city;
    public String state;
    public String country;
    public String cod;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;

public PinCodeResponseCO()
{
    country = "India";
}

    protected PinCodeResponseCO(Parcel in) {
        id = in.readString();
        pincode = in.readString();
        courier_company_name = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        cod = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pincode);
        dest.writeString(courier_company_name);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(cod);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PinCodeResponseCO> CREATOR = new Creator<PinCodeResponseCO>() {
        @Override
        public PinCodeResponseCO createFromParcel(Parcel in) {
            return new PinCodeResponseCO(in);
        }

        @Override
        public PinCodeResponseCO[] newArray(int size) {
            return new PinCodeResponseCO[size];
        }
    };
}

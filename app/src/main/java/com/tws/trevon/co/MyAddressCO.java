package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.common.AppValidate;

public class MyAddressCO implements Parcelable {

    public String phone_no;
    public String city;
    public String state;
    public String country;
    public String pincode;
    public String user_id;
    public String name;
    public String area;
    public String id;
    public String house_no;
    public String disable = "0";
    public String is_default = "0";


    public MyAddressCO()
    {

    }


    protected MyAddressCO(Parcel in) {
        phone_no = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        pincode = in.readString();
        user_id = in.readString();
        name = in.readString();
        area = in.readString();
        id = in.readString();
        house_no = in.readString();
        disable = in.readString();
        is_default = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone_no);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(pincode);
        dest.writeString(user_id);
        dest.writeString(name);
        dest.writeString(area);
        dest.writeString(id);
        dest.writeString(house_no);
        dest.writeString(disable);
        dest.writeString(is_default);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyAddressCO> CREATOR = new Creator<MyAddressCO>() {
        @Override
        public MyAddressCO createFromParcel(Parcel in) {
            return new MyAddressCO(in);
        }

        @Override
        public MyAddressCO[] newArray(int size) {
            return new MyAddressCO[size];
        }
    };

    public static String getFullName(String first_name, String last_name)
    {
        first_name=AppValidate.isNotEmpty(first_name)? first_name : "";
        last_name = AppValidate.isNotEmpty(last_name) ? last_name : "";

        return (first_name + " " + last_name).replace("  ", " ");
    }

    public static String getFullAddress(
    String hno, String street, String city, String state,
    String country, String mobile, String pincode)
    {
       String address = "";
       if(AppValidate.isNotEmpty(hno))
       {
           address = address+hno+", "+"";
       }
        if(AppValidate.isNotEmpty(street))
       {
           address = address+street+", ";
       }
        if(AppValidate.isNotEmpty(city))
       {
           address = address+city+", "+"";
       }
        if(AppValidate.isNotEmpty(state))
       {
           address = address+state+", ";
       }
        if(AppValidate.isNotEmpty(country))
       {
           address = address+country+", "+"";
       }

        if(AppValidate.isNotEmpty(pincode))
       {
           address = address+pincode+", ";
       }
        if(AppValidate.isNotEmpty(mobile))
        {
            address = address+mobile;
        }
        return address;
    }
}

package com.tws.trevon.co;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chandra.kalwar on 8/19/2015.
 */

public class CustomDialogCO implements Parcelable
{
    public String title;
    public String message;
    public String okButtonTitle;
    public String cancelButtonTitle;
    public String okUrl;
    public String duration;
    public Boolean autoPopup;
    public Boolean cancelRequired = Boolean.TRUE;

    public CustomDialogCO() {
    }

    protected CustomDialogCO(Parcel in)
    {
        title = in.readString();
        message = in.readString();
        okButtonTitle = in.readString();
        cancelButtonTitle = in.readString();
        okUrl = in.readString();
        duration = in.readString();
        autoPopup = Boolean.valueOf(in.readString());
        cancelRequired = Boolean.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(okButtonTitle);
        dest.writeString(cancelButtonTitle);
        dest.writeString(okUrl);
        dest.writeString(duration);
        dest.writeString(autoPopup.toString());
        dest.writeString(cancelRequired.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomDialogCO> CREATOR = new Creator<CustomDialogCO>()
    {
        @Override
        public CustomDialogCO createFromParcel(Parcel in) {
            return new CustomDialogCO(in);
        }

        @Override
        public CustomDialogCO[] newArray(int size) {
            return new CustomDialogCO[size];
        }
    };
}

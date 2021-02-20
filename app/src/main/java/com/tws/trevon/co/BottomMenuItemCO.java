package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.constants.IConstants;


/**
 * Created by chandra.kalwar on 8/19/2015.
 */

public class BottomMenuItemCO implements Parcelable
{
    public String code;
    public int iconRes;
    public int nameRes;
    public String nameText;
    public Boolean isSelected = Boolean.FALSE;

    public BottomMenuItemCO(String code, int icon, int name)
    {
        this.code = code;
        this.iconRes = icon;
        this.nameRes = name;
    }

    public BottomMenuItemCO(String code, int icon, String nameText)
    {
        this.code = code;
        this.iconRes = icon;
        this.nameText = nameText;
        this.nameRes = IConstants.EMPTY_RESOURCE_ID;
    }

    public BottomMenuItemCO(String code, int icon, String nameText, Boolean isSelected)
    {
        this.code = code;
        this.iconRes = icon;
        this.nameText = nameText;
        this.nameRes = IConstants.EMPTY_RESOURCE_ID;
        this.isSelected = isSelected;
    }

    // Parcelling part
    public BottomMenuItemCO(Parcel in)
    {
        code = in.readString();
        iconRes = in.readInt();
        nameRes = in.readInt();
        nameText = in.readString();
        isSelected = Boolean.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(code);
        dest.writeInt(iconRes);
        dest.writeInt(nameRes);
        dest.writeString(nameText);
        dest.writeString(isSelected.toString());
    }

    public static final Creator CREATOR = new Creator() {
        public BottomMenuItemCO createFromParcel(Parcel in) {
            return new BottomMenuItemCO(in);
        }

        public BottomMenuItemCO[] newArray(int size) {
            return new BottomMenuItemCO[size];
        }
    };
}

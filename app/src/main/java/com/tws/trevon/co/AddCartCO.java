package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AddCartCO implements Parcelable
{
    public String productId;
    public String sellerType;
    public List<StockCO> stock;

    public AddCartCO()
    {
        stock = new ArrayList<>();
    }


    protected AddCartCO(Parcel in) {
        productId = in.readString();
        sellerType = in.readString();
        stock = in.createTypedArrayList(StockCO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(sellerType);
        dest.writeTypedList(stock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddCartCO> CREATOR = new Creator<AddCartCO>() {
        @Override
        public AddCartCO createFromParcel(Parcel in) {
            return new AddCartCO(in);
        }

        @Override
        public AddCartCO[] newArray(int size) {
            return new AddCartCO[size];
        }
    };
}

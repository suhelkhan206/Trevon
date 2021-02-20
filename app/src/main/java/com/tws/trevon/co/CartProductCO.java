package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CartProductCO implements Parcelable
{
    public String id;
    public String product_id;
    public String total_quantity;
    public ProductCO product;


    public CartProductCO() {

    }


    protected CartProductCO(Parcel in) {
        id = in.readString();
        product_id = in.readString();
        total_quantity = in.readString();
        product = in.readParcelable(ProductCO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(product_id);
        dest.writeString(total_quantity);
        dest.writeParcelable(product, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartProductCO> CREATOR = new Creator<CartProductCO>() {
        @Override
        public CartProductCO createFromParcel(Parcel in) {
            return new CartProductCO(in);
        }

        @Override
        public CartProductCO[] newArray(int size) {
            return new CartProductCO[size];
        }
    };
}

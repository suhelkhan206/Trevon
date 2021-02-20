package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WishProductCO implements Parcelable
{
    public String id;
    public String user_id;
    public String product_id;
    public ProductCO product;

    protected WishProductCO(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        product_id = in.readString();
        product = in.readParcelable(ProductCO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(product_id);
        dest.writeParcelable(product, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WishProductCO> CREATOR = new Creator<WishProductCO>() {
        @Override
        public WishProductCO createFromParcel(Parcel in) {
            return new WishProductCO(in);
        }

        @Override
        public WishProductCO[] newArray(int size) {
            return new WishProductCO[size];
        }
    };
}

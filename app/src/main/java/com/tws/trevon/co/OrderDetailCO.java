package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDetailCO implements Parcelable {
    public String id;
    public String order_id;
    public String product_id;
    public String size;
    public String color;
    public String quantity;
    public String price;
    public ProductCO product;

    protected OrderDetailCO(Parcel in) {
        id = in.readString();
        order_id = in.readString();
        product_id = in.readString();
        size = in.readString();
        color = in.readString();
        quantity = in.readString();
        price = in.readString();
        product = in.readParcelable(ProductCO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(order_id);
        dest.writeString(product_id);
        dest.writeString(size);
        dest.writeString(color);
        dest.writeString(quantity);
        dest.writeString(price);
        dest.writeParcelable(product, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailCO> CREATOR = new Creator<OrderDetailCO>() {
        @Override
        public OrderDetailCO createFromParcel(Parcel in) {
            return new OrderDetailCO(in);
        }

        @Override
        public OrderDetailCO[] newArray(int size) {
            return new OrderDetailCO[size];
        }
    };
}

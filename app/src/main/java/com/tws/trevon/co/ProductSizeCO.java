package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductSizeCO implements Parcelable
{
    public String id;
    public String merchant_id;
    public String product_id;
    public String product_size;
    public String color;
    public String stock;
    public String created_at;
    public boolean isSelect = false;

    protected ProductSizeCO(Parcel in) {
        id = in.readString();
        merchant_id = in.readString();
        product_id = in.readString();
        product_size = in.readString();
        color = in.readString();
        stock = in.readString();
        created_at = in.readString();
        isSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(merchant_id);
        dest.writeString(product_id);
        dest.writeString(product_size);
        dest.writeString(color);
        dest.writeString(stock);
        dest.writeString(created_at);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductSizeCO> CREATOR = new Creator<ProductSizeCO>() {
        @Override
        public ProductSizeCO createFromParcel(Parcel in) {
            return new ProductSizeCO(in);
        }

        @Override
        public ProductSizeCO[] newArray(int size) {
            return new ProductSizeCO[size];
        }
    };
}

package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductFilterCO implements Parcelable
{
    public String id;
    public String key_id;
    public String value_id;
    public String product_id;
    public String category_id;
    public String slider_id;
    public String filter_title;
    public String filter_value_title;

    protected ProductFilterCO(Parcel in) {
        id = in.readString();
        key_id = in.readString();
        value_id = in.readString();
        product_id = in.readString();
        category_id = in.readString();
        slider_id = in.readString();
        filter_title = in.readString();
        filter_value_title = in.readString();
    }

    public static final Creator<ProductFilterCO> CREATOR = new Creator<ProductFilterCO>() {
        @Override
        public ProductFilterCO createFromParcel(Parcel in) {
            return new ProductFilterCO(in);
        }

        @Override
        public ProductFilterCO[] newArray(int size) {
            return new ProductFilterCO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key_id);
        dest.writeString(value_id);
        dest.writeString(product_id);
        dest.writeString(category_id);
        dest.writeString(slider_id);
        dest.writeString(filter_title);
        dest.writeString(filter_value_title);
    }
}

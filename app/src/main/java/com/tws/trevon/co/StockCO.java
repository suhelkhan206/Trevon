package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class StockCO implements Parcelable {
    public String id;
    public String product_id;
    public String product_size;
    public String color;
    public String stock;
    public String created_at;
    public String quantity="0";


    public StockCO()
    {
    }


    protected StockCO(Parcel in) {
        id = in.readString();
        product_id = in.readString();
        product_size = in.readString();
        color = in.readString();
        stock = in.readString();
        created_at = in.readString();
        quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(product_id);
        dest.writeString(product_size);
        dest.writeString(color);
        dest.writeString(stock);
        dest.writeString(created_at);
        dest.writeString(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StockCO> CREATOR = new Creator<StockCO>() {
        @Override
        public StockCO createFromParcel(Parcel in) {
            return new StockCO(in);
        }

        @Override
        public StockCO[] newArray(int size) {
            return new StockCO[size];
        }
    };
}

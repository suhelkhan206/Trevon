package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CheckoutCO implements Parcelable
{
    public String totalMrp;
    public String gst;
    public String totalPrice;
    public String discount;
    public String deliveryCharge;
    public String user_id;
    public String payment_mode;
    public String address_id;
    public String order_no;
    public List<CartProductCO> cartProductCOList;

    public CheckoutCO()
    {

    }


    protected CheckoutCO(Parcel in) {
        totalMrp = in.readString();
        gst = in.readString();
        totalPrice = in.readString();
        discount = in.readString();
        deliveryCharge = in.readString();
        user_id = in.readString();
        payment_mode = in.readString();
        address_id = in.readString();
        order_no = in.readString();
        cartProductCOList = in.createTypedArrayList(CartProductCO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalMrp);
        dest.writeString(gst);
        dest.writeString(totalPrice);
        dest.writeString(discount);
        dest.writeString(deliveryCharge);
        dest.writeString(user_id);
        dest.writeString(payment_mode);
        dest.writeString(address_id);
        dest.writeString(order_no);
        dest.writeTypedList(cartProductCOList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckoutCO> CREATOR = new Creator<CheckoutCO>() {
        @Override
        public CheckoutCO createFromParcel(Parcel in) {
            return new CheckoutCO(in);
        }

        @Override
        public CheckoutCO[] newArray(int size) {
            return new CheckoutCO[size];
        }
    };
}

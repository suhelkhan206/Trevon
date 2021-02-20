package com.tws.trevon.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.co.OrderDetailCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.ProductSizeCO;

import java.util.ArrayList;
import java.util.List;

public class OrderCO implements Parcelable
{
    public String id;
    public String order_group_id;
    public String gst_amount;
    public String user_id;
    public String coupon_id;
    public String address_id;
    public String total_quantity;
    public String total_discount;
    public String total_addition;
    public String total_amount;
    public String total_paid;
    public String received_date;
    public String remark;
    public String payment_mode;
    public String payment_status;
    public String delivery_status;
    public String transaction_id;
    public String transaction_msg;
    public String user_ip;
    public String web_token_id;
    public String app_token_id;
    public String device_type;
    public String browser;
    public String browser_version;
    public String os;
    public String mobile_device;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;
    public int order_position;
    public List<OrderDetailCO> order_detail;
    public String invoice_url;


    public OrderCO() {
        order_detail = new ArrayList<>();
    }


    protected OrderCO(Parcel in) {
        id = in.readString();
        order_group_id = in.readString();
        gst_amount = in.readString();
        user_id = in.readString();
        coupon_id = in.readString();
        address_id = in.readString();
        total_quantity = in.readString();
        total_discount = in.readString();
        total_addition = in.readString();
        total_amount = in.readString();
        total_paid = in.readString();
        received_date = in.readString();
        remark = in.readString();
        payment_mode = in.readString();
        payment_status = in.readString();
        delivery_status = in.readString();
        transaction_id = in.readString();
        transaction_msg = in.readString();
        user_ip = in.readString();
        web_token_id = in.readString();
        app_token_id = in.readString();
        device_type = in.readString();
        browser = in.readString();
        browser_version = in.readString();
        os = in.readString();
        mobile_device = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
        order_position = in.readInt();
        order_detail = in.createTypedArrayList(OrderDetailCO.CREATOR);
        invoice_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(order_group_id);
        dest.writeString(gst_amount);
        dest.writeString(user_id);
        dest.writeString(coupon_id);
        dest.writeString(address_id);
        dest.writeString(total_quantity);
        dest.writeString(total_discount);
        dest.writeString(total_addition);
        dest.writeString(total_amount);
        dest.writeString(total_paid);
        dest.writeString(received_date);
        dest.writeString(remark);
        dest.writeString(payment_mode);
        dest.writeString(payment_status);
        dest.writeString(delivery_status);
        dest.writeString(transaction_id);
        dest.writeString(transaction_msg);
        dest.writeString(user_ip);
        dest.writeString(web_token_id);
        dest.writeString(app_token_id);
        dest.writeString(device_type);
        dest.writeString(browser);
        dest.writeString(browser_version);
        dest.writeString(os);
        dest.writeString(mobile_device);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
        dest.writeInt(order_position);
        dest.writeTypedList(order_detail);
        dest.writeString(invoice_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderCO> CREATOR = new Creator<OrderCO>() {
        @Override
        public OrderCO createFromParcel(Parcel in) {
            return new OrderCO(in);
        }

        @Override
        public OrderCO[] newArray(int size) {
            return new OrderCO[size];
        }
    };
}

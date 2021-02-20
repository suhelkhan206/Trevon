package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class EarningCO implements Parcelable
{
    public String id;
    public String user_id;
    public String reason;
    public String credit_debit;
    public String amount;
    public String order_id;
    public String connection_id;
    public String created_date;
    public String username;
    public String earning;


    protected EarningCO(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        reason = in.readString();
        credit_debit = in.readString();
        amount = in.readString();
        order_id = in.readString();
        connection_id = in.readString();
        created_date = in.readString();
        username = in.readString();
        earning = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(reason);
        dest.writeString(credit_debit);
        dest.writeString(amount);
        dest.writeString(order_id);
        dest.writeString(connection_id);
        dest.writeString(created_date);
        dest.writeString(username);
        dest.writeString(earning);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EarningCO> CREATOR = new Creator<EarningCO>() {
        @Override
        public EarningCO createFromParcel(Parcel in) {
            return new EarningCO(in);
        }

        @Override
        public EarningCO[] newArray(int size) {
            return new EarningCO[size];
        }
    };
}

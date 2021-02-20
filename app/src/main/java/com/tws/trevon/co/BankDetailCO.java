package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class BankDetailCO implements Parcelable {

    public String bank_name;
    public String mobile;
    public String account_holder_name;
    public String account_no;
    public String confirm_accountno;
    public String ifsc_code;
    public String phonePe;
    public String phonepe;
    public String amazon_pay;
    public String google_pay;
    public String paytm;


    protected BankDetailCO(Parcel in) {
        bank_name = in.readString();
        mobile = in.readString();
        account_holder_name = in.readString();
        account_no = in.readString();
        confirm_accountno = in.readString();
        ifsc_code = in.readString();
        phonePe = in.readString();
        phonepe = in.readString();
        amazon_pay = in.readString();
        google_pay = in.readString();
        paytm = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bank_name);
        dest.writeString(mobile);
        dest.writeString(account_holder_name);
        dest.writeString(account_no);
        dest.writeString(confirm_accountno);
        dest.writeString(ifsc_code);
        dest.writeString(phonePe);
        dest.writeString(phonepe);
        dest.writeString(amazon_pay);
        dest.writeString(google_pay);
        dest.writeString(paytm);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankDetailCO> CREATOR = new Creator<BankDetailCO>() {
        @Override
        public BankDetailCO createFromParcel(Parcel in) {
            return new BankDetailCO(in);
        }

        @Override
        public BankDetailCO[] newArray(int size) {
            return new BankDetailCO[size];
        }
    };
}

package com.tws.trevon.co;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.common.AppController;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;

public class UserCO implements Parcelable {

    public String id;
    public String user_ip;
    public String username;
    public String slug;
    public String email;
    public String mobile;
    public String password;
    public String first_name;
    public String last_name;
    public String profile_pic;
    public String requirement;
    public String comment;
    public String lead_from;
    public String followup_date;
    public String is_pinned;
    public String dob;
    public String about;
    public String gender;
    public String status;
    public String parent_id;
    public String email_verified;
    public String mobile_verified;
    public String google_id;
    public String fb_id;
    public String address_id;
    public String ref_id;
    public String pincode;
    public String doc_type;
    public String doc_no;
    public String firm_type;
    public String address;
    public String gst_no;
    public String bank_name;
    public String account_holder_name;
    public String account_no;
    public String confirm_accountno;
    public String ifsc_code;
    public String phonePe;
    public String google_pay;
    public String paytm;
    public String user_type;
    public String role_id;
    public String web_token_id;
    public String app_token_id;
    public String last_login_device;
    public String device_type;
    public String browser;
    public String browser_version;
    public String os;
    public String mobile_device;
    public String last_login_date;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;
    public String image;
    public String passbook_image;
    public String otp;

    public String customer_support;
    public String web_about;
    public String web_term_conditions;
    public String web_refund_policy;
    public String is_registered;
    public String doc_front_image;
    public String doc_back_image;
    public String seller_type;
    public String is_premium;
    public String company_name;
    public String cart_count;
    public String wallet_amount;
    public String firm_address;
    public String gst_certificate;
    public String business_profile_image;
    public String business_mobile_no;
    public UserCO()
    {

    }


    protected UserCO(Parcel in) {
        id = in.readString();
        user_ip = in.readString();
        username = in.readString();
        slug = in.readString();
        email = in.readString();
        mobile = in.readString();
        password = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profile_pic = in.readString();
        requirement = in.readString();
        comment = in.readString();
        lead_from = in.readString();
        followup_date = in.readString();
        is_pinned = in.readString();
        dob = in.readString();
        about = in.readString();
        gender = in.readString();
        status = in.readString();
        parent_id = in.readString();
        email_verified = in.readString();
        mobile_verified = in.readString();
        google_id = in.readString();
        fb_id = in.readString();
        address_id = in.readString();
        ref_id = in.readString();
        pincode = in.readString();
        doc_type = in.readString();
        doc_no = in.readString();
        firm_type = in.readString();
        address = in.readString();
        gst_no = in.readString();
        bank_name = in.readString();
        account_holder_name = in.readString();
        account_no = in.readString();
        confirm_accountno = in.readString();
        ifsc_code = in.readString();
        phonePe = in.readString();
        google_pay = in.readString();
        paytm = in.readString();
        user_type = in.readString();
        role_id = in.readString();
        web_token_id = in.readString();
        app_token_id = in.readString();
        last_login_device = in.readString();
        device_type = in.readString();
        browser = in.readString();
        browser_version = in.readString();
        os = in.readString();
        mobile_device = in.readString();
        last_login_date = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
        image = in.readString();
        passbook_image = in.readString();
        otp = in.readString();
        customer_support = in.readString();
        web_about = in.readString();
        web_term_conditions = in.readString();
        web_refund_policy = in.readString();
        is_registered = in.readString();
        doc_front_image = in.readString();
        doc_back_image = in.readString();
        seller_type = in.readString();
        is_premium = in.readString();
        company_name = in.readString();
        cart_count = in.readString();
        wallet_amount = in.readString();
        firm_address = in.readString();
        gst_certificate = in.readString();
        business_profile_image = in.readString();
        business_mobile_no = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_ip);
        dest.writeString(username);
        dest.writeString(slug);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(password);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profile_pic);
        dest.writeString(requirement);
        dest.writeString(comment);
        dest.writeString(lead_from);
        dest.writeString(followup_date);
        dest.writeString(is_pinned);
        dest.writeString(dob);
        dest.writeString(about);
        dest.writeString(gender);
        dest.writeString(status);
        dest.writeString(parent_id);
        dest.writeString(email_verified);
        dest.writeString(mobile_verified);
        dest.writeString(google_id);
        dest.writeString(fb_id);
        dest.writeString(address_id);
        dest.writeString(ref_id);
        dest.writeString(pincode);
        dest.writeString(doc_type);
        dest.writeString(doc_no);
        dest.writeString(firm_type);
        dest.writeString(address);
        dest.writeString(gst_no);
        dest.writeString(bank_name);
        dest.writeString(account_holder_name);
        dest.writeString(account_no);
        dest.writeString(confirm_accountno);
        dest.writeString(ifsc_code);
        dest.writeString(phonePe);
        dest.writeString(google_pay);
        dest.writeString(paytm);
        dest.writeString(user_type);
        dest.writeString(role_id);
        dest.writeString(web_token_id);
        dest.writeString(app_token_id);
        dest.writeString(last_login_device);
        dest.writeString(device_type);
        dest.writeString(browser);
        dest.writeString(browser_version);
        dest.writeString(os);
        dest.writeString(mobile_device);
        dest.writeString(last_login_date);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
        dest.writeString(image);
        dest.writeString(passbook_image);
        dest.writeString(otp);
        dest.writeString(customer_support);
        dest.writeString(web_about);
        dest.writeString(web_term_conditions);
        dest.writeString(web_refund_policy);
        dest.writeString(is_registered);
        dest.writeString(doc_front_image);
        dest.writeString(doc_back_image);
        dest.writeString(seller_type);
        dest.writeString(is_premium);
        dest.writeString(company_name);
        dest.writeString(cart_count);
        dest.writeString(wallet_amount);
        dest.writeString(firm_address);
        dest.writeString(gst_certificate);
        dest.writeString(business_profile_image);
        dest.writeString(business_mobile_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserCO> CREATOR = new Creator<UserCO>() {
        @Override
        public UserCO createFromParcel(Parcel in) {
            return new UserCO(in);
        }

        @Override
        public UserCO[] newArray(int size) {
            return new UserCO[size];
        }
    };

    public static UserCO getUserCOFromDB()
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        UserCO userCO = new UserCO();

        userCO.id = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_ID, IConstants.EMPTY_STRING);
        userCO.email = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_EMAIL, IConstants.EMPTY_STRING);
        userCO.mobile = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_MOBILE, IConstants.EMPTY_STRING);
        userCO.username = sharedPreferences.getString(IReqParams.USER_NAME, IConstants.EMPTY_STRING);
        userCO.password = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PASSWORD, IConstants.EMPTY_STRING);
        userCO.image = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, IConstants.EMPTY_STRING);
        userCO.bank_name = sharedPreferences.getString(IReqParams.BANK_NAME, IConstants.EMPTY_STRING);
        userCO.account_holder_name = sharedPreferences.getString(IReqParams.ACCOUNT_HOLDER_NAME, IConstants.EMPTY_STRING);
        userCO.account_no = sharedPreferences.getString(IReqParams.ACCOUNT_NO, IConstants.EMPTY_STRING);
        userCO.ifsc_code = sharedPreferences.getString(IReqParams.IFSC_CODE, IConstants.EMPTY_STRING);
        userCO.phonePe = sharedPreferences.getString(IReqParams.PHONE_PAY, IConstants.EMPTY_STRING);
        userCO.google_pay = sharedPreferences.getString(IReqParams.GOOGLE_PAY, IConstants.EMPTY_STRING);
        userCO.paytm = sharedPreferences.getString(IReqParams.PAYTM, IConstants.EMPTY_STRING);
        userCO.otp = sharedPreferences.getString(IReqParams.OTP, IConstants.EMPTY_STRING);
        userCO.user_ip = sharedPreferences.getString(IReqParams.USER_IP, IConstants.EMPTY_STRING);
        userCO.slug = sharedPreferences.getString(IReqParams.SLUG, IConstants.EMPTY_STRING);
        userCO.first_name = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_FIRST_NAME, IConstants.EMPTY_STRING);
        userCO.last_name = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_LAST_NAME, IConstants.EMPTY_STRING);
        userCO.profile_pic = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, IConstants.EMPTY_STRING);
        userCO.requirement = sharedPreferences.getString(IReqParams.REQUIREMENT, IConstants.EMPTY_STRING);
        userCO.comment = sharedPreferences.getString(IReqParams.COMMENT, IConstants.EMPTY_STRING);
        userCO.lead_from = sharedPreferences.getString(IReqParams.LEAD_FROM, IConstants.EMPTY_STRING);
        userCO.followup_date = sharedPreferences.getString(IReqParams.FOLLOWUP_DATE, IConstants.EMPTY_STRING);
        userCO.is_pinned = sharedPreferences.getString(IReqParams.IS_PINNED, IConstants.EMPTY_STRING);
        userCO.dob = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_DOB, IConstants.EMPTY_STRING);
        userCO.about = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_ABOUT, IConstants.EMPTY_STRING);
        userCO.gender = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_GENDER, IConstants.EMPTY_STRING);

        userCO.status = sharedPreferences.getString(IReqParams.STATUS, IConstants.EMPTY_STRING);
        userCO.parent_id = sharedPreferences.getString(IReqParams.PARENT_ID, IConstants.EMPTY_STRING);
        userCO.email_verified = sharedPreferences.getString(IReqParams.EMAIL_VERIFIED, IConstants.EMPTY_STRING);
        userCO.mobile_verified = sharedPreferences.getString(IReqParams.MOBILE_VERIFIED, IConstants.EMPTY_STRING);

        userCO.google_id = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_GOOGLE_ID, IConstants.EMPTY_STRING);
        userCO.fb_id = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_FACEBOOK_ID, IConstants.EMPTY_STRING);
        userCO.address_id = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_DEFAULT_ADDRESS_ID, IConstants.EMPTY_STRING);
        userCO.ref_id = sharedPreferences.getString(IReqParams.REF_ID, IConstants.EMPTY_STRING);
        userCO.pincode = sharedPreferences.getString(IReqParams.PINCODE, IConstants.EMPTY_STRING);
        userCO.firm_type = sharedPreferences.getString(IReqParams.FIRM_TYPE, IConstants.EMPTY_STRING);
        userCO.address = sharedPreferences.getString(IReqParams.ADDRESS, IConstants.EMPTY_STRING);
        userCO.gst_no = sharedPreferences.getString(IReqParams.GST, IConstants.EMPTY_STRING);
        userCO.user_type = sharedPreferences.getString(IReqParams.USER_TYPE, IConstants.EMPTY_STRING);
        userCO.created_by = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_CREATED_BY, IConstants.EMPTY_STRING);
        userCO.created_date = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_CREATED_DATE, IConstants.EMPTY_STRING);
        userCO.modified_by = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_MODIFIED_BY, IConstants.EMPTY_STRING);
        userCO.modified_date = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_MODIFIED_DATE, IConstants.EMPTY_STRING);
        userCO.passbook_image = sharedPreferences.getString(IReqParams.PASSBOOK_IMAGE, IConstants.EMPTY_STRING);

        userCO.customer_support =  sharedPreferences.getString(IReqParams.CUSTOMER_SUPPORT, IConstants.EMPTY_STRING);
        userCO.web_about =  sharedPreferences.getString(IReqParams.WEB_ABOUT, IConstants.EMPTY_STRING);
        userCO.web_term_conditions =  sharedPreferences.getString(IReqParams.WEB_TERM_CONDITION, IConstants.EMPTY_STRING);
        userCO.web_refund_policy =  sharedPreferences.getString(IReqParams.WEB_REFUND_POLICY, IConstants.EMPTY_STRING);
        userCO.username =  sharedPreferences.getString(IReqParams.USER_NAME, IConstants.EMPTY_STRING);
        userCO.is_registered =  sharedPreferences.getString(IReqParams.IS_REGISTER, IConstants.EMPTY_STRING);
        userCO.doc_type =  sharedPreferences.getString(IReqParams.DOC_TYPE, IConstants.EMPTY_STRING);
        userCO.doc_no =  sharedPreferences.getString(IReqParams.DOC_NO, IConstants.EMPTY_STRING);

        userCO.doc_front_image =  sharedPreferences.getString(IReqParams.DOC_FRONT_IMAGE, IConstants.EMPTY_STRING);
        userCO.doc_back_image =  sharedPreferences.getString(IReqParams.DOC_BACK_IMAGE, IConstants.EMPTY_STRING);
        userCO.seller_type =  sharedPreferences.getString(IReqParams.SELLER_TYPE, IConstants.EMPTY_STRING);
        userCO.is_premium =  sharedPreferences.getString(IReqParams.IS_PREMIUM, IConstants.EMPTY_STRING);
        userCO.company_name =  sharedPreferences.getString(IReqParams.COMPANY_NAME, IConstants.EMPTY_STRING);
        userCO.cart_count =  sharedPreferences.getString(IReqParams.USER_CART_COUNT, IConstants.EMPTY_STRING);
        userCO.wallet_amount =  sharedPreferences.getString(IReqParams.WALLET_AMOUNT, IConstants.EMPTY_STRING);
        userCO.firm_address =  sharedPreferences.getString(IReqParams.FIRM_ADDRESS, IConstants.EMPTY_STRING);
        userCO.gst_certificate =  sharedPreferences.getString(IReqParams.GST_CERTIFICATE, IConstants.EMPTY_STRING);
        userCO.business_profile_image =  sharedPreferences.getString(IReqParams.BUSINESS_PROFILE_IMAGE, IConstants.EMPTY_STRING);
        userCO.business_mobile_no =  sharedPreferences.getString(IReqParams.BUSINESS_MOBILE_NUMBER, IConstants.EMPTY_STRING);

        return userCO;
    }

    public void saveInDB()
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IReqParams.LOGGED_IN_USER_ID, id);
        editor.putString(IReqParams.LOGGED_IN_USER_EMAIL, email);
        editor.putString(IReqParams.LOGGED_IN_USER_MOBILE, mobile);
        editor.putString(IReqParams.LOGGED_IN_USER_PASSWORD, password);
        editor.putString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, image);
        editor.putString(IReqParams.LOGGED_IN_USER_FIRST_NAME, username);
        //editor.putString(IReqParams.ACCESS_TOKEN, a);
        editor.putString(IReqParams.OTP, otp);

        editor.putString(IReqParams.BANK_NAME, bank_name);
        editor.putString(IReqParams.ACCOUNT_HOLDER_NAME, account_holder_name);
        editor.putString(IReqParams.ACCOUNT_NO, account_no);
        editor.putString(IReqParams.IFSC_CODE, ifsc_code);
        editor.putString(IReqParams.PHONE_PAY, phonePe);
        editor.putString(IReqParams.GOOGLE_PAY, google_pay);
        editor.putString(IReqParams.PAYTM, paytm);
        editor.putString(IReqParams.LOGGED_IN_USER_PASSWORD, password);
        editor.putString(IReqParams.OTP_m, otp);

        editor.putString(IReqParams.USER_IP, user_ip);
        editor.putString(IReqParams.SLUG, slug);
        editor.putString(IReqParams.LOGGED_IN_USER_FIRST_NAME, first_name);
        editor.putString(IReqParams.LOGGED_IN_USER_LAST_NAME, last_name);
        editor.putString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, profile_pic);


        editor.putString(IReqParams.REQUIREMENT, requirement);
        editor.putString(IReqParams.COMMENT, comment);
        editor.putString(IReqParams.LEAD_FROM, lead_from);
        editor.putString(IReqParams.FOLLOWUP_DATE, followup_date);

        editor.putString(IReqParams.IS_PINNED, is_pinned);
        editor.putString(IReqParams.LOGGED_IN_USER_DOB, dob);
        editor.putString(IReqParams.LOGGED_IN_USER_ABOUT, about);
        editor.putString(IReqParams.LOGGED_IN_USER_GENDER, gender);
        editor.putString(IReqParams.STATUS, status);
        editor.putString(IReqParams.PARENT_ID, parent_id);
        editor.putString(IReqParams.EMAIL_VERIFIED, email_verified);
        editor.putString(IReqParams.MOBILE_VERIFIED, mobile_verified);
        editor.putString(IReqParams.LOGGED_IN_USER_GOOGLE_ID, google_id);
        editor.putString(IReqParams.LOGGED_IN_USER_FACEBOOK_ID, fb_id);
        editor.putString(IReqParams.LOGGED_IN_USER_DEFAULT_ADDRESS_ID, address_id);
        editor.putString(IReqParams.REF_ID, ref_id);
        editor.putString(IReqParams.PINCODE, pincode);
        editor.putString(IReqParams.FIRM_TYPE, firm_type);
        editor.putString(IReqParams.ADDRESS, address);
        editor.putString(IReqParams.GST, gst_no);
        editor.putString(IReqParams.USER_TYPE, user_type);

        editor.putString(IReqParams.LOGGED_IN_USER_CREATED_BY, created_by);
        editor.putString(IReqParams.LOGGED_IN_USER_CREATED_DATE, created_date);
        editor.putString(IReqParams.LOGGED_IN_USER_MODIFIED_BY, modified_by);
        editor.putString(IReqParams.LOGGED_IN_USER_MODIFIED_DATE, modified_date);
        editor.putString(IReqParams.PASSBOOK_IMAGE, passbook_image);
        editor.putString(IReqParams.CUSTOMER_SUPPORT, customer_support);
        editor.putString(IReqParams.WEB_ABOUT, web_about);
        editor.putString(IReqParams.WEB_TERM_CONDITION, web_term_conditions);
        editor.putString(IReqParams.WEB_REFUND_POLICY, web_refund_policy);
        editor.putString(IReqParams.USER_NAME, username);
        editor.putString(IReqParams.IS_REGISTER, is_registered);
        editor.putString(IReqParams.DOC_TYPE, doc_type);
        editor.putString(IReqParams.DOC_NO, doc_no);

        editor.putString(IReqParams.DOC_FRONT_IMAGE, doc_front_image);
        editor.putString(IReqParams.DOC_BACK_IMAGE, doc_back_image);
        editor.putString(IReqParams.SELLER_TYPE, seller_type);
        editor.putString(IReqParams.IS_PREMIUM, is_premium);
        editor.putString(IReqParams.COMPANY_NAME, company_name);
        editor.putString(IReqParams.USER_CART_COUNT, cart_count);
        editor.putString(IReqParams.WALLET_AMOUNT, wallet_amount);
        editor.putString(IReqParams.FIRM_ADDRESS, firm_address);
        editor.putString(IReqParams.GST_CERTIFICATE, gst_certificate);
        editor.putString(IReqParams.BUSINESS_PROFILE_IMAGE, business_profile_image);
        editor.putString(IReqParams.BUSINESS_MOBILE_NUMBER, business_mobile_no);




        editor.commit();
    }

    public void deleteFromDB() {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(IReqParams.LOGGED_IN_USER_ID);
        editor.remove(IReqParams.LOGGED_IN_USER_EMAIL);
        editor.remove(IReqParams.LOGGED_IN_USER_MOBILE);
        editor.remove(IReqParams.LOGGED_IN_USER_FIRST_NAME);
        editor.remove(IReqParams.LOGGED_IN_USER_PASSWORD);
        editor.remove(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE);
        editor.remove(IReqParams.OTP);
        editor.remove(IReqParams.BANK_NAME);
        editor.remove(IReqParams.ACCOUNT_HOLDER_NAME);
        editor.remove(IReqParams.ACCOUNT_NO);
        editor.remove(IReqParams.IFSC_CODE);
        editor.remove(IReqParams.PHONE_PAY);
        editor.remove(IReqParams.GOOGLE_PAY);
        editor.remove(IReqParams.PAYTM);
        editor.remove(IReqParams.PAYMENT_DATE);
        editor.remove(IReqParams.PAYMENT_METHOD);
        editor.remove(IReqParams.CREDIT_AMOUNT);
        editor.remove(IReqParams.PENDING_AMOUNT);
        editor.remove(IReqParams.REG_DATE);
        editor.remove(IReqParams.CREATE_AT);
        editor.remove(IReqParams.IS_EXPIRED);
        editor.remove(IReqParams.LOGGED_IN_USER_PASSWORD);
        editor.remove(IReqParams.CUSTOMER_SUPPORT);
        editor.remove(IReqParams.WEB_ABOUT);
        editor.remove(IReqParams.WEB_TERM_CONDITION);
        editor.remove(IReqParams.WEB_REFUND_POLICY);
        editor.remove(IReqParams.OTP_m);
        editor.remove(IReqParams.WALLET_AMOUNT);


        editor.remove(IReqParams.USER_IP);
        editor.remove(IReqParams.SLUG);
        editor.remove(IReqParams.LOGGED_IN_USER_FIRST_NAME);
        editor.remove(IReqParams.LOGGED_IN_USER_LAST_NAME);
        editor.remove(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE);
        editor.remove(IReqParams.REQUIREMENT);
        editor.remove(IReqParams.COMMENT);
        editor.remove(IReqParams.LEAD_FROM);
        editor.remove(IReqParams.FOLLOWUP_DATE);
        editor.remove(IReqParams.IS_PINNED);
        editor.remove(IReqParams.LOGGED_IN_USER_DOB);
        editor.remove(IReqParams.LOGGED_IN_USER_ABOUT);
        editor.remove(IReqParams.LOGGED_IN_USER_GENDER);
        editor.remove(IReqParams.STATUS);
        editor.remove(IReqParams.PARENT_ID);
        editor.remove(IReqParams.EMAIL_VERIFIED);
        editor.remove(IReqParams.MOBILE_VERIFIED);
        editor.remove(IReqParams.LOGGED_IN_USER_GOOGLE_ID);
        editor.remove(IReqParams.LOGGED_IN_USER_FACEBOOK_ID);
        editor.remove(IReqParams.LOGGED_IN_USER_DEFAULT_ADDRESS_ID);
        editor.remove(IReqParams.REF_ID);
        editor.remove(IReqParams.PINCODE);
        editor.remove(IReqParams.FIRM_TYPE);
        editor.remove(IReqParams.ADDRESS);
        editor.remove(IReqParams.GST);

        editor.remove(IReqParams.LOGGED_IN_USER_CREATED_BY);
        editor.remove(IReqParams.LOGGED_IN_USER_CREATED_DATE);
        editor.remove(IReqParams.LOGGED_IN_USER_MODIFIED_BY);
        editor.remove(IReqParams.LOGGED_IN_USER_MODIFIED_DATE);
        editor.remove(IReqParams.PASSBOOK_IMAGE);

        editor.remove(IReqParams.CUSTOMER_SUPPORT);
        editor.remove(IReqParams.WEB_ABOUT);
        editor.remove(IReqParams.WEB_TERM_CONDITION);
        editor.remove(IReqParams.WEB_REFUND_POLICY);
        editor.remove(IReqParams.USER_NAME);
        editor.remove(IReqParams.IS_REGISTER);
        editor.remove(IReqParams.DOC_TYPE);
        editor.remove(IReqParams.DOC_NO);
        editor.remove(IReqParams.DOC_FRONT_IMAGE);
        editor.remove(IReqParams.DOC_BACK_IMAGE);
        editor.remove(IReqParams.SELLER_TYPE);
        editor.remove(IReqParams.IS_PREMIUM);
        editor.remove(IReqParams.COMPANY_NAME);
        editor.remove(IReqParams.USER_CART_COUNT);
        editor.remove(IReqParams.WALLET_AMOUNT);
        editor.remove(IReqParams.FIRM_ADDRESS);
        editor.remove(IReqParams.GST_CERTIFICATE);
        editor.remove(IReqParams.BUSINESS_PROFILE_IMAGE);
        editor.remove(IReqParams.BUSINESS_MOBILE_NUMBER);

        editor.commit();

    }

}

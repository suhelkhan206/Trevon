package com.tws.trevon.co;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.common.AppController;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;

public class User implements Parcelable
{
        public String id;
        public String firstName;
        public String lastName;
        public String loginId;
        public String password;
        public String accessToken;
        public String status;
        public String lastAccessedOn;
        public String profileImage;
        public String coApplicant;
        public String contactNo;
        public String address;
        public String formNo;
        public String propertyType;
        public String propertyNo;
        public String totalPayment;
        public String paymentDue;
        public String createdOn;
        public String modifiedOn;

	public User() {
	}

	public static User getUserCOFromDB()
	{
		SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

		User userCO = new User();
		userCO.id = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_ID, IConstants.EMPTY_STRING);
		/*userCO.firstName = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_FIRST_NAME, IConstants.EMPTY_STRING);
		userCO.lastName = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_LAST_NAME, IConstants.EMPTY_STRING);
		userCO.loginId = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_LOGIN_ID, IConstants.EMPTY_STRING);
		userCO.password = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PASSWORD, IConstants.EMPTY_STRING);
		userCO.accessToken = sharedPreferences.getString(IPreferences.API_ACCESS_TOKEN, IConstants.EMPTY_STRING);
		userCO.status = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_STATUS, IConstants.EMPTY_STRING);
		userCO.lastAccessedOn = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_LAST_ACCESSED_ON, IConstants.EMPTY_STRING);
		userCO.profileImage = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, IConstants.EMPTY_STRING);
		userCO.coApplicant = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_CO_APPLICANT, IConstants.EMPTY_STRING);
		userCO.contactNo = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_CONTACT_NO, IConstants.EMPTY_STRING);
		userCO.address = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_ADDRESS, IConstants.EMPTY_STRING);
		userCO.formNo = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_FORM_NO, IConstants.EMPTY_STRING);
		userCO.propertyType = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PROPERTY_TYPE, IConstants.EMPTY_STRING);
		userCO.propertyNo = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PROPERTY_NO, IConstants.EMPTY_STRING);
		userCO.totalPayment = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_TOTAL_PAYMENT, IConstants.EMPTY_STRING);
		userCO.paymentDue = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_PAYMENT_DUE, IConstants.EMPTY_STRING);
		userCO.createdOn = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_CREATED_ON, IConstants.EMPTY_STRING);
		userCO.modifiedOn = sharedPreferences.getString(IReqParams.LOGGED_IN_USER_MODIFIED_ON, IConstants.EMPTY_STRING);
*/
		return userCO;
	}

	public void saveInDB()
	{
		SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(IReqParams.LOGGED_IN_USER_ID, id);
		editor.putString(IReqParams.LOGGED_IN_USER_FIRST_NAME, firstName);
		/*editor.putString(IReqParams.LOGGED_IN_USER_LAST_NAME, lastName);
		editor.putString(IReqParams.LOGGED_IN_USER_LOGIN_ID, loginId);
		editor.putString(IPreferences.API_ACCESS_TOKEN, accessToken);
		editor.putString(IReqParams.LOGGED_IN_USER_STATUS, status);
		editor.putString(IReqParams.LOGGED_IN_USER_LAST_ACCESSED_ON, lastAccessedOn);
		editor.putString(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, profileImage);
		editor.putString(IReqParams.LOGGED_IN_USER_CO_APPLICANT, coApplicant);
		editor.putString(IReqParams.LOGGED_IN_USER_CONTACT_NO, contactNo);
		editor.putString(IReqParams.LOGGED_IN_USER_ADDRESS, address);
		editor.putString(IReqParams.LOGGED_IN_USER_FORM_NO, formNo);
		editor.putString(IReqParams.LOGGED_IN_USER_PROPERTY_TYPE, propertyType);
		editor.putString(IReqParams.LOGGED_IN_USER_PROPERTY_NO, propertyNo);
		editor.putString(IReqParams.LOGGED_IN_USER_TOTAL_PAYMENT, totalPayment);
		editor.putString(IReqParams.LOGGED_IN_USER_PAYMENT_DUE, paymentDue);
		editor.putString(IReqParams.LOGGED_IN_USER_CREATED_ON, createdOn);
		editor.putString(IReqParams.LOGGED_IN_USER_MODIFIED_ON, modifiedOn);
*/
		editor.commit();
	}

	public void deleteFromDB()
	{
		SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.remove(IReqParams.LOGGED_IN_USER_ID);
		editor.remove(IReqParams.LOGGED_IN_USER_FIRST_NAME);
	/*	editor.remove(IReqParams.LOGGED_IN_USER_LAST_NAME);
		editor.remove(IReqParams.LOGGED_IN_USER_LOGIN_ID);
		editor.remove(IPreferences.API_ACCESS_TOKEN);
		editor.remove(IReqParams.LOGGED_IN_USER_STATUS);
		editor.remove(IReqParams.LOGGED_IN_USER_LAST_ACCESSED_ON);
		editor.remove(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE);
		editor.remove(IReqParams.LOGGED_IN_USER_CO_APPLICANT);
		editor.remove(IReqParams.LOGGED_IN_USER_CONTACT_NO);
		editor.remove(IReqParams.LOGGED_IN_USER_ADDRESS);
		editor.remove(IReqParams.LOGGED_IN_USER_FORM_NO);
		editor.remove(IReqParams.LOGGED_IN_USER_PROPERTY_TYPE);
		editor.remove(IReqParams.LOGGED_IN_USER_PROPERTY_NO);
		editor.remove(IReqParams.LOGGED_IN_USER_TOTAL_PAYMENT);
		editor.remove(IReqParams.LOGGED_IN_USER_PAYMENT_DUE);
		editor.remove(IReqParams.LOGGED_IN_USER_CREATED_ON);
		editor.remove(IReqParams.LOGGED_IN_USER_MODIFIED_ON);*/

		editor.commit();
	}

	protected User(Parcel in) {
		id = in.readString();
		firstName = in.readString();
		lastName = in.readString();
		loginId = in.readString();
		password = in.readString();
		accessToken = in.readString();
		status = in.readString();
		lastAccessedOn = in.readString();
		profileImage = in.readString();
		coApplicant = in.readString();
		contactNo = in.readString();
		address = in.readString();
		formNo = in.readString();
		propertyType = in.readString();
		propertyNo = in.readString();
		totalPayment = in.readString();
		paymentDue = in.readString();
		createdOn = in.readString();
		modifiedOn = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(loginId);
		dest.writeString(password);
		dest.writeString(accessToken);
		dest.writeString(status);
		dest.writeString(lastAccessedOn);
		dest.writeString(profileImage);
		dest.writeString(coApplicant);
		dest.writeString(contactNo);
		dest.writeString(address);
		dest.writeString(formNo);
		dest.writeString(propertyType);
		dest.writeString(propertyNo);
		dest.writeString(totalPayment);
		dest.writeString(paymentDue);
		dest.writeString(createdOn);
		dest.writeString(modifiedOn);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
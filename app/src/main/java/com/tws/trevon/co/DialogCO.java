package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.constants.IConstants;

public class DialogCO implements Parcelable
{
	public static final String ALERT_DIALOG_TYPE = "ALERT";
	public static final String MESSAGE_DIALOG_TYPE = "MESSAGE";
	public static final String POSITIVE_DIALOG_TYPE = "POSITIVE";

	public static final String LINK_ACTION_TYPE = "LINK";
	
	public String title;
	public String description;
	public String dialogCancelable;
	public String type;

	public String okButtonTitle;
	public String okButtonActionType;
	public String okButtonActionJsonData;
	
	public String negativeButtonRequired = IConstants.NO;
	public String negativeButtonTitle;
	public String negativeButtonActionType;
	public String negativeButtonActionJsonData;

	public DialogCO() {
	}

	protected DialogCO(Parcel in) {
		title = in.readString();
		description = in.readString();
		dialogCancelable = in.readString();
		type = in.readString();

		okButtonTitle = in.readString();
		okButtonActionType = in.readString();
		okButtonActionJsonData = in.readString();

		negativeButtonRequired = in.readString();
		negativeButtonTitle = in.readString();
		negativeButtonActionType = in.readString();
		negativeButtonActionJsonData = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(dialogCancelable);
		dest.writeString(type);

		dest.writeString(okButtonTitle);
		dest.writeString(okButtonActionType);
		dest.writeString(okButtonActionJsonData);

		dest.writeString(negativeButtonRequired);
		dest.writeString(negativeButtonTitle);
		dest.writeString(negativeButtonActionType);
		dest.writeString(negativeButtonActionJsonData);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DialogCO> CREATOR = new Creator<DialogCO>() {
		@Override
		public DialogCO createFromParcel(Parcel in) {
			return new DialogCO(in);
		}

		@Override
		public DialogCO[] newArray(int size) {
			return new DialogCO[size];
		}
	};
}

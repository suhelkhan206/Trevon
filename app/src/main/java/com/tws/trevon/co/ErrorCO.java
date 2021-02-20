package com.tws.trevon.co;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.tws.trevon.common.AppDbUtilities;
import com.tws.trevon.constants.ISysConfig;

import java.util.ArrayList;

public class ErrorCO implements Parcelable
{
    public String code;
    public String errorSource;
    public String errorSourceInfo;
    public String errorSubType;
    public String errorInfo;
    public String stackTrace;
    public String createdOn;

    public ErrorCO()
    {
        super();
    }

    public ContentValues getContentValue()
    {
        ContentValues cv = new ContentValues();
        cv.put("code", code);
        cv.put("error_sub_type", errorSubType);
        cv.put("error_source", errorSource);
        cv.put("error_source_info", errorSourceInfo);
        cv.put("error_info", errorInfo);
        cv.put("stack_trace", stackTrace);
        cv.put("created_on", createdOn);

        return cv;
    }

    public ErrorCO(final Cursor cursor)
    {
        code = cursor.getString(cursor.getColumnIndex("code"));
        errorSubType = cursor.getString(cursor.getColumnIndex("error_sub_type"));
        errorSource = cursor.getString(cursor.getColumnIndex("error_source"));
        errorSourceInfo = cursor.getString(cursor.getColumnIndex("error_source_info"));
        errorInfo = cursor.getString(cursor.getColumnIndex("error_info"));
        stackTrace = cursor.getString(cursor.getColumnIndex("stack_trace"));
        createdOn = cursor.getString(cursor.getColumnIndex("created_on"));
    }

    //database interactions
    public void saveInDB()
    {
        if(ISysConfig.APPLICATION_PRODUCTION_MODE.equals(ISysConfig.APPLICATION_MODE))
        {
            AppDbUtilities.saveErrorReport(getContentValue());
        }
    }

    public void deleteFromDB()
    {
        AppDbUtilities.deleteErrorReport(code);
    }

    public static ArrayList<ErrorCO> getAll()
    {
        /*return new ArrayList<>();*/
        return AppDbUtilities.getErrorCOList();
    }
    public static void deleteAll()
    {
        AppDbUtilities.deleteAllErrors();
    }

    // Parcelling part
    public ErrorCO(Parcel in)
    {
        code = in.readString();
        errorSubType = in.readString();
        errorSource = in.readString();
        errorSourceInfo = in.readString();
        errorInfo = in.readString();
        stackTrace = in.readString();
        createdOn = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(code);
        dest.writeString(errorSubType);
        dest.writeString(errorSource);
        dest.writeString(errorSourceInfo);
        dest.writeString(errorInfo);
        dest.writeString(stackTrace);
        dest.writeString(createdOn);
    }

    public static final Creator CREATOR = new Creator() {
        public ErrorCO createFromParcel(Parcel in) {
            return new ErrorCO(in);
        }

        public ErrorCO[] newArray(int size) {
            return new ErrorCO[size];
        }
    };
}
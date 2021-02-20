package com.tws.trevon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationCO implements Parcelable
{
    public String id;
    public String task_type;
    public String user_id;
    public String param2;
    public String status;
    public String priority;
    public String title;
    public String  message;
    public String description;
    public String total_processed;
    public String error_msg;
    public String created_by;
    public String created_on;
    public String modified_on;


    public NotificationCO()
    {

    }


    protected NotificationCO(Parcel in) {
        id = in.readString();
        task_type = in.readString();
        user_id = in.readString();
        param2 = in.readString();
        status = in.readString();
        priority = in.readString();
        title = in.readString();
        message = in.readString();
        description = in.readString();
        total_processed = in.readString();
        error_msg = in.readString();
        created_by = in.readString();
        created_on = in.readString();
        modified_on = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(task_type);
        dest.writeString(user_id);
        dest.writeString(param2);
        dest.writeString(status);
        dest.writeString(priority);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(description);
        dest.writeString(total_processed);
        dest.writeString(error_msg);
        dest.writeString(created_by);
        dest.writeString(created_on);
        dest.writeString(modified_on);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationCO> CREATOR = new Creator<NotificationCO>() {
        @Override
        public NotificationCO createFromParcel(Parcel in) {
            return new NotificationCO(in);
        }

        @Override
        public NotificationCO[] newArray(int size) {
            return new NotificationCO[size];
        }
    };
}

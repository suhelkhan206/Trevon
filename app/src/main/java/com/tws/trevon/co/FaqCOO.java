package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

public class FaqCOO implements Parcelable
{
    public String id;
    public String question;
    public String  answer;
    public String video;
    public String  status;
    public String  create_at;

    protected FaqCOO(Parcel in) {
        id = in.readString();
        question = in.readString();
        answer = in.readString();
        video = in.readString();
        status = in.readString();
        create_at = in.readString();
    }

    public static final Creator<FaqCOO> CREATOR = new Creator<FaqCOO>() {
        @Override
        public FaqCOO createFromParcel(Parcel in) {
            return new FaqCOO(in);
        }

        @Override
        public FaqCOO[] newArray(int size) {
            return new FaqCOO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(video);
        dest.writeString(status);
        dest.writeString(create_at);
    }
}

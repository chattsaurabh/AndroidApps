package com.dev.rotator.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TimeRs implements Parcelable {

    @SerializedName("datetime")
    public String dateTime;

    protected TimeRs(Parcel in) {
        dateTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeRs> CREATOR = new Creator<TimeRs>() {
        @Override
        public TimeRs createFromParcel(Parcel in) {
            return new TimeRs(in);
        }

        @Override
        public TimeRs[] newArray(int size) {
            return new TimeRs[size];
        }
    };
}

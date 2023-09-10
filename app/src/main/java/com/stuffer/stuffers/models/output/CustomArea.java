package com.stuffer.stuffers.models.output;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CustomArea implements Parcelable {
    String areacode;
    String areacode_with_name;
    String mFlag;

    public CustomArea(String areacode, String areacode_with_name, String mFlag) {
        this.areacode = areacode;
        this.areacode_with_name = areacode_with_name;
        this.mFlag = mFlag;
    }

    protected CustomArea(Parcel in) {
        areacode = in.readString();
        areacode_with_name = in.readString();
        mFlag = in.readString();
    }

    public static final Creator<CustomArea> CREATOR = new Creator<CustomArea>() {
        @Override
        public CustomArea createFromParcel(Parcel in) {
            return new CustomArea(in);
        }

        @Override
        public CustomArea[] newArray(int size) {
            return new CustomArea[size];
        }
    };

    public String getAreacode() {
        return areacode;
    }

    public String getAreacode_with_name() {
        return areacode_with_name;
    }

    public String getmFlag() {
        return mFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(areacode);
        parcel.writeString(areacode_with_name);
        parcel.writeString(mFlag);
    }
}


package com.stuffer.stuffers.models.output;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomArea implements Parcelable {
    String areacode;
    String areacode_with_name;

    public CustomArea(String areacode, String areacode_with_name) {
        this.areacode = areacode;
        this.areacode_with_name = areacode_with_name;
    }

    protected CustomArea(Parcel in) {
        areacode = in.readString();
        areacode_with_name = in.readString();
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

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public void setAreacode_with_name(String areacode_with_name) {
        this.areacode_with_name = areacode_with_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areacode);
        dest.writeString(areacode_with_name);
    }

    public String getAreacode() {
        return areacode;
    }

    public String getAreacode_with_name() {
        return areacode_with_name;
    }
}

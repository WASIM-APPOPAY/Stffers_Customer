package com.stuffer.stuffers.models.output;

import android.os.Parcel;
import android.os.Parcelable;

public class SourceIncome implements Parcelable {
    String id;
    String name;

    public SourceIncome(String id, String name) {

        this.id = id;
        this.name = name;
    }

    protected SourceIncome(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SourceIncome> CREATOR = new Creator<SourceIncome>() {
        @Override
        public SourceIncome createFromParcel(Parcel in) {
            return new SourceIncome(in);
        }

        @Override
        public SourceIncome[] newArray(int size) {
            return new SourceIncome[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}

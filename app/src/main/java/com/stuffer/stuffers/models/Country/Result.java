package com.stuffer.stuffers.models.Country;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("countrycode")
    @Expose
    private String countrycode;
    @SerializedName("countryname")
    @Expose
    private String countryname;
    @SerializedName("areacode")
    @Expose
    private String areacode;
    @SerializedName("states")
    @Expose
    private List<State> states = null;

    public Result(Integer id, String countrycode, String countryname, String areacode, List<State> states) {
        this.id = id;
        this.countrycode = countrycode;
        this.countryname = countryname;
        this.areacode = areacode;
        this.states = states;
    }

    protected Result(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        countrycode = in.readString();
        countryname = in.readString();
        areacode = in.readString();
        states = in.createTypedArrayList(State.CREATOR);
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(countrycode);
        dest.writeString(countryname);
        dest.writeString(areacode);
        dest.writeTypedList(states);
    }

    public Integer getId() {
        return id;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public String getCountryname() {
        return countryname;
    }

    public String getAreacode() {
        return areacode;
    }

    public List<State> getStates() {
        return states;
    }
}
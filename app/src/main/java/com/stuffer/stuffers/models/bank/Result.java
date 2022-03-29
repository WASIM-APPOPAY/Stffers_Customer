package com.stuffer.stuffers.models.bank;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bankname")
    @Expose
    private String bankname;
    @SerializedName("bankcode")
    @Expose
    private String bankcode;
    @SerializedName("countryid")
    @Expose
    private Integer countryid;
    @SerializedName("countryname")
    @Expose
    private Object countryname;

    protected Result(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        bankname = in.readString();
        bankcode = in.readString();
        if (in.readByte() == 0) {
            countryid = null;
        } else {
            countryid = in.readInt();
        }
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public Integer getCountryid() {
        return countryid;
    }

    public void setCountryid(Integer countryid) {
        this.countryid = countryid;
    }

    public Object getCountryname() {
        return countryname;
    }

    public void setCountryname(Object countryname) {
        this.countryname = countryname;
    }

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
        dest.writeString(bankname);
        dest.writeString(bankcode);
        if (countryid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(countryid);
        }
    }
}
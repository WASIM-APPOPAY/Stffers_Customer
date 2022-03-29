package com.stuffer.stuffers.models.output;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CurrencyResult implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("currency_name")
    @Expose
    private String currencyName;
    @SerializedName("conversion_rate")
    @Expose
    private Float conversionRate;
    @SerializedName("status")
    @Expose
    private Boolean status;

    protected CurrencyResult(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        currencyCode = in.readString();
        currencyName = in.readString();
        if (in.readByte() == 0) {
            conversionRate = null;
        } else {
            conversionRate = in.readFloat();
        }
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
    }

    public static final Creator<CurrencyResult> CREATOR = new Creator<CurrencyResult>() {
        @Override
        public CurrencyResult createFromParcel(Parcel in) {
            return new CurrencyResult(in);
        }

        @Override
        public CurrencyResult[] newArray(int size) {
            return new CurrencyResult[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Float getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Float conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
        dest.writeString(currencyCode);
        dest.writeString(currencyName);
        if (conversionRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(conversionRate);
        }
        dest.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
    }
}

package com.stuffer.stuffers.models.Country;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryCodeResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;


    protected CountryCodeResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        message = in.readString();
        result = in.createTypedArrayList(Result.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeString(message);
        dest.writeTypedList(result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CountryCodeResponse> CREATOR = new Creator<CountryCodeResponse>() {
        @Override
        public CountryCodeResponse createFromParcel(Parcel in) {
            return new CountryCodeResponse(in);
        }

        @Override
        public CountryCodeResponse[] newArray(int size) {
            return new CountryCodeResponse[size];
        }
    };

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Result> getResult() {
        return result;
    }
}

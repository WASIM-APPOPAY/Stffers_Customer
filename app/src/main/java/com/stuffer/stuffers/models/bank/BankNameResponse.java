package com.stuffer.stuffers.models.bank;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankNameResponse implements Parcelable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    protected BankNameResponse(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        message = in.readString();
        result = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Creator<BankNameResponse> CREATOR = new Creator<BankNameResponse>() {
        @Override
        public BankNameResponse createFromParcel(Parcel in) {
            return new BankNameResponse(in);
        }

        @Override
        public BankNameResponse[] newArray(int size) {
            return new BankNameResponse[size];
        }
    };

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
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
}
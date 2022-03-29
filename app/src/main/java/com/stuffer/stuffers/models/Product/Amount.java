package com.stuffer.stuffers.models.Product;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount implements Parcelable {

    @SerializedName("MaxAmt")
    @Expose
    private Integer maxAmt;
    @SerializedName("MinAmt")
    @Expose
    private Integer minAmt;
    @SerializedName("DestCurr")
    @Expose
    private String destCurr;
    @SerializedName("Commission")
    @Expose
    private Integer commission;
    @SerializedName("DestAmt")
    @Expose
    private Integer destAmt;
    @SerializedName("RatioToDest")
    @Expose
    private Object ratioToDest;
    @SerializedName("Denom")
    @Expose
    private String denom;
    @SerializedName("Amt")
    @Expose
    private Integer amt;

    protected Amount(Parcel in) {
        if (in.readByte() == 0) {
            maxAmt = null;
        } else {
            maxAmt = in.readInt();
        }
        if (in.readByte() == 0) {
            minAmt = null;
        } else {
            minAmt = in.readInt();
        }
        destCurr = in.readString();
        if (in.readByte() == 0) {
            commission = null;
        } else {
            commission = in.readInt();
        }
        if (in.readByte() == 0) {
            destAmt = null;
        } else {
            destAmt = in.readInt();
        }
        denom = in.readString();
        if (in.readByte() == 0) {
            amt = null;
        } else {
            amt = in.readInt();
        }
    }

    public static final Creator<Amount> CREATOR = new Creator<Amount>() {
        @Override
        public Amount createFromParcel(Parcel in) {
            return new Amount(in);
        }

        @Override
        public Amount[] newArray(int size) {
            return new Amount[size];
        }
    };

    public Integer getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(Integer maxAmt) {
        this.maxAmt = maxAmt;
    }

    public Integer getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(Integer minAmt) {
        this.minAmt = minAmt;
    }

    public String getDestCurr() {
        return destCurr;
    }

    public void setDestCurr(String destCurr) {
        this.destCurr = destCurr;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public Integer getDestAmt() {
        return destAmt;
    }

    public void setDestAmt(Integer destAmt) {
        this.destAmt = destAmt;
    }

    public Object getRatioToDest() {
        return ratioToDest;
    }

    public void setRatioToDest(Object ratioToDest) {
        this.ratioToDest = ratioToDest;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (maxAmt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxAmt);
        }
        if (minAmt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minAmt);
        }
        dest.writeString(destCurr);
        if (commission == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(commission);
        }
        if (destAmt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(destAmt);
        }
        dest.writeString(denom);
        if (amt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(amt);
        }
    }
}
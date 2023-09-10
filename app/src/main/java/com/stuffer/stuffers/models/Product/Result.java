package com.stuffer.stuffers.models.Product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Parcelable {
    @SerializedName("ProductCode")
    @Expose
    private String productCode;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("MinLen")
    @Expose
    private Integer minLen;
    @SerializedName("Amounts")
    @Expose
    private List<Amount> amounts = null;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("CountryName")
    @Expose
    private String countryName;
    @SerializedName("MaxLen")
    @Expose
    private Integer maxLen;
    @SerializedName("CarrierId")
    @Expose
    private Integer carrierId;
    @SerializedName("Carrier")
    @Expose
    private String carrier;
    @SerializedName("ProductType")
    @Expose
    private String productType;
    @SerializedName("CarrierCode")
    @Expose
    private Integer carrierCode;
    @SerializedName("Type")
    @Expose
    private String type;

    protected Result(Parcel in) {
        productCode = in.readString();
        countryCode = in.readString();
        if (in.readByte() == 0) {
            minLen = null;
        } else {
            minLen = in.readInt();
        }
        productName = in.readString();
        countryName = in.readString();
        if (in.readByte() == 0) {
            maxLen = null;
        } else {
            maxLen = in.readInt();
        }
        if (in.readByte() == 0) {
            carrierId = null;
        } else {
            carrierId = in.readInt();
        }
        carrier = in.readString();
        productType = in.readString();
        if (in.readByte() == 0) {
            carrierCode = null;
        } else {
            carrierCode = in.readInt();
        }
        type = in.readString();
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getMinLen() {
        return minLen;
    }

    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }

    public List<Amount> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Amount> amounts) {
        this.amounts = amounts;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(Integer carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productCode);
        dest.writeString(countryCode);
        if (minLen == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minLen);
        }
        dest.writeString(productName);
        dest.writeString(countryName);
        if (maxLen == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxLen);
        }
        if (carrierId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carrierId);
        }
        dest.writeString(carrier);
        dest.writeString(productType);
        if (carrierCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carrierCode);
        }
        dest.writeString(type);
    }
}

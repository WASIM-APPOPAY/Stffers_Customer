package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


import android.os.Parcelable;

public class DetinationCurrency implements Parcelable
{

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<DetinationCurrency> CREATOR = new Creator<DetinationCurrency>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DetinationCurrency createFromParcel(android.os.Parcel in) {
            return new DetinationCurrency(in);
        }

        public DetinationCurrency[] newArray(int size) {
            return (new DetinationCurrency[size]);
        }

    }
            ;

    protected DetinationCurrency(android.os.Parcel in) {
        in.readList(this.result, (Result.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DetinationCurrency() {
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(result);
        dest.writeValue(message);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }
    public static class Result implements Parcelable
    {

        @SerializedName("region")
        @Expose
        private String region;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("modalities")
        @Expose
        private String modalities;
        @SerializedName("payoutCurrency")
        @Expose
        private String payoutCurrency;
        @SerializedName("perTransactionLimit")
        @Expose
        private String perTransactionLimit;
        public final static Creator<Result> CREATOR = new Creator<Result>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Result createFromParcel(android.os.Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        }
                ;

        protected Result(android.os.Parcel in) {
            this.region = ((String) in.readValue((String.class.getClassLoader())));
            this.country = ((String) in.readValue((String.class.getClassLoader())));
            this.modalities = ((String) in.readValue((String.class.getClassLoader())));
            this.payoutCurrency = ((String) in.readValue((String.class.getClassLoader())));
            this.perTransactionLimit = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Result() {
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getModalities() {
            return modalities;
        }

        public void setModalities(String modalities) {
            this.modalities = modalities;
        }

        public String getPayoutCurrency() {
            return payoutCurrency;
        }

        public void setPayoutCurrency(String payoutCurrency) {
            this.payoutCurrency = payoutCurrency;
        }

        public String getPerTransactionLimit() {
            return perTransactionLimit;
        }

        public void setPerTransactionLimit(String perTransactionLimit) {
            this.perTransactionLimit = perTransactionLimit;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeValue(region);
            dest.writeValue(country);
            dest.writeValue(modalities);
            dest.writeValue(payoutCurrency);
            dest.writeValue(perTransactionLimit);
        }

        public int describeContents() {
            return 0;
        }

    }

}




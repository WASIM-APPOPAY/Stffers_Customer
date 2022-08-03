package com.stuffer.stuffers.models.output;

import java.util.List;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CalTransfer implements Parcelable {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<CalTransfer> CREATOR = new Creator<CalTransfer>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CalTransfer createFromParcel(android.os.Parcel in) {
            return new CalTransfer(in);
        }

        public CalTransfer[] newArray(int size) {
            return (new CalTransfer[size]);
        }

    };

    protected CalTransfer(android.os.Parcel in) {
        in.readList(this.result, (Result.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CalTransfer() {
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

    public static class Result implements Parcelable {

        @SerializedName("exchangeRate")
        @Expose
        private String exchangeRate;
        @SerializedName("commission")
        @Expose
        private String commission;
        @SerializedName("payInCurrency")
        @Expose
        private String payInCurrency;
        @SerializedName("payoutCurrency")
        @Expose
        private String payoutCurrency;
        @SerializedName("payInAmount")
        @Expose
        private String payInAmount;
        @SerializedName("payoutAmount")
        @Expose
        private String payoutAmount;
        @SerializedName("totalPayable")
        @Expose
        private String totalPayable;
        @SerializedName("vatValue")
        @Expose
        private String vatValue;
        @SerializedName("vatPercentage")
        @Expose
        private String vatPercentage;
        @SerializedName("recommendAgent")
        @Expose
        private String recommendAgent;
        @SerializedName("payoutBranchCode")
        @Expose
        private String payoutBranchCode;
        @SerializedName("responseCode")
        @Expose
        private String responseCode;
        @SerializedName("description")
        @Expose
        private String description;
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

        };

        protected Result(android.os.Parcel in) {
            this.exchangeRate = ((String) in.readValue((String.class.getClassLoader())));
            this.commission = ((String) in.readValue((String.class.getClassLoader())));
            this.payInCurrency = ((String) in.readValue((String.class.getClassLoader())));
            this.payoutCurrency = ((String) in.readValue((String.class.getClassLoader())));
            this.payInAmount = ((String) in.readValue((String.class.getClassLoader())));
            this.payoutAmount = ((String) in.readValue((String.class.getClassLoader())));
            this.totalPayable = ((String) in.readValue((String.class.getClassLoader())));
            this.vatValue = ((String) in.readValue((String.class.getClassLoader())));
            this.vatPercentage = ((String) in.readValue((String.class.getClassLoader())));
            this.recommendAgent = ((String) in.readValue((String.class.getClassLoader())));
            this.payoutBranchCode = ((String) in.readValue((String.class.getClassLoader())));
            this.responseCode = ((String) in.readValue((String.class.getClassLoader())));
            this.description = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Result() {
        }

        public String getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getPayInCurrency() {
            return payInCurrency;
        }

        public void setPayInCurrency(String payInCurrency) {
            this.payInCurrency = payInCurrency;
        }

        public String getPayoutCurrency() {
            return payoutCurrency;
        }

        public void setPayoutCurrency(String payoutCurrency) {
            this.payoutCurrency = payoutCurrency;
        }

        public String getPayInAmount() {
            return payInAmount;
        }

        public void setPayInAmount(String payInAmount) {
            this.payInAmount = payInAmount;
        }

        public String getPayoutAmount() {
            return payoutAmount;
        }

        public void setPayoutAmount(String payoutAmount) {
            this.payoutAmount = payoutAmount;
        }

        public String getTotalPayable() {
            return totalPayable;
        }

        public void setTotalPayable(String totalPayable) {
            this.totalPayable = totalPayable;
        }

        public String getVatValue() {
            return vatValue;
        }

        public void setVatValue(String vatValue) {
            this.vatValue = vatValue;
        }

        public String getVatPercentage() {
            return vatPercentage;
        }

        public void setVatPercentage(String vatPercentage) {
            this.vatPercentage = vatPercentage;
        }

        public String getRecommendAgent() {
            return recommendAgent;
        }

        public void setRecommendAgent(String recommendAgent) {
            this.recommendAgent = recommendAgent;
        }

        public String getPayoutBranchCode() {
            return payoutBranchCode;
        }

        public void setPayoutBranchCode(String payoutBranchCode) {
            this.payoutBranchCode = payoutBranchCode;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeValue(exchangeRate);
            dest.writeValue(commission);
            dest.writeValue(payInCurrency);
            dest.writeValue(payoutCurrency);
            dest.writeValue(payInAmount);
            dest.writeValue(payoutAmount);
            dest.writeValue(totalPayable);
            dest.writeValue(vatValue);
            dest.writeValue(vatPercentage);
            dest.writeValue(recommendAgent);
            dest.writeValue(payoutBranchCode);
            dest.writeValue(responseCode);
            dest.writeValue(description);
        }

        public int describeContents() {
            return 0;
        }

    }

}


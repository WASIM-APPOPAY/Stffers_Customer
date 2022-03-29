package com.stuffer.stuffers.models.lunex_giftcard;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftProductList {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("Products")
    @Expose
    private List<Product> products = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public static class Product {

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

    }


    public static class Amount {

        @SerializedName("MaxAmt")
        @Expose
        private Float maxAmt;
        @SerializedName("MinAmt")
        @Expose
        private Float minAmt;
        @SerializedName("DestCurr")
        @Expose
        private String destCurr;
        @SerializedName("Commission")
        @Expose
        private Integer commission;
        @SerializedName("DestAmt")
        @Expose
        private Float destAmt;
        @SerializedName("RatioToDest")
        @Expose
        private Object ratioToDest;
        @SerializedName("Denom")
        @Expose
        private String denom;
        @SerializedName("Amt")
        @Expose
        private Float amt;

        public Float getMaxAmt() {
            return maxAmt;
        }

        public void setMaxAmt(Float maxAmt) {
            this.maxAmt = maxAmt;
        }

        public Float getMinAmt() {
            return minAmt;
        }

        public void setMinAmt(Float minAmt) {
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

        public Float getDestAmt() {
            return destAmt;
        }

        public void setDestAmt(Float destAmt) {
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

        public Float getAmt() {
            return amt;
        }

        public void setAmt(Float amt) {
            this.amt = amt;
        }

    }


}

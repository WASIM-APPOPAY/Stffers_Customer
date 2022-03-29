package com.stuffer.stuffers.models.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankCurrencyResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

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


    public class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("currencycode")
        @Expose
        private String currencycode;
        @SerializedName("bankname")
        @Expose
        private Object bankname;
        @SerializedName("bankid")
        @Expose
        private Integer bankid;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCurrencycode() {
            return currencycode;
        }

        public void setCurrencycode(String currencycode) {
            this.currencycode = currencycode;
        }

        public Object getBankname() {
            return bankname;
        }

        public void setBankname(Object bankname) {
            this.bankname = bankname;
        }

        public Integer getBankid() {
            return bankid;
        }

        public void setBankid(Integer bankid) {
            this.bankid = bankid;
        }

    }

}
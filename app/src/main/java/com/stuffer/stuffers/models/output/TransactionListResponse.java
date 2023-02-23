package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionListResponse {

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

    public static class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("mobilenumber")
        @Expose
        private String mobilenumber;
        @SerializedName("accountnumber")
        @Expose
        private String accountnumber;
        @SerializedName("transactiontype")
        @Expose
        private String transactiontype;
        @SerializedName("transactiondescription")
        @Expose
        private String transactiondescription;
        @SerializedName("transactiondate")
        @Expose
        private String transactiondate;
        @SerializedName("transactionamount")
        @Expose
        private Float transactionamount;
        @SerializedName("pendingbalance")
        @Expose
        private Float pendingbalance;
        @SerializedName("transactionstatus")
        @Expose
        private String transactionstatus;
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("transactionid")
        @Expose
        private String transactionid;
        @SerializedName("curencyid")
        @Expose
        private Integer curencyid;
        @SerializedName("sendername")
        @Expose
        private String sendername;
        @SerializedName("sendermobile")
        @Expose
        private String sendermobile;
        @SerializedName("processingfees")
        @Expose
        private Float processingfees;
        @SerializedName("bankfees")
        @Expose
        private Float bankfees;
        @SerializedName("paymenttype")
        @Expose
        private Object paymenttype;

        @SerializedName("receiverName")
        @Expose
        private Object receiverName;

        @SerializedName("receiverCurrencyCode")
        @Expose
        private Object receiverCurrencyCode;

        @SerializedName("areacode")
        @Expose
        private Integer areacode;
        @SerializedName("taxes")
        @Expose
        private Float taxes;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getAccountnumber() {
            return accountnumber;
        }

        public void setAccountnumber(String accountnumber) {
            this.accountnumber = accountnumber;
        }

        public String getTransactiontype() {
            return transactiontype;
        }

        public void setTransactiontype(String transactiontype) {
            this.transactiontype = transactiontype;
        }

        public String getTransactiondescription() {
            return transactiondescription;
        }

        public void setTransactiondescription(String transactiondescription) {
            this.transactiondescription = transactiondescription;
        }

        public String getTransactiondate() {
            return transactiondate;
        }

        public void setTransactiondate(String transactiondate) {
            this.transactiondate = transactiondate;
        }

        public Float getTransactionamount() {
            return transactionamount;
        }

        public void setTransactionamount(Float transactionamount) {
            this.transactionamount = transactionamount;
        }

        public Float getPendingbalance() {
            return pendingbalance;
        }

        public void setPendingbalance(Float pendingbalance) {
            this.pendingbalance = pendingbalance;
        }

        public String getTransactionstatus() {
            return transactionstatus;
        }

        public void setTransactionstatus(String transactionstatus) {
            this.transactionstatus = transactionstatus;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getTransactionid() {
            return transactionid;
        }

        public void setTransactionid(String transactionid) {
            this.transactionid = transactionid;
        }

        public Integer getCurencyid() {
            return curencyid;
        }

        public void setCurencyid(Integer curencyid) {
            this.curencyid = curencyid;
        }

        public String getSendername() {
            return sendername;
        }

        public void setSendername(String sendername) {
            this.sendername = sendername;
        }

        public String getSendermobile() {
            return sendermobile;
        }

        public void setSendermobile(String sendermobile) {
            this.sendermobile = sendermobile;
        }

        public Float getProcessingfees() {
            return processingfees;
        }

        public void setProcessingfees(Float processingfees) {
            this.processingfees = processingfees;
        }

        public Float getBankfees() {
            return bankfees;
        }

        public void setBankfees(Float bankfees) {
            this.bankfees = bankfees;
        }

        public Object getPaymenttype() {
            return paymenttype;
        }

        public Object getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(Object receiverName) {
            this.receiverName = receiverName;
        }

        public Object getReceiverCurrencyCode() {
            return receiverCurrencyCode;
        }

        public void setReceiverCurrencyCode(Object receiverCurrencyCode) {
            this.receiverCurrencyCode = receiverCurrencyCode;
        }

        public void setPaymenttype(Object paymenttype) {
            this.paymenttype = paymenttype;
        }

        public Integer getAreacode() {
            return areacode;
        }

        public void setAreacode(Integer areacode) {
            this.areacode = areacode;
        }

        public Float getTaxes() {
            return taxes;
        }

        public void setTaxes(Float taxes) {
            this.taxes = taxes;
        }

    }

}
package com.stuffer.stuffers.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveCardResponse {
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
        @SerializedName("transactionid")
        @Expose
        private String transactionid;
        @SerializedName("cardtype")
        @Expose
        private String cardtype;
        @SerializedName("isdefault")
        @Expose
        private Boolean isdefault;
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("hashid")
        @Expose
        private String hashid;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTransactionid() {
            return transactionid;
        }

        public void setTransactionid(String transactionid) {
            this.transactionid = transactionid;
        }

        public String getCardtype() {
            return cardtype;
        }

        public void setCardtype(String cardtype) {
            this.cardtype = cardtype;
        }

        public Boolean getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(Boolean isdefault) {
            this.isdefault = isdefault;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getHashid() {
            return hashid;
        }

        public void setHashid(String hashid) {
            this.hashid = hashid;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

    }
}
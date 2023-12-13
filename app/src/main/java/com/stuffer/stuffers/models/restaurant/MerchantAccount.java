
package com.stuffer.stuffers.models.restaurant;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MerchantAccount {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("accountnumber")
    @Expose
    private String accountnumber;
    @SerializedName("currentbalance")
    @Expose
    private Double currentbalance;
    @SerializedName("accountstatus")
    @Expose
    private String accountstatus;
    @SerializedName("currencyid")
    @Expose
    private Integer currencyid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public Double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(Double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public Integer getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(Integer currencyid) {
        this.currencyid = currencyid;
    }

}

package com.stuffer.stuffers.models.output;

public class AccountModel {
    String accountnumber;
    String accountstatus;
    String currencyid;
    String currentbalance;
    String id;
    String reserveamount;
    String currencyCode;
    String accountEncrypt;

    public String getAccountEncrypt() {
        return accountEncrypt;
    }

    public void setAccountEncrypt(String accountEncrypt) {
        this.accountEncrypt = accountEncrypt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public String getCurrencyid() {
        return currencyid;
    }

    public void setCurrencyid(String currencyid) {
        this.currencyid = currencyid;
    }

    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReserveamount() {
        return reserveamount;
    }

    public void setReserveamount(String reserveamount) {
        this.reserveamount = reserveamount;
    }
}

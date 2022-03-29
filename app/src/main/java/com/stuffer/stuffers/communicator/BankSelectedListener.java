package com.stuffer.stuffers.communicator;

/*"id":1,
        "bankname":"ALLBANK",
        "bankcode":"1740",
        "countryid":1,*/

public interface BankSelectedListener {
    public void onBankSelected(int id, String bankname, String bankcode);
}

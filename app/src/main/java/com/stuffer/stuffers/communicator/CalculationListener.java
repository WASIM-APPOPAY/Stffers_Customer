package com.stuffer.stuffers.communicator;

public interface CalculationListener {
    public void onCalculationRequest(String sendingCurrency, String receiverCurrency, String mRecName, String mRecBankName, String mRecAcNo, String mRecBranch, String mRecIFSC, String mPayout);

    public void onCashCalculation(String sendingCurrency, String receiverCurrency, String mRecName, String nationality, String recMobile, String address, String mPayout);
}

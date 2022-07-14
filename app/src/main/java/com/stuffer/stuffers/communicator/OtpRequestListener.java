package com.stuffer.stuffers.communicator;

public interface OtpRequestListener {
    public void onOtpRequest(String nameCode, String countryCode, String mobileNumber, String emailId, String address,String countryId);
}

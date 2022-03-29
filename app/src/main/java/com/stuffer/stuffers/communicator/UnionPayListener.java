package com.stuffer.stuffers.communicator;

public interface UnionPayListener {

    public void onEnrollmentClick();

    public void onOpenNewAccountRequest();

    public void onUnMaskRequest();

    public void onDifferentCardRequest(int cardTypePrdNumber,String walletNumber);

}

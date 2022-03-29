package com.stuffer.stuffers.communicator;

import android.graphics.Bitmap;

public interface SignatureCallback {
    public void onSignatureConfirm(Bitmap bitmap,boolean status);
}

package com.stuffer.stuffers.communicator;

import android.net.Uri;

public interface CameraListener {
    void onCameraCapturePerform(String picturePath, Uri uri);
}

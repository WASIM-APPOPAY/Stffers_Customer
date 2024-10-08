package com.stuffer.stuffers.my_camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.PictureListener;
import com.stuffer.stuffers.utils.AppoConstants;

public class MyCameraActivity extends AppCompatActivity implements PictureListener {
    private final String[] permissions = {Manifest.permission.CAMERA};
    //https://www.youtube.com/watch?v=5WXbgXd8A9Q&ab_channel=TheCodeCity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CameraFragment()).commit();
        else ActivityCompat.requestPermissions(this, permissions, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new CameraFragment()).commit();
        else ActivityCompat.requestPermissions(this, permissions, 1);
    }


    @Override
    public void onPictureSelect(String path) {
        Intent mIntent=new Intent();
        mIntent.putExtra(AppoConstants.IMAGE_PATH,path);
        setResult(RESULT_OK,mIntent);
        finish();
    }
}
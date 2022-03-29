package com.stuffer.stuffers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppoPayApplication) getApplication()).startUserSession();

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        ((AppoPayApplication) getApplication()).onUserInteracted();
    }

}

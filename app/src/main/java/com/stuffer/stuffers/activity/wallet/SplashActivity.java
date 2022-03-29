package com.stuffer.stuffers.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.DataVaultManager;

import org.apache.commons.lang3.StringUtils;


public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIMEOUT = 4000;
    ImageView secondaryImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
        setContentView(R.layout.splash);
        //DataVaultManager.getInstance(this).saveUserDetails(Helper.TEMP_USER_DATA);
        secondaryImage = findViewById(R.id.secondaryImage);
        Glide.with(this).load(R.drawable.appopay_new_logo).into(secondaryImage);
        setupUI();
    }


    private void setupUI() {
        //Log.e("TAG", "setupUI: "+ DataVaultManager.getInstance(this).getVaultValue(DataVaultManager.KEY_ACCESSTOKEN));
        String keyUserDetails = DataVaultManager.getInstance(this).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(keyUserDetails)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataVaultManager.getInstance(SplashActivity.this).saveComingFromSplash("no");
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   // DataVaultManager.getInstance(SplashActivity.this).saveComingFromSplash("no");
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }

    }


}

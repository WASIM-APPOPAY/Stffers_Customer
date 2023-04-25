package com.stuffer.stuffers.activity.wallet;

import static android.os.Build.VERSION.SDK_INT;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;
import static com.stuffer.stuffers.utils.DataVaultManager.TANDC;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.commonChat.chat.NumberActivity;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.communicator.LanguageListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomLanguage;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.utils.AppSignatureHelper;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity implements LanguageListener {
    private static final String TAG = "SplashActivity";
    // Splash screen timer
    private static final int SPLASH_TIMEOUT = 100;
    ImageView secondaryImage;
    private MyTextView tvAgree;
    private MyTextViewBold tvTapInfo;
    private MyTextViewBold tvLanguage;
    private BottomLanguage mBottomLanguage;
    private CheckBox tvCheck;
    private LinearLayout rLayout;
    private ChatHelper chatHelper;

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
        /*AppSignatureHelper mAppSignatureHelper = new AppSignatureHelper(AppoPayApplication.getInstance());
        ArrayList<String> appSignatures = mAppSignatureHelper.getAppSignatures();
        Log.e(TAG, "onCreate: " + appSignatures.get(0));*/

        tvCheck = (CheckBox) findViewById(R.id.tvCheck);
        tvAgree = (MyTextView) findViewById(R.id.tvAgree);
        tvTapInfo = (MyTextViewBold) findViewById(R.id.tvTapInfo);
        tvLanguage = (MyTextViewBold) findViewById(R.id.tvLanguage);
        rLayout = findViewById(R.id.rLayout);

        String mTandc = DataVaultManager.getInstance(SplashActivity.this).getVaultValue(TANDC);
        chatHelper = new ChatHelper(this);
        boolean checkPermission = checkPermission();

        if (!StringUtils.isEmpty(mTandc)) {
            tvCheck.setChecked(true);
        }
        DataVaultManager.getInstance(SplashActivity.this).saveTerm("true");
        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvCheck.isChecked()) {
                    Toast.makeText(SplashActivity.this, getString(R.string.info_term_condition), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkPermission) {
                    User loggedInUser = chatHelper.getLoggedInUser();
                    String s = new Gson().toJson(loggedInUser);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setupUI();
                        }
                    }, SPLASH_TIMEOUT);
                } else {
                    showPermission(getString(R.string.permission_desc_storage));
                }

            }
        });


        String tapInfoText1 = "<font color='#029DDC'>" + getString(R.string.info_split1) + " " + "&quot;" + getString(R.string.info_split2) + "&quot;" + "</font>" + "<font color='#029DDC'>" + " " + getString(R.string.info_split3) + "</font>";
        String tapInfoText2 = "<font color='#FB8310'>" + " " + getString(R.string.info_split4) + "</font>";
        String wholeText = tapInfoText1 + tapInfoText2;

        tvTapInfo.setText(Html.fromHtml(wholeText));
        tvTapInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCheck.setChecked(true);
                Intent intent = new Intent(SplashActivity.this, UrlsActivity.class);
                intent.putExtra(AppoConstants.TITLE, "Terms and Condition");
                intent.putExtra(AppoConstants.NAME, Constants.TERM_AND_CONDITIONS);
                startActivity(intent);

            }
        });

        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanDialogue();
            }
        });


    }

    private static final String MIN_SUPPORTED_PLAY_SERVICES_VERSION = "10.2";


    private static boolean isPlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void showLanDialogue() {
        mBottomLanguage = new BottomLanguage();
        mBottomLanguage.show(getSupportFragmentManager(), mBottomLanguage.getTag());
        mBottomLanguage.setCancelable(false);

    }


    private void setupUI() {

        if (chatHelper.getLoggedInUser() != null) {
            Intent i = new Intent(SplashActivity.this, HomeActivity2.class);
            startActivity(i);
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, NumberActivity.class));
            finish();
        }


    }


    @Override
    public void onLanguageSelect(String lan) {
        mBottomLanguage.dismiss();
        DataVaultManager.getInstance(SplashActivity.this).saveLanguage(lan);
        Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2298) {
            if (grantResults.length > 0) {
                boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean p2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean p3 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean p4 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean p5 = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (p1 && p2 && p3 && p4 && p5) {
                    setupUI();
                } else {
                    showPermission(getString(R.string.permission_desc_storage));
                }
            }
        } else if (requestCode == 2299) {
            if (grantResults.length > 0) {
                boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean p2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean p3 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (p1 && p2 && p3) {
                    setupUI();
                } else {
                    showPermission(getString(R.string.permission_desc_storage));
                }
            }
        }
    }

    private boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_CONTACTS);
        int result4 = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.RECORD_AUDIO);
        int result5 = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA);
        /*int result6 = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.RECEIVE_SMS);*/

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED; /*&& result6 == PackageManager.PERMISSION_GRANTED;*/

    }

    public void showPermission(String permission_desc) {
        Snackbar snackbar = Snackbar.make(rLayout, permission_desc, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA
                                }, 2298);


                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View view = snackbar.getView();
        TextView sbTextView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        sbTextView.setMaxLines(3);
        sbTextView.setTextColor(Color.YELLOW);
        snackbar.show();
    }



}

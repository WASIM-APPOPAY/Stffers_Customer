package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.LanguageListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomLanguage;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;


public class SplashActivity extends AppCompatActivity implements LanguageListener {

    // Splash screen timer
    private static final int SPLASH_TIMEOUT = 100;
    ImageView secondaryImage;
    private MyTextView tvAgree;
    private MyTextViewBold tvTapInfo;
    private MyTextViewBold tvLanguage;
    private BottomLanguage mBottomLanguage;
    private CheckBox tvCheck;

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

        tvCheck = (CheckBox) findViewById(R.id.tvCheck);
        tvAgree = (MyTextView) findViewById(R.id.tvAgree);
        tvTapInfo = (MyTextViewBold) findViewById(R.id.tvTapInfo);
        tvLanguage = (MyTextViewBold) findViewById(R.id.tvLanguage);


        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvCheck.isChecked()) {
                    Toast.makeText(SplashActivity.this, getString(R.string.info_term_condition), Toast.LENGTH_SHORT).show();
                    return;
                }
                setupUI();
            }
        });

        //Tap Agree and Continue to accept the Term of Condition.
        //String tapInfoText="message &quot;quote string 1&quot;";
        String tapInfoText1 = "<font color='#029DDC'>" + getString(R.string.info_split1) + " " + "&quot;" + getString(R.string.info_split2) + "&quot;" + "</font>" + "<font color='#029DDC'>" + " " + getString(R.string.info_split3) + "</font>";
        String tapInfoText2 = "<font color='#FB8310'>" + " " + getString(R.string.info_split4) + "</font>";
        String wholeText = tapInfoText1 + tapInfoText2;

        tvTapInfo.setText(Html.fromHtml(wholeText));
        tvTapInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCheck.setChecked(true);
            }
        });

        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanDialogue();
            }
        });


    }

    private void showLanDialogue() {
        //mBottomNotCard = new BottomNotCard();
        //mBottomNotCard.show(getSupportFragmentManager(), mBottomNotCard.getTag());
        //mBottomNotCard.setCancelable(false);

        mBottomLanguage = new BottomLanguage();
        mBottomLanguage.show(getSupportFragmentManager(), mBottomLanguage.getTag());
        mBottomLanguage.setCancelable(false);

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
        //fetch from shared preference also save to the same when applying. default is English
        //String language = MyPreferenceUtil.getInstance().getString(MyConstants.PARAM_LANGUAGE, "en");
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            ////Log.e(TAG, "attachBaseContext: english called");
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }
}

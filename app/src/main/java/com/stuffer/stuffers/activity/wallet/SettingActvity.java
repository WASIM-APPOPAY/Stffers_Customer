package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.views.MyRadioButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.StringUtils;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_UNIQUE_NUMBER;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;

public class SettingActvity extends AppCompatActivity {
    private static final String TAG = "SettingActvity";
    MyEditText edtConfirmPassword, edtNewPassword, edtOldPassword;
    MyTextView tvSubmit, tvSubmitTrans;
    private String vaultValue;
    private AlertDialog alertDialog;
    private ImageView  ivTranscPin;

    int three = 0;

    ExpandableLayout layoutExpandTrans;

    MyEditText  edtTransactionPin;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    MyRadioButton rbSpanish, rbEnglish,rbChinese;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // loadConfig();
        setContentView(R.layout.activity_setting_actvity);
        setupActionBar();
        initViews();
        mainAPIInterface = ApiUtils.getAPIService();
        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

    }

    private void initViews() {
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);



        edtTransactionPin = findViewById(R.id.edtTransactionPin);
        tvSubmitTrans = findViewById(R.id.tvSubmitTrans);


        ivTranscPin = findViewById(R.id.ivTranscPin);
        layoutExpandTrans = findViewById(R.id.layoutExpandTrans);

        rbSpanish = findViewById(R.id.rbSpanish);
        rbEnglish = findViewById(R.id.rbEnglish);
        rbChinese = findViewById(R.id.rbChinese);

        ivTranscPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three == 0) {

                    ivTranscPin.setImageResource(R.drawable.ic_add_primary);
                    layoutExpandTrans.setExpanded(true);
                    three = 1;
                } else {
                    ivTranscPin.setImageResource(R.drawable.ic_remove_primary);
                    layoutExpandTrans.setExpanded(false);
                    three = 0;
                }

            }
        });



        tvSubmitTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyTransactionPin();
            }
        });

        rbEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lang = "en";
                    setLocal(lang);
                }
            }
        });
        rbSpanish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lang = "es";
                    setLocal(lang);
                }
            }
        });

        rbChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //lang="zh-rCN";
                    lang="zh";
                    setLocal(lang);
                }
            }
        });


    }

    private void setLocal(String language) {
        DataVaultManager.getInstance(SettingActvity.this).saveLanguage(language);
        Intent intent = new Intent(SettingActvity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void verifyTransactionPin() {
        if (edtTransactionPin.getText().toString().trim().isEmpty()) {
            edtTransactionPin.setError(getString(R.string.info_old_transaction_pin));
            edtTransactionPin.requestFocus();
            return;
        }

        if (edtTransactionPin.getText().toString().trim().length() < 6) {
            edtTransactionPin.setError(getString(R.string.info_transaction_pin_length));
            edtTransactionPin.requestFocus();
            return;
        }
        Intent intentTransaction = new Intent(SettingActvity.this, TransactionPinActivity.class);
        intentTransaction.putExtra(AppoConstants.INFO, edtTransactionPin.getText().toString().trim());
        startActivity(intentTransaction);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_setting));

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }





    private void closeDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void loginRedirect() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        //for login here
        Intent intent = new Intent(SettingActvity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    /**
     * {
     * "userid" :1,
     * "password" :"welcome",
     * "question1":"a",
     * "answer1":"f",
     * "question2":"f",
     * "answer2":"D"
     * }
     */




    private void onUpdatePasswordRequest() {
        DataVaultManager.getInstance(SettingActvity.this).saveUserDetails("");
        DataVaultManager.getInstance(SettingActvity.this).saveUserAccessToken("");
        Intent intent = new Intent(SettingActvity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //fetch from shared preference also save to the same when applying. default is English
        //String language = MyPreferenceUtil.getInstance().getString(MyConstants.PARAM_LANGUAGE, "en");
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

}

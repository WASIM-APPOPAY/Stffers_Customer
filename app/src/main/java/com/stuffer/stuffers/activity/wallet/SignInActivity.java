package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.forgopassword.ForgotPasswordActvivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Token;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomPasswordPolicy;
import com.stuffer.stuffers.fragments.dialog.AreaCodeDialog;
import com.stuffer.stuffers.models.output.AuthorizationResponse;
import com.stuffer.stuffers.models.output.MappingResponse;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_FIREBASE_TOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_UNIQUE_NUMBER;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;

public class SignInActivity extends AppCompatActivity implements AreaSelectListener {

    MyTextView signup;
    MyTextView signin1, signin11;

    MyEditText edtMobile, edtPassword;
    String strMobile, strPassword;
    ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    private String TAG = "TAG";
    private AppCompatSpinner spCountry;
    private ImageView ivRefresh;


    private CountryCodePicker edtCustomerCountryCode;
    private String selectedCountryCode;
    private MyTextView skip;
    private FirebaseAuth mAuth;
    private long mLastClickTime = 0;
    MyTextView tvForgotPassword;
    private MyTextView tvPwdPolicy;
    Button tvEmailTest;
    private String mUserId;
    private DatabaseReference reference;
    private MyTextViewBold tvAreaCodeDo;
    private String selectedCountryNameCode="";
    private String mDominicaAreaCode="";
    private ArrayList<String> mAreaList;
    private AreaCodeDialog mAreaDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        tvAreaCodeDo = (MyTextViewBold) findViewById(R.id.tvAreaCodeDo);
        mAuth = FirebaseAuth.getInstance();
        mainAPIInterface = ApiUtils.getAPIService();
        edtCustomerCountryCode = findViewById(R.id.edtCustomerCountryCode);
        signup = (MyTextView) findViewById(R.id.signup);
        signin1 = (MyTextView) findViewById(R.id.signin1);
        skip = findViewById(R.id.skip);
        signin11 = findViewById(R.id.signin11);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvPwdPolicy = findViewById(R.id.tvPwdPolicy);
        getRandomNumberString();


        edtMobile = (MyEditText) findViewById(R.id.edtMobile);
        edtPassword = (MyEditText) findViewById(R.id.edtPassword);

        spCountry = (AppCompatSpinner) findViewById(R.id.spCountry);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);

        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));



        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strMobile = edtMobile.getText().toString();
                strPassword = edtPassword.getText().toString();
                if (strMobile.length() < 5) {
                    edtMobile.setFocusable(true);
                    edtMobile.setError(getString(R.string.info_enter_mobile_number));
                } else if (strPassword.length() < 5) {
                    edtPassword.setFocusable(true);
                    edtPassword.setError(getString(R.string.info_enter_password));
                } else {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        ////Log.e(TAG, "onClick: RETURN CALLED");
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (AppoPayApplication.isNetworkAvailable(SignInActivity.this)) {
                        userMapping();
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                //237,465

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(SignInActivity.this, MobileNumberRegistrationActivity.class);
                startActivity(newIntent);
                finish();
                /*Intent intent = new Intent(SignInActivity.this, ContactDemoActivity.class);
                startActivity(intent);*/
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataVaultManager.getInstance(SignInActivity.this).saveUserAccessToken("");
                DataVaultManager.getInstance(SignInActivity.this).saveUserDetails("");
                Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(i);
                finish();

            }
        });

        signin11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException();
            }
        });
        signin11.setVisibility(View.GONE);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(SignInActivity.this)) {
                    Intent intentForgot = new Intent(SignInActivity.this, ForgotPasswordActvivity.class);
                    startActivity(intentForgot);
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }
                //  getGiftCards();
            }
        });
        tvPwdPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomPasswordPolicy bottomPasswordPolicy = new BottomPasswordPolicy();
                bottomPasswordPolicy.show(getSupportFragmentManager(), bottomPasswordPolicy.getTag());
                //create();

            }
        });
        //a958b66129babb52
        // MerchantPresentedMode decode = DecoderMpm.decode("00020101021215312500034400020344100000000000006520459725303344540115802HK5913Test Merchant6002HK626001200000000000000000000005200000000000000000000007080000001063045855", MerchantPresentedMode.class);
        //MerchantPresentedMode decode = DecoderMpm.decode("00020101021215314701034400020344001584054110306520453995303840540510.005802US5918UPI QRC test K 8406009test city62600120202109142058300020350520202109142058300020350708000100016304E2FB", MerchantPresentedMode.class);
        //String param = new Gson().toJson(decode);
        //Log.e(TAG, "onCreate: " + param);


        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                if (selectedCountryNameCode.equalsIgnoreCase("DO")) {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.VISIBLE);
                } else {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.GONE);
                }
            }
        });

        tvAreaCodeDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAreaCodes();
            }
        });



    }

    private void getAreaCodes() {
        mAreaList = new ArrayList<String>();
        mAreaList.add("809");
        mAreaList.add("829");
        mAreaList.add("849");

        mAreaDialog = new AreaCodeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Area Code");
        bundle.putStringArrayList(AppoConstants.INFO, mAreaList);
        mAreaDialog.setArguments(bundle);
        mAreaDialog.setCancelable(false);
        mAreaDialog.show(getSupportFragmentManager(), mAreaDialog.getTag());

    }



    public void getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        Log.e("TAG", "getRandomNumberString: " + String.format("%06d", number));
    }

/*    public void verifyEmailId(String emailId) {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_verifying_email));
        dialog.show();

        mainAPIInterface.getEmailIdStatus(emailId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.get("message").equals(AppoConstants.SUCCESS) && !jsonObject.getBoolean("result")) {
                            //Log.e("TAG", "onResponse: Email " + jsonObject.toString());
                            //createNewUser();
                        } else {
                            Toast.makeText(SignInActivity.this, getString(R.string.error_email_already_exists), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.error_email_verification_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("tag", t.getMessage().toString());
                dialog.dismiss();

            }
        });

    }*/

    public void create(){
        String strUserEmail="support@appopay.com";
        String phWithCode="50763516303";
        mAuth.createUserWithEmailAndPassword(strUserEmail, phWithCode)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", "Mohamad Alharazi");
                            hashMap.put("imageURL", "default");
                            hashMap.put("verification", "verified");
                            hashMap.put("email_id",strUserEmail );
                            hashMap.put("phone_number", phWithCode);
                            hashMap.put("search", "Mohamad Alharazi".toString().trim().toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                        //dialog.dismiss();
                                        Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {

                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            //dialog.dismiss();
                            Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
    }

    private void userMapping() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_mapping_user));
        dialog.show();
        selectedCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
        //selectedCountryCode=selectedCountryCode+mDominicaAreaCode;

        ////Log.e(TAG, "userMapping: " + selectedCountryCode);
        mainAPIInterface.getMapping("+" + selectedCountryCode + mDominicaAreaCode+edtMobile.getText().toString().trim()).enqueue(new Callback<MappingResponse>() {
            @Override
            public void onResponse(Call<MappingResponse> call, Response<MappingResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        DataVaultManager.getInstance(SignInActivity.this).saveUniqueNumber(response.body().getResult().getUniquenumber());
                        getAccessToken();
                    }
                } else {
                    if (response.code() == 502) {
                        Toast.makeText(SignInActivity.this, getString(R.string.info_bad_request), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(SignInActivity.this, getString(R.string.info_503), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onResponse: mapping :: " + response);
                        Toast.makeText(SignInActivity.this, getString(R.string.info_user_not_exist), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MappingResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("tag", t.getMessage().toString());
            }
        });
    }

    private void getAccessToken() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage("Please wait, getting access token.");
        dialog.show();
        String strUniqueNumber = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_UNIQUE_NUMBER);
        String userName = "devglan-client";
        String password = "devglan-secret";
        String base = userName + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        String loginPassword = edtPassword.getText().toString().trim();

        mainAPIInterface.getAuthorization(authHeader, strUniqueNumber, loginPassword, "password").enqueue(new Callback<AuthorizationResponse>() {
            @Override
            public void onResponse(Call<AuthorizationResponse> call, Response<AuthorizationResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String accessToken = response.body().getAccessToken();
                    ////Log.e(TAG, "onResponse: token : " + accessToken);
                    DataVaultManager.getInstance(SignInActivity.this).saveUserAccessToken(accessToken);
                    //////Log.e("Success", new Gson().toJson(response.body()));
                    //////Log.e(TAG, "onResponse: " + accessToken);
                    //generateOtp();
                    getSignInDetails();
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.error_account_verification), Toast.LENGTH_SHORT).show();
                    ////Log.e("success", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<AuthorizationResponse> call, Throwable t) {
                dialog.dismiss();
                ////Log.e("tag", t.getMessage().toString());
            }
        });


    }

    private void generateOtp() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_generating_otp));
        dialog.show();
        JsonObject param = new JsonObject();
        param.addProperty("phone_number", "+" + selectedCountryCode + edtMobile.getText().toString().trim());
        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    response.body();
                    ////Log.e("TAG", "onResponse: " + response.body());
                    ////Log.e("TAG", "onResponse: " + new Gson().toJson(response));
                    verifyUserOtp();
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.info_otp_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                ////Log.e("tag", t.getMessage().toString());
            }
        });
    }

    private void verifyUserOtp() {
        String phNoWithCode = "+" + selectedCountryCode + edtMobile.getText().toString().trim();
        Intent intent = new Intent(SignInActivity.this, VerifyOtpActivity.class);
        intent.putExtra("phone_number", phNoWithCode);
        startActivityForResult(intent, 121);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 121) {
            if (resultCode == Activity.RESULT_OK) {
                getSignInDetails();
            }
        }
    }

    private void getSignInDetails() {

        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_verifying_user));
        dialog.show();
        String phNumber = edtMobile.getText().toString().trim();

        String vaultValue = "bearer " + DataVaultManager.getInstance(SignInActivity.this).getVaultValue(KEY_ACCESSTOKEN);
        ////Log.e(TAG, "getSignInDetails: " + vaultValue);
        String phWithDominica = mDominicaAreaCode + phNumber;

        mainAPIInterface.getLoginDetails(Long.parseLong(phWithDominica), Integer.parseInt(selectedCountryCode), vaultValue).enqueue(new Callback<JsonObject>() {
        /*mainAPIInterface.getLoginDetails(Long.parseLong(phNumber), selectedCountryCode, vaultValue).enqueue(new Callback<JsonObject>() {*/
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        //Log.e(TAG, "onResponse: " + mPrev.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();
                            //Log.e(TAG, "onResponse: 2" + jsonUserDetails);
                            DataVaultManager.getInstance(SignInActivity.this).saveUserDetails(jsonUserDetails);
                            try {
                                JSONObject obj = new JSONObject(jsonUserDetails);
                                JSONObject jsonObject = obj.getJSONObject(AppoConstants.RESULT);
                                mUserId = jsonObject.getString(AppoConstants.ID);

                                createAccountInFirebase(jsonObject.getString(AppoConstants.EMIAL), selectedCountryCode + edtMobile.getText().toString().trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
//                Log.e("tag", t.getMessage().toString());
            }
        });
    }


    private void createAccountInFirebase(String email, String password) {
        ////Log.e(TAG, "createAccountInFirebase: " + email);
        ////Log.e(TAG, "createAccountInFirebase: " + password);
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();
        //String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.info_login_successfull), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            try {

                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");

                                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_FIREBASE_TOKEN);
                                DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveUserId1(mUserId);

                                Token token = new Token(vaultValue);
                                reference.child(firebaseUser.getUid()).setValue(token);
                                /*JSONObject mJSON = new JSONObject();
                                mJSON.put("user_id", mUserId);
                                mJSON.put("fcm_token", vaultValue);
                                mJSON.put("device_id", TimeUtils.getDeviceId());


                                RegisterDevice mRegister = new RegisterDevice("https://labapi.appopay.com/api/users/registerdevice", mJSON);
                                mRegister.execute();*/

                                /*Intent mIntent = new Intent(SignInActivity.this, MyJobIntentService.class);
                                mIntent.putExtra(AppoConstants.USERID,mUserId);
                                mIntent.putExtra(AppoConstants.FCM_TOKEN,vaultValue);
                                mIntent.putExtra(AppoConstants.DEVICE_ID, TimeUtils.getDeviceId());
                                MyJobIntentService.enqueueWork(SignInActivity.this, mIntent);*/


                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            /*Wasim509@*/
                        } else {
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(getApplicationContext(), R.string.info_login_failed, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });


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

    @Override
    public void onAreaSelected(int pos) {
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
        mDominicaAreaCode = mAreaList.get(pos);
        tvAreaCodeDo.setText("Area Code : " + mDominicaAreaCode);
    }
}

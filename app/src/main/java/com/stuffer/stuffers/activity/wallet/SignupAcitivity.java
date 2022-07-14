package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.address.AutoCompleteAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.BankSelectListener;
import com.stuffer.stuffers.communicator.CarrierSelectListener;
import com.stuffer.stuffers.communicator.CountrySelectListener;
import com.stuffer.stuffers.communicator.StateSelectListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomPasswordPolicy;
import com.stuffer.stuffers.fragments.dialog.BankDialog;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.fragments.dialog.InsuranceDialog;
import com.stuffer.stuffers.fragments.dialog.StateDialogFragment;
import com.stuffer.stuffers.fragments.finance_fragment.FileUtils;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.Country.State;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.PasswordUtil;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupAcitivity extends AppCompatActivity implements BankSelectListener, CountrySelectListener, View.OnClickListener, CarrierSelectListener, StateSelectListener {
    private static final String TAG = "SignupAcitivity";
    MyTextView btnSignUp, btnSignIn;
    MyEditText txtUserName, txtUserEmail, txtUserPassword;
    CheckBox checkbocremember;
    ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String strUserName, strUserEmail, strUserPassword;
    String phWithCode, mobileNumber, phoneCode;
    private String fName;
    private String lName;
    private RadioButton rbEnglish, rbSpanish, rbChinese;
    private String language;
    private MyTextView tvCountryId;
    List<Result> mListCountry;
    int mCountyId = 0;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ImageView ivPolicy, image1;
    LinearLayout llIdType;
    private ArrayList<String> mListType;
    private InsuranceDialog mIdTypeDialog;
    private MyTextView tvIdType;
    private MyTextView tvScanDocs;
    private File photoFile;
    private Calendar newCalendar;
    private String mDob = null;
    private MyEditText edtDob, edFocus, edIdFoucs, edtExpirayDate, edFocusIdExp;
    private AutoCompleteTextView placesAutocomplete;
    AutoCompleteAdapter adapter;
    private PlacesClient placesClient;
    private List<State> mListState;
    private MyTextView edState;
    private int mStateId;
    private String mExpiry = null;
    private MyEditText tvIdNo;
    private String mIdName;
    private LinearLayout layoutScan;
    private ProgressDialog progressDialog;
    private String stringExtraPath = null;
    MainAPIInterface apiServiceOCR;
    private Dialog dialogExtract;
    private LinearLayout llBankType;
    private MyTextView tvBankName;
    private ArrayList<String> mBankList;
    private String mCountryNameCode;
    private BankDialog mBankDialog;
    private String mReqDateFormat = "yyyy-MM-dd";
    private boolean mAllow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail_layout);

        apiServiceOCR = ApiUtils.getApiServiceOCR();
        mainAPIInterface = ApiUtils.getAPIService();
        newCalendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();


        btnSignUp = (MyTextView) findViewById(R.id.btnSignUp);
        btnSignIn = (MyTextView) findViewById(R.id.btnSignIn);
        layoutScan = (LinearLayout) findViewById(R.id.layoutScan);
        txtUserName = (MyEditText) findViewById(R.id.txtUserName);
        edtDob = (MyEditText) findViewById(R.id.edtDob);


        edFocus = (MyEditText) findViewById(R.id.edFocus);
        edIdFoucs = (MyEditText) findViewById(R.id.edIdFoucs);
        placesAutocomplete = (AutoCompleteTextView) findViewById(R.id.placesAutocomplete);
        txtUserEmail = (MyEditText) findViewById(R.id.txtUserEmail);
        tvIdNo = (MyEditText) findViewById(R.id.tvIdNo);
        txtUserPassword = (MyEditText) findViewById(R.id.txtUserPassword);
        checkbocremember = (CheckBox) findViewById(R.id.checkbocremember);
        tvCountryId = (MyTextView) findViewById(R.id.tvCountryId);
        rbEnglish = (RadioButton) findViewById(R.id.rbEnglish);
        rbSpanish = (RadioButton) findViewById(R.id.rbSpanish);
        rbChinese = (RadioButton) findViewById(R.id.rbChineseS);
        llIdType = (LinearLayout) findViewById(R.id.llIdType);
        llBankType = (LinearLayout) findViewById(R.id.llBankType);

        tvBankName = (MyTextView) findViewById(R.id.tvBankName);

        ivPolicy = (ImageView) findViewById(R.id.ivPolicy);
        image1 = (ImageView) findViewById(R.id.image1);
        tvIdType = (MyTextView) findViewById(R.id.tvIdType);
        edtExpirayDate = (MyEditText) findViewById(R.id.edtExpirayDate);
        edFocusIdExp = (MyEditText) findViewById(R.id.edFocusIdExp);
        edState = (MyTextView) findViewById(R.id.edState);

        tvScanDocs = (MyTextView) findViewById(R.id.tvScanDocs);

        phWithCode = getIntent().getStringExtra(AppoConstants.PHWITHCODE);
        mobileNumber = getIntent().getStringExtra(AppoConstants.MOBILENUMBER);
        phoneCode = getIntent().getStringExtra(AppoConstants.PHONECODE);
        mCountryNameCode = getIntent().getStringExtra(AppoConstants.COUNTRYNAMECODE);

        /*
        phWithCode = "19836683269";
        mobileNumber = "9836683269";
        phoneCode = "1";
        mCountryNameCode = "DO";
        */
        //SignupAcitivity: onCreate: 19836683269 9836683269 1 DO
        //Log.e(TAG, "onCreate: PhoneNo With Code : " + phWithCode + " Mobile Number : " + mobileNumber + " Phone Code : " + phoneCode + " CountryNameCode : " + mCountryNameCode);
        if (mCountryNameCode.equalsIgnoreCase("IN")) {
            mCountryNameCode = "IND";
        }
        if (mCountryNameCode.equalsIgnoreCase("PA")) {
            //llBankType.setVisibility(View.VISIBLE);
            llBankType.setVisibility(View.GONE);
        } else if (mCountryNameCode.equalsIgnoreCase("DO")) {
            llBankType.setVisibility(View.VISIBLE);
        } else {
            llBankType.setVisibility(View.GONE);
        }
        llBankType.setOnClickListener(this);
        tvBankName.setOnClickListener(this);
        //mCountryNameCode = "PA";

        findViewById(R.id.btnSignUp1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createAccountInFirebase();
            }
        });

        findViewById(R.id.btnSignUp1).setVisibility(View.GONE);


        btnSignUp.setOnClickListener(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newIntent = new Intent(SignupAcitivity.this, SignInActivity.class);
                startActivity(newIntent);
                finish();

            }
        });

        getCountryList();

        /*tvCountryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListCountry != null && mListCountry.size() > 0) {
                    CountryDialogFragment countryDialogFragment = new CountryDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListCountry);
                    countryDialogFragment.setArguments(bundle);
                    countryDialogFragment.setCancelable(false);
                    countryDialogFragment.show(getSupportFragmentManager(), countryDialogFragment.getTag());

                }
            }
        });*/

        ivPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomPasswordPolicy bottomPasswordPolicy = new BottomPasswordPolicy();
                bottomPasswordPolicy.show(getSupportFragmentManager(), bottomPasswordPolicy.getTag());
            }
        });

        llIdType.setOnClickListener(this);
        tvIdType.setOnClickListener(this);

        tvScanDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkCameraPermissionTop();
                } else {
                    openCameraActivity();
                }

            }
        });

        edtDob.setOnClickListener(this);
        edtExpirayDate.setOnClickListener(this);
        ImageView btnClearAll = (ImageView) findViewById(R.id.btnClearAll);

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesAutocomplete.setText("");
                placesAutocomplete.setAdapter(adapter);
                placesAutocomplete.setEnabled(true);
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }

        placesClient = Places.createClient(this);
        initAutoCompleteTextView();

        edState.setOnClickListener(this);

    }

    private void initAutoCompleteTextView() {
        placesAutocomplete.setThreshold(1);
        placesAutocomplete.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        placesAutocomplete.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }
                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            String address = task.getPlace().getAddress();
                            String name = task.getPlace().getName();
                            placesAutocomplete.setAdapter(null);
                            placesAutocomplete.setText(name + "," + address);
                            placesAutocomplete.setEnabled(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void openCameraActivity() {
        Intent intentCamera = new Intent(SignupAcitivity.this, CameraActivity.class);
        startActivityForResult(intentCamera, 777);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 988) {
            boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            Log.e(TAG, "onRequestPermissionsResult:Manage ::  " + writePermission + "==" + cameraPermission + "==" + readPermission);
            if (readPermission && writePermission && cameraPermission) {
                openCameraActivity();
            } else {
                Toast.makeText(SignupAcitivity.this, "please allow permission to work properly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getDateOfBirth() {
        Calendar minCal = Calendar.getInstance();
        minCal.set(1950, 0, 1);
        DatePickerDialog StartTime = new DatePickerDialog(SignupAcitivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");
                mDob = dateFormatter.format(newDate.getTime());
                edtDob.setText("Dob : " + dateFormatter.format(newDate.getTime()));
                edFocus.setVisibility(View.GONE);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        StartTime.getDatePicker().setMaxDate(new Date().getTime());
        StartTime.getDatePicker().setMinDate(minCal.getTimeInMillis());
        StartTime.setCanceledOnTouchOutside(false);
        StartTime.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermissionTop() {

        if (ContextCompat.checkSelfPermission(SignupAcitivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignupAcitivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignupAcitivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 988);
        } else {
            openCameraActivity();
        }
    }


    @Override
    public void onCarrierSelect(int pos) {
        tvIdType.setText(mListType.get(pos));
        mIdName = mListType.get(pos);
        mIdTypeDialog.dismiss();
        layoutScan.setVisibility(View.VISIBLE);

    }

    private void getCountryList() {
        dialog = new ProgressDialog(SignupAcitivity.this);
        dialog.setMessage(getString(R.string.info_getting_country_code));
        dialog.show();
        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                dialog.dismiss();
                //Log.e(TAG, "onResponse: " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                        disableSelectCountry();
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void disableSelectCountry() {
        for (int i = 0; i < mListCountry.size(); i++) {
            if (mListCountry.get(i).getCountrycode().equalsIgnoreCase(mCountryNameCode)) {
                String country = " ( " + mListCountry.get(i).getCountrycode() + " )  " + mListCountry.get(i).getCountryname();
                tvCountryId.setText("Selected Country : " + country);
                mCountyId = mListCountry.get(i).getId();
                mListState = new ArrayList<>();
                mListState = mListCountry.get(i).getStates();
            }

        }
    }


    private void verifyUserDetailsBeforeSubmit() {
        String fullName = txtUserName.getText().toString().trim();
        if (!fullName.contains(" ")) {
            txtUserName.setFocusable(true);
            txtUserName.setError(getString(R.string.info_enter_first_name_lasr_name));
            return;
        }

        if (!txtUserEmail.getText().toString().trim().matches(emailPattern)) {
            txtUserEmail.setFocusable(true);
            txtUserEmail.setError(getString(R.string.info_enter_emial_id));
            return;

        }
        if (tvCountryId.getText().toString().trim().equalsIgnoreCase(getString(R.string.info_please_select_your_country))) {
            Toast.makeText(this, R.string.info_select_country_first, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!rbEnglish.isChecked() && !rbSpanish.isChecked()) {
            Toast.makeText(this, getString(R.string.info_seletct_prefer_language), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkbocremember.isChecked()) {
            Toast.makeText(this, R.string.info_please_accept_term_condition, Toast.LENGTH_SHORT).show();
            return;
        }

        if (rbEnglish.isChecked()) {
            language = "English";
            DataVaultManager.getInstance(SignupAcitivity.this).saveLanguage("en");
        }

        if (rbSpanish.isChecked()) {
            language = "Spanish";
            DataVaultManager.getInstance(SignupAcitivity.this).saveLanguage("es");
        }

        /*if (rbSpanish.isChecked()) {
            language = "Chinese";
            DataVaultManager.getInstance(SignupAcitivity.this).saveLanguage("zh");

        }*/


        String[] nameArray = txtUserName.getText().toString().split(" ");

        fName = nameArray[0];
        lName = nameArray[1];

        //verifyMobileNumber(phoneCode + mobileNumber);
        verifyMobileNumber(phWithCode);
        //createNewUser();


    }

    private void verifyMobileNumber(String phoneNumber) {
        dialog = new ProgressDialog(SignupAcitivity.this);
        dialog.setMessage(getString(R.string.info_verifying_mobile_number));
        dialog.show();
        mainAPIInterface.getMobileNUmberStatus(phoneNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    //Log.e("TAG", "onResponse: str" + str);
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("message").equalsIgnoreCase("success") && jsonObject.getBoolean("result")) {
                            verifyEmailId(txtUserEmail.getText().toString().trim());
                        } else {
                            Toast.makeText(SignupAcitivity.this, getString(R.string.error_phone_number_already_exists), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //if (obj.get("message").equals(AppoConstants.SUCCESS)) {

                } else {
                    //Log.e("TAG", "onResponse: failed called");
                    Toast.makeText(SignupAcitivity.this, getString(R.string.error_phone_verification_failed), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });

    }


    public void verifyEmailId(String emailId) {
        dialog = new ProgressDialog(SignupAcitivity.this);
        dialog.setMessage(getString(R.string.info_verifying_email));
        dialog.show();

        RequestBody mRequestBody =
                RequestBody.create(MediaType.parse("text/plain"), emailId);

        mainAPIInterface.getEmailStatusNew(mRequestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.get("message").equals(AppoConstants.SUCCESS) && !jsonObject.getBoolean("result")) {
                            //Log.e("TAG", "onResponse: Email " + jsonObject.toString());
                            createNewUser();
                        } else {
                            Toast.makeText(SignupAcitivity.this, getString(R.string.error_email_already_exists), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SignupAcitivity.this, getString(R.string.error_email_verification_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });

    }


    /**
     * {
     * "firstName": "TEST",
     * "lastName": "USER",
     * "username": "917702255671",
     * "password": "welcome",
     * "email": "test@gmail.com",
     * "mobilenumber": 7702255671,
     * "phonecode": "91",
     * "role": [
     * "USER"
     * ],
     * "accountexpired": false,
     * "credentialsexpired": false,
     * "accountlocked": false,
     * "enabled": true,
     * "customerdetails": {
     * "firstName": "TEST",
     * "lastName": "USER"
     * <p>
     * }
     * }
     */

    /*200
    {
       "messgahe":"success"
       "result" :1
       "maerchant":"another object"
    }*/

    public void createNewUser() {
        JsonObject index = new JsonObject();
        index.addProperty(AppoConstants.FIRSTNAME, fName);
        index.addProperty(AppoConstants.LASTNAME, lName);
        index.addProperty(AppoConstants.USERNAME, phWithCode);
        index.addProperty(AppoConstants.PASSWORD, txtUserPassword.getText().toString().trim());
        index.addProperty(AppoConstants.EMIAL, txtUserEmail.getText().toString().trim());
        index.addProperty(AppoConstants.MOBILENUMBER, Long.parseLong(mobileNumber));
        index.addProperty(AppoConstants.PHONECODE, phoneCode);
        index.addProperty(AppoConstants.ACCOUNTEXPIRED, false);
        index.addProperty(AppoConstants.CREDENTIALSEXPIRED, false);
        index.addProperty(AppoConstants.ACCOUNTLOCKED, false);
        index.addProperty(AppoConstants.ENABLE, true);
        index.addProperty(AppoConstants.COUNTRYID, mCountyId);
        index.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
        JsonArray roleArray = new JsonArray();
        roleArray.add("USER");
        index.add(AppoConstants.ROLE, roleArray);
        JsonObject customerDetails = new JsonObject();
        customerDetails.addProperty(AppoConstants.FIRSTNAME, fName.toUpperCase());
        customerDetails.addProperty(AppoConstants.LASTNAME, lName.toUpperCase());
        customerDetails.addProperty(AppoConstants.DOB, mDob);
        customerDetails.addProperty(AppoConstants.COUNTRYID, mCountyId);
        customerDetails.addProperty(AppoConstants.STATEID, mStateId);
        customerDetails.addProperty(AppoConstants.EXPIRYDATE, mExpiry);
        customerDetails.addProperty(AppoConstants.ADDRESS, placesAutocomplete.getText().toString().trim());
        customerDetails.addProperty(AppoConstants.IDNUMBER, tvIdNo.getText().toString().trim());
        customerDetails.addProperty(AppoConstants.IDTYPE, mIdName);
        index.add(AppoConstants.CUSTOMERDETAILS, customerDetails);
        //Log.e("TAG", "createNewUser: " + index.toString());

        dialog = new ProgressDialog(SignupAcitivity.this);
        dialog.setMessage("Please wait, creating your account");
        dialog.show();


        mainAPIInterface.postCreateUserAccount(index).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.e(TAG, "onResponse: "+response );
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("message").equalsIgnoreCase(AppoConstants.SUCCESS) && jsonObject.getInt("result") == 1) {
                            createAccountInFirebase();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(SignupAcitivity.this, "Error Code : 500", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupAcitivity.this, getString(R.string.error_account_creation_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SignupAcitivity.this, getString(R.string.error_request_account_creation_failed), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });


    }


    @Override
    public void onCountrySelected(String countryName, String countryId, int countryCode, int pos) {
        for (int i = 0; i < mListCountry.size(); i++) {
            if (countryName.equalsIgnoreCase(mListCountry.get(i).getCountryname())) {
                mCountyId = mListCountry.get(i).getId();
                break;
            }
        }
        tvCountryId.setText(countryName);
    }


    private void createAccountInFirebase() {
        dialog = new ProgressDialog(SignupAcitivity.this);
        dialog.setMessage(getString(R.string.info_creating_account));
        dialog.show();
        strUserEmail = txtUserEmail.getText().toString().trim();
        strUserPassword = txtUserPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(strUserEmail, phWithCode)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("area_code",String.valueOf(phoneCode));
                            hashMap.put("id", userid);
                            hashMap.put("username", txtUserName.getText().toString());
                            hashMap.put("imageURL", "default");
                            hashMap.put("verification", "verified");

                            hashMap.put("email_id", txtUserEmail.getText().toString().trim());
                            hashMap.put("phone_number", phWithCode);
                            hashMap.put("search", txtUserName.getText().toString().trim().toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(SignupAcitivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            //Log.e(TAG, "onComplete: exception called");
                            // i need to add one more condition if user already register in firebase when some one delete account manually
                            /*Exception exception = task.getException();
                            assert exception != null;
                            exception.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.info_registraion_failed_try_later), Toast.LENGTH_LONG).show();*/
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            Intent intent = new Intent(SignupAcitivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llIdType) {
            getSelectedId();
        } else if (view.getId() == R.id.tvIdType) {
            getSelectedId();
        } else if (view.getId() == R.id.llBankType) {
            getBankNames();
        } else if (view.getId() == R.id.tvBankName) {
            getBankNames();
        } else if (view.getId() == R.id.edtDob) {
            getDateOfBirth();
        } else if (view.getId() == R.id.edtExpirayDate) {
            getDateOfExpiry();
        } else if (view.getId() == R.id.edState) {
            if (mListCountry != null && mListCountry.size() > 0) {
                StateDialogFragment stateDialogFragment = new StateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.STATE, (ArrayList<? extends Parcelable>) mListState);
                stateDialogFragment.setArguments(bundle);
                stateDialogFragment.setCancelable(false);
                stateDialogFragment.show(getSupportFragmentManager(), stateDialogFragment.getTag());
            }
        } else if (view.getId() == R.id.btnSignUp) {
            strUserName = txtUserName.getText().toString().trim();
            if (tvIdType.getText().toString().trim().equalsIgnoreCase("Please Select Id Type")) {
                edIdFoucs.setVisibility(View.VISIBLE);
                edIdFoucs.setFocusable(true);
                edIdFoucs.setError("please select your id type");
                edIdFoucs.requestFocus();
                return;
            }

            if (stringExtraPath == null) {
                Toast.makeText(SignupAcitivity.this, "Please capture your Id", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strUserName.length() < 5) {
                txtUserName.setError(getString(R.string.info_enter_name));
                txtUserName.requestFocus();
                return;
            }

            if (mDob == null) {
                edFocus.setVisibility(View.VISIBLE);
                edFocus.setError("Please select your date of Birth!.");
                edFocus.requestFocus();
                return;
            }

            if (tvIdNo.getText().toString().trim().isEmpty()) {
                tvIdNo.setError("Please enter your id number");
                tvIdNo.requestFocus();
                return;
            }

            if (mExpiry == null) {
                edFocusIdExp.setVisibility(View.VISIBLE);
                edFocusIdExp.setError("Please select date of Expiry!.");
                edFocusIdExp.requestFocus();
                return;
            }

            if (placesAutocomplete.getText().toString().trim().isEmpty()) {
                placesAutocomplete.setError(getString(R.string.info_your_address));
                placesAutocomplete.requestFocus();
                placesAutocomplete.setFocusable(true);
                return;
            }

            strUserEmail = txtUserEmail.getText().toString().trim();

            if (strUserEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(strUserEmail).matches()) {
                txtUserEmail.setError("please enter valid email id");
                txtUserEmail.setFocusable(true);
                txtUserEmail.requestFocus();
                return;
            }

            if (!PasswordUtil.PASSWORD_PATTERN.matcher(txtUserPassword.getText().toString().trim()).matches()) {
                txtUserPassword.setFocusable(true);
                txtUserPassword.setError("please follow the pattern");
                txtUserPassword.requestFocus();
                return;
            }

            if (edState.getText().toString().trim().equalsIgnoreCase("please select your state")) {
                Toast.makeText(SignupAcitivity.this, "please select your state", Toast.LENGTH_SHORT).show();
                return;
            }

            if (AppoPayApplication.isNetworkAvailable(SignupAcitivity.this)) {
                verifyUserDetailsBeforeSubmit();
            } else {
                Toast.makeText(SignupAcitivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
            }


        }
    }


    public void getDateOfExpiry() {
        Calendar minCal = Calendar.getInstance();
        int i1 = minCal.get(Calendar.YEAR) + 10;
        minCal.set(i1, 11, 31);
        DatePickerDialog StartTime = new DatePickerDialog(SignupAcitivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");
                mExpiry = dateFormatter.format(newDate.getTime());
                edtExpirayDate.setText("Expiry Date : " + dateFormatter.format(newDate.getTime()));
                edFocusIdExp.setVisibility(View.GONE);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMaxDate(minCal.getTimeInMillis());
        StartTime.setCanceledOnTouchOutside(false);
        StartTime.show();
    }

    private void getSelectedId() {
        mListType = new ArrayList<>();
        mListType.add("Passport");
        mListType.add("National ID");
        mListType.add("Driver's license");
        mIdTypeDialog = new InsuranceDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Id Type");
        bundle.putStringArrayList(AppoConstants.INFO, mListType);
        mIdTypeDialog.setArguments(bundle);
        mIdTypeDialog.setCancelable(false);
        mIdTypeDialog.show(getSupportFragmentManager(), mIdTypeDialog.getTag());
    }


    private void getBankNames() {
        mBankList = new ArrayList<String>();
        /*if (mCountryNameCode.equalsIgnoreCase("PA")) {
            mBankList.add("CrediCorp Bank");
        } else*/
        if (mCountryNameCode.equalsIgnoreCase("DO")) {
            mBankList.add("COOPSME");
            mBankList.add("COOPEMERO");
            mBankList.add("COOPETEX");
            mBankList.add("COOP-HERRERA");
        }

        mBankDialog = new BankDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Bank Name");
        bundle.putStringArrayList(AppoConstants.INFO, mBankList);
        mBankDialog.setArguments(bundle);
        mBankDialog.setCancelable(false);
        mBankDialog.show(getSupportFragmentManager(), mBankDialog.getTag());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String stringExtra = data.getStringExtra(AppoConstants.INSURANCE_TYPE);
                tvIdType.setText(stringExtra);
                edIdFoucs.setVisibility(View.GONE);
            }

        } else if (requestCode == 777 && resultCode == Activity.RESULT_OK) {
            stringExtraPath = data.getStringExtra(AppoConstants.IMAGE_PATH);
            Log.e(TAG, "onActivityResult: " + stringExtraPath);

            image1.setVisibility(View.VISIBLE);
            DataVaultManager.getInstance(SignupAcitivity.this).saveIdImagePath(stringExtraPath);
            Glide.with(SignupAcitivity.this)
                    .load(stringExtraPath)
                    .into(image1);

            File imgFile = new File(stringExtraPath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //uploadFile(stringExtraPath);
            JsonObject mSent = new JsonObject();
            mSent.addProperty("image", imageString);
            getDataFromImage(mSent);


        }
    }

    String[] mDateFormat = {"dd-MM-yyyy", "dd-yyyy-MM", "MM-dd-yyyy", "MM-yyyy-dd", "yyyy-MM-dd", "yyyy-dd-MM", "dd/MM/yyyy", "dd/yyyy/MM", "MM/dd/yyyy", "MM/yyyy/dd", "yyyy/MM/dd", "yyyy/dd/MM"};

    public boolean isValidFormat1(String param) {

        if (param.matches("[0-9]{2}[-][0-9]{2}[-][0-9]{4}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat2(String param) {

        //if (param.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}")) {
        if (param.matches("[0-9]{2} [-] [0-9]{4} [-] [0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-yyyy-MM");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat3(String param) {

        if (param.matches("[0-9]{2}[-][0-9]{2}[-][0-9]{4}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat4(String param) {

        if (param.matches("[0-9]{2}[-][0-9]{4}[-][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy-dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat5(String param) {

        if (param.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat6(String param) {

        if (param.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat7(String param) {

        if (param.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat8(String param) {

        //if (param.matches("[0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4}")) {
        if (param.matches("[0-9]{2} [/] [0-9]{4} [/] [0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/yyyy/MM");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat9(String param) {

        if (param.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyyy");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat10(String param) {

        if (param.matches("[0-9]{2}[/][0-9]{4}[/][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy/dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat11(String param) {

        if (param.matches("[0-9]{4}[/][0-9]{2}[/][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidFormat12(String param) {
        if (param.matches("[0-9]{4}[/][0-9]{2}[/][0-9]{2}")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
            sdf.setLenient(false);
            try {
                Date d1 = sdf.parse(param);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }


    private void getDataFromImage(JsonObject mRoot) {
        showDialog();
        apiServiceOCR.getExtractData(mRoot).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                close();
                //Log.e(TAG, "onResponse: "+response.body() );
                if (response.isSuccessful()) {
                    tvIdNo.setText("");
                    edtExpirayDate.setText("");
                    edtDob.setText("");
                    txtUserName.setText("");
                    String responseExtract = new Gson().toJson(response.body());
                    try {
                        JSONObject mExtractROOT = new JSONObject(responseExtract);
                        JSONObject mExtractJSON = mExtractROOT.getJSONObject(AppoConstants.DATA);
                        if (mExtractJSON.getString(Constants.ERRORCODE).equalsIgnoreCase("0")) {
                            if (mExtractJSON.has("documentNumber")) {
                                if (mExtractJSON.has("documentNumber")) {
                                    tvIdNo.setText(mExtractJSON.getString("documentNumber"));
                                }
                                if (mExtractJSON.has("dateOfExpiry")) {
                                    mExpiry = mExtractJSON.getString("dateOfExpiry");
                                    edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                    if (isValidFormat1(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 1 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[0], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat2(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 2 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[1], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat3(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 3 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[2], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat4(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 4 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[3], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat5(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 5 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[4], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat6(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 6 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[5], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat7(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 7 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[6], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat8(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 8 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[7], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat9(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 9 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[8], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat10(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 10 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[9], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat11(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 11 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[10], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat12(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 12 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[11], mReqDateFormat, mExpiry);
                                        edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else {
                                        edFocusIdExp.setVisibility(View.VISIBLE);
                                        edFocusIdExp.setError("required yyyy-MM-DD ,tap on it for manually input");
                                        edFocusIdExp.setTextColor(Color.RED);
                                        edFocusIdExp.setTextSize(12);
                                        edFocusIdExp.setFocusable(true);
                                        edFocusIdExp.requestFocus();


                                        //Log.e(TAG, "onResponse: Expiry no format called");
                                    }
                                } else {
                                    edFocusIdExp.setVisibility(View.VISIBLE);
                                    edFocusIdExp.setError("required yyyy-MM-DD ,tap on it for manually input");
                                    edFocusIdExp.setTextColor(Color.RED);
                                    edFocusIdExp.setTextSize(12);
                                    edFocusIdExp.setFocusable(true);
                                    edFocusIdExp.requestFocus();
                                }

                                if (mExtractJSON.has("dateOfBirth")) {
                                    mDob = mExtractJSON.getString("dateOfBirth");
                                    edtDob.setText("Dob : " + mDob);
                                    if (isValidFormat1(mDob)) {
                                        //Log.e(TAG, "onResponse: format 1 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[0], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat2(mDob)) {
                                        //Log.e(TAG, "onResponse: format 2 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[1], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat3(mDob)) {
                                        //Log.e(TAG, "onResponse: format 3 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[2], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat4(mDob)) {
                                        //Log.e(TAG, "onResponse: format 4 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[3], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat5(mDob)) {
                                        //Log.e(TAG, "onResponse: format 5 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[4], mReqDateFormat, mDob);
                                        //mDob = TimeUtils.getRequiredFormatDate(mDateFormat[4], "yyyy/MM/dd", mDob);

                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat6(mDob)) {
                                        //Log.e(TAG, "onResponse: format 6 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[5], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat7(mDob)) {
                                        //Log.e(TAG, "onResponse: format 7 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[6], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat8(mDob)) {
                                        //Log.e(TAG, "onResponse: format 8 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[7], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat9(mDob)) {
                                        //Log.e(TAG, "onResponse: format 9 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[8], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat10(mDob)) {
                                        //Log.e(TAG, "onResponse: format 10 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[9], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat11(mDob)) {
                                        //Log.e(TAG, "onResponse: format 11 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[10], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else if (isValidFormat12(mDob)) {
                                        //Log.e(TAG, "onResponse: format 12 called");
                                        mDob = TimeUtils.getRequiredFormatDate(mDateFormat[11], mReqDateFormat, mDob);
                                        edtDob.setText("Dob : " + mDob);
                                        edFocus.setVisibility(View.GONE);
                                    } else {
                                        //Log.e(TAG, "onResponse: Dob no format called");
                                        edFocus.setVisibility(View.VISIBLE);
                                        edFocus.setError("required yyyy-MM-DD ,tap on it for manually input.");
                                        edFocus.setTextColor(Color.RED);
                                        edFocus.setTextSize(12);
                                        edFocus.setFocusable(true);
                                        edFocus.requestFocus();
                                    }
                                } else {
                                    //Log.e(TAG, "onResponse: Dob no format called");
                                    //21 Decimber 2024
                                    //21 Decimber 2024
                                    //2024-12-21
                                    edFocus.setVisibility(View.VISIBLE);
                                    edFocus.setError("required yyyy-MM-DD ,tap on it for manually input.");
                                    edFocus.setTextColor(Color.RED);
                                    edFocus.setTextSize(12);
                                    edFocus.setFocusable(true);
                                    edFocus.requestFocus();
                                }

                                if (mExtractJSON.has("name")) {
                                    txtUserName.setText(mExtractJSON.getString("name"));
                                } else if (mExtractJSON.has("surname")) {
                                    txtUserName.setText(mExtractJSON.getString("surname"));
                                }

                            }

                        } else {

                            showErrorExtract();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                close();
                //Log.e(TAG, "onFailure: " + t.getMessage());
                mAllow=true;
                Toast.makeText(SignupAcitivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        /*AndroidNetworking.post("http://3.141.54.113:8889/ocr/idcard_base64")
                .addJSONObjectBody(mRoot)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        close();
                        Log.e(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        close();
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });*/

    }

    private void showErrorExtract() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvInfo.setText(getString(R.string.info_fund_wallet));
        tvHeader.setText("Scanning Error");

        tvInfo.setText("Please scan your id again.");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectCamera();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExtract.dismiss();
            }
        });
        btnNo.setVisibility(View.INVISIBLE);

        builder.setView(dialogLayout);
        dialogExtract = builder.create();
        dialogExtract.setCanceledOnTouchOutside(false);
        dialogExtract.show();

    }

    private void redirectCamera() {
        if (dialogExtract != null) {
            dialogExtract.dismiss();
        }
        openCameraActivity();
    }

    private void startScanAgain() {
        dialogExtract.dismiss();
    }


    private void uploadFile(String path) {
        MultipartBody.Part parts;
        RequestBody phoneNumber;
        RequestBody fileName;
        File file = new File(path);
        String name = file.getName();
        String numberWithCountryCode = phWithCode;
        phoneNumber = createPartFromString(numberWithCountryCode);
        fileName = createPartFromString("id");
        //Log.e(TAG, "uploadImage: name1 :: " + name);
        String nameFile = null;

        if (name.contains(".jpeg")) {
            nameFile = "id_" + numberWithCountryCode + ".jpeg";
        } else if (name.contains(".jpg")) {
            nameFile = "id_" + numberWithCountryCode + ".jpg";
        } else if (name.contains(".png")) {
            nameFile = "id_" + numberWithCountryCode + ".png";
        }

        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        parts = MultipartBody.Part.createFormData("file", nameFile, requestBody);
        upload(phoneNumber, fileName, parts);


    }

    private void showDialog() {
        progressDialog = new ProgressDialog(SignupAcitivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
    }

    private void close() {
        progressDialog.dismiss();
    }

    private void upload(RequestBody phoneNumber, RequestBody fileName, MultipartBody.Part parts) {
        showDialog();

        mainAPIInterface.uploadFileForOpenAccount(parts, phoneNumber, fileName).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.e("TAG", "onResponse: " + response.toString());
                close();

                /*if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(body.toString());
                        if (jsonObject.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            mCount = mCount + 1;
                            uploadData(mCount);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }*/
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
                Log.e("TAG", "onFailure: " + message);
            }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @Override
    public void onStateSelected(String statename, int stateid) {
        mStateId = stateid;
        //Log.e(TAG, "onStateSelected: " + stateid);
        edState.setText("Selected State : " + statename);
    }

    @Override
    public void onBankSelect(int pos) {
        tvBankName.setText(mBankList.get(pos));
        //mIdName = mListType.get(pos);
        mBankDialog.dismiss();
        //layoutScan.setVisibility(View.VISIBLE);
    }

    //Select your bank



   /* public void update(){
        Objects.requireNonNull(mAuth.getCurrentUser()).updatePassword("wasim508").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                }

            }
        });
    }*/

}

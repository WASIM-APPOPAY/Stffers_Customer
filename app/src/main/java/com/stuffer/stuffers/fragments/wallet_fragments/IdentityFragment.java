package com.stuffer.stuffers.fragments.wallet_fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.CircularArray;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.SignupAcitivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomPasswordPolicy;
import com.stuffer.stuffers.fragments.dialog.InsuranceDialog;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.PasswordUtil;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IdentityFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "IdentityFragment";
    private View mView;
    private MyEditText edtDob, edtExpiryDate, edFocus, edFocusIdExp;

    private String mDob;
    private Calendar newCalendar;
    private String mExpiry;
    private LinearLayout llIdType, layoutDetails, layoutScan;
    private MyTextView tvIdType;
    private MyTextViewBold  tvScanDocs;
    private InsuranceDialog mIdTypeDialog;
    private ArrayList<String> mListType;
    private String stringExtraPath;
    private ImageView imageId;
    private ProgressDialog progressDialog;
    private MyEditText txtUserName;
    private MyEditText tvIdNo;
    private MyEditText txtUserPassword;
    private ImageView ivPolicy;
    private MyTextView btnSignUp;
    MainAPIInterface apiServiceOCR;
    private MainAPIInterface mainAPIInterface;
    private String mReqDateFormat = "yyyy-MM-dd";
    private boolean mAllow = false;
    private androidx.appcompat.app.AlertDialog dialogExtract;
    String mParamNameCode, mParamCountryCode, mParamMobile, mParamEmail, mParamAdd, mParamCountryId, mParamZip, mParamCity, mParamStateId;
    private String fullName;
    private String fName, lName;
    private String mIdName;
    private ProgressDialog mProgress;
    private String strUserEmail, strUserPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    public IdentityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamNameCode = getArguments().getString(AppoConstants.COUNTRYNAMECODE);
            mParamCountryCode = getArguments().getString(AppoConstants.COUNTRYCODE);
            mParamMobile = getArguments().getString(AppoConstants.MOBILENUMBER);
            mParamEmail = getArguments().getString(AppoConstants.EMIAL_ID);
            mParamAdd = getArguments().getString(AppoConstants.ADDRESS);
            mParamCountryId = getArguments().getString(AppoConstants.COUNTRYID);
            mParamStateId = getArguments().getString(AppoConstants.STATEID);
            mParamZip = getArguments().getString(AppoConstants.ZIPCODE2);
            mParamCity = getArguments().getString(AppoConstants.CITY);
        }
    }
    //https://github.com/badoualy/stepper-indicator

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        apiServiceOCR = ApiUtils.getApiServiceOCR();
        mainAPIInterface = ApiUtils.getAPIService();
        newCalendar = Calendar.getInstance();
        mView = inflater.inflate(R.layout.fragment_identity, container, false);
        mAuth = FirebaseAuth.getInstance();
        edtDob = mView.findViewById(R.id.edtDob);
        edFocus = mView.findViewById(R.id.edFocus);
        edFocusIdExp = mView.findViewById(R.id.edFocusIdExp);
        edtExpiryDate = mView.findViewById(R.id.edtExpiryDate);
        tvIdType = mView.findViewById(R.id.tvIdType);

        llIdType = (LinearLayout) mView.findViewById(R.id.llIdType);
        layoutDetails = (LinearLayout) mView.findViewById(R.id.layoutDetails);
        layoutScan = (LinearLayout) mView.findViewById(R.id.layoutScan);
        tvScanDocs = (MyTextViewBold) mView.findViewById(R.id.tvScanDocs);
        imageId = (ImageView) mView.findViewById(R.id.imageId);


        txtUserName = (MyEditText) mView.findViewById(R.id.txtUserName);
        tvIdNo = (MyEditText) mView.findViewById(R.id.tvIdNo);
        txtUserPassword = (MyEditText) mView.findViewById(R.id.txtUserPassword);
        ivPolicy = (ImageView) mView.findViewById(R.id.ivPolicy);
        btnSignUp = (MyTextView) mView.findViewById(R.id.btnSignUp);


        edtDob.setOnClickListener(this);
        edtExpiryDate.setOnClickListener(this);
        llIdType.setOnClickListener(this);
        tvIdType.setOnClickListener(this);
        layoutScan.setOnClickListener(this);
        ivPolicy.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edtDob) {
            getDateOfBirth();
        } else if (view.getId() == R.id.edtExpiryDate) {
            getDateOfExpiry();
        } else if (view.getId() == R.id.llIdType) {
            getSelectedId();
        } else if (view.getId() == R.id.tvIdType) {
            getSelectedId();
        } else if (view.getId() == R.id.layoutScan) {
            openCameraActivity();

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkCameraPermissionTop();
            } else {
                openCameraActivity();
            }*/
        } else if (view.getId() == R.id.ivPolicy) {
            BottomPasswordPolicy bottomPasswordPolicy = new BottomPasswordPolicy();
            bottomPasswordPolicy.show(getChildFragmentManager(), bottomPasswordPolicy.getTag());
        } else if (view.getId() == R.id.btnSignUp) {
            fullName = txtUserName.getText().toString().trim();
            if (StringUtils.isEmpty(fullName)) {
                txtUserName.setFocusable(true);
                txtUserName.setError(getString(R.string.info_enter_name));
                return;
            }

            if (!fullName.contains(" ")) {
                txtUserName.setFocusable(true);
                txtUserName.setError(getString(R.string.info_enter_first_name_lasr_name));
                return;
            }

            /*if (txtUserName.getText().toString().trim().length() < 5) {
                txtUserName.setError(getString(R.string.info_enter_name));
                txtUserName.requestFocus();
                return;
            }*/

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


            if (!PasswordUtil.PASSWORD_PATTERN.matcher(txtUserPassword.getText().toString().trim()).matches()) {
                txtUserPassword.setFocusable(true);
                txtUserPassword.setError("please follow the pattern");
                txtUserPassword.requestFocus();
                return;
            }
            String[] nameArray = txtUserName.getText().toString().split(" ");
            fName = nameArray[0];
            lName = nameArray[1];

            makeRequest();


        }
    }

    public void showProgress(String msg) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(msg);
        mProgress.show();
    }

    public void hideProgress() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    private void makeRequest() {
        JsonObject index = new JsonObject();
        index.addProperty(AppoConstants.FIRSTNAME, fName);
        index.addProperty(AppoConstants.LASTNAME, lName);
        index.addProperty(AppoConstants.USERNAME, mParamCountryCode + mParamMobile);
        index.addProperty(AppoConstants.PASSWORD, txtUserPassword.getText().toString().trim());
        index.addProperty(AppoConstants.EMIAL, mParamEmail);
        index.addProperty(AppoConstants.MOBILENUMBER, Long.parseLong(mParamMobile));
        index.addProperty(AppoConstants.PHONECODE, mParamCountryCode);
        index.addProperty(AppoConstants.ACCOUNTEXPIRED, false);
        index.addProperty(AppoConstants.CREDENTIALSEXPIRED, false);
        index.addProperty(AppoConstants.ACCOUNTLOCKED, false);
        index.addProperty(AppoConstants.ENABLE, true);
        index.addProperty(AppoConstants.COUNTRYID, mParamCountryId);
        index.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
        JsonArray roleArray = new JsonArray();
        roleArray.add("USER");
        index.add(AppoConstants.ROLE, roleArray);
        JsonObject customerDetails = new JsonObject();
        customerDetails.addProperty(AppoConstants.FIRSTNAME, fName.toUpperCase());
        customerDetails.addProperty(AppoConstants.LASTNAME, lName.toUpperCase());
        customerDetails.addProperty(AppoConstants.DOB, mDob);
        customerDetails.addProperty(AppoConstants.COUNTRYID, mParamCountryId);
        customerDetails.addProperty(AppoConstants.STATEID, mParamStateId);
        customerDetails.addProperty(AppoConstants.EXPIRYDATE, mExpiry);
        customerDetails.addProperty(AppoConstants.ADDRESS, mParamAdd);
        customerDetails.addProperty(AppoConstants.ZIPCODE2, mParamZip);
        customerDetails.addProperty(AppoConstants.CITY, mParamCity);

        customerDetails.addProperty(AppoConstants.IDNUMBER, tvIdNo.getText().toString().trim());
        customerDetails.addProperty(AppoConstants.IDTYPE, mIdName);
        index.add(AppoConstants.CUSTOMERDETAILS, customerDetails);
        //Log.e(TAG, "makeRequest: " + index.toString());

        showProgress("Please wait, creating your account");

        mainAPIInterface.postCreateUserAccount(index).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("message").equalsIgnoreCase(AppoConstants.SUCCESS) && jsonObject.getInt("result") == 1) {
                            //createAccountInFirebase();
                            Toast.makeText(getActivity(), "Successfully Created!!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(getActivity(), "Error Code : 500", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.error_account_creation_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_request_account_creation_failed), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideProgress();
                //Log.e("tag", t.getMessage().toString());
            }
        });
    }

    private void createAccountInFirebase() {

        showProgress(getString(R.string.info_creating_account));
        strUserEmail = mParamEmail;
        strUserPassword = txtUserPassword.getText().toString().trim();
        String phWithCode = mParamCountryCode + mParamMobile;
        mAuth.createUserWithEmailAndPassword(strUserEmail, phWithCode)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("area_code", String.valueOf(mParamCountryCode));
                            hashMap.put("id", userid);
                            hashMap.put("username", txtUserName.getText().toString());
                            hashMap.put("imageURL", "default");
                            hashMap.put("verification", "verified");

                            hashMap.put("email_id", mParamEmail);
                            hashMap.put("phone_number", phWithCode);
                            hashMap.put("search", txtUserName.getText().toString().trim().toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_LONG).show();
                                        hideProgress();
                                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
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

                            Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_LONG).show();
                            hideProgress();
                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    }
                });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermissionTop() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 988);
        } else {
            openCameraActivity();
        }
    }

    private void openCameraActivity() {
        Intent intentCamera = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intentCamera, 777);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 988) {
            boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
           // Log.e("TAG", "onRequestPermissionsResult:Manage ::  " + writePermission + "==" + cameraPermission + "==" + readPermission);
            if (readPermission && writePermission && cameraPermission) {
                openCameraActivity();
            } else {
                Toast.makeText(getActivity(), "please allow permission to work properly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && resultCode == Activity.RESULT_OK) {
            stringExtraPath = data.getStringExtra(AppoConstants.IMAGE_PATH);
           // Log.e(TAG, "onActivityResult: " + stringExtraPath);

            imageId.setVisibility(View.VISIBLE);
            DataVaultManager.getInstance(getActivity()).saveIdImagePath(stringExtraPath);
            Glide.with(getActivity())
                    .load(stringExtraPath)
                    .into(imageId);

            File imgFile = new File(stringExtraPath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            JsonObject mSent = new JsonObject();
            mSent.addProperty("image", imageString);
            getDataFromImage(mSent);


        }
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait...");
        progressDialog.show();
    }

    private void close() {
        progressDialog.dismiss();
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
                    edtExpiryDate.setText("");
                    edtDob.setText("");
                    txtUserName.setText("");
                    String responseExtract = new Gson().toJson(response.body());
                    try {
                        JSONObject mExtractROOT = new JSONObject(responseExtract);
                        JSONObject mExtractJSON = mExtractROOT.getJSONObject(AppoConstants.DATA);
                        if (mExtractJSON.getString(Constants.ERRORCODE).equalsIgnoreCase("0")) {
                            layoutDetails.setVisibility(View.VISIBLE);
                            if (mExtractJSON.has("documentNumber")) {
                                if (mExtractJSON.has("documentNumber")) {
                                    tvIdNo.setText(mExtractJSON.getString("documentNumber"));
                                }
                                if (mExtractJSON.has("dateOfExpiry")) {
                                    mExpiry = mExtractJSON.getString("dateOfExpiry");
                                    edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                    if (isValidFormat1(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 1 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[0], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat2(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 2 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[1], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat3(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 3 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[2], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat4(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 4 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[3], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat5(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 5 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[4], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat6(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 6 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[5], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat7(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 7 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[6], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat8(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 8 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[7], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat9(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 9 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[8], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat10(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 10 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[9], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat11(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 11 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[10], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
                                        edFocusIdExp.setVisibility(View.GONE);
                                    } else if (isValidFormat12(mExpiry)) {
                                        //Log.e(TAG, "onResponse: Expiry format 12 called");
                                        mExpiry = TimeUtils.getRequiredFormatDate(mDateFormat[11], mReqDateFormat, mExpiry);
                                        edtExpiryDate.setText("Expiry Date: " + mExpiry);
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
                mAllow = true;
                layoutDetails.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showErrorExtract() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvInfo.setText(getString(R.string.info_fund_wallet));
        tvHeader.setText("Scanning Error");

        tvInfo.setText("Please scan your id again, or Enter Manually");
        btnYes.setText("Try Again");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectCamera();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllow = true;
                layoutDetails.setVisibility(View.VISIBLE);
                dialogExtract.dismiss();
            }
        });

        btnNo.setText("Enter Manually");
        btnNo.setVisibility(View.VISIBLE);
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
        mIdTypeDialog.show(getChildFragmentManager(), mIdTypeDialog.getTag());
    }

    public void getDateOfExpiry() {
        Calendar minCal = Calendar.getInstance();
        int i1 = minCal.get(Calendar.YEAR) + 10;
        minCal.set(i1, 11, 31);
        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");

                mExpiry = dateFormatter.format(newDate.getTime());
                edtExpiryDate.setText("Expiry Date : " + dateFormatter.format(newDate.getTime()));
                edFocusIdExp.setVisibility(View.GONE);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMaxDate(minCal.getTimeInMillis());
        StartTime.setCanceledOnTouchOutside(false);
        StartTime.show();
    }

    public void getDateOfBirth() {
        Calendar minCal = Calendar.getInstance();
        minCal.set(1950, 0, 1);
        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
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

    public void setScanName(int pos) {
        tvIdType.setText(mListType.get(pos));
        mIdName = mListType.get(pos);
        layoutScan.setVisibility(View.VISIBLE);
        if (mIdTypeDialog != null) {
            mIdTypeDialog.dismiss();
        }

    }
}
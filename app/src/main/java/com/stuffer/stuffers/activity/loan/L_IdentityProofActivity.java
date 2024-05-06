package com.stuffer.stuffers.activity.loan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainLoanInterface;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.my_camera.MyCameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class L_IdentityProofActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "L_IdentityProofActivity";
    RelativeLayout relative1, relative2, relative3;
    ImageView image1, image2, image3;
    ImageView delete1, delete2, delete3;
    MyButton capture1, capture2, capture3, btnSubmitId;
    private String stringExtra1, stringExtra2, stringExtra3;
    private MainLoanInterface apiServiceLoan;
    private String base64Profile;
    private String base64IdCard;
    private String base64PaySlip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lidentity_proof);
        apiServiceLoan = ApiUtils.getApiServiceLoan();

        relative1 = findViewById(R.id.relative1);
        relative2 = findViewById(R.id.relative2);
        relative3 = findViewById(R.id.relative3);
        btnSubmitId = findViewById(R.id.btnSubmitId);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        delete1 = findViewById(R.id.delete1);
        delete2 = findViewById(R.id.delete2);
        delete3 = findViewById(R.id.delete3);
        capture1 = findViewById(R.id.capture1);
        capture2 = findViewById(R.id.capture2);
        capture3 = findViewById(R.id.capture3);

        capture1.setOnClickListener(this);
        capture2.setOnClickListener(this);
        capture3.setOnClickListener(this);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        btnSubmitId.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(L_IdentityProofActivity.this, CameraActivity.class);

        switch (view.getId()) {
            case R.id.capture1:
                startActivityForResult(mIntent, 100);

                break;
            case R.id.capture2:
                startActivityForResult(mIntent, 101);
                break;
            case R.id.capture3:
                startActivityForResult(mIntent, 102);
                break;
            case R.id.delete1:
                stringExtra1 = "";
                relative1.setVisibility(View.GONE);
                delete1.setVisibility(View.GONE);

                break;
            case R.id.delete2:
                stringExtra2 = "";
                relative2.setVisibility(View.GONE);
                delete2.setVisibility(View.GONE);
                break;
            case R.id.delete3:
                stringExtra2 = "";
                relative3.setVisibility(View.GONE);
                delete3.setVisibility(View.GONE);
                break;
            case R.id.btnSubmitId:

                if (StringUtils.isEmpty(stringExtra1)) {
                    Helper.showLongMessage(L_IdentityProofActivity.this, "Upload profile photo");

                } else if (StringUtils.isEmpty(stringExtra2)) {
                    Helper.showLongMessage(L_IdentityProofActivity.this, "Upload Identification card");

                } else if (StringUtils.isEmpty(stringExtra3)) {
                    Helper.showLongMessage(L_IdentityProofActivity.this, "Upload Previous paySlip");
                } else {


                    Helper.showLongMessage(L_IdentityProofActivity.this, "Allow to upload");
                    File filePath = new File(stringExtra1);
                    Bitmap bm = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
                    base64Profile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                    //Log.e("TAG", "onClick: before reduce : " + base64Profile.length());

                    uploadProfile();


//                    byte[] data = bOut.toByteArray();
//                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    Bitmap bitmap2 = reduzeBitmapSize(bitmap1, 240000);
//                    ByteArrayOutputStream bOut2 = new ByteArrayOutputStream();
//                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
//                    String base64Image = Base64.encodeToString(bOut2.toByteArray(), Base64.DEFAULT);


                }


                break;


        }


    }

    private void uploadProfile() {
        Helper.showLoading(getString(R.string.info_please_wait), L_IdentityProofActivity.this);
        String param1 = String.valueOf(BuildConfig.param1);
        String param2 = BuildConfig.param2;
        String param3 = BuildConfig.param3;
        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        JsonObject mProfileRequest = new JsonObject();
        mProfileRequest.addProperty("wWalletID", "202310060001");
        mProfileRequest.addProperty("base64ImageString", base64Profile);


        apiServiceLoan.setLoanProfilePhoto(mProfileRequest, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helper.hideLoading();
                //{"message":"success","error":false,"SubmitStatus":"Submitted","WalletID":"202310060001"}
                if (response.code() == 200) {
                    JsonObject body = response.body();
                    if (body.getAsJsonPrimitive(AppoConstants.MESSAGE).getAsString().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        if (!body.getAsJsonPrimitive(AppoConstants.ERROR).getAsBoolean()) {
                            uploadIdCard();
                        }
                    }
                } else {
                    Toast.makeText(L_IdentityProofActivity.this, "Error Code :" + response.code(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.hideLoading();
                //Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });


    }

    private void uploadIdCard() {

        File filePath = new File(stringExtra2);
        Bitmap bm = BitmapFactory.decodeFile(filePath.getAbsolutePath());
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        base64IdCard = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
        //Log.e("TAG", "onClick: before reduce : " + base64IdCard.length());

        Helper.showLoading(getString(R.string.info_please_wait), L_IdentityProofActivity.this);
        String param1 = String.valueOf(BuildConfig.param1);
        String param2 = BuildConfig.param2;
        String param3 = BuildConfig.param3;
        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        JsonObject mIdRequest = new JsonObject();
        mIdRequest.addProperty("wWalletID", "202310060001");
        mIdRequest.addProperty("base64ImageString", base64IdCard);
        //Log.e(TAG, "uploadIdCard: " + mIdRequest);
        apiServiceLoan.setLoanIdCard(mIdRequest, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helper.hideLoading();
                if (response.code() == 200) {
                    JsonObject body = response.body();
                    if (body.getAsJsonPrimitive(AppoConstants.MESSAGE).getAsString().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        if (!body.getAsJsonPrimitive(AppoConstants.ERROR).getAsBoolean()) {
                            uploadPaySlip();
                        }
                    }
                } else {
                    Toast.makeText(L_IdentityProofActivity.this, "Error Code :" + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.hideLoading();

                //Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });


    }

    private void uploadPaySlip() {

        File filePath = new File(stringExtra3);
        Bitmap bm = BitmapFactory.decodeFile(filePath.getAbsolutePath());
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        base64PaySlip = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
        //Log.e("TAG", "onClick: before reduce : " + base64PaySlip.length());

        Helper.showLoading(getString(R.string.info_please_wait), L_IdentityProofActivity.this);
        String param1 = String.valueOf(BuildConfig.param1);
        String param2 = BuildConfig.param2;
        String param3 = BuildConfig.param3;
        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        JsonObject mPaySLipRequest = new JsonObject();
        mPaySLipRequest.addProperty("wWalletID", "202310060001");
        mPaySLipRequest.addProperty("base64ImageString", base64PaySlip);
        //Log.e(TAG, "uploadPaySlip: " + mPaySLipRequest);
        apiServiceLoan.setLoanPaySlip(mPaySLipRequest, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helper.hideLoading();
                ////Log.e(TAG, "onResponse: paySlip " + response);
                ////Log.e("TAG", "onResponse: paySlip " + response.body());

                if (response.code() == 200) {
                    JsonObject body = response.body();
                    if (body.getAsJsonPrimitive(AppoConstants.MESSAGE).getAsString().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        if (!body.getAsJsonPrimitive(AppoConstants.ERROR).getAsBoolean()) {
                            Toast.makeText(L_IdentityProofActivity.this, "Successfully Sent the Details!!!. ", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(L_IdentityProofActivity.this, "Error Code :" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.hideLoading();
                //Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });
    }

    public Bitmap reduzeBitmapSize(Bitmap bitmap, int MAX_SIZE) {
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1) {
            return bitmap;
        }
        double ratio = Math.sqrt(ratioSquare);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                stringExtra1 = data.getStringExtra(AppoConstants.IMAGE_PATH);
                image1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra1).into(image1);
                relative1.setVisibility(View.VISIBLE);
            }

        } else if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                stringExtra2 = data.getStringExtra(AppoConstants.IMAGE_PATH);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra2).into(image2);
                relative2.setVisibility(View.VISIBLE);

            }

        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                stringExtra3 = data.getStringExtra(AppoConstants.IMAGE_PATH);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra3).into(image3);
                relative3.setVisibility(View.VISIBLE);
            }
        }
    }
}
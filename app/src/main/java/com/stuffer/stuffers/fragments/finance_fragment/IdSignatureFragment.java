package com.stuffer.stuffers.fragments.finance_fragment;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_IDPATH;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyCheckBox;
import com.stuffer.stuffers.views.MySwitchView;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdSignatureFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "IdSignatureFragment";
    private static final int MY_SCAN_REQUEST_CODE_TOP = 1001;
    private static final int MY_SCAN_REQUEST_CODE_BOTTOM = 1002;
    private MyButton btnReset, btnSaveSign, btnSubmitForm;
    private SignatureView signatureView;
    private MyTextViewBold tvExpiryDate, tvSourceOfIncome, tvSourceOfIncome1, tvUserName1, tvPassportNumber1;

    private String mDob;
    private MyTextView tvFirstBlank, tvInfo3, tvInfo4, tvInfo5, tvInfo6;
    private String mUserName, mSourceIncome, mMonthlyIncome, mNoOfHold, mPassNumber;
    private LinearLayout llSignature, llParent;
    private MyCheckBox materialCheckBox;
    private MySwitchView signatureBox;
    private ImageView signature_view;
    private FrameLayout frameSignature;
    private ImageView image1, image2;

    private LinearLayout layoutUpload;
    private ScrollView scrollView;
    private int mCounter = 0;
    private File mfileSignature;
    private MainAPIInterface mainAPIInterface;
    private String mExpiryDate;
    private ProgressDialog dialog;
    private String imagePath1 = "";
    private String imagePath2 = "";
    private int mCount = 1;
    private ProgressDialog progressDialog;
    private AlertDialog dialogPayment;
    private FrameLayout fBottom, fTop;
    private MyTextViewBold tvTop1, tvTop2;
    private MyTextView tvInfoIncome1, tvInfoIncome2,tvPart5,tvPart6;
    private OnBankSubmit mConfirmListener;
    private String mIdPath;

    public IdSignatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_id_signature, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        Bundle arguments = this.getArguments();
        String param = arguments.getString(AppoConstants.INFO);

        try {
            JSONObject paramReceived = new JSONObject(param);
            mUserName = paramReceived.getString(AppoConstants.USERNAME);
            mSourceIncome = paramReceived.getString(AppoConstants.SOURCE_OF_INCOME);
            mMonthlyIncome = paramReceived.getString(AppoConstants.MONTHLY_INCOME);
            mNoOfHold = paramReceived.getString(AppoConstants.NO_OF_HOUSE_HOLD);
            mPassNumber = paramReceived.getString(AppoConstants.PASSPORT_NUMBER);
            mExpiryDate = paramReceived.getString(AppoConstants.EXPIRYDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setViews(mView);
        mIdPath = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_IDPATH);
        mCounter=1;
        setImage(mIdPath);

        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!materialCheckBox.isChecked()) {
                    Toast.makeText(getContext(), "Please accept agreement", Toast.LENGTH_SHORT).show();
                    llParent.setVisibility(View.GONE);
                    return;
                }
                llParent.setVisibility(View.VISIBLE);
            }
        });

        llSignature = mView.findViewById(R.id.llSignature);
        llParent = mView.findViewById(R.id.llParent);
        setData();

        btnSubmitForm.setOnClickListener(this);


        signatureBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    BottomSheetSignature fragmentBottomSheet = new BottomSheetSignature();
                    fragmentBottomSheet.show(getChildFragmentManager(), fragmentBottomSheet.getTag());
                    fragmentBottomSheet.setCancelable(false);

                }
            }
        });

        return mView;
    }

    private void setViews(View mView) {
        btnSubmitForm = mView.findViewById(R.id.btnSubmitForm);
        tvFirstBlank = mView.findViewById(R.id.tvFirstBlank);
        tvSourceOfIncome = mView.findViewById(R.id.tvSourceOfIncome);
        tvSourceOfIncome1 = mView.findViewById(R.id.tvSourceOfIncome1);
        tvUserName1 = mView.findViewById(R.id.tvUserName1);
        tvPassportNumber1 = mView.findViewById(R.id.tvPassportNumber1);
        tvTop1 = mView.findViewById(R.id.tvTop1);
        tvTop2 = mView.findViewById(R.id.tvTop2);
        tvInfoIncome1 = (MyTextView) mView.findViewById(R.id.tvInfoIncome1);
        tvInfoIncome2 = (MyTextView) mView.findViewById(R.id.tvInfoIncome2);
        tvInfo3 = mView.findViewById(R.id.tvInfo3);
        tvInfo4 = mView.findViewById(R.id.tvInfo4);
        tvInfo5 = mView.findViewById(R.id.tvInfo5);
        tvInfo6 = mView.findViewById(R.id.tvInfo6);
        tvPart5 = mView.findViewById(R.id.tvPart5);
        tvPart6 = mView.findViewById(R.id.tvPart6);
        materialCheckBox = mView.findViewById(R.id.materialCheckBox);
        signatureBox = mView.findViewById(R.id.signatureBox);
        signature_view = mView.findViewById(R.id.signature_view);
        frameSignature = mView.findViewById(R.id.frameSignature);
        image1 = mView.findViewById(R.id.image1);
        image2 = mView.findViewById(R.id.image2);

        layoutUpload = mView.findViewById(R.id.layoutUpload);
        scrollView = mView.findViewById(R.id.scrollView);

        fTop = mView.findViewById(R.id.fTop);
        materialCheckBox.setText(getString(R.string.info_aceptar_acuerdo_antes_de_enviar_su_aplicacion));

        tvTop1.setText(getString(R.string.info_declaraci_n_jurada_de_ingresos));
        tvTop2.setText(getText(R.string.info_cuentas_de_debida_diligencia_simplificada));
        tvInfoIncome1.setText(getString(R.string.info_income));
        tvInfoIncome2.setText(getString(R.string.info_income2));




    }

    private void setData() {

        String firstBalnk = "Quién suscribe," + "<u><b>" + "   " + mUserName + "   " + "</b></u>" + "concédula / pasaportes número" + "<u><b>" + "   " + mPassNumber + "   " + "</b></u>"
                + "actuando en mi propio nombre y representación, declaro bajo la gravedad del juramento que recibo ingresos mensuales de" +
                " US$ " + "<u><b>" + "  " + mMonthlyIncome + "  " + "</b></u>" + ", los cuales provienen de";
        tvFirstBlank.setText(Html.fromHtml(firstBalnk));
        tvSourceOfIncome.setText(mSourceIncome);
        tvSourceOfIncome1.setText(mSourceIncome);
        tvInfo3.setText(getString(R.string.info_income3));
        String info4 = getString(R.string.info_part1) + "<u><b>" + "   CREDICROP BANK   " + "</b></u>" + getString(R.string.info_part2);

        tvInfo4.setText(Html.fromHtml(info4));
        tvInfo5.setText(getString(R.string.info_part3));

        tvInfo6.setText(getString(R.string.info_part4));

        tvUserName1.setText(mUserName);
        tvPassportNumber1.setText(mPassNumber);
        tvPart5.setText(getString(R.string.info_part5));
        tvPart6.setText(getString(R.string.info_part6));
        signatureBox.setText(getString(R.string.info_part7));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSubmitForm:

                if (!materialCheckBox.isChecked()) {
                    Toast.makeText(getContext(), "Please accept agreement", Toast.LENGTH_SHORT).show();
                    llParent.setVisibility(View.GONE);
                    return;
                }
                llParent.setVisibility(View.VISIBLE);

                if (mfileSignature == null) {
                    Toast.makeText(getActivity(), "Please upload your signature", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (imagePath1.isEmpty()) {
                    Toast.makeText(getActivity(), "Please upload your id and address proof", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                showDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSuccessDialog();
                    }
                },3000);





                //uploadData(1);

                break;

            default:
                break;
        }
    }

    private void uploadData(int pos) {
        MultipartBody.Part parts;
        RequestBody phoneNumber;
        RequestBody fileName;
        if (pos == 1) {
            String numberWithCountryCode = Helper.getNumberWithCountryCode();
            phoneNumber = createPartFromString(numberWithCountryCode);
            fileName = createPartFromString("signature");
            RequestBody requestBody;
            requestBody = RequestBody.create(MediaType.parse("image/*"), mfileSignature);
            String name = "signature_" + numberWithCountryCode + ".png";
            parts = MultipartBody.Part.createFormData("file", name, requestBody);
            upload(phoneNumber, fileName, parts);
        } else if (pos == 2) {
            File file = new File(imagePath1);
            String name = file.getName();

            String numberWithCountryCode = Helper.getNumberWithCountryCode();
            phoneNumber = createPartFromString(numberWithCountryCode);
            fileName = createPartFromString("id");
//            Log.e(TAG, "uploadImage: name1 :: " + name);
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

        } else if (pos == 3) {
            File file = new File(imagePath2);
            String name = file.getName();
            String numberWithCountryCode = Helper.getNumberWithCountryCode();
            phoneNumber = createPartFromString(numberWithCountryCode);
            fileName = createPartFromString("address");
//            Log.e(TAG, "uploadImage: name2 :: " + name);
            String nameFile = null;
            if (name.contains(".jpeg")) {
                nameFile = "addres_" + numberWithCountryCode + ".jpeg";

            } else if (name.contains(".jpg")) {
                nameFile = "addres_" + numberWithCountryCode + ".jpg";
            } else if (name.contains(".png")) {
                nameFile = "addres_" + numberWithCountryCode + ".png";
            }
            RequestBody requestBody;
            requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            parts = MultipartBody.Part.createFormData("file", nameFile, requestBody);
            upload(phoneNumber, fileName, parts);
        } else if (pos == 4) {
            updateUserAccountDetails();
        }

    }

    private void upload(RequestBody phoneNumber, RequestBody fileName, MultipartBody.Part parts) {
        showDialog();

        mainAPIInterface.uploadFileForOpenAccount(parts, phoneNumber, fileName).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                close();
                mCount = mCount + 1;
                uploadData(mCount);
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
            }
        });

    }

    private void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait...");
        progressDialog.show();
    }

    private void close() {
        progressDialog.dismiss();
    }




    public void setSignture(Bitmap bitmap, boolean status) {
        if (status) {

            frameSignature.setVisibility(View.VISIBLE);
            signatureBox.setChecked(false);
            signature_view.setImageBitmap(bitmap);
            layoutUpload.setVisibility(View.VISIBLE);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
            mfileSignature = new File(getActivity().getCacheDir(), "sign.png");
            try {
                boolean newFile = mfileSignature.createNewFile();
                if (newFile) {
                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mfileSignature);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            frameSignature.setVisibility(View.GONE);
            layoutUpload.setVisibility(View.GONE);
        }


    }

    /**
     * need to code here
     */
    public void setImage(String path) {
        //mCounter = mCounter + 1;
        if (mCounter == 1) {
            image1.setPadding(0, 0, 0, 0);
            Glide.with(getActivity())
                    .load(path)
                    .into(image1);
            imagePath1 = path;
        } else {
            image2.setPadding(0, 0, 0, 0);
            Glide.with(getActivity())
                    .load(path)
                    .centerInside()
                    .into(image2);
            imagePath2 = path;
        }

    }

    private void uploadFile() {
        /*JsonObject param = new JsonObject();
        param.addProperty("phoneNumber", "+919836683269");
        param.addProperty("fileName", "signature");*/
        RequestBody phoneNumber = createPartFromString("919836683269");
        RequestBody fileName = createPartFromString("signature");
        //RequestBody phoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"),"919836683269");//createPartFromString("919836683269");
        //RequestBody fileName = RequestBody.create(MediaType.parse("multipart/form-data"),"signature");
        //RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mfileSignature);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), mfileSignature);

        MultipartBody.Part parts = MultipartBody.Part.createFormData("file", mfileSignature.getName(), requestBody);

//        Log.e(TAG, "uploadFile: name :: " + mfileSignature.getName());
//        Log.e(TAG, "uploadFile: absolute path :: " + mfileSignature.getAbsolutePath());

        //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", mfileSignature.getName(), RequestBody.create(MediaType.parse("image/*"), mfileSignature));


        mainAPIInterface.uploadFileForOpenAccount(parts, phoneNumber, fileName).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e("TAG", "onResponse: " + response.toString());
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
//                    Log.e("TAG", "onResponse: " + body);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
//                Log.e("TAG", "onFailure: " + message);
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }


    private void updateUserAccountDetails() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);

            JsonObject sentIndex = new JsonObject();
            sentIndex.addProperty(AppoConstants.ID, jsonResult.getString(AppoConstants.ID));
            sentIndex.addProperty(AppoConstants.FIRSTNAME, jsonResult.getString(AppoConstants.FIRSTNAME));
            sentIndex.addProperty(AppoConstants.LASTNAME, jsonResult.getString(AppoConstants.LASTNAME));
            sentIndex.addProperty(AppoConstants.USERNAME, jsonResult.getString(AppoConstants.USERNAME));
            sentIndex.addProperty(AppoConstants.PASSWORD, jsonResult.getString(AppoConstants.PASSWORD));
            sentIndex.addProperty(AppoConstants.EMIAL, jsonResult.getString(AppoConstants.EMIAL));
            sentIndex.addProperty(AppoConstants.ACCOUNTEXPIRED, jsonResult.getString(AppoConstants.ACCOUNTEXPIRED));
            sentIndex.addProperty(AppoConstants.ACCOUNTLOCKED, jsonResult.getString(AppoConstants.ACCOUNTLOCKED));
            sentIndex.addProperty(AppoConstants.CREDENTIALSEXPIRED, jsonResult.getString(AppoConstants.CREDENTIALSEXPIRED));
            sentIndex.addProperty(AppoConstants.ENABLE, jsonResult.getString(AppoConstants.ENABLE));
            sentIndex.addProperty(AppoConstants.MOBILENUMBER, jsonResult.getString(AppoConstants.MOBILENUMBER));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, jsonResult.getString(AppoConstants.TRANSACTIONPIN));
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));

            sentIndex.addProperty(AppoConstants.USERTYPE, (String) null);
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, (String) null);

  /*For creating Bank account through Appopay
    private String SourceOfIncome;
    private String monthlyIncome;
    private String passportNumber;
    private Date expiryDate;*/

            sentIndex.addProperty("SourceOfIncome", mSourceIncome);
            sentIndex.addProperty("monthlyIncome", mMonthlyIncome);
            sentIndex.addProperty("passportNumber", mPassNumber);
            sentIndex.addProperty("expiryDate", mExpiryDate);


            JsonArray jsonArrayRole = new JsonArray();
            jsonArrayRole.add("USER");
            sentIndex.add(AppoConstants.ROLE, jsonArrayRole);

            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            JsonObject sentJsonCustomerDetails = new JsonObject();
            sentJsonCustomerDetails.addProperty(AppoConstants.ID, jsonCustomerDetails.getString(AppoConstants.ID));
            sentJsonCustomerDetails.addProperty(AppoConstants.FIRSTNAME, jsonCustomerDetails.getString(AppoConstants.FIRSTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.LASTNAME, jsonCustomerDetails.getString(AppoConstants.LASTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.MIDDLENAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CARDTOKEN, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, jsonCustomerDetails.getString(AppoConstants.COUNTRYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, jsonCustomerDetails.getString(AppoConstants.STATEID));
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, jsonCustomerDetails.getString(AppoConstants.ADDRESS));
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, jsonCustomerDetails.getString(AppoConstants.CITYNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, jsonCustomerDetails.getString(AppoConstants.DOB));
            //sentJsonCustomerDetails.addProperty(AppoConstants.DOB, "1993-02-10");
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, 1);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "USD");
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
//            Log.e(TAG, "updateUserProfile: " + sentIndex);
            processUpdateRequest(sentIndex);

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void processUpdateRequest(JsonObject sentIndex) {
        String accessToken = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

        mainAPIInterface.putUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: update : " + new Gson().toJson(response.body()));
                    String res = new Gson().toJson(response);
//                    Log.e(TAG, "onResponse: =========== " + res);
                    //Toast.makeText(getActivity(), getString(R.string.info_profile_successfully_updated), Toast.LENGTH_SHORT).show();
                    showSuccessDialog();

                   /*tvProfileDetails.setVisibility(View.VISIBLE);
                    txtUpdateProfile.setVisibility(View.GONE);
                    onUpdateProfile();*/
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        ////Log.e(TAG, "onResponse: bad request");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void showSuccessDialog() {
        //closeDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_success_topup, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        MyTextView tvTitleCommon = dialogLayout.findViewById(R.id.tvTitleCommon);
        tvTitleCommon.setText("Open Bank Account");
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        btnClose.setText("Ok");
        /*<string name="info_success_recharge">Thank you!\n Your Recharge Successfully Done</string>*/
        tvSuccess.setText("Thank you!\n Your Request Has Been Successfully Sent To Admin");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();

            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    private void closeDialog() {
        if (dialogPayment != null && dialogPayment.isShowing()) {
            dialogPayment.dismiss();
            dialogPayment=null;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                    progressDialog=null;
                }
                mConfirmListener.onConfirm();
            }
        });





    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mConfirmListener=(OnBankSubmit)context;
    }
}
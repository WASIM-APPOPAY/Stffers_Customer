package com.stuffer.stuffers.activity.forgopassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyForgotFragment extends Fragment {

    private View mView;
    private ImageView ivFlag;
    private CountryCodePicker edtCustomerCountryCode;
    private MyTextView tvSendOtpCommon;
    private MyEditText edUserPhone;
    private User userMe;
    private ImageView imageForgot1, imageForgot2;
    private String strCustomerCountryCode;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private FragmentReplaceListener mReplaceListener;

    public VerifyForgotFragment() {
        // Required empty public constructor
    }

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_verify_forgot, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        ivFlag = (ImageView) mView.findViewById(R.id.ivFlag);
        imageForgot1 = (ImageView) mView.findViewById(R.id.imageForgot1);
        imageForgot2 = (ImageView) mView.findViewById(R.id.imageForgot2);
        edtCustomerCountryCode = (CountryCodePicker) mView.findViewById(R.id.edtCustomerCountryCode);
        tvSendOtpCommon = (MyTextView) mView.findViewById(R.id.tvSendOtpCommon);
        edUserPhone = (MyEditText) mView.findViewById(R.id.edUserPhone);

        setUpInfo();

        return mView;
    }

    private void setUpInfo() {
        String ccode = DataVaultManager.getInstance(getActivity()).getVaultValue(DataVaultManager.KEY_CCODE);
        edtCustomerCountryCode.setCountryForNameCode(ccode);
        ImageView imageViewFlag = edtCustomerCountryCode.getImageViewFlag();
        Bitmap bitmap = ((BitmapDrawable) imageViewFlag.getDrawable()).getBitmap();
        ivFlag.setImageBitmap(bitmap);
        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                ImageView imageViewFlag1 = edtCustomerCountryCode.getImageViewFlag();
                Bitmap bitmap = ((BitmapDrawable) imageViewFlag1.getDrawable()).getBitmap();
                ivFlag.setImageBitmap(bitmap);
            }
        });
        edUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edUserPhone.getText().toString().trim().length() > 0) {
                    imageForgot1.setVisibility(View.GONE);
                    imageForgot2.setVisibility(View.VISIBLE);
                } else {
                    imageForgot2.setVisibility(View.GONE);
                    imageForgot1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvSendOtpCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edUserPhone.getText().toString().trim().length() == 0) {
                    edUserPhone.setError("Enter mobile number");
                    edUserPhone.requestFocus();
                    return;
                }
                //later requestForOtp();
                strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                String strCustomerPhone = edUserPhone.getText().toString().trim();
                String phWithCode = strCustomerCountryCode + strCustomerPhone;
                Bundle bundle = new Bundle();
                bundle.putString(AppoConstants.PHONECODE, strCustomerCountryCode);
                bundle.putString(AppoConstants.MOBILENUMBER, strCustomerPhone);
                bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                bundle.putBoolean(AppoConstants.OTPSECRREN, true);

                if (mReplaceListener != null) {
                    mReplaceListener.onFragmentReplaceClick(bundle);
                }

            }
        });
    }

    private void requestForOtp() {
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait sending OTP.");
        dialog.show();
        JsonObject param = new JsonObject();

        String smsHash = Helper.getHashCode();

        param.addProperty("hashKey", smsHash);

        param.addProperty("mobileNumber", edUserPhone.getText().toString().trim());
        param.addProperty("phoneCode", strCustomerCountryCode);

        //Log.e("TAG", "requestForOtp: " + param.toString());

        mainAPIInterface.getOtpforUser(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        String strCustomerPhone = edUserPhone.getText().toString().trim();
                        String phWithCode = strCustomerCountryCode + strCustomerPhone;
                        Bundle bundle = new Bundle();
                        bundle.putString(AppoConstants.PHONECODE, strCustomerCountryCode);
                        bundle.putString(AppoConstants.MOBILENUMBER, strCustomerPhone);
                        bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                        bundle.putBoolean(AppoConstants.OTPSECRREN, true);

                        if (mReplaceListener != null) {
                            mReplaceListener.onFragmentReplaceClick(bundle);
                        }
                    } else {
                        if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                            Helper.showErrorMessage(getActivity(), response.body().get("message").getAsString());
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.info_request_otp_failed), Toast.LENGTH_LONG).show();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mReplaceListener = (FragmentReplaceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent should implement FragmentReplaceListener");
        }

    }

}
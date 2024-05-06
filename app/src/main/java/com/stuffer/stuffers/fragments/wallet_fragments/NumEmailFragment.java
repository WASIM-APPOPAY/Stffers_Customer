package com.stuffer.stuffers.fragments.wallet_fragments;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_CCODE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.util.StringUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.CustomerProfileActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity3;
import com.stuffer.stuffers.activity.wallet.MobileNumberRegistrationActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.adapter.address.AutoCompleteAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.communicator.OtpRequestListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomAlreadyFragment;
import com.stuffer.stuffers.fragments.dialog.StateDialogFragment;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.Country.State;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NumEmailFragment extends Fragment {
    private static final String TAG = "NumEmailFragment";

    private MyTextView floatingOtpNext;
    private OtpRequestListener mOtpRequestListener;
    private MyEditText edtCustomerMobileNumber, edtEmail;
    //private AutoCompleteTextView placesAutocomplete;
    private MyEditText placesAutocomplete;
    private ImageView btnClearAll;
    AutoCompleteAdapter mAutoAdapter;
    private PlacesClient mPlaceClient;
    private CountryCodePicker edtCustomerCountryCode;
    private String mMobileNumber, mNameCode, mCountryCode, mEmail, mCountryName;
    private ProgressDialog mProgress;
    MainAPIInterface mainAPIInterface;
    List<Result> mListCountry;
    private Integer mCountyId = 0;
    List<State> mStates;
    ImageView ivState;
    MyTextView tvState;
    MyEditText tvCity, tvZip;

    private int mStateId;
    private ChatHelper helper;
    private User userMe;
    private PhoneNumberUtil phoneUtil;

    public NumEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        helper = new ChatHelper(getActivity());
        userMe = helper.getLoggedInUser();
        mainAPIInterface = ApiUtils.getAPIService();
        View mView = inflater.inflate(R.layout.fragment_num_email, container, false);

        floatingOtpNext = mView.findViewById(R.id.floatingOtpNext);
        edtCustomerMobileNumber = mView.findViewById(R.id.edtCustomerMobileNumber);
        edtCustomerCountryCode = mView.findViewById(R.id.edtCustomerCountryCode);
        placesAutocomplete = mView.findViewById(R.id.placesAutocomplete);
        btnClearAll = mView.findViewById(R.id.btnClearAll);
        ivState = mView.findViewById(R.id.ivState);
        tvState = mView.findViewById(R.id.tvState);
        edtEmail = mView.findViewById(R.id.edtEmail);
        tvZip = mView.findViewById(R.id.tvZip);
        tvCity = mView.findViewById(R.id.tvCity);
        String id = userMe.getId();
        try {
            if (phoneUtil == null) {
                phoneUtil = PhoneNumberUtil.createInstance(getActivity());
            }
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + id, "");
            long nationalNumber = numberProto.getNationalNumber();
            edtCustomerMobileNumber.setText("" + nationalNumber);
            edtCustomerCountryCode.setCountryForPhoneCode(numberProto.getCountryCode());

            try {
                String vaultValue1 = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_CCODE);
                if (!StringUtils.isEmpty(vaultValue1)) {
                    edtCustomerCountryCode.setCountryForNameCode(vaultValue1);
                } else {
                    edtCustomerCountryCode.setCountryForPhoneCode(edtCustomerCountryCode.getDefaultCountryCodeAsInt());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }


        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddress = "";
                placesAutocomplete.setText("");

            }
        });


        floatingOtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//wd element
                mMobileNumber = edtCustomerMobileNumber.getText().toString();
                mCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                mNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                mCountryName = edtCustomerCountryCode.getSelectedCountryName();
                mEmail = edtEmail.getText().toString().trim();
                /*if (edtCustomerMobileNumber.getText().toString().trim().isEmpty()) {
                    edtCustomerMobileNumber.setError(getString(R.string.info_enter_mobile_number));
                    edtCustomerMobileNumber.requestFocus();
                    edtCustomerMobileNumber.setFocusable(true);
                    return;
                }
                if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    edtEmail.setError(getString(R.string.info_valid_email_id));
                    edtEmail.setFocusable(true);
                    edtEmail.requestFocus();
                    return;
                }

                if (placesAutocomplete.getText().toString().trim().isEmpty()) {
                    placesAutocomplete.setError(getString(R.string.info_your_address));
                    placesAutocomplete.requestFocus();
                    placesAutocomplete.setFocusable(true);
                    return;
                }
                if (tvState.getText().toString().trim().isEmpty()) {
                    Helper.showErrorMessage(getActivity(), getString(R.string.info_select_state));
                    return;
                }
                if (tvCity.getText().toString().trim().isEmpty()) {
                    Helper.showErrorMessage(getActivity(), getString(R.string.info_city_name));
                    return;
                }*/


                mOtpRequestListener.onOtpRequest("IN", "91", "9830450542", "mdwasim508@gmail.com", "bankra mondal para killa math kolkata 711403, West Bengal","27",100,"711403","kolkata");
                //requestForOtp();


                //verifyMobileNumber(mCountryCode + mMobileNumber);

            }
        });


        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        edtCustomerCountryCode.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                //your code
                TextView title = (TextView) dialog.findViewById(R.id.textView_title);
                title.setText(getString(R.string.info_cc_reg));
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                //your code
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                //your code
            }
        });

        mCountryName = edtCustomerCountryCode.getSelectedCountryName();

        /*if (!Places.isInitialized()) {
            Places.initialize(AppoPayApplication.getInstance(), getString(R.string.google_maps_api_key));
        }*/

        //mPlaceClient = Places.createClient(getActivity());
        //initAutoCompleteTextView();
        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //String selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                //DataVaultManager.getInstance(getActivity()).saveCCODE(selectedCountryNameCode);
                mCountryName = edtCustomerCountryCode.getSelectedCountryName();
                tvState.setText("");

                getCountryList();
            }
        });

        ivState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListCountry != null && mListCountry.size() > 0) {
                    StateDialogFragment stateDialogFragment = new StateDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(AppoConstants.STATE, (ArrayList<? extends Parcelable>) mStates);
                    stateDialogFragment.setArguments(bundle);
                    stateDialogFragment.setCancelable(false);
                    stateDialogFragment.show(getChildFragmentManager(), stateDialogFragment.getTag());
                } else {
                    Toast.makeText(getActivity(), "Please Select Your Country Code...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getCountryList();

        return mView;


    }

    private void verifyMobileNumber(String phWithCode) {
        showProgress(getString(R.string.info_verifying_mobile_number));
        mainAPIInterface.getMobileNUmberStatus(phWithCode).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("message").equalsIgnoreCase("success") && jsonObject.getBoolean("result")) {
                            if (AppoPayApplication.isNetworkAvailable(getActivity())) {
                                verifyEmailId(mEmail);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.no_inteenet_connection), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            BottomAlreadyFragment fragmentBottomAlready = new BottomAlreadyFragment();
                            Bundle mBundle = new Bundle();
                            String param1 = "<font color='#000000'>" + getString(R.string.info_provide_mobile) + "</font>";
                            String param2 = "<font color='#FF0000'>" + mCountryCode + " " + mMobileNumber + "</font>";
                            String param3 = "<font color='#000000'>" + getString(R.string.info_already_used) + "</font>";
                            mBundle.putString(AppoConstants.PARAM, param1 + param2 + param3);
                            fragmentBottomAlready.setArguments(mBundle);
                            fragmentBottomAlready.show(getChildFragmentManager(), fragmentBottomAlready.getTag());
                            fragmentBottomAlready.setCancelable(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_phone_verification_failed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideProgress();

            }
        });
    }

    //ANWARI KHATOON 59 f
    //MOHAMMAD HAFIJ 74 MALE

    public void verifyEmailId(String emailId) {

        showProgress(getString(R.string.info_verifying_email));

        RequestBody mRequestBody =
                RequestBody.create(MediaType.parse("text/plain"), emailId);

        mainAPIInterface.getEmailStatusNew(mRequestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.get("message").equals(AppoConstants.SUCCESS) && !jsonObject.getBoolean("result")) {


                            requestForOtp();

                            /*String zip;
                            if (StringUtils.isEmpty(tvZip.getText().toString().trim())) {
                                zip = "-1";
                            } else {
                                zip = tvZip.getText().toString().trim();
                            }
                            mOtpRequestListener.onOtpRequest(mNameCode, mCountryCode, mMobileNumber, mEmail, mAddress, String.valueOf(mCountyId), mStateId, zip, tvCity.getText().toString().trim());*/


                        } else {

                            Toast.makeText(getActivity(), getString(R.string.error_email_already_exists), Toast.LENGTH_SHORT).show();
                            BottomAlreadyFragment fragmentBottomAlready = new BottomAlreadyFragment();
                            Bundle mBundle = new Bundle();
                            String param1 = "<font color='#000000'>" + getString(R.string.info_provided_email) + "</font>";
                            String param2 = "<font color='#FF0000'>" + mEmail + "</font>";
                            String param3 = "<font color='#000000'>" + " has been already used." + "</font>";
                            mBundle.putString(AppoConstants.PARAM, param1 + param2 + param3);
                            fragmentBottomAlready.setArguments(mBundle);
                            fragmentBottomAlready.show(getChildFragmentManager(), fragmentBottomAlready.getTag());
                            fragmentBottomAlready.setCancelable(false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_email_verification_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideProgress();

            }
        });

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

    private void getCountryList() {
        showProgress(getString(R.string.info_getting_country_code));

        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                hideProgress();
                mListCountry = new ArrayList<>();
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
                mProgress.dismiss();
            }
        });
    }

    private void disableSelectCountry() {

        int pos = -1;
        for (int i = 0; i < mListCountry.size(); i++) {
            pos = pos + 1;
            String countryname = mListCountry.get(i).getCountryname();
            String regex = "\\s+";
            //Replacing the pattern with single space
            String result = countryname.replaceAll(regex, " ");
            if (result.equalsIgnoreCase(mCountryName)) {
                String country = " ( " + mListCountry.get(i).getCountrycode() + " )  " + mListCountry.get(i).getCountryname();
                mCountyId = mListCountry.get(i).getId();

                break;
            }
        }
        mStates = mListCountry.get(pos).getStates();

    }

    private void requestForOtp() {
        showProgress(getString(R.string.info_sending_otp));
        JsonObject param = new JsonObject();
        param.addProperty("mobileNumber", mMobileNumber);
        String hashCode = Helper.getHashCode();
        param.addProperty("hashKey", hashCode);
        param.addProperty("phoneCode", mCountryCode);


        mainAPIInterface.getOtpforUser(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        String zip;
                        if (StringUtils.isEmpty(tvZip.getText().toString().trim())) {
                            zip = "-1";
                        } else {
                            zip = tvZip.getText().toString().trim();
                        }
                        mOtpRequestListener.onOtpRequest(mNameCode, mCountryCode, mMobileNumber, mEmail, mAddress, String.valueOf(mCountyId), mStateId, zip, tvCity.getText().toString().trim());
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

                hideProgress();

            }
        });

    }


    /*private void initAutoCompleteTextView() {
        placesAutocomplete.setThreshold(1);
        placesAutocomplete.setOnItemClickListener(autocompleteClickListener);
        mAutoAdapter = new AutoCompleteAdapter(getActivity(), mPlaceClient);
        placesAutocomplete.setAdapter(mAutoAdapter);
    }*/

    private String mAddress = "";
    /*private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> mAutoAdapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = mAutoAdapter.getItem(i);
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
                    mPlaceClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            String address = task.getPlace().getAddress();
                            String name = task.getPlace().getName();

                            mAddress=name +","+address;
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
    };*/


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOtpRequestListener = (OtpRequestListener) context;

    }

    public void setStateName(String statename, int stateid) {
        tvState.setText(statename);
        mStateId = stateid;
    }
}
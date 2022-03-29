package com.stuffer.stuffers.fragments.bottom;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.GzipRequestInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.utils.Helper;
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class ScanFragment extends Fragment {
    private static final String TAG = "ScanFragment";
    View mView;
    private CodeScanner mCodeScanner;
    ScanRequestListener mListener;
    TextView tvText;
    private MainAPIInterface unionPayService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.scan_fragment, container, false);
        tvText = mView.findViewById(R.id.tvText);
        unionPayService = ApiUtils.getUnionPayService();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new GzipRequestInterceptor())
                .build();
        AndroidNetworking.initialize(getContext(), okHttpClient);


        CodeScannerView scannerView = mView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);


        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String scanText = result.getText();
                        Log.e(TAG, "run: " + scanText);
                        if (AppoPayApplication.isNetworkAvailable(getContext())) {
                            String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                            String vaultValue1 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
                            if (StringUtils.isEmpty(vaultValue)) {
                                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                                errorDialogFragment.setArguments(bundle);
                                errorDialogFragment.setCancelable(false);
                                errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                            } else {
                                try {
                                    JSONObject root = new JSONObject(vaultValue);
                                    JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                                    JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                                    if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {//|| zipCode.equalsIgnoreCase("null")) {
                                        ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                                        fragment.setArguments(bundle);
                                        fragment.setCancelable(false);
                                        fragment.show(getChildFragmentManager(), fragment.getTag());
                                    } else {
                                        getDecodedData2(vaultValue1, scanText);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        /*if (AppoPayApplication.isNetworkAvailable(getContext())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String vaultValue1 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
                    String scanText = "000201010211153134270591000203440000000000000965204481653038405802PA5913STUFFRS, S.A.6006PANAMA626001200000000000000000000005200000000000000000000007080000009663047290";
                    getDecodedData2(vaultValue1, scanText);
                }
            });
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }*/

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        return mView;
    }

    private void getDecodedData2(String vaultValue1, String scanText) {
        try {
            MerchantPresentedMode decode = DecoderMpm.decode(scanText, MerchantPresentedMode.class);
            String param = new Gson().toJson(decode);
            mListener.onRequestListener(scanText);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Invalid Format", Toast.LENGTH_LONG).show();
            mCodeScanner.startPreview();
        }
    }

    private void getDecodedData(String accesstoken, String scanText) {
        String bearer_ = Helper.getAppendAccessToken("Bearer ", accesstoken);
        String decodeUrl = Constants.BASE_UNION_PAY + Constants.QRCODE_DECODE;
        Log.e(TAG, "getDecodedData: " + decodeUrl);
        AndroidNetworking.post(decodeUrl)
                .addHeaders("Content-Type", "text/plain")
                //.addHeaders("Authorization", bearer_)
                .addHeaders("Authorization", " Bearer sdsd")
                .addStringBody(scanText)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
            }

            @Override
            public void onError(ANError anError) {
                Log.e(TAG, "onError: message :: " + anError.getMessage());
                Log.e(TAG, "onError: details :: " + anError.getErrorDetail());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ScanRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement ScanRequestListener");
        }
    }
}

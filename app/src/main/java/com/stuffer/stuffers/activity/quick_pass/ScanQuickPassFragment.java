package com.stuffer.stuffers.activity.quick_pass;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.PayUserActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.Result;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;


public class ScanQuickPassFragment extends Fragment {
    View mView;
    private CodeScanner mCodeScanner;
    ScanRequestListener mListener;
    private static final String TAG = "ScanQuickPassFragment";
    private MainAPIInterface unionPayService;

    public ScanQuickPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_scan_quick_pass, container, false);
        final CodeScannerView scannerView = mView.findViewById(R.id.scanner_view);
        unionPayService = ApiUtils.getUnionPayService();
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e(TAG, "run: called :: "+result.getText());
                        String scanText = result.getText();
                        String[] scanTextArrays = scanText.split("\\|");
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
                                    if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {
                                        ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                                        fragment.setArguments(bundle);
                                        fragment.setCancelable(false);
                                        fragment.show(getChildFragmentManager(), fragment.getTag());
                                    } else {
                                        /*if (!(scanTextArrays.length > 5)) {
                                            Toast.makeText(getContext(), getString(R.string.error_merchant_details), Toast.LENGTH_SHORT).show();
                                        } else {
                                            mListener.onRequestListener(scanText);
                                        }*/
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

    /*    getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               String scanText="6367820101779950083|Cerca24|63516303|507| support@cerca24.com|undefined|USD";
                mListener.onRequestListener(scanText);
            }
        });*/






        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        return mView;
    }

    private void getDecodedData2(String vaultValue1, String scanText) {
        String bearer_ = Helper.getAppendAccessToken("Bearer ", vaultValue1);
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), scanText);
        unionPayService.getDecodeFormat(body, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + response);
                Log.e(TAG, "onResponse: " + response.body());
                JsonObject scanText = response.body();
                String scan = new Gson().toJson(scanText);
                if (scan == null || scan.equalsIgnoreCase("null")) {
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                    mCodeScanner.startPreview();
                } else {
                    mListener.onRequestListener(scan);
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
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
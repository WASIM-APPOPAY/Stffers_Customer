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
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.google.gson.Gson;
import com.google.zxing.Result;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;


public class QrScanFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private ScanRequestListener mListener;
    private static final String TAG = "QrScanFragment";
    public QrScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        final CodeScannerView scannerView = mView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: called :: "+result.getText());
                        String scanText = result.getText();
                        //String[] scanTextArrays = scanText.split("\\|");
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
                                    /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {
                                        ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                                        fragment.setArguments(bundle);
                                        fragment.setCancelable(false);
                                        fragment.show(getChildFragmentManager(), fragment.getTag());
                                    } else {
                                       getDecodedData2(vaultValue1, scanText);

                                    }*/
                                    getDecodedData2(vaultValue1, scanText);
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

        try {
            MerchantPresentedMode decode = DecoderMpm.decode(scanText, MerchantPresentedMode.class);
            String param = new Gson().toJson(decode);
            mListener.onRequestListener(scanText);
        } catch (Exception e) {
            Log.e(TAG, "getDecodedData2: called" );
            e.printStackTrace();
            Toast.makeText(getActivity(), "Invalid Format", Toast.LENGTH_LONG).show();
            mCodeScanner.startPreview();
        }


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
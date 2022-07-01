package com.stuffer.stuffers.fragments.bottom;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

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
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.InnerScanListener;
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class ScanAppopayFragment extends Fragment {
    private static final String TAG = "ScanAppopayFragment";

    private View mView;
    private CodeScanner mCodeScanner;
    private InnerScanListener mListener;

    public ScanAppopayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_scan_appopay, container, false);

        mView = inflater.inflate(R.layout.scan_fragment, container, false);

        CodeScannerView scannerView = mView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);


        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e(TAG, "run: called :: " + result.getText());
                        String scanText = result.getText();
                        //String[] scanTextArrays = scanText.split("\\|");
                        if (AppoPayApplication.isNetworkAvailable(getContext())) {
                            String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                            if (StringUtils.isEmpty(vaultValue)) {
                                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                                errorDialogFragment.setArguments(bundle);
                                errorDialogFragment.setCancelable(false);
                                errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                            } else {
                                try {
                                    MerchantPresentedMode decode = DecoderMpm.decode(scanText, MerchantPresentedMode.class);

                                    //scanText = new Gson().toJson(decode);
                                    mListener.onInnerRequestListener(scanText);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "Not a valid QR-Code Format", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
//39990571040177995008382|Cerca24|63516303|507|USD|321654876534215|78434

       /* if (AppoPayApplication.isNetworkAvailable(getContext())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "run: ");
                    mListener.onRequestListener("6367820101870174396|Cerca 24|63516303|507| support@cerca24.com|undefined|USD");
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
            mListener = (InnerScanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement ScanRequestListener");
        }
    }
}

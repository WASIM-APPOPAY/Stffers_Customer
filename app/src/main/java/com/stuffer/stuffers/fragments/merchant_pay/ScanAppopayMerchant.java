package com.stuffer.stuffers.fragments.merchant_pay;

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
import com.google.zxing.Result;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.InnerScanListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

import org.apache.commons.lang3.StringUtils;


public class ScanAppopayMerchant extends Fragment {
    private static final String TAG = "ScanAppopayMerchant";
    private View mView;
    private CodeScanner mCodeScanner;
    private InnerScanListener mListener;

    public ScanAppopayMerchant() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_scan_appopay_merchant, container, false);

        mView = inflater.inflate(R.layout.scan_fragment, container, false);

        CodeScannerView scannerView = mView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);


        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "run: called :: " + result.getText());
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
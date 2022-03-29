package com.stuffer.stuffers.fragments.homeitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.lifestyle.ElectricityActivity;
import com.stuffer.stuffers.activity.lifestyle.PhoneActivity;
import com.stuffer.stuffers.activity.lifestyle.TvActivity;
import com.stuffer.stuffers.activity.lifestyle.WaterActivity;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class LifeStyleFragment extends Fragment {


    private String mParam1;
    private String mParam2;
    private StartActivityListener mListener;
    private static final String TAG = "LifeStyleFragment";
    private LinearLayout llRecharge, llElectricity, llPhone, llTv, llWater;

    public LifeStyleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_life_style, container, false);
        llRecharge = (LinearLayout) mView.findViewById(R.id.llRecharge);
        llElectricity = (LinearLayout) mView.findViewById(R.id.llElectricity);
        llPhone = (LinearLayout) mView.findViewById(R.id.llPhone);
        llTv = (LinearLayout) mView.findViewById(R.id.llTv);
        llWater = (LinearLayout) mView.findViewById(R.id.llWater);

        llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                //Log.e(TAG, "onClick: " + vaultValue);
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

                        /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0") ){//|| zipCode.equalsIgnoreCase("null")) {
                            ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                            fragment.setArguments(bundle);
                            fragment.setCancelable(false);
                            fragment.show(getChildFragmentManager(), fragment.getTag());
                        } else {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }*/
                        if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

        llElectricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentElectricity = new Intent(getContext(), ElectricityActivity.class);
                startActivity(intentElectricity);

            }
        });


        llPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPhone = new Intent(getContext(), PhoneActivity.class);
                startActivity(intentPhone);
            }
        });

        llTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTv = new Intent(getContext(), TvActivity.class);
                startActivity(intentTv);
            }
        });

        llWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentllWater = new Intent(getContext(), WaterActivity.class);
                startActivity(intentllWater);
            }
        });

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (StartActivityListener) context;

    }
}
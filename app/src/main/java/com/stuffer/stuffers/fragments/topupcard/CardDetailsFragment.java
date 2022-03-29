package com.stuffer.stuffers.fragments.topupcard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.fragments.dialog.CurrencyDialogFragment;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.CardNumberEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardDetailsFragment extends Fragment {
    private static final String TAG = "CardDetailsFragment";
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private List<CurrencyResult> result;
    private MyTextView btnCurrency;
    private MyTextView tvCurrency;
    private CurrencyDialogFragment dialogFragment;
    private ImageView ivCardType;
    private CardNumberEditText card_number_field_text;
    private TextInputEditText cardholder_field_text;
    private TextInputEditText expiry_date_field_text;
    private TextInputEditText card_filed_cvv;

    public CardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_details, container, false);
        tvCurrency = (MyTextView) view.findViewById(R.id.tvCurrency);
        ivCardType = (ImageView) view.findViewById(R.id.ivCardType);
        card_number_field_text = (CardNumberEditText) view.findViewById(R.id.card_number_field_text);
        cardholder_field_text = (TextInputEditText) view.findViewById(R.id.cardholder_field_text);
        expiry_date_field_text = (TextInputEditText) view.findViewById(R.id.expiry_date_field_text);
        card_filed_cvv = (TextInputEditText) view.findViewById(R.id.card_filed_cvv);
        mainAPIInterface = ApiUtils.getAPIService();
        Bundle arguments = this.getArguments();

        String stringParam = arguments.getString(AppoConstants.INFO);
        initValues(stringParam);


        getCurrencyCode();
        tvCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new CurrencyDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.INFO, (ArrayList<? extends Parcelable>) result);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
            }
        });
        return view;
    }

    private void initValues(String stringParam) {
        try {
            JSONObject jsonParam = new JSONObject(stringParam);
            String cc_number = jsonParam.getString(AppoConstants.CC_NUMBER);
            String cc_exp = jsonParam.getString(AppoConstants.CC_EXP);
            String first_name = jsonParam.getString(AppoConstants.FIRST_NAME);
            String last_name = jsonParam.getString(AppoConstants.LAST_NAME);
            String cc_type = jsonParam.getString(AppoConstants.CC_TYPE);
            setData(cc_number, cc_exp, first_name, last_name, cc_type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(String cc_number, String cc_exp, String first_name, String last_name, String cc_type) {
        if (cc_type.equalsIgnoreCase(AppoConstants.VISA)) {
            ivCardType.setImageResource(R.drawable.visa_my_card);
        } else if (cc_type.equalsIgnoreCase(AppoConstants.MASTERCARD)) {
            ivCardType.setImageResource(R.drawable.mastercard_my_card);
        } else if (cc_type.equalsIgnoreCase(AppoConstants.AMERICA_EXPRESS)) {
            ivCardType.setImageResource(R.drawable.amex_my_card);
        }
        card_number_field_text.setText(cc_number);
        cardholder_field_text.setText(first_name + " " + last_name);
        expiry_date_field_text.setText(cc_exp);


    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_currency_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    result = response.body().getResult();

                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    public void setCurrency(int mId, String mCurrencyCode) {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getId() == mId) {
                tvCurrency.setText(result.get(i).getCurrencyCode());
                return;
            }
        }
    }
}

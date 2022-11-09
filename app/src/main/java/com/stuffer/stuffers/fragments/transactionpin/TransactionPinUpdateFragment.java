package com.stuffer.stuffers.fragments.transactionpin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionPinUpdateFragment extends Fragment {
    private static final String TAG = "TransactionPinUpdateFra";
    private FragmentReplaceListener mReplaceListener;
    private MyTextView tvHintsTransaction;
    private MyEditText edtOldTransactionPin, edtNewTransactionPin;
    private MyTextView tvSubmit;
    private String phonecode, mobilenumber, phwithcode, info;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private AlertDialog dialogPayment;

    public TransactionPinUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_pin_update, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        edtOldTransactionPin = view.findViewById(R.id.edtOldTransactionPin);
        edtNewTransactionPin = view.findViewById(R.id.edtNewTransactionPin);
        tvHintsTransaction = view.findViewById(R.id.tvHintsTransaction);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        Bundle arguments = this.getArguments();
        phonecode = arguments.getString(AppoConstants.PHONECODE);
        mobilenumber = arguments.getString(AppoConstants.MOBILENUMBER);
        phwithcode = arguments.getString(AppoConstants.PHWITHCODE);
        info = arguments.getString(AppoConstants.INFO);
        String infoTransaction = "<font color='#00baf2'>" + getString(R.string.trans_note) + "</font>" + " : " + getString(R.string.trans_info);
        tvHintsTransaction.setText(Html.fromHtml(infoTransaction));
        edtOldTransactionPin.setText(info);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOldTransactionPin.getText().toString().trim().isEmpty()) {
                    edtOldTransactionPin.setError(getString(R.string.info_old_transaction_pin));
                    edtOldTransactionPin.requestFocus();
                    return;
                }

                if (edtOldTransactionPin.getText().toString().trim().length() < 6) {
                    edtOldTransactionPin.setError(getString(R.string.info_transaction_pin_length));
                    edtOldTransactionPin.requestFocus();
                    return;
                }

                if (edtNewTransactionPin.getText().toString().trim().isEmpty()) {
                    edtNewTransactionPin.setError(getString(R.string.info_new_transaction_pin));
                    edtNewTransactionPin.requestFocus();
                    return;
                }

                if (edtNewTransactionPin.getText().toString().trim().length() < 6) {
                    edtNewTransactionPin.setError(getString(R.string.info_transaction_pin_length));
                    edtNewTransactionPin.requestFocus();
                    return;
                }

                if (!edtNewTransactionPin.getText().toString().trim().equalsIgnoreCase(edtOldTransactionPin.getText().toString().trim())) {
                    Toast.makeText(getContext(), "pin mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                Helper.hideKeyboard(edtOldTransactionPin, getContext());
                if (AppoPayApplication.isNetworkAvailable(getContext())) {

                    setTransactionPin();

                } else {
                    Toast.makeText(getContext(), "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;

    }


    private void setTransactionPin() {
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        int userId = Helper.getUserId();
        showLoading();
        mainAPIInterface.getSetTransactionPin(String.valueOf(userId), edtNewTransactionPin.getText().toString().trim(), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();

                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();
                            Helper.setUserDetailsNull();
                            DataVaultManager.getInstance(getActivity()).saveUserDetails(jsonUserDetails);
                            Toast.makeText(getContext(), "Request Successfully updated", Toast.LENGTH_SHORT).show();
                            onUpdateProfile();

                        } else {
                            if (response.code() == 401) {
                                DataVaultManager.getInstance(getContext()).saveUserDetails("");
                                DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                                Intent intent = new Intent(getContext(), SignInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (response.code() == 400) {
                                Helper.showErrorMessage(getActivity(), "Error Code : " + response.code());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.required_filled), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.showErrorMessage(getActivity(), t.getMessage());
                hideLoading();
            }
        });
    }


    private void showLoading() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait, Sending your request.");
        dialog.show();
    }

    private void hideLoading() {
        dialog.dismiss();
    }

    private void onUpdateProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_success_topup, null);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thank You!\nYour Transaction pin has been updated Successfully");
        MyTextView tvTitleCommon = dialogLayout.findViewById(R.id.tvTitleCommon);
        tvTitleCommon.setText(getString(R.string.info_transaction_pin));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invalidateDetails();
                onOkPress();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    public void onOkPress() {
        if (dialogPayment != null)
            dialogPayment.dismiss();
        getActivity().finish();
        /*DataVaultManager.getInstance(getContext()).saveUserDetails("");
        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
        DataVaultManager.getInstance(getContext()).saveCardToken("");
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
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

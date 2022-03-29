package com.stuffer.stuffers.fragments.finance_fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;


public class RequestAccountFragment extends Fragment {
    private View mView;
    MyTextView tvAddress, tvCityName, tvEmail, tvDob, tvPhone, tvUserName;
    ImageView ivExpand;
    MyTextViewBold tvExpiryDate;
    private int one = 0;
    private ExpandableLayout layoutExpand;
    private String mDob;
    private Calendar newCalendar;
    private androidx.appcompat.app.AlertDialog dialogMerchant;
    private MyButton btnSubmit;
    private MyEditText edPassportNo;


    public RequestAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_request_account, container, false);
        btnSubmit = mView.findViewById(R.id.btnSubmit);
        ivExpand = mView.findViewById(R.id.ivExpand);
        edPassportNo = mView.findViewById(R.id.edPassportNo);
        layoutExpand = (ExpandableLayout) mView.findViewById(R.id.layoutExpand);

        tvAddress = mView.findViewById(R.id.tvAddress);
        tvCityName = mView.findViewById(R.id.tvCityName);
        tvEmail = mView.findViewById(R.id.tvEmial);
        tvDob = mView.findViewById(R.id.tvDob);
        tvPhone = mView.findViewById(R.id.tvPhone);
        tvExpiryDate = mView.findViewById(R.id.tvExpiryDate);
        tvUserName = mView.findViewById(R.id.tvUserName);
        newCalendar = Calendar.getInstance();
        tvExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateOfExpiry();
            }
        });
        invalidateUserInfo();
        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one == 0) {
                    ivExpand.setImageResource(R.drawable.ic_add_primary);
                    layoutExpand.setExpanded(false);
                    one = 1;
                } else {
                    ivExpand.setImageResource(R.drawable.ic_remove_primary);
                    layoutExpand.setExpanded(true);
                    one = 0;
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edPassportNo.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "please enter id no", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tvExpiryDate.getText().toString().trim().equalsIgnoreCase("Select Expiry Date")) {
                    Toast.makeText(getActivity(), "please select expiry date", Toast.LENGTH_SHORT).show();
                    return;
                }
                showSuccessDialog("Your request has been successfully sent, we will contact you very shortly " + "\nThank Your");
            }
        });

        return mView;
    }

    public void getDateOfExpiry() {
        Calendar minCal = Calendar.getInstance();
        int i1 = minCal.get(Calendar.YEAR) + 10;
        minCal.set(i1, 11, 31);
        DatePickerDialog StartTime = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");
                mDob = dateFormatter.format(newDate.getTime());
                tvExpiryDate.setText("Expiry Date : " + dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMaxDate(minCal.getTimeInMillis());
        StartTime.setCanceledOnTouchOutside(false);
        StartTime.show();
    }

    private void invalidateUserInfo() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                JSONObject mIndex = new JSONObject(vaultValue);
                //Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                tvUserName.setText("User Name : " + result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                tvPhone.setText("Mobile Number : " + result.getString(AppoConstants.MOBILENUMBER));
                tvEmail.setText("Emial Id : " + result.getString(AppoConstants.EMIAL));
                JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                if (customerDetails.isNull(AppoConstants.CITYNAME)) {
                    //layoutAfterUpdate.setVisibility(View.GONE);
                    //tvScreen.setText("Screen Lock Pin : " + " NA ");
                    //tvTrans.setText("Transaction Pin : " + " NA ");
                    tvCityName.setText("City Name : " + " NA ");
                    tvAddress.setText("Address : " + " NA ");
                    tvDob.setText("Date of birth : " + " NA ");
                } else {
                    //layoutAfterUpdate.setVisibility(View.VISIBLE);
                    //tvScreen.setText("Screen Lock Pin : " + " NA ");
                    // tvTrans.setText("Transaction Pin : " + result.getString(AppoConstants.TRANSACTIONPIN));
                    tvCityName.setText("City Name : " + customerDetails.getString(AppoConstants.CITYNAME));
                    tvAddress.setText("Address : " + customerDetails.getString(AppoConstants.ADDRESS));
                    tvDob.setText("Date of birth : " + customerDetails.getString(AppoConstants.DOB));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void showSuccessDialog(String param) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_merchant, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText(param);
        tvHeader.setText("Clientes");

        MyTextView tvTransactionId = dialogLayout.findViewById(R.id.tvTransactionId);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvTransactionId.setText(param);
        tvTransactionId.setVisibility(View.GONE);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);
        dialogMerchant.setCancelable(false);

        dialogMerchant.show();
    }

    private void redirect() {
        dialogMerchant.dismiss();
        getActivity().onBackPressed();
    }

}
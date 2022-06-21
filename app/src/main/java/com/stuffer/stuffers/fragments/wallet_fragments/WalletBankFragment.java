package com.stuffer.stuffers.fragments.wallet_fragments;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.OptionSelectListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.TimeUtils;
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


public class WalletBankFragment extends Fragment {
    private static final String TAG = "WalletBankFragment";
    private View mView;
    MyTextView tvAddress, tvCityName, tvEmail, tvDob, tvPhone, tvUserName, tvState;
    ImageView ivExpand;
    private int one = 0;
    private ExpandableLayout layoutExpand;
    MyButton btnNext;
    private Calendar newCalendar;
    MyEditText edSourceIncome, edMonthlyIncome, edTotalMember, edPassportNo;
    MyTextViewBold tvExpiryDate;
    private String mDob;
    //Amount to Top-Up

    private OptionSelectListener mListenerSelect;
    private String userName;

    public WalletBankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_wallet_bank, container, false);
        ivExpand = mView.findViewById(R.id.ivExpand);
        layoutExpand = (ExpandableLayout) mView.findViewById(R.id.layoutExpand);
        newCalendar = Calendar.getInstance();

        tvAddress = mView.findViewById(R.id.tvAddress);
        tvCityName = mView.findViewById(R.id.tvCityName);
        tvState = mView.findViewById(R.id.tvState);
        tvEmail = mView.findViewById(R.id.tvEmial);
        tvDob = mView.findViewById(R.id.tvDob);
        tvPhone = mView.findViewById(R.id.tvPhone);
        tvUserName = mView.findViewById(R.id.tvUserName);
        edSourceIncome = mView.findViewById(R.id.edSourceIncome);
        edMonthlyIncome = mView.findViewById(R.id.edMonthlyIncome);
        edTotalMember = mView.findViewById(R.id.edTotalMember);
        edPassportNo = mView.findViewById(R.id.edPassportNo);
        tvExpiryDate = mView.findViewById(R.id.tvExpiryDate);
        btnNext = mView.findViewById(R.id.btnNext);
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyDetails();
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

    private void verifyDetails() {


        if (edPassportNo.getText().toString().trim().isEmpty()) {
            edPassportNo.setError("Enter id/passport number");
            edPassportNo.requestFocus();
            return;
        }
        if (tvExpiryDate.getText().toString().trim().equals("Select Expiry Date")) {
            Toast.makeText(getContext(), "Select Expiry Date", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject paramSent = new JSONObject();
        try {
            paramSent.put(AppoConstants.USERNAME, userName);
            paramSent.put(AppoConstants.SOURCE_OF_INCOME, "NA");
            paramSent.put(AppoConstants.MONTHLY_INCOME, "NA");
            paramSent.put(AppoConstants.NO_OF_HOUSE_HOLD, "NA");
            paramSent.put(AppoConstants.PASSPORT_NUMBER, edPassportNo.getText().toString().trim());
            paramSent.put(AppoConstants.EXPIRYDATE, mDob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListenerSelect.onSelectConfirm(AppoConstants.NEXT_SCREEN, paramSent);

    }

    private void invalidateUserInfo() {
        //Log.e(TAG, "invalidateUserInfo: invalid info called " );
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                JSONObject mIndex = new JSONObject(vaultValue);
                //Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                userName = result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME);
                tvUserName.setText("User Name : " + userName);
                tvPhone.setText("Mobile Number : " + result.getString(AppoConstants.MOBILENUMBER));
                tvEmail.setText("Emial Id : " + result.getString(AppoConstants.EMIAL));

                JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                //Log.e(TAG, "invalidateUserInfo: "+customerDetails.toString() );

                tvAddress.setText("Address : " + customerDetails.getString(AppoConstants.ADDRESS));
                //tvDob.setText("Date of birth : " + customerDetails.getString(AppoConstants.DOB));

                edPassportNo.setText("Id Number " + customerDetails.getString(AppoConstants.IDNUMBER));
                //tvExpiryDate.setText("Expiry Date : " + customerDetails.getString(AppoConstants.EXPIRYDATE));
                try {
                    String mExpiry = TimeUtils.parseLongDate(customerDetails.getString(AppoConstants.EXPIRYDATE), TimeUtils.DOBFORMAT);
                    tvExpiryDate.setText("Expiry Date : " + mExpiry);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    String mDob = TimeUtils.parseLongDate(customerDetails.getString(AppoConstants.DOB), TimeUtils.DOBFORMAT);
                    tvDob.setText("Date of birth : " + mDob);
                }catch (Exception e){
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListenerSelect = (OptionSelectListener) context;
    }
}
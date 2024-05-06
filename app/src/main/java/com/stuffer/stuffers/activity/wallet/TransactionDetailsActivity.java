package com.stuffer.stuffers.activity.wallet;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MySwitchView;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionDetailsActivity extends AppCompatActivity {
    MyTextView tvTransactionId, tvDateTime, tvSenderName, tvMobileNUmber, tvAmountCurrency;
    MyTextView tvMessage;
    String mReceiverString, mAccountNumber, mEncryptAccountNumber;
    MySwitchView swAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        setupActionBar();

        swAccountNumber = findViewById(R.id.swAccountNumber);
        tvTransactionId = findViewById(R.id.tvTransactionId);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvSenderName = findViewById(R.id.tvSenderName);
        tvMobileNUmber = findViewById(R.id.tvMobileNUmber);
        tvAmountCurrency = findViewById(R.id.tvAmountCurrency);
        tvMessage = findViewById(R.id.tvMessage);
        mReceiverString = getIntent().getStringExtra(AppoConstants.INFO);

        if (StringUtils.isEmpty(mReceiverString)) {
            Toast.makeText(this, "exception occur", Toast.LENGTH_SHORT).show();
        } else {
            readTransactionDetails();
        }

        swAccountNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!StringUtils.isEmpty(mAccountNumber)) {
                    if (isChecked) {
                        swAccountNumber.setText("A/c No : "+mAccountNumber);
                    } else {
                        swAccountNumber.setText("A/c No : "+mEncryptAccountNumber);
                    }
                }
            }
        });

    }

    /*String id;
       String mobilenumber;
       String accountnumber;
       String transactiontype;
       String transactiondescription;
       String transactiondate;
       String transactionamount;
       String pendingbalance;
       String transactionstatus;
       String userid;
       String transactionid;
       String curencyid;
       String sendername;
       String sendermobile;
       String processingfees;
       String bankfees;
       String paymenttype;
       String areacode;
       String taxes;*/
    private void readTransactionDetails() {
        try {
            JSONObject receivedParam = new JSONObject(mReceiverString);
            if (receivedParam.has("transactionid")){
                tvTransactionId.setText(receivedParam.getString("transactionid"));
            }else {
                long currentTimeMillis = System.currentTimeMillis();
                long id = currentTimeMillis / 2;
                tvTransactionId.setText(""+id);
            }
            tvDateTime.setText(receivedParam.getString("viewdate"));
            //tvDateTime.setText(receivedParam.getString("transactiondate"));
            tvSenderName.setText(receivedParam.getString("sendername"));

            tvMobileNUmber.setText(receivedParam.getString("sendermobile"));
            String amountCurrency = receivedParam.getString("currencycode") + " " + receivedParam.getString("transactionamount");
            tvAmountCurrency.setText(amountCurrency);
            tvMessage.setText(receivedParam.getString("transactiondescription"));
            mAccountNumber = receivedParam.getString("mAccountNumber");
            mEncryptAccountNumber = receivedParam.getString("mEncryptAccountNumber");
            swAccountNumber.setText("A/c No : "+mEncryptAccountNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void setupActionBar() {
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText(getString(R.string.info_transaction_details));
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

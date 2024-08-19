package com.stuffer.stuffers.commonChat.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.quick_pass.CardTermsActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity3;
import com.stuffer.stuffers.communicator.LaterListener;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

public class PinDemoActivity extends AppCompatActivity implements View.OnClickListener, UnionPayCardListener, LaterListener {

    private MyEditText edCnfmTransPin, edTransPin;
    private MyButton btnConfirm, btnCloseDialog;
    private BottomNotCard mBottomNotCard;
    private AppCompatDialog dialogLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_demo);
        setupActionBar();
        edCnfmTransPin = (MyEditText) findViewById(R.id.edCnfmTransPin);
        edTransPin = (MyEditText) findViewById(R.id.edTransPin);
        btnConfirm = (MyButton) findViewById(R.id.btnConfirm);
        btnCloseDialog = (MyButton) findViewById(R.id.btnCloseDialog);
        btnConfirm.setOnClickListener(this);

        btnCloseDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnConfirm:
                //getRandomKeyFromRange();
                if (edTransPin.getText().toString().trim().isEmpty()) {
                    edTransPin.setError("Please enter transaction pin");
                    edTransPin.requestFocus();

                    return;
                }
                if (edTransPin.getText().toString().length() < 6) {
                    edTransPin.setError("Transaction pin should be six digit");
                    edTransPin.requestFocus();

                    return;
                }
                if (edCnfmTransPin.getText().toString().trim().isEmpty()) {
                    edCnfmTransPin.setError("Please Confirm transaction pin");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edCnfmTransPin.getText().toString().trim().length() < 6) {
                    //Toast.makeText(getActivity(), "Transaction pin should be six digit", Toast.LENGTH_SHORT).show();
                    edCnfmTransPin.setError("Confirm pin should be six digit");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edTransPin.getText().toString().trim().equalsIgnoreCase(edCnfmTransPin.getText().toString().trim())) {
                    //mPinListenr.onPinConfirm(edTransPin.getText().toString().trim());

                    Toast.makeText(PinDemoActivity.this, "Your have created your transaction pin Successfully", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showNoCardDialog();
                        }
                    }, 2000);

                } else {
                    Toast.makeText(PinDemoActivity.this, "pin mismatch", Toast.LENGTH_SHORT).show();
                }


                //Log.e(TAG, "onClick: " + codeString);
                break;


        }
    }

    private void setupActionBar() {
        //MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        //common_toolbar_title.setText(mTitle);
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showNoCardDialog() {
        mBottomNotCard = new BottomNotCard();
        mBottomNotCard.show(getSupportFragmentManager(), mBottomNotCard.getTag());
        mBottomNotCard.setCancelable(false);
    }

    @Override
    public void onCardRequest(int type) {
        if (mBottomNotCard != null)
            mBottomNotCard.dismiss();

        Intent intentUnion = new Intent(PinDemoActivity.this, CardTermsActivity.class);
        intentUnion.putExtra(AppoConstants.CARDTYPE, type);
        startActivity(intentUnion);
        finish();
        /*if (type == 1) {
            Intent intentUnion = new Intent(HomeActivity3.this, UnionPayActivity.class);
            intentUnion.putExtra(AppoConstants.CARDTYPE, type);
            startActivity(intentUnion);

        } else {
            Intent intentUnion = new Intent(HomeActivity3.this, VisaUnionActivity.class);
            intentUnion.putExtra(AppoConstants.CARDTYPE, type);
            startActivity(intentUnion);

        }*/
    }

    @Override
    public void onLaterRequest() {
        try {
            if (mBottomNotCard != null)
                mBottomNotCard.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_no_thanks, null);
        MyTextView tvCommonContent = dialogLayout.findViewById(R.id.tvCommonContent);
        MyTextView btnNoThanks = dialogLayout.findViewById(R.id.btnNoThanks);
        MyTextView btnApply = dialogLayout.findViewById(R.id.btnApply);
        String info = "Apply for a personal Visa or UnionPay virtual card tied directly to your " + "<font color='#0658A1'>" + "WALLET" + "</font>";
        tvCommonContent.setText(Html.fromHtml(info));

        builder.setView(dialogLayout);

        btnNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLater.dismiss();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCardAgain();
            }
        });

        dialogLater = builder.create();
        dialogLater.setCanceledOnTouchOutside(false);
        dialogLater.show();


    }

    private void requestCardAgain() {
        dialogLater.dismiss();
        showNoCardDialog();
    }
}
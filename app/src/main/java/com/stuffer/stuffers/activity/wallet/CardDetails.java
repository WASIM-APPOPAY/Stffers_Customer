package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stuffer.stuffers.utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.CardNumberValidation;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.CardDateEditText;
import com.stuffer.stuffers.views.CardNumberEditText;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyCardEditText;
import com.stuffer.stuffers.views.MyCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class CardDetails extends AppCompatActivity {
    private static final String TAG = "CardDetails";
    private MyCardEditText card_number_field_text;
    private TextInputEditText cardholder_field_text;
    private CardDateEditText expiry_date_field_text;
    private TextInputEditText card_filed_cvv;
    private MyButton btnSaveCard;
    private String cardLastName;
    private String mCardType;
    private ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    private MyCheckBox defaultCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        mainAPIInterface = ApiUtils.getAPIService();
        card_number_field_text = (MyCardEditText) findViewById(R.id.card_number_field_text);
        cardholder_field_text = (TextInputEditText) findViewById(R.id.cardholder_field_text);
        expiry_date_field_text = (CardDateEditText) findViewById(R.id.expiry_date_field_text);
        card_filed_cvv = (TextInputEditText) findViewById(R.id.card_filed_cvv);
        btnSaveCard = (MyButton) findViewById(R.id.btnSaveCard);
        defaultCheckBox = (MyCheckBox) findViewById(R.id.defaultCheckBox);

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCardDetails();
            }
        });
        setupActionBar();
    }

    private void verifyCardDetails() {

        if (card_number_field_text.getText().toString().trim().isEmpty()) {
            card_number_field_text.setError("please enter your card number");
            card_number_field_text.requestFocus();
            return;
        }

        /*if (card_number_field_text.getCardNumber().length() < 20) {
            card_number_field_text.setError("invalid format ");
            card_number_field_text.requestFocus();
            return;
        }*/

        long cardNumberLong = Long.parseLong(card_number_field_text.getCardNumber().toString().trim());
        //Log.e(TAG, "verifyCardDetails: " + cardNumberLong);
        if (!CardNumberValidation.isValid(cardNumberLong)) {
            card_number_field_text.setError("please enter a valid card ");
            card_number_field_text.requestFocus();
            return;
        }

        if (cardholder_field_text.getText().toString().trim().isEmpty()) {
            cardholder_field_text.setError("please enter your card holder name");
            cardholder_field_text.requestFocus();
            return;
        }

        if (expiry_date_field_text.getText().toString().trim().isEmpty()) {
            expiry_date_field_text.setError("Please enter expiry date");
            expiry_date_field_text.requestFocus();
            return;
        }

        if (expiry_date_field_text.getText().toString().trim().length() < 3) {
            expiry_date_field_text.setError("invalid format");
            expiry_date_field_text.requestFocus();
            return;
        }

        String cardNumber = card_number_field_text.getCardNumber();
        int cardType = card_number_field_text.getCardType(cardNumber);
        mCardType = null;
        switch (cardType) {
            case 0:
                mCardType = "Visa";
                break;
            case 1:
                mCardType = "MasterCard";
                break;
            case 2:
                mCardType = "American Express";
                break;
            case 3:
                mCardType = "Diners Club";
                break;
            case 4:
                mCardType = "Discover";
                break;
            case 5:
                mCardType = "JCB";
                break;
            default:
                mCardType = "unknown";
                break;
        }

        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);

        String cardHolderName = cardholder_field_text.getText().toString().trim();

        try {
            String[] nameArr = cardHolderName.split(" ");
            if (nameArr.length > 0) {
                cardLastName = nameArr[0];
            } else {
                cardLastName = cardHolderName;
            }
        } catch (Exception e) {
            //Log.e(TAG, "verifyCardDetails: exception");
            cardLastName = cardHolderName;
        }


        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String id = result.getString(AppoConstants.ID);
            String firstName = result.getString(AppoConstants.FIRSTNAME);
            String lastName = result.getString(AppoConstants.LASTNAME);
            JsonObject sentPayloads = new JsonObject();
            sentPayloads.addProperty(AppoConstants.CARDFIRSTNAME, "");
            sentPayloads.addProperty(AppoConstants.CARDLASTNAME, cardLastName);
            sentPayloads.addProperty(AppoConstants.CARDTYPE, mCardType);
            sentPayloads.addProperty(AppoConstants.CCEXP, expiry_date_field_text.getText().toString().trim());
            sentPayloads.addProperty(AppoConstants.CCNUMBER, card_number_field_text.getCardNumber().trim());
            sentPayloads.addProperty(AppoConstants.CVV, card_filed_cvv.getText().toString().trim());
            sentPayloads.addProperty(AppoConstants.FIRSTNAME, firstName);
            sentPayloads.addProperty(AppoConstants.ID, id);
            if (defaultCheckBox.isChecked()) {
                sentPayloads.addProperty(AppoConstants.ISDEFAULT, true);
            } else {
                sentPayloads.addProperty(AppoConstants.ISDEFAULT, false);
            }
            sentPayloads.addProperty(AppoConstants.LASTNAME, lastName);
            //saveCardType(sentPayloads, accesstoken); //open it later

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveCardType(JsonObject sentPayloads, String accesstoken) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, saving card");
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);

        mainAPIInterface.postSaveCardType(sentPayloads, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject index = new JSONObject(res);
                        if (index.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            String result = index.getString(AppoConstants.RESULT);
                            String regex = "\\d+";
                            if (result.matches(regex)) {
                                verifyCardToken(card_number_field_text.getCardNumber());
                            } else {
                                Toast.makeText(CardDetails.this, "Status : " + result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(CardDetails.this).saveUserDetails("");
                        DataVaultManager.getInstance(CardDetails.this).saveUserAccessToken("");
                        Intent intent = new Intent(CardDetails.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });


    }

    private void verifyCardToken(String cardNumber) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, saving card");
        dialog.show();
        JsonObject param = new JsonObject();
        param.addProperty(AppoConstants.CARDTOKEN, cardNumber);

        mainAPIInterface.postCheckVersateCard(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    //Log.e(TAG, "onResponse: " + res);
                    try {
                        JSONObject index = new JSONObject(res);
                        JSONObject result = index.getJSONObject(AppoConstants.RESULT);
                        if (result.toString().equalsIgnoreCase("{}")) {

                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("allow", "allow");
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Card Details");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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

  /*
    String regex = "\\d+";
    // positive test cases, should all be "true"
    System.out.println("1".matches(regex));
    System.out.println("12345".matches(regex));
    System.out.println("123456789".matches(regex));

    // negative test cases, should all be "false"
    System.out.println("".matches(regex));
    System.out.println("foo".matches(regex));
    System.out.println("aa123bb".matches(regex));
  */
//375987654321001

}

package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.adapter.recyclerview.MyCardAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.output.CardVaultModel;
import com.stuffer.stuffers.models.output.SaveCardResponse;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class MyCardsActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener {
    private static final String TAG = "MyCardsActivity";
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;

    private MainAPIInterface mainAPIInterfaceNode;

    List<SaveCardResponse.Result> mListSaveCards;
    private int counter = -1;


    private CardView cardViewDefault;
    private ImageView ivCardType;
    private MyTextView tvCardNumber;
    private ImageView ivForward;
    private int defaultPosition = -1;
    private RecyclerView rvMyOtherCards;
    ArrayList<CardVaultModel> mListVault;
    ArrayList<CardVaultModel> mListVaultDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        mainAPIInterface = ApiUtils.getAPIService();
        mainAPIInterfaceNode = ApiUtils.getAPIServiceNode();
        cardViewDefault = (CardView) findViewById(R.id.cardViewDefault);
        ivCardType = (ImageView) findViewById(R.id.ivCardType);
        tvCardNumber = (MyTextView) findViewById(R.id.tvCardNumber);
        ivForward = (ImageView) findViewById(R.id.ivForward);
        rvMyOtherCards = (RecyclerView) findViewById(R.id.rvMyOtherCards);
        rvMyOtherCards.setLayoutManager(new LinearLayoutManager(this));

        setupActionBar();
        mListVault = new ArrayList<>();
        mListVaultDefault = new ArrayList<>();
        loadCardDetails();

        cardViewDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCardDetails = new Intent(MyCardsActivity.this, RemoveCardActivity.class);
                intentCardDetails.putExtra(AppoConstants.CC_NUMBER,mListVaultDefault.get(0).getCc_number());
                intentCardDetails.putExtra(AppoConstants.CC_EXP,mListVaultDefault.get(0).getCc_exp());
                intentCardDetails.putExtra(AppoConstants.FIRST_NAME,mListVaultDefault.get(0).getFirst_name());
                intentCardDetails.putExtra(AppoConstants.LAST_NAME,mListVaultDefault.get(0).getLast_name());
                intentCardDetails.putExtra(AppoConstants.CC_TYPE,mListVaultDefault.get(0).getCc_type());
                startActivity(intentCardDetails);
            }
        });

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        ImageButton ibtnAddCards = toolbar.findViewById(R.id.ibtnAddCards);
        ibtnAddCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCard = new Intent(MyCardsActivity.this, CardDetails.class);
                startActivityForResult(intentCard, AppoConstants.CARD_REQUEST);
            }
        });

        toolbarTitle.setText("My Cards");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.CARD_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                loadCardDetails();
            } else {
                //Log.e(TAG, "onActivityResult: cancel called");
            }
        }
    }

    private void loadCardDetails() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            getCardDetails(userId, accesstoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCardDetails(String userId, String accesstoken) {

        dialog = new ProgressDialog(MyCardsActivity.this);
        dialog.setMessage("Please wait, getting currency code.");
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);

        mainAPIInterface.getUserSaveCards(userId, bearer_).enqueue(new Callback<SaveCardResponse>() {
            @Override
            public void onResponse(Call<SaveCardResponse> call, Response<SaveCardResponse> response) {
                dialog.dismiss();
                mListSaveCards = new ArrayList<>();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    mListSaveCards = response.body().getResult();
                    if (mListSaveCards.size() > 0) {
                        readVoltCards();
                    }
                    try {
                        //Log.e(TAG, "onResponse: get cards " + new JSONObject(res).toString());
                        String s = new JSONObject(res).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(MyCardsActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(MyCardsActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(MyCardsActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveCardResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void readVoltCards() {
        counter = counter + 1;

        if (counter < mListSaveCards.size()) {


            dialog = new ProgressDialog(MyCardsActivity.this);
            dialog.setMessage("Please wait");
            dialog.show();
            String transactionId = mListSaveCards.get(counter).getTransactionid();

            if (mListSaveCards.get(counter).getIsdefault()) {
                defaultPosition = counter;
            }
            JsonObject param = new JsonObject();
            param.addProperty(AppoConstants.TRASACTIONID, transactionId);
            //mainAPIInterface.getVaultById(param).enqueue(new Callback<JsonObject>() {
            mainAPIInterfaceNode.getVaultById(param).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        ////Log.e(TAG, "onResponse: counter : "+counter+" response  "+new Gson().toJson(response.body()) );
                        String res = new Gson().toJson(response.body());
                        try {
                            JSONObject index = new JSONObject(res);
                            JSONObject customerVault = index.getJSONObject(AppoConstants.CUSTOMER_VAULT);
                            JSONObject customer = customerVault.getJSONObject(AppoConstants.CUSTOMER);
                            CardVaultModel vaultModel = new CardVaultModel();
                            vaultModel.setFirst_name(customer.getString(AppoConstants.FIRST_NAME));
                            vaultModel.setLast_name(customer.getString(AppoConstants.LAST_NAME));
                            vaultModel.setCc_number(customer.getString(AppoConstants.CC_NUMBER));
                            vaultModel.setCc_hash(customer.getString(AppoConstants.CC_HASH));
                            vaultModel.setCc_type(customer.getString(AppoConstants.CC_TYPE));
                            vaultModel.setCc_exp(customer.getString(AppoConstants.CC_EXP));
                            vaultModel.setCreated(customer.getString(AppoConstants.CREATED));
                            vaultModel.setUpdated(customer.getString(AppoConstants.UPDATED));
                            vaultModel.setCustomer_vault_id(customer.getString(AppoConstants.CUSTOMER_VAULT_ID));
                            mListVault.add(vaultModel);
                            readVoltCards();
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

        } else {
            //Log.e(TAG, "readVoltCards: size of counter " + counter);
            if (defaultPosition != -1) {
                CardVaultModel vaultModel = new CardVaultModel();
                vaultModel.setFirst_name(mListVault.get(defaultPosition).getFirst_name());
                vaultModel.setLast_name(mListVault.get(defaultPosition).getLast_name());
                vaultModel.setCc_number(mListVault.get(defaultPosition).getCc_number());
                vaultModel.setCc_hash(mListVault.get(defaultPosition).getCc_hash());
                vaultModel.setCc_type(mListVault.get(defaultPosition).getCc_type());
                vaultModel.setCc_exp(mListVault.get(defaultPosition).getCc_exp());
                vaultModel.setCreated(mListVault.get(defaultPosition).getCreated());
                vaultModel.setUpdated(mListVault.get(defaultPosition).getUpdated());
                vaultModel.setCustomer_vault_id(mListVault.get(defaultPosition).getCustomer_vault_id());
                mListVaultDefault.add(vaultModel);
                mListVault.remove(defaultPosition);
                cardViewDefault.setVisibility(View.VISIBLE);
                tvCardNumber.setText(mListVaultDefault.get(0).getCc_number());
                if (mListVaultDefault.get(0).getCc_type().equalsIgnoreCase(AppoConstants.VISA)) {
                    ivCardType.setImageResource(R.drawable.visa_my_card);
                } else if (mListVaultDefault.get(0).getCc_type().equalsIgnoreCase(AppoConstants.MASTERCARD)) {
                    ivCardType.setImageResource(R.drawable.mastercard_my_card);
                } else if (mListVaultDefault.get(0).getCc_type().equalsIgnoreCase(AppoConstants.AMERICA_EXPRESS)) {
                    ivCardType.setImageResource(R.drawable.amex_my_card);
                }
            }
            // need to work here
            if (mListVault.size() > 0 && mListVault != null) {
                setDataIntoAdapter();
            }


        }
    }

    private void setDataIntoAdapter() {
        MyCardAdapter adapter = new MyCardAdapter(MyCardsActivity.this, mListVault);
        rvMyOtherCards.setAdapter(adapter);
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

    @Override
    public void onRowItemClick(int pos) {
        Intent intentCardDetails = new Intent(MyCardsActivity.this, RemoveCardActivity.class);
        intentCardDetails.putExtra(AppoConstants.CC_NUMBER,mListVault.get(pos).getCc_number());
        intentCardDetails.putExtra(AppoConstants.CC_EXP,mListVault.get(pos).getCc_exp());
        intentCardDetails.putExtra(AppoConstants.FIRST_NAME,mListVault.get(pos).getFirst_name());
        intentCardDetails.putExtra(AppoConstants.LAST_NAME,mListVault.get(pos).getLast_name());
        intentCardDetails.putExtra(AppoConstants.CC_TYPE,mListVault.get(pos).getCc_type());
        startActivity(intentCardDetails);
    }
}

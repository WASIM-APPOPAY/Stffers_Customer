package com.stuffer.stuffers.activity.wallet;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.CardNumberEditText;
import com.stuffer.stuffers.views.MyButton;

public class RemoveCardActivity extends AppCompatActivity {

    private ImageView ivCardType;
    private CardNumberEditText card_number_field_text;
    private TextInputEditText cardholder_field_text;
    private TextInputEditText expiry_date_field_text;
    private TextInputEditText card_filed_cvv;
    private MyButton btnRemoveCard;
    private MyButton btnSetDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_card);
        setupActionBar();
        ivCardType = (ImageView) findViewById(R.id.ivCardType);
        card_number_field_text = (CardNumberEditText) findViewById(R.id.card_number_field_text);
        cardholder_field_text = (TextInputEditText) findViewById(R.id.cardholder_field_text);
        expiry_date_field_text = (TextInputEditText) findViewById(R.id.expiry_date_field_text);
        card_filed_cvv = (TextInputEditText) findViewById(R.id.card_filed_cvv);
        btnRemoveCard = (MyButton) findViewById(R.id.btnRemoveCard);
        btnSetDefault = (MyButton) findViewById(R.id.btnSetDefault);

        String cc_number = getIntent().getStringExtra(AppoConstants.CC_NUMBER);
        String cc_exp = getIntent().getStringExtra(AppoConstants.CC_EXP);
        String first_name = getIntent().getStringExtra(AppoConstants.FIRST_NAME);
        String last_name = getIntent().getStringExtra(AppoConstants.LAST_NAME);
        String cc_type = getIntent().getStringExtra(AppoConstants.CC_TYPE);

        setData(cc_number, cc_exp, first_name, last_name, cc_type);


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
        cardholder_field_text.setText(first_name+" "+last_name);
        expiry_date_field_text.setText(cc_exp);



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
}

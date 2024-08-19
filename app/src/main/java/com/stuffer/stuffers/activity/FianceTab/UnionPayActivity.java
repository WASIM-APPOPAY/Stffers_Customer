package com.stuffer.stuffers.activity.FianceTab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.quick_pass.QrGenerateFragment;
import com.stuffer.stuffers.activity.wallet.AccountActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity3;
import com.stuffer.stuffers.activity.wallet.PayNowActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.communicator.TitleListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UnionPayListener;
import com.stuffer.stuffers.fragments.finance_fragment.BankChildFragment;
import com.stuffer.stuffers.fragments.union_fragments.ActionFragment;
import com.stuffer.stuffers.fragments.union_fragments.CardCategoryFragment;
import com.stuffer.stuffers.fragments.union_fragments.CardEnrollMentFragment;
import com.stuffer.stuffers.fragments.union_fragments.UnMaskFragment;
import com.stuffer.stuffers.fragments.union_fragments.UnionOpenAccountFragment;
import com.stuffer.stuffers.fragments.union_fragments.VisaFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class UnionPayActivity extends AppCompatActivity implements UnionPayListener, TitleListener, TransactionPinListener, OnBankSubmit {
    private static final String TAG = "UnionPayActivity";
    private MyTextViewBold toolbarTitle;
    private JSONObject mIndex;
    private AlertDialog alertDialog;
    boolean flag1 = false, flag2 = false;
    int intExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay);

        intExtra = getIntent().getIntExtra(AppoConstants.CARDTYPE, 0);
        setupActionBar();
        CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppoConstants.PRDNUMBER, 1003);
        bundle.putInt(AppoConstants.CARDTYPE, intExtra);
        //String walletAccountNumber = Helper.getWalletAccountNumber();
        bundle.putString("newNumber", "2233445566778899");
        cardEnrollMentFragment.setArguments(bundle);
        toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
        initFragments(cardEnrollMentFragment, getString(R.string.info_wallet_card_enrollment));
        /*if (savedInstanceState == null) {
            if (intExtra == 1) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, 1003);
                bundle.putInt(AppoConstants.CARDTYPE, intExtra);
                String walletAccountNumber = Helper.getWalletAccountNumber();
                bundle.putString("newNumber", walletAccountNumber);
                cardEnrollMentFragment.setArguments(bundle);
                toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
                initFragments(cardEnrollMentFragment, getString(R.string.info_wallet_card_enrollment));
                flag1=true;
            } else if (intExtra == 2) {
                VisaFragment visaFragment = new VisaFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, 1003);
                bundle.putInt(AppoConstants.CARDTYPE, intExtra);
                String walletAccountNumber = Helper.getWalletAccountNumber();

                bundle.putString("newNumber", walletAccountNumber);
                visaFragment.setArguments(bundle);
                toolbarTitle.setText("VISA Card Enrollment");
                initFragments(visaFragment, "Visa Card Enrollment");
                flag2=true;
            }

          *//*ActionFragment mActionFragment = new ActionFragment();
          initFragments(mActionFragment, "action");*//*
         *//*CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
          initFragments(cardEnrollMentFragment, "enrollment");*//*
        }*/


    }


    private void setupActionBar() {
        toolbarTitle = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        toolbarTitle.setText(getString(R.string.info_union_pay));

        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initFragments(Fragment fragment, String param) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.unionContainer, fragment, param);
        transaction.addToBackStack(param);
        /*transaction.replace(R.id.mainContainer, fragment, tag);
         transaction.addToBackStack(tag);*/
        //transaction.commitAllowingStateLoss();
        transaction.commit();
    }

    @Override
    public void onEnrollmentClick() {
        String mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(mUserDetails)) {
            CardCategoryFragment cardCategoryFragment = new CardCategoryFragment();
            toolbarTitle.setText(getString(R.string.info_card_enrollment));
            initFragments(cardCategoryFragment, "enrollment");
        } else {
            showErrorDialog();
        }

    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_error_dialog, null, false);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvInfo.setText(getString(R.string.profile_update_error));
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRedirect();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void loginRedirect() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        //for login here
        Intent intent = new Intent(UnionPayActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void closeDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onOpenNewAccountRequest() {
        //Log.e(TAG, "onOpenNewAccountRequest: called");
        UnionOpenAccountFragment unionOpenAccountFragment = new UnionOpenAccountFragment();
        toolbarTitle.setText(getString(R.string.info_verify_kyc));
        initFragments(unionOpenAccountFragment, "open_ac");


    }

    @Override
    public void onUnMaskRequest() {
        UnMaskFragment mUnMaskFragment = new UnMaskFragment();
        toolbarTitle.setText(getString(R.string.info_card_unmask));
        initFragments(mUnMaskFragment, "unmask");


    }

    @Override
    public void onDifferentCardRequest(int cardTypePrdNumber, String walletNumber) {
        String mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(mUserDetails)) {
            if (cardTypePrdNumber == AppoConstants.EXPRESS_CODE) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, cardTypePrdNumber);
                bundle.putString("newNumber", walletNumber);
                toolbarTitle.setText(getString(R.string.info_money_express_card_enrollment));
                cardEnrollMentFragment.setArguments(bundle);
                initFragments(cardEnrollMentFragment, "enrollment");
            } else if (cardTypePrdNumber == AppoConstants.GIFT_CODE) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, cardTypePrdNumber);
                bundle.putString("newNumber", walletNumber);
                cardEnrollMentFragment.setArguments(bundle);
                toolbarTitle.setText(getString(R.string.info_gift_card_enrollment));
                initFragments(cardEnrollMentFragment, "enrollment");
            } else if (cardTypePrdNumber == AppoConstants.WALLET_CODE) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, cardTypePrdNumber);
                String walletAccountNumber = Helper.getWalletAccountNumber();
                bundle.putString("newNumber", walletAccountNumber);
                cardEnrollMentFragment.setArguments(bundle);
                toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
                initFragments(cardEnrollMentFragment, "enrollment");
            } else if (cardTypePrdNumber == AppoConstants.LOAN_CODE) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, cardTypePrdNumber);
                bundle.putString("newNumber", walletNumber);
                cardEnrollMentFragment.setArguments(bundle);
                toolbarTitle.setText(getString(R.string.info_loan_card_enrollment));
                initFragments(cardEnrollMentFragment, "enrollment");
            } else if (cardTypePrdNumber == AppoConstants.CREDIT_CODE) {
                CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(AppoConstants.PRDNUMBER, cardTypePrdNumber);
                bundle.putString("newNumber", walletNumber);
                cardEnrollMentFragment.setArguments(bundle);
                toolbarTitle.setText(getString(R.string.info_credit_card_enrollment));
                initFragments(cardEnrollMentFragment, "enrollment");
            }
        } else {
            showErrorDialog();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                verifyStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
        verifyStack();
    }

    private void verifyStack() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.unionContainer);
            if (currentFragment instanceof CardEnrollMentFragment) {
                //Log.d("message", "home fragment");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText(getString(R.string.info_card_enrollment));
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTitleUpdate(String mTitle) {
        toolbarTitle.setText(mTitle);
    }

    @Override
    public void onPinConfirm(String pin) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.unionContainer);
        if (fragment instanceof UnMaskFragment) {
            ((UnMaskFragment) fragment).closeBottomDialog(pin);
        }
    }

    @Override
    public void onConfirm(int param) {
        //later enable
      /*Intent intentBank = new Intent();
        setResult(RESULT_OK, intentBank);
        finish();*/

        DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveDemoValue("yes");
        //Intent intent = new Intent(UnionPayActivity.this, HomeActivity3.class);
        //Intent intent = new Intent(UnionPayActivity.this, AccountActivity.class);
        Intent intent = new Intent(UnionPayActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();

        /*if (param == 2) {
            VisaFragment visaFragment = new VisaFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AppoConstants.PRDNUMBER, 1003);
            bundle.putInt(AppoConstants.CARDTYPE, intExtra);
            *//*String walletAccountNumber = Helper.getWalletAccountNumber();
            bundle.putString("newNumber", walletAccountNumber);*//*
            bundle.putString("newNumber", "2233445566778899");
            visaFragment.setArguments(bundle);
            toolbarTitle.setText("VISA Card Enrollment");
            initFragments(visaFragment, "Visa Card Enrollment");
        } else if (param == 1) {
            CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AppoConstants.PRDNUMBER, 1003);
            bundle.putInt(AppoConstants.CARDTYPE, intExtra);
            *//*String walletAccountNumber = Helper.getWalletAccountNumber();
            bundle.putString("newNumber", walletAccountNumber);*//*
            bundle.putString("newNumber", "2233445566778899");
            cardEnrollMentFragment.setArguments(bundle);
            toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
            initFragments(cardEnrollMentFragment, getString(R.string.info_wallet_card_enrollment));
        } else {
            DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveDemoValue("yes");
            Intent intent = new Intent(UnionPayActivity.this, HomeActivity3.class);
            startActivity(intent);
            finish();
        }*/


    }
}
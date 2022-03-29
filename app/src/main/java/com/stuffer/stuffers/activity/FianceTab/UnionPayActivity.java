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
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class UnionPayActivity extends AppCompatActivity implements UnionPayListener, TitleListener, TransactionPinListener, OnBankSubmit {
    private static final String TAG = "UnionPayActivity";
    private TextView toolbarTitle;
    private JSONObject mIndex;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay);
        setupActionBar();
        if (savedInstanceState == null) {
            CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AppoConstants.PRDNUMBER, 1003);
            String walletAccountNumber = Helper.getWalletAccountNumber();
            bundle.putString("newNumber", walletAccountNumber);
            cardEnrollMentFragment.setArguments(bundle);
            toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
            initFragments(cardEnrollMentFragment, "enrollment");
            /*ActionFragment mActionFragment = new ActionFragment();
            initFragments(mActionFragment, "action");*/
          /*CardEnrollMentFragment cardEnrollMentFragment = new CardEnrollMentFragment();
          initFragments(cardEnrollMentFragment, "enrollment");*/
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.info_union_pay));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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
        Log.e(TAG, "onOpenNewAccountRequest: called");
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
    public void onConfirm() {
        Intent intentBank = new Intent();
        setResult(RESULT_OK,intentBank);
        finish();
    }
}
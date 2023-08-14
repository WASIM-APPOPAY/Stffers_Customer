package com.stuffer.stuffers.fragments.landing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.E_ShopActivity;
import com.stuffer.stuffers.activity.restaurant.E_StoreDiscountActivity;
import com.stuffer.stuffers.activity.restaurant.RestaurantListActivity;
import com.stuffer.stuffers.activity.restaurant.RestaurantWebActivity;
import com.stuffer.stuffers.activity.restaurant.RewardActivity;
import com.stuffer.stuffers.activity.wallet.CardDetails;
import com.stuffer.stuffers.activity.wallet.HomeActivity2;
import com.stuffer.stuffers.activity.wallet.InnerPayActivity;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.activity.wallet.P2PTransferActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.TabsActivity;
import com.stuffer.stuffers.my_camera.MyCameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

public class LandingFragment extends Fragment implements View.OnClickListener {


    private LinearLayout llResturant, llTransfer, llScan, llLinkCard, llRecharge, llDiscount, llTrain;
    private ImageView ivReward;
    private LinearLayout llShop;

    public LandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        llTransfer = view.findViewById(R.id.llTransfer);
        llTrain = view.findViewById(R.id.llTrain);
        llShop = view.findViewById(R.id.llShop);
        llDiscount = view.findViewById(R.id.llDiscount);
        llResturant = view.findViewById(R.id.llResturant);
        llScan = view.findViewById(R.id.llScan);
        llLinkCard = view.findViewById(R.id.llLinkCard);
        llRecharge = view.findViewById(R.id.llRecharge);
        ivReward = view.findViewById(R.id.ivReward);


        llTransfer.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llLinkCard.setOnClickListener(this);
        llRecharge.setOnClickListener(this);
        llResturant.setOnClickListener(this);
        ivReward.setOnClickListener(this);
        llShop.setOnClickListener(this);
        llDiscount.setOnClickListener(this);
        llTrain.setOnClickListener(this);

        return view;
    }


    private void goToLoginScreen(int where) {

        if (where == 10) {
            Intent mIntentQrCode = new Intent(getActivity(), SignInActivity.class);
            startActivityForResult(mIntentQrCode, 200);
        } else {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.putExtra(AppoConstants.WHERE, where);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTransfer:
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData)) {
                    goToLoginScreen(3);
                } else {
                    Intent mIntent = new Intent(getActivity(), P2PTransferActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 3);
                    startActivityForResult(mIntent, 100);
                }

                break;
            case R.id.llLinkCard:
                Intent intent = new Intent(getActivity(), CardDetails.class);
                startActivity(intent);
                break;
            case R.id.llScan:
                Intent intentscan = new Intent(getActivity(), InnerPayActivity.class);
                startActivity(intentscan);
                break;
            case R.id.llRecharge:
                String userData1 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData1)) {
                    goToLoginScreen(2);
                } else {
                    Intent mIntent = new Intent(getActivity(), MobileRechargeActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 2);
                    startActivity(mIntent);
                }
                break;
            case R.id.llResturant:
                //Intent intentResturant = new Intent(getActivity(), TabsActivity.class);
                //Intent intentResturant = new Intent(getActivity(), RestaurantListActivity.class);
                Intent intentResturant = new Intent(getActivity(), RestaurantWebActivity.class);
                startActivity(intentResturant);
                break;
            case R.id.ivReward:

                Intent intentReward = new Intent(getActivity(), RewardActivity.class);
                startActivity(intentReward);
                break;

            case R.id.llShop:

                String userData2 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData2)) {
                    goToLoginScreen(12);
                } else {
                    Intent mIntent = new Intent(getActivity(), E_ShopActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 12);
                    startActivity(mIntent);
                }

                break;
            case R.id.llDiscount:
                String userData3 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData3)) {
                    goToLoginScreen(13);
                } else {
                    Intent mIntent = new Intent(getActivity(), E_StoreDiscountActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 13);
                    startActivity(mIntent);
                }

                break;

            case R.id.llTrain:
                /*Intent train=new Intent(getActivity(), MyCameraActivity.class);
                startActivity(train);*/

                break;
        }
    }
}
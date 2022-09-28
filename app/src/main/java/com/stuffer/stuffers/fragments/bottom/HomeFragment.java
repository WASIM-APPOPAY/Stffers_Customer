package com.stuffer.stuffers.fragments.bottom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.activity.lunex_card.LunexMoreActivity;
import com.stuffer.stuffers.activity.shop_mall.ShopAdapter;
import com.stuffer.stuffers.activity.wallet.AccountActivity;
import com.stuffer.stuffers.activity.wallet.FundCountry;
import com.stuffer.stuffers.activity.wallet.MerchantMapsActivity;
import com.stuffer.stuffers.activity.wallet.PayNowActivity;
import com.stuffer.stuffers.adapter.recyclerview.AllGiftProduct;
import com.stuffer.stuffers.communicator.OnTransactionPinSuccess;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomTransactionPin;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomUpdateProfile;
import com.stuffer.stuffers.fragments.homeitem.EntertainmentFragment;
import com.stuffer.stuffers.fragments.homeitem.FinanceFragment;
import com.stuffer.stuffers.fragments.homeitem.GovermentFragment;
import com.stuffer.stuffers.fragments.homeitem.LifeStyleFragment;
import com.stuffer.stuffers.fragments.homeitem.PaymentFragment;
import com.stuffer.stuffers.fragments.homeitem.TravelFragment;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.activity.wallet.AddMoneyToWallet;
import com.stuffer.stuffers.activity.wallet.BecomeMerchantActivity;
import com.stuffer.stuffers.activity.wallet.HotelSearchActivity;
import com.stuffer.stuffers.activity.wallet.MovieTicketsActivity;
import com.stuffer.stuffers.activity.wallet.RequestMoney;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.TravelActivity;
import com.stuffer.stuffers.adapter.mall.TopShoppingOfferAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.SideWalletListener;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.models.output.GetTopShoppingOffersModel;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.ChildAnimationExample;
import com.stuffer.stuffers.views.ExpandableHeightGridView;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.SliderLayout;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class HomeFragment extends Fragment implements ShopListener {

    SliderLayout slider_home, slider_home2;

    PagerIndicator custom_indicator_home, custom_indicator_home2;

    View mView;

    HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
    HashMap<String, String> file_maps2 = new HashMap<String, String>();

    LinearLayout llRecharge, llTrainTicket, llFlightTicket, llBus, llMovieTicket, llHotels;
    LinearLayout llPayments, llPayNow, llAddMoney, llTransferMoney, llTransactions, layoutLinkBank;

    ExpandableHeightGridView topShoppingOffersGridView;

    TopShoppingOfferAdapter topShoppingOfferAdapter;
    ProgressBar topOfferProgressBar;

    MainAPIInterface mainAPIInterface;
    MainAPIInterface mainAPIInterfaceNode;
    ImageView ivDebitCard;
    MyTextView tvWalletAmount;
    StartActivityListener mListener;
    SideWalletListener mSideWalletListener;

    ArrayList<GetTopShoppingOffersModel.Offer> offerArrayList;
    private static final String TAG = "HomeFragment";
    private ProgressDialog dialog;
    private String areacode;
    private String mobileNumber;
    RelativeLayout layoutMerchant, layoutOurMerchant;
    private LinearLayout llRequestMoney, llOwnGiftCard;
    private TabLayout menuTabLayout;
    private FrameLayout menuContainer;
    private RecyclerView rvShop;
    private RecyclerView rvGiftLunex;
    List<GiftProductList.Product> mListGift;
    private MyTextViewBold tvMore;
    private BottomUpdateProfile mBottomUpdateProfile;
    private BottomTransactionPin mBottomTransDialog;
    private OnTransactionPinSuccess mTransactionSuccess;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.home_fragment, container, false);
        ivDebitCard = mView.findViewById(R.id.ivDebitCard);
        mainAPIInterface = ApiUtils.getAPIService();
        //mainAPIInterfaceNode = ApiUtils.getAPIService();
        mainAPIInterfaceNode = ApiUtils.getAPIServiceNode();
        tvWalletAmount = mView.findViewById(R.id.tvWalletAmount);
        slider_home = mView.findViewById(R.id.slider_home);
        custom_indicator_home = mView.findViewById(R.id.custom_indicator_home);

        slider_home2 = mView.findViewById(R.id.slider_home2);
        custom_indicator_home2 = mView.findViewById(R.id.custom_indicator_home2);
        rvShop = mView.findViewById(R.id.rvShop);
        rvShop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        llRecharge = mView.findViewById(R.id.llRecharge);
        llPayments = mView.findViewById(R.id.llPayments);
        llPayNow = mView.findViewById(R.id.llPayNow);
        llAddMoney = mView.findViewById(R.id.llAddMoney);
        llTransactions = mView.findViewById(R.id.llTransactions);


        llTrainTicket = mView.findViewById(R.id.llTrainTicket);
        llFlightTicket = mView.findViewById(R.id.llFlightTicket);
        llBus = mView.findViewById(R.id.llBus);
        llMovieTicket = mView.findViewById(R.id.llMovieTicket);
        llHotels = mView.findViewById(R.id.llHotels);
        llTransferMoney = mView.findViewById(R.id.llTransferMoney);
        layoutMerchant = mView.findViewById(R.id.layoutMerchant);
        layoutOurMerchant = mView.findViewById(R.id.layoutOurMerchant);
        llRequestMoney = mView.findViewById(R.id.llRequestMoney);
        tvMore = mView.findViewById(R.id.tvMore);
        rvGiftLunex = mView.findViewById(R.id.rvGiftLunex);
        rvGiftLunex.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        rvGiftLunex.setNestedScrollingEnabled(false);

        topShoppingOffersGridView = (ExpandableHeightGridView) mView.findViewById(R.id.topShoppingOffersGridView);
        topOfferProgressBar = (ProgressBar) mView.findViewById(R.id.topOfferProgressBar);

        //for menu
        menuTabLayout = (TabLayout) mView.findViewById(R.id.menuTabLayout);
        menuTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        menuContainer = (FrameLayout) mView.findViewById(R.id.menuContainer);

        //for gift cards
        // llOwnGiftCard = (LinearLayout) mView.findViewById(R.id.llOwnGiftCard);

        checkIsLoginEnable();
        //layoutByCard

        /*file_maps.put("Sale banner 1", R.drawable.small_banner1);
        file_maps.put("Sale banner 2", R.drawable.small_banner1);
        file_maps.put("Sale banner 3", R.drawable.small_banner1);*/
        file_maps.put("Sale banner 1", R.drawable.gift_card111);
        file_maps.put("Sale banner 2", R.drawable.shopping_card120);
        file_maps.put("Sale banner 3", R.drawable.merchant_code130);
        file_maps.put("Sale banner 4", R.drawable.wallet_card140);
        file_maps.put("Sale banner 5", R.drawable.credit_card150);

        file_maps2.put("Sale banner 1", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay1.jpg");
        file_maps2.put("Sale banner 2", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay2.jpg");
        file_maps2.put("Sale banner 3", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay3.jpg");
        file_maps2.put("Sale banner 4", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay4.jpg");
        file_maps2.put("Sale banner 5", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay5.jpg");
        file_maps2.put("Sale banner 6", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay6.jpg");
        file_maps2.put("Sale banner 7", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay7.jpg");
        file_maps2.put("Sale banner 8", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay8.jpg");
        file_maps2.put("Sale banner 9", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay9.jpg");
        file_maps2.put("Sale banner 10", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay10.jpg");
        file_maps2.put("Sale banner 11", "https://appopayimages.s3.us-east-2.amazonaws.com/static/appopay11.jpg");


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {


                        }
                    });

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            slider_home.addSlider(textSliderView);
        }

        slider_home.setPresetTransformer(SliderLayout.Transformer.Default);
        slider_home.setCustomIndicator(custom_indicator_home);
        slider_home.setPadding(0, 0, 0, 5);
        slider_home.setCustomAnimation(new ChildAnimationExample());

        //for slider 2
       // 1.19
         //       2.19
           //             3.12
             //                   4.11
               //                         5.19
                 //                               6.18
                   //                                     7.46



        for (String name : file_maps2.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps2.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {


                        }
                    });

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            slider_home2.addSlider(textSliderView);
        }

        slider_home2.setPresetTransformer(SliderLayout.Transformer.Default);
        slider_home2.setCustomIndicator(custom_indicator_home2);
        slider_home2.setPadding(0, 0, 0, 5);
        slider_home2.setCustomAnimation(new ChildAnimationExample());

        addClickListener();

        Bundle arguments = this.getArguments();
        if (arguments.getString(AppoConstants.INFO).equalsIgnoreCase("1")) {
            //Log.e(TAG, "onCreateView: called for update");
            //updateWalletBalance();
        } else {
            //getMoneyFromWallet();
        }

        updateWalletBalance();

        getAllGiftLst();
        //getList();

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LunexMoreActivity.class);
                startActivity(intent);

            }
        });
        tvMore.setVisibility(View.GONE);
        //showUpdateProfileDialog();
        showTransaPinDialog();

        return mView;
    }

    private void showTransaPinDialog() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        //Log.e(TAG, "showTransaPinDialog: "+vaultValue );
        if (StringUtils.isEmpty(vaultValue)) {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
            errorDialogFragment.setArguments(bundle);
            errorDialogFragment.setCancelable(false);
            errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
        } else {
            try {
                JSONObject root = new JSONObject(vaultValue);
                JSONObject result = root.getJSONObject(AppoConstants.RESULT);

                if (result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                    //Log.e(TAG, "showTransaPinDialog: need to call");
                    mBottomTransDialog = new BottomTransactionPin();
                    mBottomTransDialog.show(getChildFragmentManager(), mBottomTransDialog.getTag());
                    mBottomTransDialog.setCancelable(false);
                } else {
                    /*mBottomTransDialog = new BottomTransactionPin();
                    mBottomTransDialog.show(getChildFragmentManager(), mBottomTransDialog.getTag());
                    mBottomTransDialog.setCancelable(false);*/
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void showProfileDialog() {
        mBottomUpdateProfile = new BottomUpdateProfile();
        mBottomUpdateProfile.show(getChildFragmentManager(), mBottomUpdateProfile.getTag());
        mBottomUpdateProfile.setCancelable(false);
    }


    private void checkIsLoginEnable() {
        if (!Helper.isLoginEnable(getContext())) {
            addMenuIntoContainer2();
        } else {
            addMenuIntoContainer();
        }
    }

    private void addMenuIntoContainer2() {
        TabLayout.Tab fifthTab = menuTabLayout.newTab();
        fifthTab.setCustomView(getTabView(getString(R.string.info_finance), R.drawable.ic_finance_tab_icon));
        menuTabLayout.addTab(fifthTab);
        /*TabLayout.Tab firstTab = menuTabLayout.newTab();
        firstTab.setCustomView(getTabView(getString(R.string.info_lifestyle), R.drawable.ic_lifestyle_tab_icon));
        menuTabLayout.addTab(firstTab);
        TabLayout.Tab forthTab = menuTabLayout.newTab();
        forthTab.setCustomView(getTabView(getString(R.string.info_payment), R.drawable.ic_payment_tab_icon));
        menuTabLayout.addTab(forthTab);*/

        TabLayout.Tab tab = menuTabLayout.getTabAt(0);
        if (tab != null) {
            ////Log.e(TAG, "addMenuIntoContainer: called");
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.menuContainer, new FinanceFragment());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            tab.select();

        }

        /*menuTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FinanceFragment();
                        break;
                    case 1:
                        fragment = new LifeStyleFragment();
                        break;
                    case 2:
                        fragment = new PaymentFragment();
                        break;
                    case 3:
                        fragment = new GovermentFragment();
                        break;
                }
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.menuContainer, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

    private void addClickListener() {
        ArrayList<ShopModel> mList = Helper.getShopItems(getActivity());


        llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                } else {
                    try {
                        JSONObject root = new JSONObject(vaultValue);
                        JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                        JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);

                        /*if(customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {//|| zipCode.equalsIgnoreCase("null")) {
                            ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                            fragment.setArguments(bundle);
                            fragment.setCancelable(false);
                            fragment.show(getChildFragmentManager(), fragment.getTag());
                        } else {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }*/
                        if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });


        llPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "onClick: pay,ment click");
                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                } else {
                    try {
                        JSONObject root = new JSONObject(vaultValue);
                        JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                        JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                        /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {//|| zipCode.equalsIgnoreCase("null")) {
                            ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                            fragment.setArguments(bundle);
                            fragment.setCancelable(false);
                            fragment.show(getChildFragmentManager(), fragment.getTag());
                        } else {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }*/
                        if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                            mListener.OnStartActivityRequest(AppoConstants.RECHARGE_REQUEST_CODE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

        llTrainTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "train");
                startActivity(intent);

            }
        });

        llFlightTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "flight");
                startActivity(intent);

            }
        });


        llBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), TravelActivity.class);
                intent.putExtra("travel", "bus");
                startActivity(intent);

            }
        });


        llMovieTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MovieTicketsActivity.class);
                startActivity(intent);

            }
        });

        llHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), HotelSearchActivity.class);
                startActivity(intent);

            }
        });

        llPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Helper.isLoginEnable(getActivity())) {
                    Intent intent = new Intent(getActivity(), PayNowActivity.class);
                    startActivity(intent);
                } else {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                }


            }
        });

        llAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                } else {

                    Intent intent = new Intent(getActivity(), AddMoneyToWallet.class);
                    startActivity(intent);

                }
                /*Intent intent = new Intent(getActivity(), FundCountry.class);
                startActivity(intent);*/


            }
        });

        ShopAdapter adapter = new ShopAdapter(getActivity(), mList, this);
        rvShop.setAdapter(adapter);


        llTransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                } else {
                    try {
                        JSONObject root = new JSONObject(vaultValue);
                        JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                        JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                        /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {//|| zipCode.equalsIgnoreCase("null")) {
                            ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                            fragment.setArguments(bundle);
                            fragment.setCancelable(false);
                            fragment.show(getChildFragmentManager(), fragment.getTag());
                        } else {
                            mListener.OnStartActivityRequest(AppoConstants.WALLET_REQUEST_CODE);
                        }*/

                        if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                            mListener.OnStartActivityRequest(AppoConstants.WALLET_REQUEST_CODE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        llTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                    errorDialogFragment.setArguments(bundle);
                    errorDialogFragment.setCancelable(false);
                    errorDialogFragment.show(getChildFragmentManager(), errorDialogFragment.getTag());
                } else {
                    Intent intentAccount = new Intent(getContext(), AccountActivity.class);
                    startActivity(intentAccount);
                }
            }
        });

        layoutMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMerchant = new Intent(getActivity(), BecomeMerchantActivity.class);
                startActivity(intentMerchant);
            }
        });

        layoutOurMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOurMerchant = new Intent(getActivity(), MerchantMapsActivity.class);
                startActivity(intentOurMerchant);
            }
        });

        llRequestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRequest = new Intent(getActivity(), RequestMoney.class);
                startActivity(intentRequest);
            }
        });

        //for gift cards
        /*llOwnGiftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OwnGiftCardActivity.class);
                startActivity(intent);
            }
        });*/


    }

    private void getMoneyFromWallet() {
        String keyUserDetails = DataVaultManager.getInstance(getActivity()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        ////Log.e(TAG, "getMoneyFromWallet: " + keyUserDetails);
        if (StringUtils.isEmpty(keyUserDetails)) {
            tvWalletAmount.setText("$0");
        } else {
            try {
                JSONObject index = new JSONObject(keyUserDetails);
                JSONObject result = index.getJSONObject(AppoConstants.RESULT);
                JSONObject customerdetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                JSONArray customeraccount = customerdetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
                if (customeraccount.length() > 0) {
                    JSONObject obj = customeraccount.getJSONObject(0);
                    String balance = obj.getString(AppoConstants.CURRENTBALANCE);
                    try {
                        DecimalFormat df2 = new DecimalFormat("#.00");
                        Double doubleV = Double.parseDouble(balance);
                        String format = df2.format(doubleV);
                        tvWalletAmount.setText("$" + format);
                        mSideWalletListener.onSideBalanceRequestUpdate(balance);
                    } catch (Exception e) {
                        tvWalletAmount.setText("$" + balance);
                        mSideWalletListener.onSideBalanceRequestUpdate(balance);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateWalletBalance() {
        ////Log.e(TAG, "updateWalletBalance: home fragment called");
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);

        if (StringUtils.isEmpty(vaultValue)) {
            ////Log.e(TAG, "updateWalletBalance: empty called");
            return;
        }


        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_please_wait_dots));
        dialog.show();

        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            areacode = result.getString(AppoConstants.PHONECODE);
            mobileNumber = result.getString(AppoConstants.MOBILENUMBER);
        } catch (JSONException e) {
            Log.e(TAG, "updateWalletBalance: error called in update");
            e.printStackTrace();
        }

        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getProfileDetails(Long.parseLong(mobileNumber), Integer.parseInt(areacode), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Log.e(TAG, "onResponse: " );
                dialog.dismiss();
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    String res = body.toString();
                    Helper.setUserDetailsNull();
                    DataVaultManager.getInstance(getContext()).saveUserDetails(res);

                    getMoneyFromWallet();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (StartActivityListener) context;
            mSideWalletListener = (SideWalletListener) context;
            mTransactionSuccess = (OnTransactionPinSuccess) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement StartActivityListener");
        }

    }

    public void addMenuIntoContainer() {
        // Create a new Tab named "First"
        TabLayout.Tab firstTab = menuTabLayout.newTab();
        firstTab.setCustomView(getTabView(getString(R.string.info_lifestyle), R.drawable.ic_lifestyle_tab_icon));
        // first tab
        menuTabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        // Create a new Tab named "Second"
        TabLayout.Tab secondTab = menuTabLayout.newTab();
        secondTab.setCustomView(getTabView(getString(R.string.info_entertainment), R.drawable.ic_entertainment_tab_icon));
        menuTabLayout.addTab(secondTab);
        // Create a new Tab named "Third"
        TabLayout.Tab thirdTab = menuTabLayout.newTab();
        thirdTab.setCustomView(getTabView(getString(R.string.info_travel), R.drawable.ic_travel_tab_icon));
        menuTabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

        // Create a new Tab named "Third"
        TabLayout.Tab forthTab = menuTabLayout.newTab();
        //forthTab.setCustomView(getTabView("Payment", R.drawable.ic_payment_tab_icon));
        forthTab.setCustomView(getTabView(getString(R.string.info_payment), R.drawable.ic_payment_tab_icon));
        menuTabLayout.addTab(forthTab); // add  the tab at in the TabLayout

        // Create a new Tab named "Third"
        TabLayout.Tab fifthTab = menuTabLayout.newTab();
        fifthTab.setCustomView(getTabView(getString(R.string.info_finance), R.drawable.ic_finance_tab_icon));
        menuTabLayout.addTab(fifthTab); // add  the tab at in the TabLayout

        TabLayout.Tab sixthTab = menuTabLayout.newTab();
        sixthTab.setCustomView(getTabView(getString(R.string.info_government), R.drawable.ic_government));
        menuTabLayout.addTab(sixthTab); // add  the tab at in the TabLayout


        TabLayout.Tab tab = menuTabLayout.getTabAt(0);
        if (tab != null) {
            ////Log.e(TAG, "addMenuIntoContainer: called");
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.menuContainer, new LifeStyleFragment());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            tab.select();

        }

        menuTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new LifeStyleFragment();
                        break;
                    case 1:
                        fragment = new EntertainmentFragment();
                        break;
                    case 2:
                        fragment = new TravelFragment();
                        break;
                    case 3:
                        fragment = new PaymentFragment();
                        break;
                    case 4:
                        fragment = new FinanceFragment();

                        break;
                    case 5:
                        fragment = new GovermentFragment();
                        break;
                }
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.menuContainer, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public View getTabView(String textParam, int idParam) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab_custome_view, null);
        MyTextView tv = (MyTextView) v.findViewById(R.id.tvTabTitle);
        tv.setText(textParam);
        ImageView img = (ImageView) v.findViewById(R.id.ivTabIcon);
        img.setImageResource(idParam);
        //img.setColorFilter(getResources().getColor(R.color.icon_color), PorterDuff.Mode.SRC_IN );
        return v;
    }


    @Override
    public void onShopItemClick(int pos, String title) {
        //Log.e(TAG, "onShopItemClick: Pos :: " + pos + "  Title :: " + title);
        /*if (AppoPayApplication.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), SopActivity.class);
            intent.putExtra(AppoConstants.TITLE, title);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
        }*/
    }

    public void getAllGiftLst() {
        //Log.e(TAG, "getAllGiftLst: called");
        JsonObject param = new JsonObject();
        param.addProperty("countryCode", "");
        mainAPIInterfaceNode.getAllGiftCardList(param).enqueue(new Callback<GiftProductList>() {
            @Override
            public void onResponse(@NotNull Call<GiftProductList> call, @NotNull Response<GiftProductList> response) {
                //Log.e(TAG, "onResponse: called");
                if (response.isSuccessful()) {
                    try {
                        mListGift = response.body().getProducts();
                        //mListGift.add(0, Helper.getAppopayGift());
                        String listCard = new Gson().toJson(mListGift);
                        DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveGiftCardList(listCard);
                        getList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e(TAG, "onResponse: error called");
                    }
                }
            }

            @Override
            public void onFailure(Call<GiftProductList> call, Throwable t) {
                //Log.e(TAG, "onFailure: called" + t.getMessage());

            }
        });
    }

    private void getList() {
        List<GiftProductList.Product> mListTemp = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < mListGift.size(); i++) {
            if (mListGift.get(i).getProductType().equalsIgnoreCase("GIFT_CARD")) {
                mListTemp.add(count, mListGift.get(i));
                count = count + 1;
            }
        }
        mListTemp.add(0, Helper.getAppopayGift());
        int[] giftCardLogos = Helper.getGiftTypeLogo();
        Log.e(TAG, "getList: " + giftCardLogos.length);
        AllGiftProduct allGiftProduct = new AllGiftProduct(giftCardLogos, getActivity(), getActivity(), true);
        rvGiftLunex.setAdapter(allGiftProduct);
    }


    //for activity result
    public void updateUserProfile(String mTransPin) {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);

            JsonObject sentIndex = new JsonObject();
            sentIndex.addProperty(AppoConstants.ID, jsonResult.getString(AppoConstants.ID));
            sentIndex.addProperty(AppoConstants.FIRSTNAME, jsonResult.getString(AppoConstants.FIRSTNAME));
            sentIndex.addProperty(AppoConstants.LASTNAME, jsonResult.getString(AppoConstants.LASTNAME));
            sentIndex.addProperty(AppoConstants.USERNAME, jsonResult.getString(AppoConstants.USERNAME));
            sentIndex.addProperty(AppoConstants.PASSWORD, jsonResult.getString(AppoConstants.PASSWORD));
            sentIndex.addProperty(AppoConstants.EMIAL, jsonResult.getString(AppoConstants.EMIAL));
            sentIndex.addProperty(AppoConstants.ACCOUNTEXPIRED, jsonResult.getString(AppoConstants.ACCOUNTEXPIRED));
            sentIndex.addProperty(AppoConstants.ACCOUNTLOCKED, jsonResult.getString(AppoConstants.ACCOUNTLOCKED));
            sentIndex.addProperty(AppoConstants.CREDENTIALSEXPIRED, jsonResult.getString(AppoConstants.CREDENTIALSEXPIRED));
            sentIndex.addProperty(AppoConstants.ENABLE, jsonResult.getString(AppoConstants.ENABLE));
            sentIndex.addProperty(AppoConstants.MOBILENUMBER, jsonResult.getString(AppoConstants.MOBILENUMBER));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, mTransPin);
            sentIndex.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, (String) null);

            JsonArray jsonArrayRole = new JsonArray();
            jsonArrayRole.add("USER");
            sentIndex.add(AppoConstants.ROLE, jsonArrayRole);

            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            JsonObject sentJsonCustomerDetails = new JsonObject();
            sentJsonCustomerDetails.addProperty(AppoConstants.ID, jsonCustomerDetails.getString(AppoConstants.ID));
            sentJsonCustomerDetails.addProperty(AppoConstants.FIRSTNAME, jsonCustomerDetails.getString(AppoConstants.FIRSTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.LASTNAME, jsonCustomerDetails.getString(AppoConstants.LASTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.MIDDLENAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CARDTOKEN, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, jsonCustomerDetails.getString(AppoConstants.COUNTRYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, jsonCustomerDetails.getString(AppoConstants.STATEID));
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, jsonCustomerDetails.getString(AppoConstants.ADDRESS));
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ZIPCODE2, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, jsonCustomerDetails.getString(AppoConstants.DOB));
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, Helper.getCurrencyId());
            sentJsonCustomerDetails.addProperty(AppoConstants.MONTHLYINCOME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.PASSPORTNUMBER, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.EXPIRYDATE, jsonCustomerDetails.getString(AppoConstants.EXPIRYDATE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDTYPE, jsonCustomerDetails.getString(AppoConstants.IDTYPE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDNUMBER, jsonCustomerDetails.getString(AppoConstants.IDNUMBER));

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            if (Helper.getCurrencyId().equalsIgnoreCase("1")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "USD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("2")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "INR");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("3")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "CAD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("4")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "ERU");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("5")){
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "DOP");
            }
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.SOURCEOFINCOME, (String) null);


            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
            //Log.e(TAG, "updateUserProfile: " + sentIndex);
            /*String s = new Gson().toJson(sentIndex.toString());
            JSONObject jsonObject = new JSONObject(s);
            Log.e(TAG, "updateUserProfile: " + jsonObject);*/

            processUpdateTransactionPin(sentIndex);


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void processUpdateTransactionPin(JsonObject sentIndex) {
        String accessToken = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_updaing_profile));
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

        mainAPIInterface.postUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Log.e(TAG, "onResponse: " + response);
                //Log.e(TAG, "onResponse: " + response.body());
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //  Log.e(TAG, "onResponse: yes called ");
                    //String res = new Gson().toJson(response);
                    Toast.makeText(getActivity(), "Your have created your transaction pin Successfully", Toast.LENGTH_SHORT).show();
                    if (mBottomTransDialog != null) {
                        mBottomTransDialog.dismiss();
                    }
                    //now you need to show new account screen
                    mTransactionSuccess.onPinCreated();
                    updateWalletBalance();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(getActivity(), "Service Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: profile update" + t.getMessage());
                dialog.dismiss();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void dismissBottomPin(String pin) {
        updateUserProfile(pin);
    }
}

/*
1000 will be giftCard
1001 shoping card
1002 merchnat card
1003 wallet card
1004 credit card

1. Only will be wallet card in the card enrollment option
 */
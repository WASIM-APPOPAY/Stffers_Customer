package com.stuffer.stuffers.fragments.landing;

import static com.stuffer.stuffers.fragments.landing.LifeFragment.mSelectedColor;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.E_ShopActivity;
import com.stuffer.stuffers.activity.restaurant.E_StoreDiscountActivity;
import com.stuffer.stuffers.activity.restaurant.R_AllFragment;
import com.stuffer.stuffers.activity.restaurant.R_EntertainmentFragment;
import com.stuffer.stuffers.activity.restaurant.R_FoodFragment;
import com.stuffer.stuffers.activity.restaurant.R_ShopFragment;
import com.stuffer.stuffers.activity.restaurant.RestaurantListActivity;
import com.stuffer.stuffers.activity.restaurant.RestaurantWebActivity;
import com.stuffer.stuffers.activity.restaurant.RewardActivity;
import com.stuffer.stuffers.activity.wallet.CardDetails;
import com.stuffer.stuffers.activity.wallet.InnerPayActivity;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.adapter.recyclerview.AllRestaurantAdapter;
import com.stuffer.stuffers.adapter.recyclerview.ListRestaurent;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.models.all_restaurant.Content;
import com.stuffer.stuffers.models.restaurant.MRestaurent;
import com.stuffer.stuffers.models.restaurant.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingFragment extends Fragment implements View.OnClickListener {


    private LinearLayout llResturant, llTransfer, llScan, llLinkCard, llRecharge, llDiscount, llTrain;
    private ImageView ivReward;
    private LinearLayout llShop;
    private StartActivityListener mListener;

    private MyTextViewBold allView, shopView, foodView, entertainmentView;
    private NestedScrollView nestedScrollView;
    RecyclerView restaurentView;
    private MainAPIInterface apiService;
    private static final String TAG = "LandingFragment";
    //WebView advanceWeb;
    List<MyTextViewBold> mTextBold;

    public LandingFragment() {
        // Required empty public constructor
    }



    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: called" );



    }*/

    private void getAllRestaurents() {
        apiService.getListRestaurant().enqueue(new Callback<MRestaurent>() {
            @Override
            public void onResponse(Call<MRestaurent> call, Response<MRestaurent> response) {
                //Log.e(TAG, "onResponse: called" );
                if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {

                    List<Result> content = response.body().getResult();
                    String s = new Gson().toJson(content);
                    //Log.e("TAG", "onResponse: " + s);
                    passToAdapter(content);


                }
            }

            @Override
            public void onFailure(Call<MRestaurent> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    private void passToAdapter(List<Result> content) {
        ListRestaurent mAllRestaurantAdapter = new ListRestaurent(content, getActivity());
        restaurentView.setAdapter(mAllRestaurantAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        apiService = ApiUtils.getAPIService();
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        llTransfer = view.findViewById(R.id.llTransfer);

        llTrain = view.findViewById(R.id.llTrain);
        llShop = view.findViewById(R.id.llShop);
        llDiscount = view.findViewById(R.id.llDiscount);
        llResturant = view.findViewById(R.id.llResturant);
        llScan = view.findViewById(R.id.llScan);
        llLinkCard = view.findViewById(R.id.llLinkCard);
        llRecharge = view.findViewById(R.id.llRecharge);
        ivReward = view.findViewById(R.id.ivReward);
        mTextBold = new ArrayList<>();


        restaurentView = (RecyclerView) view.findViewById(R.id.restaurentView);

        restaurentView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));


        getAllRestaurents();


        llTransfer.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llLinkCard.setOnClickListener(this);
        llRecharge.setOnClickListener(this);
        llResturant.setOnClickListener(this);
        ivReward.setOnClickListener(this);
        llShop.setOnClickListener(this);
        llDiscount.setOnClickListener(this);
        llTrain.setOnClickListener(this);

        allView = view.findViewById(R.id.allView);
        shopView = view.findViewById(R.id.shopView);
        foodView = view.findViewById(R.id.foodView);
        entertainmentView = view.findViewById(R.id.entertainmentView);


        mTextBold.add(allView);
        mTextBold.add(shopView);
        mTextBold.add(foodView);
        mTextBold.add(entertainmentView);
        //container = view.findViewById(R.id.container);
        R_AllFragment mRAllFragment = new R_AllFragment();
        initFragment(mRAllFragment);
        makeActiveInactive(0);

        allView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeActiveInactive(0);
                R_AllFragment mRAllFragment = new R_AllFragment();
                initFragment(mRAllFragment);
            }
        });
        shopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeActiveInactive(1);
                R_ShopFragment mFragment = new R_ShopFragment();
                initFragment(mFragment);

            }
        });

        foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeActiveInactive(2);
                R_FoodFragment mFragment = new R_FoodFragment();
                initFragment(mFragment);
            }
        });

        entertainmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeActiveInactive(3);
                R_EntertainmentFragment mFragment = new R_EntertainmentFragment();
                initFragment(mFragment);
            }
        });


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

    private void makeActiveInactive(int i) {

        for (int m = 0; m < mTextBold.size(); m++) {

            if (m == i) {
                mTextBold.get(m).setTextColor(Color.parseColor("#FFFFFF"));

                mTextBold.get(m).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.view_normal_blue2));
            } else {
                mTextBold.get(m).setTextColor(Color.parseColor("#FF9201"));
                mTextBold.get(m).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_rounded));

            }
        }
        if (i == 0) {

            allView.setTextSize(18f);
            allView.setTextColor(Color.parseColor(mSelectedColor));
            shopView.setTextColor(Color.parseColor("#000000"));
            foodView.setTextColor(Color.parseColor("#000000"));
            entertainmentView.setTextColor(Color.parseColor("#000000"));
            shopView.setTextSize(15f);
            foodView.setTextSize(15f);
            entertainmentView.setTextSize(15f);

            mTextBold.get(i).setTextColor(Color.parseColor(mSelectedColor));


        } else if (i == 1) {
            shopView.setTextSize(18f);
            shopView.setTextColor(Color.parseColor(mSelectedColor));
            allView.setTextColor(Color.parseColor("#000000"));
            foodView.setTextColor(Color.parseColor("#000000"));
            entertainmentView.setTextColor(Color.parseColor("#000000"));
            allView.setTextSize(15f);
            foodView.setTextSize(15f);
            entertainmentView.setTextSize(15f);
        } else if (i == 2) {
            foodView.setTextSize(18f);
            foodView.setTextColor(Color.parseColor(mSelectedColor));
            shopView.setTextColor(Color.parseColor("#000000"));
            allView.setTextColor(Color.parseColor("#000000"));
            entertainmentView.setTextColor(Color.parseColor("#000000"));
            shopView.setTextSize(15f);
            allView.setTextSize(15f);
            entertainmentView.setTextSize(15f);
        } else if (i == 3) {
            entertainmentView.setTextSize(18f);
            entertainmentView.setTextColor(Color.parseColor(mSelectedColor));
            shopView.setTextColor(Color.parseColor("#000000"));
            foodView.setTextColor(Color.parseColor("#000000"));
            allView.setTextColor(Color.parseColor("#000000"));

            shopView.setTextSize(15f);
            foodView.setTextSize(15f);
            allView.setTextSize(15f);
        }

    }

    private void initFragment(Fragment mFragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTransfer:
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData)) {
                    goToLoginScreen(3);
                } else {
                    mListener.OnStartActivityRequest(3);
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
                /*String userData2 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
                if (TextUtils.isEmpty(userData2)|| StringUtils.isEmpty(accesstoken)) {
                    goToLoginScreen(0);
                }else {
                    Intent intentResturant = new Intent(getActivity(), RestaurantWebActivity.class);
                    startActivity(intentResturant);
                }*/
                break;
            case R.id.ivReward:

                Intent intentReward = new Intent(getActivity(), RewardActivity.class);
                startActivity(intentReward);
                break;

            case R.id.llShop:

                String userData3 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData3)) {
                    goToLoginScreen(12);
                } else {
                    Intent mIntent = new Intent(getActivity(), E_ShopActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 12);
                    startActivity(mIntent);
                }

                break;
            case R.id.llDiscount:
                String userData4 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData4)) {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (StartActivityListener) context;

    }
}
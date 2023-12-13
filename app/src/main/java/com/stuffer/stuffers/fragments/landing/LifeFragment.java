package com.stuffer.stuffers.fragments.landing;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.R_AllFragment;
import com.stuffer.stuffers.activity.restaurant.R_EntertainmentFragment;
import com.stuffer.stuffers.activity.restaurant.R_FoodFragment;
import com.stuffer.stuffers.activity.restaurant.R_ShopFragment;
import com.stuffer.stuffers.views.MyTextViewBold;


public class LifeFragment extends Fragment {

    private FrameLayout container;
    //public static final String mSelectedColor = "#FF6B35";
    public static final String mSelectedColor = "#FFFFFF";
    private MyTextViewBold allView, shopView, foodView, entertainmentView;

    public LifeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_life, container, false);
        allView = mView.findViewById(R.id.allView);
        shopView = mView.findViewById(R.id.shopView);
        foodView = mView.findViewById(R.id.foodView);
        entertainmentView = mView.findViewById(R.id.entertainmentView);


        container = mView.findViewById(R.id.container);
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

        return mView;
    }

    private void makeActiveInactive(int i) {
        if (i == 0) {
            allView.setTextSize(18f);
            allView.setTextColor(Color.parseColor(mSelectedColor));
            shopView.setTextColor(Color.parseColor("#000000"));
            foodView.setTextColor(Color.parseColor("#000000"));
            entertainmentView.setTextColor(Color.parseColor("#000000"));
            shopView.setTextSize(15f);
            foodView.setTextSize(15f);
            entertainmentView.setTextSize(15f);


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

}
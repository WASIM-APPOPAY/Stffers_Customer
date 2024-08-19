package com.stuffer.stuffers.activity.restaurant;

import static com.stuffer.stuffers.fragments.landing.LifeFragment.mSelectedColor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.stuffer.stuffers.R;

import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;
import java.util.List;

public class StoreListActivity extends AppCompatActivity {
    private MyTextViewBold allView, shopView, foodView, entertainmentView;
    List<MyTextViewBold> mTextBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        allView = findViewById(R.id.allView);
        shopView = findViewById(R.id.shopView);
        foodView = findViewById(R.id.foodView);
        entertainmentView = findViewById(R.id.entertainmentView);

        setupActionBar();
        mTextBold=new ArrayList<>();
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


    }

    private void setupActionBar() {
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText("Stores");
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void makeActiveInactive(int i) {

        for (int m = 0; m < mTextBold.size(); m++) {

            if (m == i) {
                mTextBold.get(m).setTextColor(Color.parseColor("#FFFFFF"));

                mTextBold.get(m).setBackground(ContextCompat.getDrawable(StoreListActivity.this, R.drawable.view_normal_blue2));
            } else {
                mTextBold.get(m).setTextColor(Color.parseColor("#FF9201"));
                mTextBold.get(m).setBackground(ContextCompat.getDrawable(StoreListActivity.this, R.drawable.edit_text_rounded));

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
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
package com.stuffer.stuffers.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.all_restaurant.RestaurantItems;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;
import java.util.List;

public class ROrderActivity extends AppCompatActivity {
    private static final String TAG = "ROrderActivity";
    private LinearLayout linearTable;
    private MyTextView tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rorder);
        TextView tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getIntent().getStringExtra("name"));
        tvHeading.setAllCaps(true);
        findViewById(R.id.search_back).setOnClickListener(view -> ROrderActivity.this.finish());
        linearTable = findViewById(R.id.linearTable);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        String order = getIntent().getStringExtra("order");
        //List<RestaurantItems.Result> result = new Gson().fromJson(order, RestaurantItems.Result.class);
        List<RestaurantItems.Result> result = new Gson().fromJson(order, new TypeToken<ArrayList<RestaurantItems.Result>>() {
        }.getType());
        //Log.e(TAG, "onCreate: " + result.size());
        //Log.e(TAG, "onCreate: ID ::: " + result.get(0).getId());
        /*LinearLayout mLinearLayout = new LinearLayout(ROrderActivity.this);
        mLinearLayout.setWeightSum(8);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(lparams);*/
        int count = result.size();
        int i = 0;

        while (count > 0) {
            LinearLayout mLinearLayout = new LinearLayout(ROrderActivity.this);
            mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayout.setWeightSum(8);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            mLinearLayout.setLayoutParams(lparams);

            MyTextView mTextview = new MyTextView(ROrderActivity.this);
            LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams1.weight = 4;
            mTextview.setLayoutParams(lparams1);
            mTextview.setGravity(Gravity.CENTER);
            mTextview.setTextColor(Color.parseColor("#000000"));
            mTextview.setText(result.get(i).getName());

            MyTextView mTextview2 = new MyTextView(ROrderActivity.this);
            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams2.weight = 1;
            mTextview2.setLayoutParams(lparams2);
            mTextview2.setGravity(Gravity.CENTER);
            mTextview2.setTextColor(Color.parseColor("#000000"));
            mTextview2.setText("$" + result.get(i).getPrice());


            MyTextView mTextview3 = new MyTextView(ROrderActivity.this);
            LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams3.weight = 1.5F;
            mTextview3.setLayoutParams(lparams3);
            mTextview3.setGravity(Gravity.CENTER);
            mTextview3.setTextColor(Color.parseColor("#000000"));
            mTextview3.setText("" + result.get(i).getCount());

            MyTextView mTextview4 = new MyTextView(ROrderActivity.this);
            LinearLayout.LayoutParams lparams4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams4.weight = 1.5F;
            mTextview4.setLayoutParams(lparams4);
            double mFinalAmt = (result.get(i).getCount() * result.get(i).getPrice());
            mTextview4.setGravity(Gravity.CENTER);
            mTextview4.setTextColor(Color.parseColor("#000000"));
            mTextview4.setText("$" + mFinalAmt);

            mLinearLayout.addView(mTextview, 0);
            mLinearLayout.addView(mTextview2, 1);
            mLinearLayout.addView(mTextview3, 2);
            mLinearLayout.addView(mTextview4, 3);

            this.linearTable.addView(mLinearLayout);
            count = count - 1;
            i = i + 1;
        }


        double sum = 0;
        for (int j = 0; j < result.size(); j++) {
            double amt = result.get(j).getCount() * result.get(j).getPrice();
            sum = sum + amt;
        }
        MyTextView mTextview4 = new MyTextView(ROrderActivity.this);
        LinearLayout.LayoutParams lparams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTextview4.setLayoutParams(lparams4);
        lparams4.rightMargin = 10;
        lparams4.topMargin = 10;

        mTextview4.setGravity(Gravity.END);
        mTextview4.setTextSize(20F);
        mTextview4.setTextColor(Color.parseColor("#000000"));
        mTextview4.setText("Total Amount : $" + sum);

        linearTable.addView(mTextview4);

        MyButton myButton = new MyButton(ROrderActivity.this);
        LinearLayout.LayoutParams lparamsButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myButton.setLayoutParams(lparamsButton);
        lparamsButton.gravity = Gravity.CENTER;
        lparamsButton.setMargins(10, 10, 10, 10);
        myButton.setBackground(ContextCompat.getDrawable(ROrderActivity.this, R.drawable.blue_rounded_bg3));

        myButton.setGravity(Gravity.CENTER);

        myButton.setTextSize(20F);
        myButton.setTextColor(Color.parseColor("#000000"));
        myButton.setText("ORDER NOW");
        linearTable.addView(myButton);

    }
}
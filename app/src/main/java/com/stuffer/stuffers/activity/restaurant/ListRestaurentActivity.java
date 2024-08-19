package com.stuffer.stuffers.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.ListRestaurent;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.models.restaurant.MRestaurent;
import com.stuffer.stuffers.models.restaurant.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRestaurentActivity extends AppCompatActivity {
    private static final String TAG = "ListRestaurentActivity";
    private RecyclerView restaurentView;
    private MainAPIInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurent);
        apiService = ApiUtils.getAPIService();
        setupActionBar();
        restaurentView = (RecyclerView) findViewById(R.id.restaurentView);

        restaurentView.setLayoutManager(new GridLayoutManager(ListRestaurentActivity.this, 1, LinearLayoutManager.VERTICAL, false));


        getAllRestaurents();
    }

    private void getAllRestaurents() {
        Helper.showLoading("please wait...", this);
        apiService.getListRestaurant().enqueue(new Callback<MRestaurent>() {
            @Override
            public void onResponse(Call<MRestaurent> call, Response<MRestaurent> response) {
                //Log.e(TAG, "onResponse: called" );
                Helper.hideLoading();
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
                Helper.hideLoading();

            }
        });

    }

    private void passToAdapter(List<Result> content) {
        ListRestaurent mAllRestaurantAdapter = new ListRestaurent(content, ListRestaurentActivity.this);
        restaurentView.setAdapter(mAllRestaurantAdapter);
    }

    private void setupActionBar() {
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText("Restaurant");
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
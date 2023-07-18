package com.stuffer.stuffers.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.gson.Gson;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.AlItemsAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.RestaurantListener;
import com.stuffer.stuffers.models.all_restaurant.Content;
import com.stuffer.stuffers.models.all_restaurant.RestaurantItems;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantItemActivity extends AppCompatActivity implements RestaurantListener {
    private static final String TAG = "RestaurantItemActivity";
    private MainAPIInterface mainAPIInterface;
    private TextView tvTitle;
    private RecyclerView rvAllShop;
    private ProgressDialog mProgress;
    private List<RestaurantItems.Result> mTemp;
    private List<RestaurantItems.Result> mListCart;
    private AlItemsAdapter mAlItemsAdapter;
    private MyButton btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_item);
        String userid = getIntent().getStringExtra("userid");
        String name = getIntent().getStringExtra("name");
        mListCart = new ArrayList<>();

        mainAPIInterface = ApiUtils.getAPIService();
        tvTitle = findViewById(R.id.tabs_title);
        rvAllShop = findViewById(R.id.rvAllShop);
        btnCart = findViewById(R.id.btnCart);
        tvTitle.setText(name);
        findViewById(R.id.search_back).setOnClickListener(view -> RestaurantItemActivity.this.finish());
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAllShop.setLayoutManager(lm);
        rvAllShop.addItemDecoration(new MaterialDividerItemDecoration(this, MaterialDividerItemDecoration.VERTICAL));
        //fetchMerchantByShopType("Restaurant");
        fetchAllRestaurantItems(userid);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentOrder = new Intent(RestaurantItemActivity.this, ROrderActivity.class);
                String s = new Gson().toJson(mListCart);
                //Log.e(TAG, "onClick: " + s);
                intentOrder.putExtra("order", s);
                intentOrder.putExtra("name",name);
                startActivity(intentOrder);

            }
        });
    }

    private void fetchAllRestaurantItems(String userid) {
        Log.e(TAG, "fetchAllRestaurantItems: " + userid);
        showLoading();
        /*String save = "{\n" + "  \"result\": [\n" + "    {\n" + "      \"id\": 17,\n" + "      \"name\": \"oncomelania hupensis\",\n" + "      \"price\": 15,\n" + "      \"imageUrl\": \"https://s3.us-east-2.amazonaws.com/appopayimages/16833412292495881614821033615765.jpg\",\n" + "      \"description\": \"oncomelania hupensis\",\n" + "      \"shelvesId\": 10,\n" + "      \"shelvesName\": \"SEA FOOD\"\n" + "    },\n" + "    {\n" + "      \"id\": 18,\n" + "      \"name\": \"lobster\",\n" + "      \"price\": 15,\n" + "      \"imageUrl\": \"https://s3.us-east-2.amazonaws.com/appopayimages/16833412679838901200175050181616.jpg\",\n" + "      \"description\": \"lobster\",\n" + "      \"shelvesId\": 10,\n" + "      \"shelvesName\": \"SEA FOOD\"\n" + "    },\n" + "    {\n" + "      \"id\": 19,\n" + "      \"name\": \"clam\",\n" + "      \"price\": 10,\n" + "      \"imageUrl\": \"https://s3.us-east-2.amazonaws.com/appopayimages/16833413078933780289281583367262.jpg\",\n" + "      \"description\": \"clam\",\n" + "      \"shelvesId\": 10,\n" + "      \"shelvesName\": \"SEA FOOD\"\n" + "    }\n" + "  ]\n" + "}";
        List<RestaurantItems.Result> mListContent = new ArrayList<>();
        try {
            JSONObject mJson = new JSONObject(save);
            Log.e(TAG, "fetchAllRestaurant: " + mJson);
            JSONArray result = mJson.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject mJsonObject = result.getJSONObject(i);
                RestaurantItems.Result mContent = new RestaurantItems.Result();
                mContent.setId(Integer.valueOf(mJsonObject.getString("id")));
                mContent.setName(mJsonObject.getString("name"));
                mContent.setPrice(Double.valueOf(mJsonObject.getString("price")));
                mContent.setImageUrl(mJsonObject.getString("imageUrl"));
                mContent.setDescription(mJsonObject.getString("description"));
                mContent.setShelvesId(Integer.valueOf(mJsonObject.getString("shelvesId")));
                mContent.setShelvesName(mJsonObject.getString("shelvesName"));
                mContent.setCount(0);
                mListContent.add(mContent);

            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        passToAdapter(mListContent);*/
        mainAPIInterface.getAllRestaurantItems(userid).enqueue(new Callback<RestaurantItems>() {
            @Override
            public void onResponse(Call<RestaurantItems> call, Response<RestaurantItems> response) {
                hideLoading();
                if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                    List<RestaurantItems.Result> result = response.body().getResult();
                    passToAdapter(result);
                }

            }

            @Override
            public void onFailure(Call<RestaurantItems> call, Throwable t) {
                hideLoading();
            }
        });

    }

    private void passToAdapter(List<RestaurantItems.Result> result) {
        mTemp = result;
        mAlItemsAdapter = new AlItemsAdapter(result, RestaurantItemActivity.this);
        rvAllShop.setAdapter(mAlItemsAdapter);
    }

    private void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    private void hideLoading() {
        mProgress.dismiss();
    }

    @Override
    public void onIncreaseItemRequest(int pos, RestaurantItems.Result obj, Integer id) {
        int count = mTemp.get(pos).getCount();
        count = count + 1;
        mTemp.get(pos).setCount(count);
        mAlItemsAdapter.notifyItemChanged(pos);
        addToCart(obj, id);

    }

    private void addToCart(RestaurantItems.Result obj, Integer id) {
        btnCart.setVisibility(View.VISIBLE);

        if (mListCart.size() > 0) {
            int pos = -1;
            boolean isData = false;
            for (int i = 0; i < mListCart.size(); i++) {

                if (Objects.equals(mListCart.get(i).getId(), id)) {
                    //int count = mListCart.get(i).getCount();
                    //count = count + 1;
                    //mListCart.get(i).setCount(count);
                    isData = true;
                    break;
                }
                isData = false;
            }
            if (!isData) {
                mListCart.add(obj);
            }

        } else {
            mListCart.add(obj);
        }

    }

    @Override
    public void onDeCreaseRequest(int pos, Integer id) {
        int count = mTemp.get(pos).getCount();
        if (count > 0) {
            count = count - 1;
            mTemp.get(pos).setCount(count);
            if (mListCart.size() > 0 && count == 0) {
                for (int i = 0; i < mListCart.size(); i++) {
                    if (Objects.equals(mListCart.get(i).getId(), id)) {
                        mListCart.remove(i);
                        break;
                    }
                }

            }
            if (mListCart.size() > 0) {
                btnCart.setVisibility(View.VISIBLE);
            } else {
                btnCart.setVisibility(View.GONE);
            }
            mAlItemsAdapter.notifyItemChanged(pos);

        } else {
            if (mListCart.size() > 0 && count == 0) {
                for (int i = 0; i < mListCart.size(); i++) {
                    if (Objects.equals(mListCart.get(i).getId(), id)) {
                        mListCart.remove(i);
                        break;
                    }
                }

            }
            if (mListCart.size() > 0) {
                btnCart.setVisibility(View.VISIBLE);
            } else {
                btnCart.setVisibility(View.GONE);
            }
            Toast.makeText(this, "pleas add items first!!!", Toast.LENGTH_SHORT).show();
        }


    }
}
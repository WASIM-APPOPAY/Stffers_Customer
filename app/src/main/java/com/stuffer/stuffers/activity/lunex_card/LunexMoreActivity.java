package com.stuffer.stuffers.activity.lunex_card;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.AllGiftProduct;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomSonyPlayStation;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LunexMoreActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener {
    private static final String TAG = "LunexMoreActivity";
    private MainAPIInterface mainAPIInterface;
    private MainAPIInterface mainAPIInterfaceNode;
    List<GiftProductList.Product> mListGift;
    RecyclerView rvGiftLunex;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunex_more);
        mainAPIInterface = ApiUtils.getAPIService();
        mainAPIInterfaceNode = ApiUtils.getAPIServiceNode();
        rvGiftLunex = findViewById(R.id.rvGiftLunex);
        rvGiftLunex.setLayoutManager(new GridLayoutManager(LunexMoreActivity.this, 3, LinearLayoutManager.VERTICAL, false));
        setupActionBar();
        getAllGiftLst();
        //getList();
    }

    public void getAllGiftLst() {
        showDialog();
        //Log.e(TAG, "getAllGiftLst: called");
        JsonObject param = new JsonObject();
        param.addProperty("countryCode", "1");
        mainAPIInterfaceNode.getAllGiftCardList(param).enqueue(new Callback<GiftProductList>() {
            @Override
            public void onResponse(@NotNull Call<GiftProductList> call, @NotNull Response<GiftProductList> response) {
                //Log.e(TAG, "onResponse: called");
                dismissDialog();
                if (response.isSuccessful()) {
                    try {
                        mListGift = response.body().getProducts();
                        //  mListGift.add(0, Helper.getAppopayGift());
                       /* String listCard = new Gson().toJson(mListGift);
                        DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveGiftCardList(listCard);*/
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

    private void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    private void getList() {


        /*List<GiftProductList.Product> mListTemp = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < mListGift.size(); i++) {
            if (mListGift.get(i).getProductType().equalsIgnoreCase("GIFT_CARD")) {
                mListTemp.add(count, mListGift.get(i));
                count = count + 1;
            }
        }
        mListTemp.add(0, Helper.getAppopayGift());
        int[] mIcons = {R.drawable.appopay_gift_card, R.drawable.sony_play_station, R.drawable.xbox_live_logo, R.drawable.xbox_live_gold, R.drawable.subsription};*/
        //AllGiftProduct allGiftProduct = new AllGiftProduct(mListTemp, mIcons, LunexMoreActivity.this, LunexMoreActivity.this);
        int[] giftCardLogos = Helper.getGiftCardLogos();
        AllGiftProduct allGiftProduct = new AllGiftProduct( giftCardLogos, LunexMoreActivity.this, LunexMoreActivity.this,false);
        rvGiftLunex.setAdapter(allGiftProduct);


    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.info_brand_vouchers));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait.");
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRowItemClick(int pos) {
        BottomSonyPlayStation bottomSonyPlayStation = new BottomSonyPlayStation();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM1, mListGift.get(1).getProductName());
        bundle.putString(Constants.PARAM2, mListGift.get(4).getProductName());
        bottomSonyPlayStation.setArguments(bundle);
        bottomSonyPlayStation.setCancelable(false);
        bottomSonyPlayStation.show(getSupportFragmentManager(), bottomSonyPlayStation.getTag());
    }
}
package com.stuffer.stuffers.activity.shop_mall;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.all_restaurant.RestaurentModels;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.ChildAnimationExample;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.stuffer.stuffers.views.SliderLayout;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    SliderLayout slider_home, slider_home2;
    HashMap<String, String> file_maps = new HashMap<String, String>();

    PagerIndicator custom_indicator_home, custom_indicator_home2;
    private TextView toolbarTitle;
    MyTextViewBold tvItemName, price, tvItemDescription;
    private RestaurentModels.Array mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        slider_home = findViewById(R.id.slider_home);
        custom_indicator_home = findViewById(R.id.custom_indicator_home);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemDescription = findViewById(R.id.tvItemDescription);
        price = findViewById(R.id.price);
        String stringExtra = getIntent().getStringExtra(AppoConstants.DATA);
        setBanner(stringExtra);
        setupActionBar();
        tvItemName.setText(mResponse.getName());
        tvItemDescription.setText(mResponse.getDescription());
        price.setText("$"+mResponse.getPrice());


    }

    private void setBanner(String stringExtra) {
        Gson gson = new Gson();
        mResponse = gson.fromJson(stringExtra, RestaurentModels.Array.class);
        String swiperImage = mResponse.getSwiperImage();
        List<String> listSwipper = Arrays.asList(swiperImage.split(","));
        for (int i = 0; i < listSwipper.size(); i++) {
            file_maps.put("Sale banner" + i, listSwipper.get(i));
        }
        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(ItemDetailsActivity.this);
            // initialize a SliderLayout
            textSliderView.description(name).image(file_maps.get(name)).setScaleType(BaseSliderView.ScaleType.FitCenterCrop).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
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
    }



    private void setupActionBar() {
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText("Item Description");
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
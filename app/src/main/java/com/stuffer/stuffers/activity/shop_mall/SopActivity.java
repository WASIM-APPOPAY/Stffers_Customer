package com.stuffer.stuffers.activity.shop_mall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.communicator.ItemDetailsListener;
import com.stuffer.stuffers.communicator.ShopItemListener;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.communicator.TitleListener;
import com.stuffer.stuffers.models.shop_model.ShopItem;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.CountDrawable;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

public class SopActivity extends AppCompatActivity implements ShopListener, TitleListener, ShopItemListener, ItemDetailsListener {
    private static final String TAG = "SopActivity";
    private MenuInflater inflater;
    private Menu defaultMenu;
    private TextView toolbarTitle;
    private MyTextViewBold tvAlert;
    private MainShopAPIInterface apiServiceShop;
    List<ShopItem.Datum> mListShopItems;
    private ProgressDialog mLoading = null;
    RecyclerView rvCategory;
    private String extra;
    private List<ShopItem.Datum.Child> mListchildren;
    private BottomShopOnMallFragment fragmentBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sop);
        setupActionBar();
        extra = getIntent().getStringExtra(AppoConstants.TITLE);
        toolbarTitle.setText("Shop On Mall" + "\n" + extra);
        ShopItemFragment fragment = new ShopItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, extra);
        fragment.setArguments(bundle);
        initFragments(fragment);


    }

    private void initFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.shopContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void showLoading(String message) {
        if (mLoading == null) {
            mLoading = new ProgressDialog(SopActivity.this);
        }
        mLoading.setMessage(message);
        mLoading.show();
    }

    private void dismissDialog() {
        mLoading.dismiss();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, "0");
        return true;
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Select Media");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        defaultMenu = menu;
        return true;
    }

    public void setCount(Context context, String count) {
        MenuItem menuItem = defaultMenu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopOnMall:
                //show bottom shop on mall
                //Toast.makeText(this, "shop on mall click", Toast.LENGTH_SHORT).show();
                fragmentBottomSheet = new BottomShopOnMallFragment();
                fragmentBottomSheet.show(getSupportFragmentManager(), fragmentBottomSheet.getTag());
                fragmentBottomSheet.setCancelable(true);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onShopItemClick(int pos, String title) {

        /*fragmentBottomSheet.dismiss();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.shopContainer);
        if (currentFragment instanceof ShopItemFragment) {
            ((ShopItemFragment) currentFragment).setNewItem(title);
        }*/
    }

    @Override
    public void onTitleUpdate(String mTitle) {
        toolbarTitle.setText("Shop On Mall" + "\n" + mTitle);
    }

    @Override
    public void onShopItemClick(String categoryId) {
        ShopItemDetailsFragment fragment = new ShopItemDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        initFragments(fragment);
    }

    @Override
    public void onItemDetailsReceived(String param) {
        //Log.e(TAG, "onItemDetailsReceived: "+param );
        AddToCardShopFragment addToCardShopFragment = new AddToCardShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.INFO,param);
        addToCardShopFragment.setArguments(bundle);
        initFragments(addToCardShopFragment);

    }


    /**
     * https://cerca24.com/v1/products/categories/tree
     */


}
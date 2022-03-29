package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.InnerScanListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom.AppoPayFragment;
import com.stuffer.stuffers.fragments.bottom.ScanAppopayFragment;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.FromAccountDialogFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InnerPayActivity extends AppCompatActivity implements TransactionPinListener , InnerScanListener {
    private static final String TAG = "InnerPayActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_pay_activity);
        setupActionBar();
        if (isPermissionGranted()){
            if (savedInstanceState==null){
              ScanAppopayFragment mScanAppopayFragment=new ScanAppopayFragment();
              initFragments(mScanAppopayFragment);
            }
        }else {
            ActivityCompat.requestPermissions(InnerPayActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, AppoConstants.CAMERA_REQUEST_CODE);
        }




    }

    private void initFragments(Fragment mFragment) {
        FragmentManager fragmentManager4 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
        fragmentTransaction4.addToBackStack(null);
        fragmentTransaction4.replace(R.id.innerPayContainer, mFragment);
        fragmentTransaction4.commit();
    }

    private boolean isPermissionGranted() {
        boolean cameraPermission = ActivityCompat.checkSelfPermission(InnerPayActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        return cameraPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.CAMERA_REQUEST_CODE) {
            if (isPermissionGranted()) {
                ScanAppopayFragment mScanAppopayFragment = new ScanAppopayFragment();
                initFragments(mScanAppopayFragment);

            } else {
                Toast.makeText(this, "permission denied by user", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(R.string.toolbar_title_merchant_details);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG, "onActivityResult: result called" );
        redirectHomePay();
    }

    public void redirectHomePay() {

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onPinConfirm(String pin) {

        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.innerPayContainer);
        if (fragmentById instanceof AppoPayFragment) {
            ((AppoPayFragment) fragmentById).onConfirm(pin);
        }

    }

    @Override
    public void onInnerRequestListener(String param) {
        AppoPayFragment mFragment=new AppoPayFragment();
        Bundle mBundle=new Bundle();
        mBundle.putString(AppoConstants.MERCHANTSCANCODE,param);
        mFragment.setArguments(mBundle);
        initFragments(mFragment);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

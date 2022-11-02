package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.communicator.OnMapAndViewReadyListener;
import com.stuffer.stuffers.utils.DataManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurMapActivity extends AppCompatActivity implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, GoogleMap.OnMapClickListener, Toolbar.OnMenuItemClickListener {

    private GoogleMap mMap;
    private Marker selectMarker;
    private TextView addressTv;
    private SupportMapFragment mapFragment;
    private Toolbar mapToolBar;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        addressTv = findViewById(R.id.google_map_address);
        addressTv.setText(DataManager.newPosition);
        mapToolBar = findViewById(R.id.address_toolbar);
        mapToolBar.inflateMenu(R.menu.map_menu);
        mapToolBar.setTitle("Your Address");
        mapToolBar.setNavigationOnClickListener(view -> CurMapActivity.this.finish());
        mapToolBar.setOnMenuItemClickListener(this);
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            new OnMapAndViewReadyListener(mapFragment, this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissions = false;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isPermissions = true;
            }
        }
        if (isPermissions) {
            Toast.makeText(this, "Please enable GPS permission", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //启动我的位置
        googleMap.setMyLocationEnabled(true);
        setBestPosition();
        mMap.setOnMapClickListener(this);
    }

    private void setBestPosition() {
        //启用我的位置按钮
        View mapView = mapFragment.getView();
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            //获取按钮视图
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // 将按钮的位置设置在右下角
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (selectMarker != null) {
            selectMarker.remove();
        }
        selectMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("current position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        new GeocodeAddress().execute(latLng);
    }

    public class GeocodeAddress extends AsyncTask<LatLng, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            return setLocationInfo(latLngs[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
            update(s);
        }
    }

    private void update(String address) {
        if (address != null && !TextUtils.isEmpty(address)) {
            addressTv.setText(address);
        } else {
            ToastUtil.showTextShort(getResources().getString(R.string.position_fail));
        }
    }

    private String setLocationInfo(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            if (latLng != null) {
                List<Address> list =  geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (list == null || list.size() == 0) {
                    return "";
                }
                Address address = list.get(0);
                DataManager.newLatLog = latLng.latitude + "," + latLng.longitude;
                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    if (!TextUtils.isEmpty(address.getCountryName())) {
                        sb.append(address.getCountryName());
                    }
                    if (!TextUtils.isEmpty(address.getAdminArea())) {
                        sb.append(", " + address.getAdminArea());
                    }
                    if (!TextUtils.isEmpty(address.getLocality())) {
                        sb.append(", " + address.getLocality());
                    }
                    if (!TextUtils.isEmpty(address.getAddressLine(1))) {
                        sb.append(", " + address.getAddressLine(1));
                    }
                    if (!TextUtils.isEmpty(address.getAddressLine(0))) {
                        sb.append(", " + address.getAddressLine(0));
                    }
                    return sb.toString();
                }

            }
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            DataManager.newPosition = addressTv.getText().toString();
            finish();
        }
        return false;
    }

    protected void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    protected void hideLoading() {
        mProgress.dismiss();
    }
}
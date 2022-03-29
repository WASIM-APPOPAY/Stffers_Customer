package com.stuffer.stuffers.activity.wallet;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.stuffer.stuffers.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MerchantMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng mCurrLatLng = new LatLng(8.098145463244633, -80.98057365996564);
        ArrayList<LatLng> mCurrLatLng = new ArrayList<>();
        mCurrLatLng.add(new LatLng(8.099145463244633, -80.98057365996564));
        mCurrLatLng.add(new LatLng(8.972412300872481, -79.5351835639659));
        mCurrLatLng.add(new LatLng(8.972342986096171, -79.53520270217805));
        mCurrLatLng.add(new LatLng(8.972342986096171, -79.53520270217805));
        mCurrLatLng.add(new LatLng(8.972279972651679, -79.53515804634968));
        mCurrLatLng.add(new LatLng(8.972034220113597, -79.53509425230915));
        mCurrLatLng.add(new LatLng(8.984112609191843, -79.51621223141694));
        mCurrLatLng.add(new LatLng(8.984054686668067, -79.51627087337522));
        mCurrLatLng.add(new LatLng(8.983974486235264, -79.51605886014151));
        mCurrLatLng.add(new LatLng(8.983867552297239, -79.5166046814453));
        mCurrLatLng.add(new LatLng(8.984535888892205, -79.51545439687946));
        mCurrLatLng.add(new LatLng(8.984718567347018, -79.51575211759061));
        mCurrLatLng.add(new LatLng(8.984678467206281, -79.51588744518659));
        mCurrLatLng.add(new LatLng(8.984571533475986, -79.5161941877375));
        mCurrLatLng.add(new LatLng(8.984477966436101, -79.51618967681763));
        mCurrLatLng.add(new LatLng(8.984371032646633, -79.51625282969576));
        mCurrLatLng.add(new LatLng(8.98523541321829, -79.51557619171565));
        mCurrLatLng.add(new LatLng(8.98535125787911, -79.51567092103285));
        mCurrLatLng.add(new LatLng(8.98531561337203, -79.51578820494936));


        int count = 0;
        int countM = 0;
        for (int i = 0; i < mCurrLatLng.size(); i++) {
            MarkerOptions markerOpt = new MarkerOptions();
            if (i % 2 == 0) {
                count = count + 1;
                markerOpt.position(mCurrLatLng.get(i)).title("Agent " + count);
                markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                countM = countM + 1;
                markerOpt.position(mCurrLatLng.get(i)).title("Merchant " + countM);
                markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


            }

            mMap.addMarker(markerOpt);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrLatLng.get(i), 18));
        }
        //Marker


        //Set Custom InfoWindow Adapter


    }
}
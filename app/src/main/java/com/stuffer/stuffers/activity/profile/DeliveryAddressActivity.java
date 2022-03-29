package com.stuffer.stuffers.activity.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.address.AutoCompleteAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class
DeliveryAddressActivity extends AppCompatActivity {
    private ImageView ivBackCommon;
    private MainAPIInterface mainAPIInterface;
    private String vaultValue;
    private MyTextView tvPhone, tvUserName, tvEmail;
    private JSONObject mIndex;
    private PlacesClient placesClient;
    private AutoCompleteTextView placesAutocomplete;
    private AutoCompleteAdapter mPlaceAdapter;
    private static final String TAG = "DeliveryAddressActivity";
    private ImageView btnClearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ivBackCommon = findViewById(R.id.ivBackCommon);
        mainAPIInterface = ApiUtils.getAPIService();
        tvPhone = findViewById(R.id.tvPhone);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmial);
        btnClearAll = (ImageView) findViewById(R.id.btnClearAll);
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesAutocomplete.setText("");
                placesAutocomplete.setAdapter(mPlaceAdapter);

            }
        });

        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        ivBackCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        invalidateUserInfo();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }

        placesClient = Places.createClient(this);
        initAutoCompleteTextView();

    }

    private void initAutoCompleteTextView() {
        placesAutocomplete = findViewById(R.id.placesAutocomplete);
        placesAutocomplete.setThreshold(1);
        placesAutocomplete.setOnItemClickListener(autocompleteClickListener);
        mPlaceAdapter = new AutoCompleteAdapter(this, placesClient);
        placesAutocomplete.setAdapter(mPlaceAdapter);
    }

    private void invalidateUserInfo() {
        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                mIndex = new JSONObject(vaultValue);
                JSONObject result = mIndex.getJSONObject("result");
                tvUserName.setText("User Name : " + result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                tvPhone.setText("Mobile Number : " + result.getString(AppoConstants.MOBILENUMBER));
                tvEmail.setText("Emial Id : " + result.getString(AppoConstants.EMIAL));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = mPlaceAdapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            /*Log.e(TAG, "onSuccess: name  " + task.getPlace().getName());
                            Log.e(TAG, "onSuccess: address " + task.getPlace().getAddress());*/
                            String address = task.getPlace().getAddress();
                            //placesAutocomplete.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            //placesAutocomplete.setAdapter(null);
                            String name = task.getPlace().getName();
                            placesAutocomplete.setAdapter(null);
                            placesAutocomplete.setText(name + "," + address);


                            //responseView.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            if (task.getPlace().getLatLng() != null) {
                                // Log.e(TAG, "onActivityResult: lat lng :: " + place.getLatLng());
                                // Location mLocation=new Location();
                                closeKeyboard();
                                LatLng latLng = task.getPlace().getLatLng();
                                //getGeoLocationAddress(latLng);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            //responseView.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void closeKeyboard() {
        /*View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
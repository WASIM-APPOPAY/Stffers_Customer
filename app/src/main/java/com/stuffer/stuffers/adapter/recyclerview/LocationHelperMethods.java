package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.stuffer.stuffers.communicator.AutoCompleteListner;
import com.stuffer.stuffers.models.PredictionModel;


public class LocationHelperMethods {
    private static LocationHelperMethods instance;

    public static LocationHelperMethods getInstance() {
        if (instance == null) instance = new LocationHelperMethods();
        return instance;
    }

    public LocationHelperMethods() {

    }


    public void displayError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private String getAddressText(Address address) {
        String addressText = "";
        final int maxAddressLineIndex = address.getMaxAddressLineIndex();

        for (int i = 0; i <= maxAddressLineIndex; i++) {
            addressText += address.getAddressLine(i);
            if (i != maxAddressLineIndex) {
                addressText += "\n";
            }
        }

        return addressText;
    }


    public void getAutoCompleteResult(Context context, String input, final AutoCompleteListner listner) {
        //String main_url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDHTPV6rMVL5tVhstWL1PsAT_OTr0slzas&language=en-US&locationbias=rectangle%3A6.462699900000000%2C68.109700000000000%7C35.513327000000000%2C97.395358700000000&types=establishment&input=" + input;
       // String main_url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDHTPV6rMVL5tVhstWL1PsAT_OTr0slzas&language=en-US&location="+ start.getLatitude()+","+start.getLongitude() +"&radius=1500&types=establishment&input=" + input;
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDHTPV6rMVL5tVhstWL1PsAT_OTr0slzas&language=en-US&locationbias=rectangle%3A6.462699900000000%2C68.109700000000000%7C35.513327000000000%2C97.395358700000000&sessiontoken=dd3761fb-ea17-4048-a875-0628fca91fdc&types=establishment&input=k";
        //String filt_url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ input+"&types=establishment&key=AIzaSyBJ-gfrAjD6HQ_1FllX8KhyKeCzx-3YZYc";
        String filt_url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+input+"&types=geocode&key=AIzaSyBJ-gfrAjD6HQ_1FllX8KhyKeCzx-3YZYc";
        String filt_url2="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ input+"&types=geocode&components=country%3AIN&key=AIzaSyDHTPV6rMVL5tVhstWL1PsAT_OTr0slzas";
        //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=110%20Ward&location=40.7128%2C-74.0059&radius=500&types=address&components=country%3ACA%7Ccountry%3AUS&key=YOUR_API_KEY
        AndroidNetworking.get(filt_url)
                .setTag("predict")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(PredictionModel.class, new ParsedRequestListener<PredictionModel>() {
                    @Override
                    public void onResponse(PredictionModel response) {
                        Log.e("onLocTest","LocationHelperMethods_Success");
                        listner.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("onLocTest","LocationHelperMethods_Error");
                        Log.e("Code", String.valueOf(anError.getErrorCode()));
                        listner.onError(anError.getErrorDetail());
                    }
                });
    }
}
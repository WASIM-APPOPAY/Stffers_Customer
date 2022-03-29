package com.stuffer.stuffers.asyntask;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
//import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class RegisterDevice extends AsyncTask<String, Void, Void> {
    private static final String TAG = "RegisterDevice";
    final Handler mHandler = new Handler();
    String mRequestUrl;
    JSONObject mRequestParam;


    public RegisterDevice(String mRequestUrl, JSONObject mRequestParam) {
        //showToast("hi ");
        this.mRequestUrl = mRequestUrl;
        this.mRequestParam = mRequestParam;


    }

    @Override
    protected Void doInBackground(String... strings) {
        //Log.e(TAG, "doInBackground: called");
        //String token = FirebaseInstanceId.getInstance().getToken();
        //Log.e(TAG, "doInBackground: " + token);
        //Log.e(TAG, "doInBackground: " + mRequestParam);

        //AndroidNetworking.post("https://labapi.appopay.com/api/users/registerdevice")
        AndroidNetworking.post("https://prodapi.appopay.com/api/users/registerdevice")
                .setTag("addFcm")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Access-Control-Allow-Origin", "appopay.com")
                .setPriority(Priority.IMMEDIATE)
                .addJSONObjectBody(mRequestParam)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e(TAG, "onResponse: success");
                        //showToast("successfully updated");

                    }

                    @Override
                    public void onError(ANError anError) {

                        //Log.e(TAG, "onError: " + anError.getErrorDetail());
                        //Log.e(TAG, "onError: " + anError.getErrorBody());
                        showToast(anError.getErrorBody());


                    }
                });
        return null;
    }

    // Helper for showing tests
    void showToast(String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppoPayApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Type of Account
    1.Checking
    2.Saving
    3.Upi Card*/


}

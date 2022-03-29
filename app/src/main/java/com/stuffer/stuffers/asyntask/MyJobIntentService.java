package com.stuffer.stuffers.asyntask;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONException;
import org.json.JSONObject;

@Keep
public class MyJobIntentService extends JobIntentService {
    final Handler mHandler = new Handler();
    private static final String TAG = "MyJobIntentService";
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 2;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, intent);
    }

    /*@Override
    public void onCreate() {
        super.onCreate();
        showToast("Job Execution Started");
    }*/

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String mUserId = intent.getStringExtra(AppoConstants.USERID);
        String mFCM_TOKE = intent.getStringExtra(AppoConstants.FCM_TOKEN);
        String mDevice_ID = intent.getStringExtra(AppoConstants.DEVICE_ID);
        JSONObject mJSON = new JSONObject();
        try {
            mJSON.put("user_id", mUserId);
            mJSON.put("fcm_token", mFCM_TOKE);
            mJSON.put("device_id", mDevice_ID);
            //Log.e(TAG, "onHandleWork: " + mJSON);

            AndroidNetworking.post("https://labapi.appopay.com/api/users/registerdevice")
                    .setTag("addFcm")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Access-Control-Allow-Origin", "appopay.com")
                    .setPriority(Priority.IMMEDIATE)
                    .addJSONObjectBody(mJSON)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.e(TAG, "onResponse: " + response);
                            showToast(response.toString());

                        }

                        @Override
                        public void onError(ANError anError) {

                            //Log.e(TAG, "onError: " + anError.getErrorDetail());
                            //Log.e(TAG, "onError: " + anError.getErrorBody());
                            showToast(anError.getErrorBody());


                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // Helper for showing tests
    void showToast(String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

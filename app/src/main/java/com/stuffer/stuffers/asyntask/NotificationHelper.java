package com.stuffer.stuffers.asyntask;

import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.utils.AppoConstants;


import org.json.JSONException;
import org.json.JSONObject;

public class NotificationHelper extends AsyncTask<String, Void, Void> {
    private static final String TAG = "NotificationHelper";
    String mBearear;
    String mRequestUrl;
    JSONObject mRequestParam;
    NoticeReadListener mNoticeReadListener;

    public NotificationHelper(String mRequestUrl,String bearer_, JSONObject mRequestParam, NoticeReadListener mNoticeReadListener) {
        this.mBearear=bearer_;
        this.mRequestUrl = mRequestUrl;
        this.mRequestParam = mRequestParam;
        this.mNoticeReadListener = mNoticeReadListener;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.e(TAG, "doInBackground: "+mRequestUrl );
        Log.e(TAG, "doInBackground: body : "+mRequestParam );
        Log.e(TAG, "doInBackground: Authorization : "+mBearear );
        AndroidNetworking.post(mRequestUrl)
                .setTag("READNOTICE")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(mRequestParam)
                .addHeaders("Authorization",mBearear)
                .setContentType("Access-Control-Allow-Origin: appopay.com.xyz")
                .setOkHttpClient(AppoPayApplication.getOkHttpClient(20))

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ////Log.e(TAG, "onResponse: UNREAD " + response);
                        try {
                            if (response.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                                mNoticeReadListener.noOfUnRead(response, String.valueOf(response.getJSONArray(AppoConstants.RESULT).length()));
                            } else {
                                mNoticeReadListener.onErrorOccur("error occurred");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mNoticeReadListener.onErrorOccur(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                       /* //Log.e(TAG, "onError: details : " + anError.getErrorDetail());
                        //Log.e(TAG, "onError: response : " + anError.getResponse());
                        //Log.e(TAG, "onError: code : " + anError.getErrorCode());
                        //Log.e(TAG, "onError: body : " + anError.getErrorBody());*/
                        //Log.e(TAG, "onError: "+anError.getErrorDetail() );
                        mNoticeReadListener.onErrorOccur(String.valueOf(anError.getErrorCode()));
                    }
                });
        return null;
    }

    public interface NoticeReadListener {
        void noOfUnRead(JSONObject response, String countNotification);

        void onErrorOccur(String error);
    }

}

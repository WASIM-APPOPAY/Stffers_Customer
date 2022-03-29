package com.stuffer.stuffers.myService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Keep;

import com.stuffer.stuffers.utils.AppoConstants;

@Keep
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e(TAG, "onReceive: called for discout ");
        String discount = intent.getStringExtra("discount");
        //Log.e(TAG, "onReceive: discount :: " + discount);
        Intent intentBroadCast = new Intent(AppoConstants.NOTIFY_ACTION);
        intentBroadCast.putExtra(AppoConstants.HAS_DISCOUNT, true);
        intentBroadCast.putExtra(AppoConstants.DISCOUNT, discount);
        context.sendBroadcast(intentBroadCast);


    }
}

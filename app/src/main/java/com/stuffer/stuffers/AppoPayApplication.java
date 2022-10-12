package com.stuffer.stuffers;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.multidex.MultiDex;

import com.onesignal.OneSignal;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatUtils.ToastApp;
import com.stuffer.stuffers.communicator.ScreenTimeoutListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.stuffer.stuffers.myService.MyOnesignalNotificationOpenedHandler;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

//import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AppoPayApplication extends Application {
    public static  boolean UPDATE_WALLET = false;
    private static AppoPayApplication mInstance;
    Context context;
    public SharedPreferences preferences;
    public String prefName;

    MainAPIInterface mainAPIInterface;

    public static String TRACKING_MORE_API_KEY = "";
    public static String APP_SUPPORT_EMAIL = "";
    public static String APP_SUPPORT_PHONE = "";
    public static String CURRENCY_SYMBOL = "";
    public static String CURRENCY_CODE = "";
    private ScreenTimeoutListener timeoutListener;
    private Timer timer;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String ONESIGNAL_APP_ID = "57708791-f9a1-4d04-bbca-cafe1b6588ef";

    public AppoPayApplication() {
        mInstance = this;
    }

    public AppoPayApplication(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        mInstance = this;
        prefName = getResources().getString(R.string.app_name);
        preferences = getSharedPreferences(prefName, MODE_PRIVATE);
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new MyOnesignalNotificationOpenedHandler(this));
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        EmojiManager.install(new GoogleEmojiProvider());
        ToastApp.initToastUtils(this);

/*
HceApiService.initialize(
                mInstance,
                Environment.WALLET_ID
                );
*/



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppoPayApplication getInstance() {
        return mInstance;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            @SuppressLint("MissingPermission") NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

            for (NetworkInfo tempNetworkInfo : networkInfos) {


                /**
                 * Can also check if the user is in roaming
                 */
                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }

        return outcome;
    }



    public static OkHttpClient getOkHttpClient(long timeOut) {
        return new OkHttpClient().newBuilder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                //.sslSocketFactory(CustomSSLSocketFactory.create(AppoPayApplication.getInstance(), R.raw.appopay_com))
                .build();
    }
    public static OkHttpClient getOkHttpClient2(long timeOut) {
        return new OkHttpClient().newBuilder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                //.sslSocketFactory(CustomSSLSocketFactory.create(AppoPayApplication.getInstance(), R.raw.appopay_com))
                .build();
    }

    public void startUserSession() {
        cancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeoutListener.onTimeoutConfirm();
            }
        }, 60000);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void registerSessionListener(ScreenTimeoutListener timeoutListener) {
        this.timeoutListener = timeoutListener;
    }

    public void onUserInteracted() {
        startUserSession();
    }
}

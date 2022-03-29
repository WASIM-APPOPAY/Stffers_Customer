package com.stuffer.stuffers.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.stuffer.stuffers.AppoPayApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TimeUtils {
    private static final String TAG = "TimeUtils";
    public static final String DOBFORMAT = "yyyy-MM-dd";

    public static String getUniqueTimeStamp() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String millisInString = dateFormat.format(new Date());

        return millisInString;
    }

    public static String getUniqueMsgId(String timeStamp) {
        String mWalletId = "A" + "39990157";
        String mWalletWithTime = mWalletId + timeStamp;
        String randomNumber = getRandomNumber();
        return mWalletWithTime + randomNumber;
    }


    @SuppressLint("DefaultLocale")
    public static String getRandomNumber() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999999);
        String format = String.format("%08d", number);
        return format;


    }

    public static String getDeviceId() {
        /*String mDeviceID = Settings.Secure.getString(AppoPayApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
        Long senderMobileNumber = Helper.getSenderMobileNumber();
        String phoneCode = Helper.getPhoneCode();
        String phWithCode = phoneCode + senderMobileNumber;
        return phWithCode;
    }

    // User id will to be change
    // device id will to be change
    // device token it will chage


    public static String getVersion() {
        return "1.0.0";
    }

    public static String getInsId() {
        return "39990157";
    }

    public static String getRequiredFormatDate(String fromApi, String required, String dateParam) {
        Date value = null;
        String ourDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(fromApi);
            value = formatter.parse(dateParam);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(required); //this format changeable
            ourDate = dateFormatter.format(value);
            // Log.e(TAG, "getRequiredFormatDate: From Api  : "+dateParam );
            //Log.e(TAG, "getRequiredFormatDate: After Con : "+ourDate );
            return ourDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseLongDate(String dateParam, String requiredFormat) {
        // milliseconds
        long milliSec = Long.parseLong(dateParam);
        // Creating date format
        //DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        //DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        DateFormat simple = new SimpleDateFormat(requiredFormat);
        Date result = new Date(milliSec);
        //Log.e(TAG, "getTimeDateOther: "+simple.format(result) );

        return simple.format(result);
    }
}

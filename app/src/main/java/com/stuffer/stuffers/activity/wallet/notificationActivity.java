package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.adapter.recyclerview.NotificationAdapter;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.asyntask.NotificationHelper;
import com.stuffer.stuffers.asyntask.RegisterDevice;
import com.stuffer.stuffers.models.output.Notifications;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_FIREBASE_TOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_ID;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_ID1;

public class notificationActivity extends AppCompatActivity {
    private static final String TAG = "notificationActivity";
    private String userIds;
    private RecyclerView rvNotifications;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(notificationActivity.this));
        setupActionBar();
        readCountNotifications();


    }

    private void readCountNotifications() {
        dialog = new ProgressDialog(notificationActivity.this);
        dialog.setMessage("Please wait, getting notification.");
        dialog.show();
        //https://appopay.com/api/wallet/notifications?access_token=
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        JSONObject jsonUser = null;
        try {
            jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            userIds = objResult.getString(AppoConstants.ID);
            String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
            //String url = Constants.APPOPAY_BASE_URL+ "/api/wallet/notifications?access_token=" + accesstoken;
            String url = Constants.APPOPAY_BASE_URL + "api/wallet/notifications";
            JSONObject param = new JSONObject();
            String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
            param.put(AppoConstants.USERID, userIds);


            NotificationHelper notificationHelper = new NotificationHelper(url, bearer_, param, new NotificationHelper.NoticeReadListener() {
                @Override
                public void noOfUnRead(JSONObject response, String countNotification) {
                    //Log.e(TAG, "noOfUnRead: COUNT :: " + countNotification);
                    dialog.dismiss();
                    getNotificationDetails(response);



                }

                @Override
                public void onErrorOccur(String error) {
                    dialog.dismiss();
                    //Log.e(TAG, "onErrorOccur: CODE :: " + error);
                    if (error.equals("401")) {
                        //Log.e(TAG, "onErrorOccur: invalid access token");
                        DataVaultManager.getInstance(notificationActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(notificationActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(notificationActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
            notificationHelper.execute();

        } catch (JSONException e) {
            e.printStackTrace();
            dialog.dismiss();
        }


    }



    /**
     * "id": 433,
     * "mobilenumber": 9836683269,
     * "notification_text": "An amount of (1.3 USD ) will be debited from your bank account,Post approval to fund your wallet",
     * "notification_date": "2020-08-24T12:59:22.000+0000",
     * "userid": 89
     *
     * @param response
     */

    private void getNotificationDetails(JSONObject response) {
        ArrayList<Notifications> mListNotifications = new ArrayList<>();
        try {
            JSONArray result = response.getJSONArray(AppoConstants.RESULT);
            if (result.length() > 0) {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject index = result.getJSONObject(i);
                    Notifications notifications = new Notifications();
                    notifications.setId(index.getString(AppoConstants.ID));
                    notifications.setMobilenumber(index.getString(AppoConstants.MOBILENUMBER));
                    notifications.setNotification_date(Helper.getTimeDate(index.getString(AppoConstants.NOTIFICATION_DATE)));
                    notifications.setNotification_text(index.getString(AppoConstants.NOTIFICATION_TEXT));
                    notifications.setUserid(index.getString(AppoConstants.USERID));
                    mListNotifications.add(notifications);
                }

                if (mListNotifications.size() > 0) {
                    NotificationAdapter adapter = new NotificationAdapter(mListNotifications, notificationActivity.this);
                    rvNotifications.setAdapter(adapter);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Notifications");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;


class BaseBusinessActivity extends AppCompatActivity {

    private ProgressDialog mProgress;
    protected MainAPIInterface mainAPIInterface;
    protected ChatHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainAPIInterface = ApiUtils.getAPIService();
        helper = new ChatHelper(this);
    }

    protected void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    protected void hideLoading() {
        mProgress.dismiss();
    }

}

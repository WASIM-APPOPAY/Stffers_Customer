package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomClaveError;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomClaveFragment;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomClaveSuccess;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;

public class FundSistemaActivity extends AppCompatActivity implements TransactionPinListener {
    private static final String TAG = "FundSistemaActivity";
    LinearLayout layoutConfirm;
    ImageView ivBackClave;
    private BottomClaveFragment fragmentBottomSheet;
    private int mCounter=0;
    private BottomClaveSuccess fragmentBottomSuccess;
    private BottomClaveError fragmentBottomError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_sistema);
        ivBackClave = findViewById(R.id.ivBackClave);
        layoutConfirm = findViewById(R.id.layoutConfirm);
        ivBackClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        layoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    fragmentBottomSheet = new BottomClaveFragment();
                    fragmentBottomSheet.show(getSupportFragmentManager(), fragmentBottomSheet.getTag());
                    fragmentBottomSheet.setCancelable(true);



                    /*fragmentBottomSuccess = new BottomClaveSuccess();
                    fragmentBottomSuccess.show(getSupportFragmentManager(), fragmentBottomSuccess.getTag());
                    fragmentBottomSuccess.setCancelable(false);*/

                    /*fragmentBottomError = new BottomClaveError();
                    fragmentBottomError.show(getSupportFragmentManager(), fragmentBottomError.getTag());
                    fragmentBottomError.setCancelable(false);*/

            }
        });

    }


    @Override
    public void onPinConfirm(String pin) {
        if (fragmentBottomSheet != null)
            fragmentBottomSheet.dismiss();
        Log.e(TAG, "onPinConfirm: " + pin);

    }
}
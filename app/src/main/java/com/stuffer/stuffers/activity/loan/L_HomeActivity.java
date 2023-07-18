package com.stuffer.stuffers.activity.loan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.stuffer.stuffers.R;

public class L_HomeActivity extends AppCompatActivity {
    LinearLayout lHome, lLoan, lCalculator, lMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhome);
        lHome = findViewById(R.id.lHome);
        lLoan = findViewById(R.id.lLoan);
        lCalculator = findViewById(R.id.lCalculator);
        lMore = findViewById(R.id.lMore);







    }


}
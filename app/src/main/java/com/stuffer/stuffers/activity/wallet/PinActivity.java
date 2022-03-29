package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PinActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PinActivity";
    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10, btnClear, btnConfirm;
    ArrayList<MyButton> mListBtn;
    ImageView ivDots1, ivDots2, ivDots3, ivDots4, ivDots5, ivDots6;
    List<Integer> list;
    private String codeString = "";
    List<ImageView> dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        mListBtn = new ArrayList<>();
        dots = new ArrayList<>();
        findIds();
        getRandomKeyFromRange();
    }

    private void getRandomKeyFromRange() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        list = Arrays.asList(array);
        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            int answer = list.get(i);
            ////Log.e(TAG, "getRandomKeyFromRange: " + answer);
            mListBtn.get(i).setText("" + list.get(answer));
        }
    }

    private void findIds() {
        //tvInputPin = (MyTextView) findViewById(R.id.tvInputPin);
        /*@BindViews({R.id.dot_1, R.id.dot_2, R.id.dot_3, R.id.dot_4})
         */
        ivDots1 = (ImageView) findViewById(R.id.dot_1);
        ivDots2 = (ImageView) findViewById(R.id.dot_2);
        ivDots3 = (ImageView) findViewById(R.id.dot_3);
        ivDots4 = (ImageView) findViewById(R.id.dot_4);
        ivDots5 = (ImageView) findViewById(R.id.dot_5);
        ivDots6 = (ImageView) findViewById(R.id.dot_6);
        btnKey1 = (MyButton) findViewById(R.id.btnKey1);
        btnKey2 = (MyButton) findViewById(R.id.btnKey2);
        btnKey3 = (MyButton) findViewById(R.id.btnKey3);
        btnKey4 = (MyButton) findViewById(R.id.btnKey4);
        btnKey5 = (MyButton) findViewById(R.id.btnKey5);
        btnKey6 = (MyButton) findViewById(R.id.btnKey6);
        btnKey7 = (MyButton) findViewById(R.id.btnKey7);
        btnKey8 = (MyButton) findViewById(R.id.btnKey8);
        btnKey9 = (MyButton) findViewById(R.id.btnKey9);
        btnKey10 = (MyButton) findViewById(R.id.btnKey10);
        btnClear = (MyButton) findViewById(R.id.btnClear);
        btnConfirm = (MyButton) findViewById(R.id.btnConfirm);

        btnKey1.setOnClickListener(this);
        btnKey2.setOnClickListener(this);
        btnKey3.setOnClickListener(this);
        btnKey4.setOnClickListener(this);
        btnKey5.setOnClickListener(this);
        btnKey6.setOnClickListener(this);
        btnKey7.setOnClickListener(this);
        btnKey8.setOnClickListener(this);
        btnKey9.setOnClickListener(this);
        btnKey10.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        mListBtn.add(btnKey1);
        mListBtn.add(btnKey2);
        mListBtn.add(btnKey3);
        mListBtn.add(btnKey4);
        mListBtn.add(btnKey5);
        mListBtn.add(btnKey6);
        mListBtn.add(btnKey7);
        mListBtn.add(btnKey8);
        mListBtn.add(btnKey9);
        mListBtn.add(btnKey10);
        dots.add(ivDots1);
        dots.add(ivDots2);
        dots.add(ivDots3);
        dots.add(ivDots4);
        dots.add(ivDots5);
        dots.add(ivDots6);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnKey1:
                if (codeString.length()==6)
                    return;
                codeString += btnKey1.getText().toString().trim();
                //tvInputPin.setText(codeString);

                setDotEnable();
                break;
            case R.id.btnKey2:
                if (codeString.length()==6)
                    return;
                codeString += btnKey2.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();


                break;
            case R.id.btnKey3:
                if (codeString.length()==6)
                    return;
                codeString += btnKey3.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey4:
                if (codeString.length()==6)
                    return;
                codeString += btnKey4.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey5:
                if (codeString.length()==6)
                    return;
                codeString += btnKey5.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey6:
                if (codeString.length()==6)
                    return;
                codeString += btnKey6.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey7:
                if (codeString.length()==6)
                    return;
                codeString += btnKey7.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey8:
                if (codeString.length()==6)
                    return;
                codeString += btnKey8.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();
                break;

            case R.id.btnKey9:
                if (codeString.length()==6)
                    return;
                codeString += btnKey9.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey10:
                if (codeString.length()==6)
                    return;
                codeString += btnKey10.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnConfirm:
                //getRandomKeyFromRange();
//                Log.e(TAG, "onClick: " + codeString);
                break;
            case R.id.btnClear:
                setDotDisable();
                break;


        }
    }

    private void setDotEnable() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
    }

    private void setDotDisable() {
        for (int i = 0; i < 6; i++) {
            dots.get(i).setImageResource(R.drawable.dot_disable);
        }
        codeString = "";
    }
}
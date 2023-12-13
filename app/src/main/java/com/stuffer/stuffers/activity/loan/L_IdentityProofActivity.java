package com.stuffer.stuffers.activity.loan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.my_camera.MyCameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;

public class L_IdentityProofActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout relative1, relative2, relative3;
    ImageView image1, image2, image3;
    ImageView delete1, delete2, delete3;
    MyButton capture1, capture2, capture3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lidentity_proof);
        relative1 = findViewById(R.id.relative1);
        relative2 = findViewById(R.id.relative2);
        relative3 = findViewById(R.id.relative3);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        delete1 = findViewById(R.id.delete1);
        delete2 = findViewById(R.id.delete2);
        delete3 = findViewById(R.id.delete3);
        capture1 = findViewById(R.id.capture1);
        capture2 = findViewById(R.id.capture2);
        capture3 = findViewById(R.id.capture3);

        capture1.setOnClickListener(this);
        capture2.setOnClickListener(this);
        capture3.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(L_IdentityProofActivity.this, CameraActivity.class);

        switch (view.getId()) {
            case R.id.capture1:
                startActivityForResult(mIntent, 100);

                break;
            case R.id.capture2:
                startActivityForResult(mIntent, 101);
                break;
            case R.id.capture3:
                startActivityForResult(mIntent, 102);
                break;

        }

    }

    /*{
        category:"shop",
                [ {
                    name:"shop item1"
                },
{
                    name:"shop item1"
                },
{
                    name:"shop item1"
                },
{
                    name:"shop item1"
                }]

    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra).into(image1);
                relative1.setVisibility(View.VISIBLE);

            }

        } else if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra).into(image2);
                relative2.setVisibility(View.VISIBLE);

            }

        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);
                Glide.with(L_IdentityProofActivity.this).load(stringExtra).into(image3);
                relative3.setVisibility(View.VISIBLE);

            }
        }
    }
}
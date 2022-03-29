package com.stuffer.stuffers.fragments.finance_fragment.media;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.PictureListener;
import com.stuffer.stuffers.utils.AppoConstants;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity implements itemClickListener, imageSelectListener, PictureListener {
    private static final String TAG = "MediaActivity";
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        AllMediaFragment fragment = new AllMediaFragment();
        initFragments(fragment);
        setupActionBar();
    }

    private void initFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mediaContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        //Log.e(TAG, "onPicClicked: pictureFolderPath :: " + pictureFolderPath);
        //Log.e(TAG, "onPicClicked: folderName :: " + folderName);
        /*Intent move = new Intent(MainActivity.this,ImageDisplay.class);
        move.putExtra("folderPath",pictureFolderPath);
        move.putExtra("folderName",folderName);
        //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
        startActivity(move);*/
        toolbarTitle.setText(folderName);
        DisplayImageFragment fragment = new DisplayImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("folderPath", pictureFolderPath);
        bundle.putString("folderName", folderName);
        fragment.setArguments(bundle);
        initFragments(fragment);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Select Media");
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
                verifyStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verifyStack() {
        Fragment currentFragmnet = getSupportFragmentManager().findFragmentById(R.id.mediaContainer);
        if (currentFragmnet instanceof SelectImageFragment) {
            getSupportFragmentManager().popBackStackImmediate();
        } else if (currentFragmnet instanceof DisplayImageFragment) {
            getSupportFragmentManager().popBackStackImmediate();
            toolbarTitle.setText("Select Media");
        } else {
            finish();
        }
    }

    @Override
    public void onImageSelect(PicHolder holder, int position, ArrayList<pictureFacer> pics) {

        SelectImageFragment browser = SelectImageFragment.newInstance(pics, position, MediaActivity.this);
        initFragments(browser);
    }

    @Override
    public void onImageSelect(String pictureFolderPath, String folderName) {

    }

    @Override
    public void onPictureSelect(String path) {
        //Log.e(TAG, "onPictureSelect: " + path);
        Intent intent = new Intent();
        intent.putExtra(AppoConstants.INFO, path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        verifyStack();
    }
}
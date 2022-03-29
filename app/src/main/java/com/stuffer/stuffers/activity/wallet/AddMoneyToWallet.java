package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.firebase.database.DatabaseError;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AddMoneyToWallet extends AppCompatActivity/* implements CardScanActivityResultHandler*/ {
    private static final String TAG = "AddMoneyToWallet";
    MainAPIInterface mainAPIInterface;
    ProgressDialog dialog;
    LinearLayout layoutByBank, layoutByCard, layoutByClave, llAgentLocation;
    static String SCAN_KEY = "6Yx9deK4JNef_Q4Q79V_LaCCHoqBFIGD";
    private MyButton btnSaveCard;
    private MyTextView tvCardDetails;
    MyButton btnShow;
    private AlertDialog mDialog;
    private File mFileSSort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_to_wallet);
        tvCardDetails = findViewById(R.id.tvCardDetails);
        btnShow = findViewById(R.id.btnShow);
        mainAPIInterface = ApiUtils.getAPIService();
        /*CardScanActivity.warmUp(this, SCAN_KEY, true);*/
        layoutByBank = findViewById(R.id.layoutByBank);
        layoutByCard = findViewById(R.id.layoutByCard);
        //btnSaveCard = findViewById(R.id.btnSaveCard);
        layoutByClave = findViewById(R.id.layoutByClave);
        llAgentLocation = findViewById(R.id.llAgentLocation);
        setupActionBar();

        layoutByBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppoPayApplication.isNetworkAvailable(AddMoneyToWallet.this)) {
                    Intent intentBank = new Intent(AddMoneyToWallet.this, WalletBankActivity.class);
                    startActivity(intentBank);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });


        layoutByCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppoPayApplication.isNetworkAvailable(AddMoneyToWallet.this)) {
                    Intent intentCard = new Intent(AddMoneyToWallet.this, WalletCardActivity.class);
                    startActivity(intentCard);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });

        /*btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardScanActivity.start(AddMoneyToWallet.this,
                        SCAN_KEY,
                        false,
                        true,
                        true,
                        true,
                        true,
                        true, true);

            }
        });*/

        layoutByClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(AddMoneyToWallet.this)) {
                    Intent intentSistema = new Intent(AddMoneyToWallet.this, FundSistemaActivity.class);
                    startActivity(intentSistema);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });

        llAgentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAgent = new Intent(AddMoneyToWallet.this, AgentMapActivity.class);
                startActivity(intentAgent);
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPayDialog();
                showPayDialogLikeUnion();
            }
        });
        btnShow.setVisibility(View.GONE);

        getDate();

    }

    private void getDate() {
        String mDateFormat = "dd MMM yyyy";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat);
        Date mDate = new Date();
        String format = mSimpleDateFormat.format(mDate);
        Log.e(TAG, "getDate: " + format);
    }

    private String getDateTime() {
        String mDateFormat = "dd MMM yyyy HH:mm:ss";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat);
        Date mDate = new Date();
        String format = mSimpleDateFormat.format(mDate);
        //Log.e(TAG, "getDate: " + format);
        return format;
    }


    private void showPayDialogLikeUnion() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddMoneyToWallet.this);
        View mCustomLayout = LayoutInflater.from(AddMoneyToWallet.this).inflate(R.layout.success_dialog_inner_appopay, null);
        LinearLayout layoutRoot = mCustomLayout.findViewById(R.id.layoutRoot);
        MyTextView tvInfo = mCustomLayout.findViewById(R.id.tvInfo);
        MyTextViewBold tvAmountPay = mCustomLayout.findViewById(R.id.tvAmountPay);//edAmount
        MyTextView tvCurrencyPay = mCustomLayout.findViewById(R.id.tvCurrencyPay);
        MyTextView tvTransactionTime = mCustomLayout.findViewById(R.id.tvTransactionTime);
        MyTextView tvVoucherPay = mCustomLayout.findViewById(R.id.tvVoucherPay);
        MyButton btnShare = mCustomLayout.findViewById(R.id.btnShare);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnClose);
        tvAmountPay.setText("Amount : 10.00");
        tvCurrencyPay.setText("Currency : USD");
        tvTransactionTime.setText("Transaction Time : " + getDateTime());
        tvVoucherPay.setText("Transaction No : " + "#245784578");
        tvInfo.setText("SUCCESS");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddMoneyToWallet.this, "Show Receipt", Toast.LENGTH_SHORT).show();
                takeScreenShort(layoutRoot);
            }
        });
        mBuilder.setView(mCustomLayout);
        mDialog = mBuilder.create();
        // mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    private void showPayDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddMoneyToWallet.this);
        View mCustomLayout = LayoutInflater.from(AddMoneyToWallet.this).inflate(R.layout.success_dialog_inner_pay, null);
        LinearLayout rootLayout = mCustomLayout.findViewById(R.id.rootLayout);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnCloseInner);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShort(rootLayout);
            }
        });
        mBuilder.setView(mCustomLayout);
        mDialog = mBuilder.create();
        // mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    /*private fun getScreenShot1(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }*/

    private Bitmap getScreenShot(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;

    }

    //android.os.FileUriExposedException:
    private void takeScreenShort(LinearLayout rootLayout) {
        mDialog.dismiss();
        //Bitmap bitmap = screenShot(rootLayout);
        Bitmap bitmap = getScreenShot(rootLayout);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        mFileSSort = new File(getCacheDir(), "screen_short_" + now + ".jpeg");
        try {
            boolean newFile = mFileSSort.createNewFile();
            if (newFile) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                //write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFileSSort);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    openScreenshot(mFileSSort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void openScreenshot(File imageFile) {
        Intent intentShareFile = new Intent();
        intentShareFile.setAction(Intent.ACTION_SEND);
        Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), "com.stuffer.stuffers.fileprovider", imageFile);
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.setType("image/jpeg");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uriForFile);
        Intent chooser = Intent.createChooser(intentShareFile, "Share File");
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uriForFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(chooser);
    }

    public Bitmap screenShot(View view) {
        /*Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);*/
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);

        if (CardScanActivity.isScanResult(requestCode)) {
            Log.e(TAG, "onActivityResult: isScanCalled");
            CardScanActivity.parseScanResult(resultCode, data, this);
        }


    }*/

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_add_money));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(getString(R.string.info_add_money));
        toolbar.setTitleTextColor(Color.WHITE);


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

    /*@Override
    public void analyzerFailure(@org.jetbrains.annotations.Nullable String s) {
        Log.e(TAG, "analyzerFailure: called");
    }

    @Override
    public void cameraError(@org.jetbrains.annotations.Nullable String s) {
        Log.e(TAG, "cameraError: called");
    }

    @Override
    public void canceledUnknown(@org.jetbrains.annotations.Nullable String s) {
        Log.e(TAG, "canceledUnknown: called");
    }

    @Override
    public void cardScanned(@org.jetbrains.annotations.Nullable String s, @NotNull CardScanActivityResult cardScanActivityResult) {
        Log.e(TAG, "cardScanned: called");
        String output = "";

        output = "Pan :: " + cardScanActivityResult.getPan() + "\n";
        output = output + "CardHolder Name :: " + cardScanActivityResult.getCardholderName() + "\n";
        output = output + "Card Type :: " + cardScanActivityResult.getNetworkName() + "\n";
        output = output + "CVV :: " + cardScanActivityResult.getCvc() + "\n";
        output = output + "Expiry Date :: " + cardScanActivityResult.getExpiryDay() + "\n";
        output = output + "Expiry Month :: " + cardScanActivityResult.getExpiryMonth() + "\n";
        output = output + "Expiry Day :: " + cardScanActivityResult.getExpiryDay() + "\n";
        output = output + "Expiry Year :: " + cardScanActivityResult.getExpiryYear() + "\n";
        output = output + "Error String ::  " + cardScanActivityResult.getErrorString();

        tvCardDetails.setText(output);
    }

    @Override
    public void enterManually(@org.jetbrains.annotations.Nullable String s) {
        Log.e(TAG, "enterManually: called");
    }

    @Override
    public void userCanceled(@org.jetbrains.annotations.Nullable String s) {
        Log.e(TAG, "userCanceled: called");
    }
*/

//android.os.FileUriExposedException: file:///storage/emulated/0/Screen/Fri%20Feb%2025%2020%3A22%3A33%20GMT%2B05%3A30%202022.jpg exposed beyond app through Intent.getData()

}

package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.adapter.recyclerview.TransactionListAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chat.UserSelectDialogFragment;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.commonChat.interfaces.UserGroupSelectionDismissListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.SeeListener;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.models.output.TransactionList2;
import com.stuffer.stuffers.models.output.TransactionListResponse;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MySwitchView;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;

public class TransactionListActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener, UserGroupSelectionDismissListener, ChatItemClickListener, SeeListener {
    private List<TransactionListResponse.Result> mListTransaction;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private static final String TAG = "TransactionListActivity";
    private RecyclerView rvTransactionList;
    private String mAccountNumber, mEncryptAccountNumber;
    List<CurrencyResult> result;
    MySwitchView swAccountNumber;

    ArrayList<TransactionList2> mListFinal;
    private static String USER_SELECT_TAG = "userselectdialog";
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private UserSelectDialogFragment userSelectDialogFragment;
    private String mShare = "";


    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private ArrayList<User> myUsers;
    private AlertDialog mDialog;
    private File mFileSSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        mainAPIInterface = ApiUtils.getAPIService();
        rvTransactionList = findViewById(R.id.rvTransactionList);
        swAccountNumber = findViewById(R.id.swAccountNumber);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(TransactionListActivity.this));
        mAccountNumber = getIntent().getStringExtra(AppoConstants.ACCOUNTNUMBER);
        mEncryptAccountNumber = getIntent().getStringExtra(AppoConstants.ENCRYPTACCOUNTNUMBER);
        setupActionBar();
        swAccountNumber.setText("Account Number :  " + mEncryptAccountNumber);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTransactionList();
            }
        }, 200);

        swAccountNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swAccountNumber.setText("A/C Number :  " + mAccountNumber);
                } else {
                    swAccountNumber.setText("A/C Number :  " + mEncryptAccountNumber);
                }
            }
        });
        ChatHelper mChatHelper = new ChatHelper(TransactionListActivity.this);
        myUsers = mChatHelper.getMyUsers();


    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(TransactionListActivity.this);
        dialog.setMessage(getString(R.string.info_get_curreny_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    readTranscationDetails();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //  Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    /*String id;
    String mobilenumber;
    String accountnumber;
    String transactiontype;
    String transactiondescription;
    String transactiondate;
    String transactionamount;
    String pendingbalance;
    String transactionstatus;
    String userid;
    String transactionid;
    String curencyid;
    String sendername;
    String sendermobile;
    String processingfees;
    String bankfees;
    String paymenttype;
    String areacode;
    String taxes;*/

    private void readTranscationDetails() {

        mListFinal = new ArrayList<>();
        for (int i = 0; i < mListTransaction.size(); i++) {
            TransactionList2 model = new TransactionList2();
            model.setId(String.valueOf(mListTransaction.get(i).getId()));
            model.setMobilenumber(mListTransaction.get(i).getMobilenumber());
            model.setAccountnumber(mListTransaction.get(i).getAccountnumber());
            model.setTransactiontype(mListTransaction.get(i).getTransactiontype());

            model.setTransactiondescription(mListTransaction.get(i).getTransactiondescription());
            model.setTransactiondate(mListTransaction.get(i).getTransactiondate());//need change here
            model.setViewdate(getTimeDateOther(mListTransaction.get(i).getTransactiondate()));
            String balance = String.valueOf(mListTransaction.get(i).getTransactionamount());
            DecimalFormat df2 = new DecimalFormat("#.00");
            Double doubleV = Double.parseDouble(balance);
            String format = df2.format(doubleV);
            model.setTransactionamount(format);
            model.setPendingbalance(String.valueOf(mListTransaction.get(i).getPendingbalance()));
            model.setTransactionstatus(mListTransaction.get(i).getTransactionstatus());
            model.setUserid(String.valueOf(mListTransaction.get(i).getUserid()));
            model.setTransactionid(mListTransaction.get(i).getTransactionid());
            model.setCurencyid(String.valueOf(mListTransaction.get(i).getCurencyid()));
            model.setCurrencycode(getCurrency(String.valueOf(mListTransaction.get(i).getCurencyid())));
            model.setSendername(mListTransaction.get(i).getSendername());
            model.setSendermobile(mListTransaction.get(i).getSendermobile());
            model.setProcessingfees(String.valueOf(mListTransaction.get(i).getProcessingfees()));
            model.setBankfees(String.valueOf(mListTransaction.get(i).getBankfees()));
            model.setPaymenttype(String.valueOf(mListTransaction.get(i).getPaymenttype()));
            model.setAreacode(String.valueOf(mListTransaction.get(i).getAreacode()));
            model.setTaxes(String.valueOf(mListTransaction.get(i).getTaxes()));
            if (mListTransaction.get(i).getReceiverName() == null) {
                model.setReceiverName("");
            } else {
                model.setReceiverName(String.valueOf(mListTransaction.get(i).getReceiverName()));
            }

            if (mListTransaction.get(i).getReceiverCurrencyCode() == null) {
                model.setReceiverCurrencyCode("");
            } else {
                model.setReceiverCurrencyCode(String.valueOf(mListTransaction.get(i).getReceiverCurrencyCode()));
            }


            mListFinal.add(model);
        }

        TransactionListAdapter adapter = new TransactionListAdapter(TransactionListActivity.this, mListFinal);
        rvTransactionList.setAdapter(adapter);
    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < result.size(); i++) {
            String sid = result.get(i).getId().toString();
            if (sid.equals(param)) {
                res = result.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }

    private void getTransactionList() {
        dialog = new ProgressDialog(TransactionListActivity.this);
        dialog.setMessage(getString(R.string.info_get_transaction));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        JsonObject paramSent = new JsonObject();
        paramSent.addProperty(AppoConstants.ACCOUNTNUMBER, mAccountNumber);
        //  Log.e(TAG, "getTransactionList: " + paramSent.toString());
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.postUserTransactionList(paramSent, bearer_).enqueue(new Callback<TransactionListResponse>() {
            @Override
            public void onResponse(Call<TransactionListResponse> call, Response<TransactionListResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        //  Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        mListTransaction = response.body().getResult();
                        getCurrencyCode();
                    } else {
                        //  Log.e(TAG, "onResponse: else called");
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(TransactionListActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(TransactionListActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(TransactionListActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 405) {
                        Helper.showErrorMessage(TransactionListActivity.this, getString(R.string.error_method_not_alloed));
                    } else if (response.code() == 400) {
                        Helper.showErrorMessage(TransactionListActivity.this, getString(R.string.error_bad_request));
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionListResponse> call, Throwable t) {
                dialog.dismiss();
                //  Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private String getTimeDateOther(String dateParam) {
        // milliseconds
        long milliSec = Long.parseLong(dateParam);
        //DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(milliSec);


        //Log.e(TAG, "getTimeDateOther: " + simple.format(result));

        return simple.format(result);
    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_transaction_list));

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

    @Override
    public void onRowItemClick(int pos) {
        JSONObject objSent = new JSONObject();
        try {
            objSent.put("id", mListFinal.get(pos).getId());
            objSent.put("mobilenumber", mListFinal.get(pos).getMobilenumber());
            objSent.put("accountnumber", mListFinal.get(pos).getAccountnumber());
            objSent.put("transactiontype", mListFinal.get(pos).getTransactiontype());
            objSent.put("transactiondescription", mListFinal.get(pos).getTransactiondescription());
            objSent.put("transactiondate", mListFinal.get(pos).getTransactiondate());
            objSent.put("viewdate", mListFinal.get(pos).getViewdate());
            objSent.put("transactionamount", mListFinal.get(pos).getTransactionamount());
            objSent.put("pendingbalance", mListFinal.get(pos).getPendingbalance());
            objSent.put("transactionstatus", mListFinal.get(pos).getTransactionstatus());
            objSent.put("userid", mListFinal.get(pos).getUserid());
            objSent.put("transactionid", mListFinal.get(pos).getTransactionid());
            objSent.put("curencyid", mListFinal.get(pos).getCurencyid());
            objSent.put("currencycode", mListFinal.get(pos).getCurrencycode());
            objSent.put("sendername", mListFinal.get(pos).getSendername());

            objSent.put("sendermobile", mListFinal.get(pos).getSendermobile());
            objSent.put("processingfees", mListFinal.get(pos).getProcessingfees());
            objSent.put("bankfees", mListFinal.get(pos).getBankfees());
            objSent.put("paymenttype", mListFinal.get(pos).getPaymenttype());
            objSent.put("areacode", mListFinal.get(pos).getAreacode());
            objSent.put("taxes", mListFinal.get(pos).getTaxes());
            objSent.put("mAccountNumber", mAccountNumber);
            objSent.put("mEncryptAccountNumber", mEncryptAccountNumber);
            //Log.e(TAG, "onRowItemClick: " + objSent);

            Intent intentDetails = new Intent(TransactionListActivity.this, TransactionDetailsActivity.class);
            intentDetails.putExtra(AppoConstants.INFO, objSent.toString());
            startActivity(intentDetails);
            //share();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void share() {
        //mShare = data.getStringExtra("link");
        //Log.e(TAG, "onActivityResult: " + mShare);
        mShare = mFileSSort.getAbsolutePath();

        userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        userSelectDialogFragment.show(manager, USER_SELECT_TAG);
    }

    @Override
    public void onUserGroupSelectDialogDismiss(ArrayList<User> selectedUsers) {
        messageForwardList.clear();

    }

    @Override
    public void selectionDismissed() {

    }

    @Override
    public void onChatItemClick(Chat chat, int position, View userImage) {
        openChat(ChatActivity.newIntent(TransactionListActivity.this, messageForwardList, chat, mShare), userImage);
    }

    @Override
    public void placeCall(boolean callIsVideo, User user) {

    }

    private void openChat(Intent intent, View userImage) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(TransactionListActivity.this, userImage, "userImage");
        startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, activityOptionsCompat.toBundle());
        if (userSelectDialogFragment != null) {
            userSelectDialogFragment.dismiss();
            messageForwardList.clear();
            //mShare = "";
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_CHAT_FORWARD):
                if (resultCode == Activity.RESULT_OK) {
                    //show forward dialog to choose users
                    messageForwardList.clear();
                    ArrayList<Message> temp = data.getParcelableArrayListExtra("FORWARD_LIST");
                    String s = new Gson().toJson(temp);
                    //Log.e(TAG, "onActivityResult: "+s );
                    messageForwardList.addAll(temp);
                    userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    userSelectDialogFragment.show(manager, USER_SELECT_TAG);
                }
                break;
        }
    }

    @Override
    public void onSeeRequest(int pos) {
        Log.e(TAG, "onSeeRequest: called");
        TransactionList2 transactionList2 = mListFinal.get(pos);
        showPayDialogLikeUnion(transactionList2);


    }

    private void showPayDialogLikeUnion(TransactionList2 mResponse) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransactionListActivity.this);
        View mCustomLayout = LayoutInflater.from(TransactionListActivity.this).inflate(R.layout.success_dialog_inner_appopay_transfer, null);
        LinearLayout layoutRoot = mCustomLayout.findViewById(R.id.layoutRoot);
        MyTextView tvInfo = mCustomLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = mCustomLayout.findViewById(R.id.tvHeader);
        MyTextViewBold tvAmountPay = mCustomLayout.findViewById(R.id.tvAmountPay);
        MyTextViewBold tvCost = mCustomLayout.findViewById(R.id.tvCost);
        MyTextViewBold tvReceiverAmt = mCustomLayout.findViewById(R.id.tvReceiverAmt);
        MyTextView tvCurrencyPay = mCustomLayout.findViewById(R.id.tvCurrencyPay);
        MyTextView tvTransactionTime = mCustomLayout.findViewById(R.id.tvTransactionTime);
        MyTextView tvVoucherPay = mCustomLayout.findViewById(R.id.tvVoucherPay);
        MyButton btnShare = mCustomLayout.findViewById(R.id.btnShare);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnClose);
        tvHeader.setText(mResponse.getPaymenttype().toUpperCase());
        float mTransactionAmt = Float.parseFloat(mResponse.getTransactionamount());
        Float mProcessing = Float.valueOf(mResponse.getProcessingfees());
        Float mTaxes = Float.valueOf(mResponse.getTaxes());
        float mCreditAmt = mTransactionAmt - (mProcessing + mTaxes);
        if (mResponse.getReceiverCurrencyCode().isEmpty()) {
            tvReceiverAmt.setText("Receiver Amount : " + mCreditAmt);
        } else {
            tvReceiverAmt.setText("Receiver Amount : " + mCreditAmt + " " + mResponse.getCurrencycode());
        }


        tvReceiverAmt.setTextColor(Color.parseColor("#334CFF"));
        //float cost = amountaftertax_fees - Float.parseFloat(edAmount.getText().toString().trim());
        //float twoDecimal = Helper.getTwoDecimal(cost);

        String param1 = "Processing Fees : " + Helper.getTwoDecimal(mProcessing) + "\n" + " Taxes : " + Helper.getTwoDecimal(mTaxes) + "\n" + "Transaction Cost : " + Helper.getTwoDecimal(mProcessing + mTaxes);
        tvCost.setText(param1);
        tvCost.setTextColor(Color.parseColor("#FE3156"));


        tvAmountPay.setText(" Amount : " + mTransactionAmt + " " + mResponse.getCurrencycode());
        tvAmountPay.setTextSize(18);
        tvCurrencyPay.setText("Currency : " + mResponse.getCurrencycode());
        tvTransactionTime.setText("Transaction Time : " + mResponse.getViewdate());
        tvVoucherPay.setText("Transaction No : " + mResponse.getTransactionid());
        //tvInfo.setText("Transfer to " + recname + "" + "\nSUCCESS");

        if (mResponse.getReceiverName().isEmpty()) {
            tvInfo.setText(mResponse.getTransactiondescription());
        } else {
            tvInfo.setText("Transfer To " + mResponse.getReceiverName() + "\n" + mResponse.getTransactiondescription());
        }

        tvInfo.setTextSize(14);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShort(layoutRoot);
            }
        });
        mBuilder.setView(mCustomLayout);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    private void takeScreenShort(LinearLayout rootLayout) {
        mDialog.dismiss();

        Bitmap bitmap = getScreenShot(rootLayout);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "/YourDirName";
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YourDirName";
        }

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        String uniqueFileName = Helper.getUniqueFileName();
        mFileSSort = new File(dir, uniqueFileName);

        try {
            boolean newFile = mFileSSort.createNewFile();
            if (newFile) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFileSSort);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    //openScreenshot(mFileSSort);
                    share();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

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
}

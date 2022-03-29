package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.adapter.address.AutoCompleteAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.CountrySelectListener;
import com.stuffer.stuffers.communicator.StateSelectListener;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.fragments.dialog.StateDialogFragment;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.Country.State;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MySwitchView;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class UpdateProfileActivity extends AppCompatActivity implements CountrySelectListener, StateSelectListener {
    private static final String TAG = "UpdateProfileActivity";
    private static final int REQUEST_CHECK_SETTINGS = 9999;
    private boolean LOCATION_UPDATE_REQUEST = false;

    MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    List<Result> mListCountry;
    private List<State> mListState;
    private JSONObject mIndex;
    MyEditText edtUserName, edtEmail, edt_mobile_number, edtCityName;
    MyEditText edtTransactionPin, edtScreenlockPin, edtAddress;
    MyTextView tvInfoUpdateProfile, tvHintsScreenLock, tvHintsTransaction;
    MyEditText edtDob;
    Calendar newCalendar;
    TextInputLayout tilDob;
    MyTextView updateProfile;
    private String mDob;
    private MyTextView edCountry, edState;
    List<CountryCodeResponse> mListMain;
    private int mCountryId;
    private int countryPos;
    private int mStateId;
    MyEditText edFocus;
    MySwitchView swPickLocation;

    private ProgressDialog progress;
    private MyEditText edtZipCode;
    private AutoCompleteTextView placesAutocomplete;
    AutoCompleteAdapter adapter;
    private PlacesClient placesClient;
    private ImageView ivSelf;
    private File photoFile;
    private ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        /**
         * important below async to mainThread work
         */
        //Only the original thread that created a view hierarchy can touch its views.
        initViews();
        edFocus = findViewById(R.id.edFocus);

        mainAPIInterface = ApiUtils.getAPIService();
        newCalendar = Calendar.getInstance();
        setupActionBar();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                mIndex = new JSONObject(vaultValue);
                Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                edtUserName.setText(result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                edt_mobile_number.setText(result.getString(AppoConstants.MOBILENUMBER));
                edtEmail.setText(result.getString(AppoConstants.EMIAL));
                if (!result.getString("transactionpin").equalsIgnoreCase("null")) {
                    Log.e(TAG, "onCreate: called");
                    String dob = Helper.getDob();
                    String cityName = Helper.getCityName();
                    String address = Helper.getAddress();
                    String transactionPin = Helper.getTransactionPin();
                    mDob = dob;
                    edtDob.setText("Dob : " + dob);
                    edtCityName.setText(cityName);
                    edtTransactionPin.setText(transactionPin);
                    //edtAddress.setText(address);
                    placesAutocomplete.setText(address);
                    placesAutocomplete.setAdapter(null);
                    placesAutocomplete.setEnabled(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String infoScreenLock = "<font color='#00baf2'>" + getString(R.string.screen_note) + "</font>" + " : " + getString(R.string.screen_info);
        tvHintsScreenLock.setText(Html.fromHtml(infoScreenLock));

        String infoTransaction = "<font color='#00baf2'>" + getString(R.string.trans_note) + "</font>" + " : " + getString(R.string.trans_info);
        tvHintsTransaction.setText(Html.fromHtml(infoTransaction));

        String infoUpdate = "<font color='#00baf2'>" + getString(R.string.update_note) + "</font>" + " : " + getString(R.string.update_note_info);
        tvInfoUpdateProfile.setText(Html.fromHtml(infoUpdate));

        getCountry();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDetails();
            }
        });

        edCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryDialogFragment countryDialogFragment = new CountryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListCountry);
                countryDialogFragment.setArguments(bundle);
                countryDialogFragment.setCancelable(false);
                countryDialogFragment.show(getSupportFragmentManager(), countryDialogFragment.getTag());
            }
        });

        edState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateDialogFragment stateDialogFragment = new StateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.STATE, (ArrayList<? extends Parcelable>) mListState);
                stateDialogFragment.setArguments(bundle);
                stateDialogFragment.setCancelable(false);
                stateDialogFragment.show(getSupportFragmentManager(), stateDialogFragment.getTag());
            }
        });

    }

    private void initViews() {
        ivSelf = (ImageView) findViewById(R.id.ivSelf);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfile);
        edtZipCode = (MyEditText) findViewById(R.id.edtZipCode);
        placesAutocomplete = (AutoCompleteTextView) findViewById(R.id.placesAutocomplete);
        edFocus = findViewById(R.id.edFocus);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        edCountry = findViewById(R.id.edCountry);
        edState = findViewById(R.id.edState);
        edtDob = findViewById(R.id.edtDob);
        edtCityName = findViewById(R.id.edtCityName);
        edtTransactionPin = findViewById(R.id.edtTransactionPin);
        edtScreenlockPin = findViewById(R.id.edtScreenlockPin);
        edtAddress = findViewById(R.id.edtAddress);
        updateProfile = findViewById(R.id.updateProfile);
        edCountry = findViewById(R.id.edCountry);
        tvInfoUpdateProfile = findViewById(R.id.tvInfoUpdateProfile);
        tvHintsScreenLock = findViewById(R.id.tvHintsScreenLock);
        tvHintsTransaction = findViewById(R.id.tvHintsTransaction);
        ImageView btnClearAll = (ImageView) findViewById(R.id.btnClearAll);

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesAutocomplete.setText("");
                placesAutocomplete.setAdapter(adapter);
                placesAutocomplete.setEnabled(true);
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }

        placesClient = Places.createClient(this);
        initAutoCompleteTextView();

        ivSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = Uri.fromFile(getOutputPhotoFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE);*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkCameraPermissionTop();
                } else {
                    topRequest();
                }


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermissionTop() {
        if (ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //ContextCompat.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 987);

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 988);

        } else {
            topRequest();
        }
    }

    private void topRequest() {
        openCameraActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 988) {
            boolean readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            if (readPermission && writePermission && cameraPermission) {
                topRequest();
            } else {
                Toast.makeText(UpdateProfileActivity.this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCameraActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {

                photoFile = createImageFile();
                //displayMessage(getBaseContext(), photoFile.getAbsolutePath());
                //Log.i("Mayank",photoFile.getAbsolutePath());

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.stuffer.stuffers.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(takePictureIntent, Constants.CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                displayMessage(getBaseContext(), ex.getMessage().toString());
            }


        } else {
            displayMessage(getBaseContext(), "Nullll");
        }
    }

    private void displayMessage(Context baseContext, String toString) {
        Toast.makeText(baseContext, "" + toString, Toast.LENGTH_SHORT).show();
    }

    private File createImageFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Appopay";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        String uniqueFileName = Helper.getUniqueFileName();
        File mFile = new File(dir, uniqueFileName);
        return mFile;
    }

    private void initAutoCompleteTextView() {
        placesAutocomplete.setThreshold(1);
        placesAutocomplete.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        placesAutocomplete.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            Log.e(TAG, "onSuccess: " + task.getPlace().getAddress());
                            //responseView.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            String address = task.getPlace().getAddress();
                            //placesAutocomplete.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            //placesAutocomplete.setAdapter(null);
                            String name = task.getPlace().getName();
                            placesAutocomplete.setAdapter(null);
                            placesAutocomplete.setText(name + "," + address);
                            placesAutocomplete.setEnabled(false);

                            if (task.getPlace().getLatLng() != null) {
                                // Log.e(TAG, "onActivityResult: lat lng :: " + place.getLatLng());
                                // Location mLocation=new Location();
                                LatLng latLng = task.getPlace().getLatLng();
                                getGeoLocationAddress(latLng);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            //responseView.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private String getGeoLocationAddress(LatLng location) {
        //Geocoder geocoder = new Geocoder(UpdateProfileActivity.this, Locale.getDefault());
        Geocoder geocoder = new Geocoder(UpdateProfileActivity.this);
        List<Address> addresses = null;
        String resultMessage = null;
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null || addresses.size() == 0) {
            Log.e(TAG, "getGeoLocationAddress: " + " Address is NULL ");
        } else {
            String postalCode = addresses.get(0).getPostalCode();
            if (postalCode != null && !postalCode.isEmpty()) {
                edtZipCode.setText(postalCode);
                try {
                    //Helper.hideKeyboard(edtZipCode,UpdateProfileActivity.this);
                    closeKeyboard();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return resultMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Bundle extras = data.getExtras();
        //Bitmap imageBitmap = (Bitmap) extras.get("data");
        //imageView.setImageBitmap(imageBitmap);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            /*Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);*/
            File absoluteFile = photoFile.getAbsoluteFile();
            Glide.with(UpdateProfileActivity.this).load(photoFile.getAbsoluteFile()).into(ivProfilePic);
            //updateLayout(absoluteFile.getAbsolutePath());

        } else {
            displayMessage(UpdateProfileActivity.this, "Request cancelled or something went wrong.");
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void getCountry() {
        dialog = new ProgressDialog(UpdateProfileActivity.this);
        dialog.setMessage(getString(R.string.info_getting_country_code));
        dialog.show();
        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                dialog.dismiss();
//                //Log.e(TAG, "onResponse: " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        // mListMain = new ArrayList<>();
                        //mListMain=response.body().getResult();
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                        intiCountyCode();
                    }

                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                dialog.dismiss();
//                //Log.e("tag", t.getMessage().toString());
            }
        });
    }

    private void intiCountyCode() {
        int foundAt = 0;
        for (int i = 0; i < mListCountry.size(); i++) {
            if (mListCountry.get(i).getCountryname().equalsIgnoreCase("PANAMA")) {
                foundAt = i;
                break;
            }
        }

        Collections.swap(mListCountry, 0, foundAt);

        for (int i = 0; i < mListCountry.size(); i++) {
//            //Log.e(TAG, "intiCountyCode: " + mListCountry.get(i).getCountryname());
        }


        String country = " ( " + mListCountry.get(0).getCountrycode() + " )  " + mListCountry.get(0).getCountryname();
        edCountry.setText(country);
        String state = mListCountry.get(0).getStates().get(0).getStatename();
        edState.setText(state);
        mListState = new ArrayList<>();
        mListState = mListCountry.get(0).getStates();
        mStateId = mListState.get(0).getId();
        mCountryId = mListCountry.get(0).getId();

        initCountryNotInPanama();

    }

    public void initCountryNotInPanama() {
        String transactionPin = Helper.getTransactionPin();
        if (transactionPin != null && !transactionPin.isEmpty()) {
            String countryId = Helper.getCountryId();
            if (!countryId.equalsIgnoreCase("1")) {
                String stateId = Helper.getStateId();
                int fountAt = 0;
                for (int i = 0; i < mListCountry.size(); i++) {
                    if (mListCountry.get(i).getId() == Integer.parseInt(countryId)) {
                        mCountryId = mListCountry.get(i).getId();
                        fountAt = i;
                        String country = " ( " + mListCountry.get(i).getCountrycode() + " )  " + mListCountry.get(i).getCountryname();
                        edCountry.setText(country);
                        break;
                    }
                }
                mListState = mListCountry.get(fountAt).getStates();
                for (int i = 0; i < mListState.size(); i++) {
                    if (mListState.get(i).getId() == Integer.parseInt(stateId)) {
                        mStateId = mListState.get(i).getId();
                        edState.setText(mListState.get(i).getStatename());
                        break;
                    }
                }
            }
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(R.string.toolbar_title_update_profile);

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
    public void onCountrySelected(String countryName, String countryId, int countryCode, int pos) {
        mCountryId = countryCode;
        countryPos = 0;
        String country = " ( " + countryId + " )  " + countryName;
        edCountry.setText(country);
        for (int i = 0; i < mListCountry.size(); i++) {
            if (countryName.toLowerCase().contains(mListCountry.get(i).getCountryname().toLowerCase())) {
                countryPos = i;
                break;
            }
        }
        mListState = new ArrayList<>();
        mListState = mListCountry.get(countryPos).getStates();
        String state = mListState.get(0).getStatename();
        edState.setText(state);
        mStateId = mListState.get(0).getId();
    }


    @Override
    public void onStateSelected(String statename, int stateid) {
        mStateId = stateid;
        edState.setText(statename);
    }

    public void getDateOfBirth(View view) {
        Calendar minCal = Calendar.getInstance();
        minCal.set(1950, 0, 1);
        DatePickerDialog StartTime = new DatePickerDialog(UpdateProfileActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");
                mDob = dateFormatter.format(newDate.getTime());
                edtDob.setText("Dob : " + dateFormatter.format(newDate.getTime()));
                edFocus.setVisibility(View.GONE);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        StartTime.getDatePicker().setMaxDate(new Date().getTime());
        StartTime.getDatePicker().setMinDate(minCal.getTimeInMillis());
        StartTime.setCanceledOnTouchOutside(false);
        StartTime.show();


    }

    private void verifyDetails() {
        if (edtDob.getText().toString().trim().isEmpty()) {
            edFocus.setError(getString(R.string.info_select_dob));
            edFocus.requestFocus();
            return;
        }
        if (edtCityName.getText().toString().trim().isEmpty()) {
            edtCityName.setError(getString(R.string.info_your_city_name));
            edtCityName.requestFocus();
            edtCityName.setFocusable(true);
            return;
        }

        if (edtTransactionPin.getText().toString().trim().isEmpty()) {
            edtTransactionPin.setError(getString(R.string.info_create_your_transaction_pin));
            edtTransactionPin.requestFocus();
            edtTransactionPin.setFocusable(true);
            return;
        }
        if (edtTransactionPin.getText().toString().trim().length() < 6) {
            edtTransactionPin.setError(getString(R.string.info_transaction_pin_length));
            edtTransactionPin.requestFocus();
            edtTransactionPin.setFocusable(true);
            return;
        }
        if (edtScreenlockPin.getText().toString().trim().isEmpty()) {
            edtScreenlockPin.setError(getString(R.string.info_create_your_screen_lock_pin));
            edtScreenlockPin.requestFocus();
            edtScreenlockPin.setFocusable(true);
            return;
        }
        if (edtScreenlockPin.getText().toString().length() < 4) {
            edtScreenlockPin.setError(getString(R.string.info_screen_pin_length));
            edtScreenlockPin.requestFocus();
            edtScreenlockPin.setFocusable(true);
            return;
        }
        /*if (edtAddress.getText().toString().trim().isEmpty()) {
            edtAddress.setError(getString(R.string.info_your_address));
            edtAddress.requestFocus();
            edtAddress.setFocusable(true);
            return;
        }*/
        if (placesAutocomplete.getText().toString().trim().isEmpty()) {
            placesAutocomplete.setError(getString(R.string.info_your_address));
            placesAutocomplete.requestFocus();
            placesAutocomplete.setFocusable(true);
            return;
        }
        if (edtZipCode.getText().toString().trim().isEmpty()) {
            edtZipCode.setError("please enter zip code");
            edtZipCode.requestFocus();
            edtZipCode.setFocusable(true);
            return;
        }

        if (edtZipCode.getText().toString().trim().length() < 3) {
            edtZipCode.setError("please enter valid zip code");
            edtZipCode.requestFocus();
            edtZipCode.setFocusable(true);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(AppoConstants.USERNAME, edtUserName.getText().toString().trim());
        intent.putExtra(AppoConstants.EMIAL, edtEmail.getText().toString().trim());
        intent.putExtra(AppoConstants.MOBILENUMBER, edt_mobile_number.getText().toString().trim());
        intent.putExtra(AppoConstants.DOB, mDob);
        intent.putExtra(AppoConstants.COUNTRYID, mCountryId);
        intent.putExtra(AppoConstants.STATEID, mStateId);
        intent.putExtra(AppoConstants.TRANSACTIONPIN, edtTransactionPin.getText().toString().trim());
        intent.putExtra(AppoConstants.SCREENLOCKPIN, edtScreenlockPin.getText().toString().trim());
        intent.putExtra(AppoConstants.CITYNAME, edtCityName.getText().toString().trim());
        intent.putExtra(AppoConstants.ADDRESS, placesAutocomplete.getText().toString().trim());
        intent.putExtra(AppoConstants.ZIPCODE2, edtZipCode.getText().toString().trim());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    private void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(this);
        }
        progress.setMessage(getString(R.string.info_please_wait));
        progress.setCancelable(false);
        progress.show();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}

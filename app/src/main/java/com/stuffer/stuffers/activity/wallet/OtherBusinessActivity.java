package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatUtils.Constant;
import com.stuffer.stuffers.commonChat.chatUtils.PermissionUtils;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.commonChat.chatUtils.Utils;
import com.stuffer.stuffers.utils.DataManager;
import com.stuffer.stuffers.utils.MerchantInfoBean;
import com.stuffer.stuffers.utils.ScheduleTimeBean;
import com.stuffer.stuffers.utils.SellerHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OtherBusinessActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private boolean isToast = true;
    private MerchantInfoBean otherBean;
    private ImageView otherAvator;
    private TextView otherName;
    private TextView otherDesc;
    private TextView otherType;
    private TextView otherAddress;
    private TextView otherEmail;
    private TextView otherWebSite;
    private TextView otherPhone;
    private TextView otherPhone_;

    private ViewGroup categoryContainer;

    private ViewGroup productContainer;
    private ViewGroup productContainer1;
    private ViewGroup productContainer2;

    private ViewGroup scheduleTimeContainer;
    private TextView scheduleTime;
    private TextView scheduleTimeNo;

    private View findConstactListLine;
    private View findConstactList;

    private TextView scheduleStatus;

    ViewGroup statusContainer;

    private LocationManager locationManager;
    private double curLatitude = 0;
    private double curLongitude = 0;

    private SellerHelper sellerHelper = SellerHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_business);

        otherAvator = findViewById(R.id.other_business_info_avator);
        scheduleStatus = findViewById(R.id.other_business_current_status);
        scheduleTimeNo = findViewById(R.id.other_business_info_time_no);
        otherPhone = findViewById(R.id.other_business_info_phone);
        otherPhone_ = findViewById(R.id.other_business_info_phone_);
        otherName = findViewById(R.id.other_business_info_name);
        otherDesc = findViewById(R.id.other_business_info_desc);
        otherType = findViewById(R.id.other_business_info_type);
        otherAddress = findViewById(R.id.other_business_info_address);
        otherEmail = findViewById(R.id.other_business_info_email);
        otherWebSite = findViewById(R.id.other_business_info_website);
        productContainer = findViewById(R.id.business_info_detail_container);
        productContainer1 = findViewById(R.id.business_info_product_container1);
        productContainer2 = findViewById(R.id.business_info_product_container2);
        scheduleTimeContainer = findViewById(R.id.other_business_info_time_container);
        scheduleTime = findViewById(R.id.other_business_info_time);
        findConstactListLine = findViewById(R.id.find_constact_list_line);
        findConstactList = findViewById(R.id.find_constact_list);
        categoryContainer = findViewById(R.id.other_business_category_container);
        statusContainer = findViewById(R.id.business_info_status_container);
        findViewById(R.id.other_business_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherBusinessActivity.this.finish();
            }
        });
        if (isExistConstact()) {
            findConstactListLine.setVisibility(View.GONE);
            findConstactList.setVisibility(View.GONE);
        }
        productContainer.setOnClickListener(this);
        categoryContainer.setOnClickListener(this);
        statusContainer.setOnClickListener(this);
        findViewById(R.id.other_business_call_container).setOnClickListener(this);
        findViewById(R.id.business_info_address_container).setOnClickListener(this);
        findViewById(R.id.find_constact_list).setOnClickListener(this);
        otherBean = (MerchantInfoBean) getIntent().getSerializableExtra("otherMerchant");
        DataManager.merchantInfoBean = otherBean;
        fillMerchantInfoBean();
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            openGPSSettings();
        }
    }

    private void findOtherMenuBookView() {
        Intent intent001 = new Intent();
        intent001.setClassName("com.android.contacts", "com.android.contacts.activities.PeopleActivity");
        startActivity(intent001);
    }

    private void fillMerchantInfoBean() {
        if (otherBean == null) {
            return;
        }
        if (!TextUtils.isEmpty(otherBean.avatar)) {
            Glide.with(OtherBusinessActivity.this).load(otherBean.avatar).circleCrop()
                    .placeholder(R.drawable.avator_profile).error(R.drawable.avator_profile).into(otherAvator);
        }
        if (!TextUtils.isEmpty(otherBean.businessName)) {
            otherName.setText(otherBean.businessName);
        } else {
            otherName.setText("No Information");
        }
        if (!TextUtils.isEmpty(otherBean.mobileNumber)) {
            otherPhone.setText("+" + otherBean.mobileNumber);
            otherPhone_.setText("+" + otherBean.mobileNumber);
        } else {
            otherPhone.setText("No Information");
            otherPhone_.setText("No Information");
        }
        if (!TextUtils.isEmpty(otherBean.description)) {
            otherDesc.setText(otherBean.description);
        } else {
            otherDesc.setText("No Information");
        }
        if (!TextUtils.isEmpty(otherBean.merchantAccount)) {
            statusContainer.setVisibility(View.VISIBLE);
        } else {
            statusContainer.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(otherBean.businessType)) {
            otherType.setText(otherBean.businessType + ", " + otherBean.subBusinessType);
        }
        if (!TextUtils.isEmpty(otherBean.businessAddress)) {
            String[] splitAdd = otherBean.businessAddress.split("@#");
            if (splitAdd.length > 0) {
                otherAddress.setText(splitAdd[0]);
            }
            if (splitAdd.length > 1) {
                otherBean.address = splitAdd[1];
            }
        } else {
            otherAddress.setEnabled(false);
            otherAddress.setText("No Location");
        }
        if (!TextUtils.isEmpty(otherBean.email)) {
            otherEmail.setText(otherBean.email);
        } else {
            otherEmail.setText("No Information");
        }
        if (!TextUtils.isEmpty(otherBean.website)) {
            otherWebSite.setText(otherBean.website);
        } else {
            otherWebSite.setText("No Information");
        }
        if (otherBean.categories != null) {
            int count = Math.min(otherBean.categories.size(), 3);
            for (int i = 0; i < count; i++) {
                final int index = i;
                if (otherBean.categories.get(i) == null ||
                        otherBean.categories.get(i).pictureList == null ||
                        otherBean.categories.get(i).pictureList.size() == 0) {
                    continue;
                }
                productContainer1.setVisibility(View.VISIBLE);
                RoundedCorners roundedCorners = new RoundedCorners(15);
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(OtherBusinessActivity.this).load(otherBean.categories.get(i).pictureList.get(0)).placeholder(R.drawable.picture_default).error(R.drawable.picture_default).apply(options).into(((ImageView) productContainer1.getChildAt(i)));
                productContainer1.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.isFastClick()) {
                            return;
                        }
                        DataManager.index = index;
                        Intent intent = new Intent(OtherBusinessActivity.this, ProductItemDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
            count = Math.min(otherBean.categories.size() - 3, 3);
            for (int i = 0; i < count; i++) {
                final int index = i;
                if (otherBean.categories.get(i + 3) == null ||
                        otherBean.categories.get(i + 3).pictureList == null ||
                        otherBean.categories.get(i + 3).pictureList.size() == 0) {
                    continue;
                }
                productContainer2.setVisibility(View.VISIBLE);
                RoundedCorners roundedCorners = new RoundedCorners(15);
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(OtherBusinessActivity.this).load(otherBean.categories.get(i + 3).pictureList.get(0)).placeholder(R.drawable.picture_default).error(R.drawable.picture_default).apply(options).into(((ImageView) productContainer2.getChildAt(i)));
                productContainer2.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.isFastClick()) {
                            return;
                        }
                        DataManager.index = index + 3;
                        Intent intent = new Intent(OtherBusinessActivity.this, ProductItemDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        if (otherBean.schedule != null) {
            fillScheduleTime(otherBean.schedule);
        } else {
            scheduleTimeNo.setVisibility(View.VISIBLE);
        }
    }

    private void fillScheduleTime(ScheduleTimeBean scheduleBean) {
        List<ScheduleTimeBean.ScheduleTimeInfo> list = scheduleBean.timeInfoList;
        fillScheduleTime(list);
    }

    private void fillScheduleTime(List<ScheduleTimeBean.ScheduleTimeInfo> list) {
        scheduleTimeContainer.removeAllViews();
        if (list.size() == 0) {
            scheduleTimeContainer.setVisibility(View.GONE);
            scheduleTime.setVisibility(View.VISIBLE);
            scheduleTimeNo.setVisibility(View.VISIBLE);
            return;
        }
        scheduleTimeNo.setVisibility(View.GONE);
        scheduleTimeContainer.setVisibility(View.VISIBLE);
        if (list.size() > 0) {
            String currentDate = getCurrentWeek();
            ScheduleTimeBean.ScheduleTimeInfo info = null;
            for (int i = 0; i < list.size(); i++) {
                info = list.get(i);
                if (currentDate.equals(info.dateName)) {
                    sellerHelper.showScheduleStatus(info, scheduleStatus);
                    break;
                }
            }
            if (info == null) {
                info = list.get(0);
            }
            View view = View.inflate(this, R.layout.include_schedule_time_info, null);
            TextView date = view.findViewById(R.id.schedule_time_info_date);
            TextView time1 = view.findViewById(R.id.schedule_time_info_time1);
            TextView time2 = view.findViewById(R.id.schedule_time_info_time2);
            ImageView indicator = view.findViewById(R.id.schedule_time_indicator);

            date.setText(info.dateName);
            time2.setVisibility(View.GONE);
            time1.setText(info.timerList.size() > 0 ? info.timerList.get(0) : "");
            if (info.timerList.size() > 1) {
                time2.setVisibility(View.VISIBLE);
                time2.setText(info.timerList.get(1));
            }
            indicator.setVisibility(View.VISIBLE);
            indicator.setOnClickListener(view1 -> expand(list));
            scheduleTimeContainer.addView(view);
        }
    }

    private void expand(List<ScheduleTimeBean.ScheduleTimeInfo> list) {
        scheduleTimeContainer.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ScheduleTimeBean.ScheduleTimeInfo info = list.get(i);
            View view = View.inflate(this, R.layout.include_schedule_time_info, null);
            TextView date = view.findViewById(R.id.schedule_time_info_date);
            TextView time1 = view.findViewById(R.id.schedule_time_info_time1);
            TextView time2 = view.findViewById(R.id.schedule_time_info_time2);
            ImageView indicator = view.findViewById(R.id.schedule_time_indicator);
            date.setText(info.dateName);
            time2.setVisibility(View.GONE);
            time1.setText(info.timerList.size() > 0 ? info.timerList.get(0) : "");
            if (info.timerList.size() > 1) {
                time2.setVisibility(View.VISIBLE);
                time2.setText(info.timerList.get(1));
            }
            if (i == 0) {
                indicator.setVisibility(View.VISIBLE);
                indicator.setImageResource(R.drawable.study_black_back1);
                indicator.setOnClickListener(view1 -> fillScheduleTime(list));
            }
            scheduleTimeContainer.addView(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.business_info_detail_container:
            case R.id.other_business_category_container:
                if (DataManager.merchantInfoBean == null || DataManager.merchantInfoBean.categories == null ||
                        DataManager.merchantInfoBean.categories.size() == 0) {
                    ToastUtil.showTextShort("Seller did not upload items");
                    return;
                }
                jumpBusinessInfoDetailPage();
                break;
            case R.id.other_business_call_container:
                if (!TextUtils.isEmpty(otherBean.mobileNumber)) {
                    //请求应用需要的所有权限
                    boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, Constant.PERMISSION_CODE_FIRST,
                            new String[]{Manifest.permission.CALL_PHONE});
                    if (checkPermissionFirst) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + otherBean.mobileNumber));
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.showTextLong("phone number is empty");
                }
                break;
            case R.id.business_info_address_container:
                try {
                    jumpGoogleMap();
                } catch (Exception e) {
                    ToastUtil.showTextShort("route planning failed");
                }
                break;
            case R.id.find_constact_list:
                boolean checkPermissionFirst = PermissionUtils.checkPermissionFirst(this, Constant.PERMISSION_CODE_FIRST,
                        new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS});
                if (checkPermissionFirst) {
                    jumpAddConstact();
                }
                break;
            case R.id.business_info_status_container:
                jumpStatusPage();
                break;
        }
    }

    private void jumpStatusPage() {
        Intent intent = new Intent(this, StatusActivity.class);
        startActivity(intent);
    }

    private void jumpAddConstact() {
        if (!TextUtils.isEmpty(DataManager.merchantInfoBean.businessName) && !TextUtils.isEmpty(DataManager.merchantInfoBean.mobileNumber)) {
            if (!isExistConstact()) {
                findConstactListLine.setVisibility(View.VISIBLE);
                findConstactList.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, DataManager.merchantInfoBean.businessName);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, DataManager.merchantInfoBean.mobileNumber);
                startActivity(intent);
            } else {
                findConstactListLine.setVisibility(View.GONE);
                findConstactList.setVisibility(View.GONE);
            }
        } else {
            findConstactListLine.setVisibility(View.GONE);
            findConstactList.setVisibility(View.GONE);
        }
    }

    private boolean isExistConstact() {
        try {
            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.PhoneLookup.DISPLAY_NAME + "=?", new String[]{DataManager.merchantInfoBean.businessName}, null);
            while (c.moveToNext()) {
                int nameFieldColumnIndex = c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String contact = c.getString(nameFieldColumnIndex);
                if (DataManager.merchantInfoBean.businessName.equals(contact)) {
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor pc = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null, null);
                    while (pc.moveToNext()) {
                        String phoneNumber = pc.getString(pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (DataManager.merchantInfoBean.mobileNumber.equals(phoneNumber)) {
                            return true;
                        }
                    }
                    pc.close();
                }
            }
            c.close();
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length <= 0) {
            return;
        }
        if (permissions[0].equals(Manifest.permission.WRITE_CONTACTS) || permissions[0].equals(Manifest.permission.READ_CONTACTS)) {
            boolean isPermissions = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isPermissions = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        if (isToast) {
                            ToastUtil.showTextLong(getResources().getString(R.string.enable_required_permissions));
                            isToast = false;
                        }
                    }
                }
            }
            isToast = true;
            if (isPermissions) {
                jumpAddConstact();
            }
        } else {
            boolean isPermissions = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isPermissions = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        if (isToast) {
                            ToastUtil.showTextLong(getResources().getString(R.string.enable_required_permissions));
                            isToast = false;
                        }
                    }
                }
            }
            isToast = true;
            if (isPermissions) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + otherBean.mobileNumber));
                startActivity(intent);
            }
        }

    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            getLocation();
            return;
        } else {
            Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, 100); // 此为设置完成后返回到获取界面
        }
    }

    private void jumpGoogleMap() {
        if (TextUtils.isEmpty(otherBean.address)) {
            ToastUtil.showTextShort("Seller did not upload the address");
            return;
        }
        String[] dest = otherBean.address.split(",");
        if (dest.length != 2) {
            ToastUtil.showTextShort("Seller did not upload the address");
            return;
        }
        if (TextUtils.isEmpty(DataManager.curLatLog)) {
            ToastUtil.showTextShort("The current location failed");
            return;
        }
        String[] src = DataManager.curLatLog.split(",");
        if (src.length != 2) {
            ToastUtil.showTextShort("Seller did not upload the address");
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="
                + src[0]
                + ","
                + src[1]
                + "&daddr="
                + dest[0]
                + ","
                + dest[1]));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(i);
    }

    private void jumpBusinessInfoDetailPage() {
        if (otherBean == null || otherBean.categories == null || otherBean.categories.size() == 0) {
            ToastUtil.showTextShort("This seller has no products");
            return;
        }
        Intent intent = new Intent(this, OtherProductActivity.class);
        startActivity(intent);
    }

    public String getCurrentWeek() {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        int day = instance.get(Calendar.DAY_OF_WEEK);
        int offDay = day;
        switch (offDay % 7) {
            case 0:
                return "Saturday";
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
        }
        return "";
    }

    public String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return dateFormat.format(date);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        curLatitude = location.getLatitude();
        curLongitude = location.getLongitude();
        DataManager.curLatLog = curLatitude + "," + curLongitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            // 得到第一个经纬度位置解析信息
//            Address address = addresses.get(0);
//            // 获取到详细的当前位置
//            // Address里面还有很多方法你们可以自行实现去尝试。比如具体省的名称、市的名称...
//            String info = address.getAddressLine(0) + // 获取国家名称
//                    address.getAddressLine(1) + // 获取省市县(区)
//                    address.getAddressLine(2);  // 获取镇号(地址名称)
        // 赋值
//            nowAddress.setText(info);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 移除位置管理器
        // 需要一直获取位置信息可以去掉这个
        locationManager.removeUpdates(this);
    }
}
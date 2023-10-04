package com.stuffer.stuffers.utils;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.ProductItemDetailActivity;
import com.stuffer.stuffers.activity.wallet.SubTabsActivity;
import com.stuffer.stuffers.commonChat.chatUtils.SharedPreferenceHelper;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.models.output.CustomArea;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class Helper {

    public static String mUserDetails = null;
    public static final String FILE_NAME_FORMAT = "dd_MM_yyyy_HH_mm_ss";
    public static final String AREA_CODE_JSON = "[\n" +
            "  {\n" +
            "    \"areacode\": \"57\",\n" +
            "    \"areacode_with_name\": \"(+57) COLUMBIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"506\",\n" +
            "    \"areacode_with_name\": \"(+506) COSTA RICA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"507\",\n" +
            "    \"areacode_with_name\": \"(+507) PANAMA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"52\",\n" +
            "    \"areacode_with_name\": \"(+52) MEXICO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"809\",\n" +
            "    \"areacode_with_name\": \"(+809) DOMINICAN REPUBLIC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1246\",\n" +
            "    \"areacode_with_name\": \"(+1246) BARBADOS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"592\",\n" +
            "    \"areacode_with_name\": \"(+592) GUYANA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1758\",\n" +
            "    \"areacode_with_name\": \"(+1758) SAINT LUCIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"505\",\n" +
            "    \"areacode_with_name\": \"(+505) NICARAGUA\"\n" +
            "  }\n" +
            "]";

    public static final String AREA_CODE_JSON1 = "[\n" +
            "  {\n" +
            "    \"areacode\": \"507\",\n" +
            "    \"areacode_with_name\": \"(+507) PANAMA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"57\",\n" +
            "    \"areacode_with_name\": \"(+57) COLOMBIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"506\",\n" +
            "    \"areacode_with_name\": \"(+506) COSTA RICA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"52\",\n" +
            "    \"areacode_with_name\": \"(+52) MEXICO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1809\",\n" +
            "    \"areacode_with_name\": \"(+1) DOMINICAN REPUBLIC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1246\",\n" +
            "    \"areacode_with_name\": \"(+1246) BARBADOS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"592\",\n" +
            "    \"areacode_with_name\": \"(+592) GUYANA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1758\",\n" +
            "    \"areacode_with_name\": \"(+1758) SAINT LUCIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"505\",\n" +
            "    \"areacode_with_name\": \"(+505) NICARAGUA\"\n" +
            "  }\n" +
            "]";

    public static final String AREA_CODE_GIFT_JSON = "[\n" +
            "  {\n" +
            "    \"areacode\": \"507\",\n" +
            "    \"areacode_with_name\": \"(+507) PANAMA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1\",\n" +
            "    \"areacode_with_name\": \"(+1) USA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"57\",\n" +
            "    \"areacode_with_name\": \"(+57) COLOMBIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"506\",\n" +
            "    \"areacode_with_name\": \"(+506) COSTA RICA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"52\",\n" +
            "    \"areacode_with_name\": \"(+52) MEXICO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"809\",\n" +
            "    \"areacode_with_name\": \"(+809) DOMINICAN REPUBLIC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1246\",\n" +
            "    \"areacode_with_name\": \"(+1246) BARBADOS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"592\",\n" +
            "    \"areacode_with_name\": \"(+592) GUYANA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1758\",\n" +
            "    \"areacode_with_name\": \"(+1758) SAINT LUCIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"505\",\n" +
            "    \"areacode_with_name\": \"(+505) NICARAGUA\"\n" +
            "  }\n" +
            "]";


    private static AlertDialog dialogCommon;

    public static final String AREA_CODE_JSON2 = "[\n" +
            "  {\n" +
            "    \"areacode\": \"507\",\n" +
            "    \"areacode_with_name\": \"(+507) PANAMA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1\",\n" +
            "    \"areacode_with_name\": \"(+1) United States of America\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"57\",\n" +
            "    \"areacode_with_name\": \"(+57) COLOMBIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"506\",\n" +
            "    \"areacode_with_name\": \"(+506) COSTA RICA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"52\",\n" +
            "    \"areacode_with_name\": \"(+52) MEXICO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"809\",\n" +
            "    \"areacode_with_name\": \"(+809) DOMINICAN REPUBLIC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1246\",\n" +
            "    \"areacode_with_name\": \"(+1246) BARBADOS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"592\",\n" +
            "    \"areacode_with_name\": \"(+592) GUYANA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"1758\",\n" +
            "    \"areacode_with_name\": \"(+1758) SAINT LUCIA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"areacode\": \"505\",\n" +
            "    \"areacode_with_name\": \"(+505) NICARAGUA\"\n" +
            "  }\n" +
            "]";
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Gson gson;

    public Helper(Context context) {
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        gson = new Gson();
    }

    /**
     * @param view
     * @param context for hide keyboard
     */
    public static void hideKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * @param params
     * @return get two decimal place of parameter
     */

    public static float getTwoDecimal(float params) {
        float roundedFloat = (float) ((float) Math.round(params * 100.0) / 100.0);
        DecimalFormat df = new DecimalFormat("#.00");
        String format = df.format(roundedFloat);
        //String ret=String.format("%.2f", roundedFloat);
        //Log.e(TAG, "getTwoDecimal: ::: " + roundedFloat);
        return Float.parseFloat(format);

    }

    public static float getTwoDecimal1(float params) {
        float roundedFloat = (float) ((float) Math.round(params * 100.0) / 100.0);
        DecimalFormat df = new DecimalFormat("#.00");
        String format = df.format(roundedFloat);
        //String ret=String.format("%.2f", roundedFloat);
        //Log.e(TAG, "getTwoDecimal: ::: " + roundedFloat);
        return Float.parseFloat(format);

    }

    public static float getTwoDecimalTransfer(float params) {
        float roundedFloat = (float) ((float) Math.round(params * 100.0) / 100.0);
        //Log.e(TAG, "getTwoDecimal: ::: " + roundedFloat);
        return roundedFloat;
    }

    public static void showCommonErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_common_error, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvHeader.setText(title);
        tvInfo.setText(message);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCommon.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogCommon = builder.create();

        dialogCommon.setCanceledOnTouchOutside(true);

        dialogCommon.show();
    }

    public static boolean isPhonePermissionGranted(Context context) {
        boolean phonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        return phonePermission;
    }

    /**
     * @param context
     * @param message for show  error message
     */
    public static void showErrorMessage(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
    }


    /**
     * @param dateString
     * @return
     */

    public static String getTimeDate(String dateString) {
        String TAG = "MyTag";
        //  DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String ourDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS");
            TimeZone utc = TimeZone.getTimeZone("UTC");
            formatter.setTimeZone(utc);
            Date value = formatter.parse(dateString);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd,yyyy,HH:mm:ss aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());

            ourDate = dateFormatter.format(value);
            //Log.e(TAG, "getTimeDate: :::: " + ourDate);
            return ourDate;
            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, "getTimeDate: exception called");
        }
        return ourDate;
    }


    /**
     * @param userResponse
     * @return userid
     */
    public static int getUserId(String userResponse) {
        try {
            JSONObject index = new JSONObject(userResponse);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            return Integer.parseInt(userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static final String TEMP_USER_DATA = "{\n" +
            "        \"id\": 82,\n" +
            "        \"firstName\": \"MD\",\n" +
            "        \"lastName\": \"WASIM\",\n" +
            "        \"username\": \"1000159\",\n" +
            "        \"password\": \"$2a$10$pj6sGFHAf2bv1x.e/xlYhuI.9RGsURrjxIKT689Vvi429/0LIzqnm\",\n" +
            "        \"email\": \"mdwasim508@gmail.com\",\n" +
            "        \"accountexpired\": false,\n" +
            "        \"credentialsexpired\": false,\n" +
            "        \"accountlocked\": false,\n" +
            "        \"enabled\": true,\n" +
            "        \"mobilenumber\": 9836683269,\n" +
            "        \"transactionpin\": 1234,\n" +
            "        \"role\": [\n" +
            "          \"USER\"\n" +
            "          ],\n" +
            "        \"customerdetails\": {\n" +
            "            \"id\": 66,\n" +
            "            \"firstName\": \"MD\",\n" +
            "            \"lastName\": \"WASIM\",\n" +
            "            \"middlename\": null,\n" +
            "            \"cardtoken\": null,\n" +
            "            \"countryid\": 1,\n" +
            "            \"stateid\": 1,\n" +
            "            \"address\": null,\n" +
            "            \"cityname\": null,\n" +
            "            \"dob\": null,\n" +
            "            \"currencyid\": 1,\n" +
            "            \"bankaccount\": null,\n" +
            "            \"imageurl\": null,\n" +
            "            \"bankusername\": null,\n" +
            "            \"merchantqrcode\": null,\n" +
            "            \"isdeal\": null,\n" +
            "            \"currencysymbol\": \"USD\",\n" +
            "            \"customeraccounts\": [\n" +
            "                {\n" +
            "                    \"id\": 66,\n" +
            "                    \"accountnumber\": \"CS1000000000061\",\n" +
            "                    \"currentbalance\": 1,\n" +
            "                    \"accountstatus\": \"ACTIVE\",\n" +
            "                    \"reserveamount\": 0,\n" +
            "                    \"currencyid\": 1\n" +
            "                }\n" +
            "            ],\n" +
            "            \"idCuenta\": null,\n" +
            "            \"idAsociado\": null,\n" +
            "            \"idPlastico\": null\n" +
            "        },\n" +
            "        \"phonecode\": 91,\n" +
            "        \"securityanswer\": \"cash send\",\n" +
            "        \"usertype\": null,\n" +
            "        \"storename\": null,\n" +
            "        \"latitude\": 0,\n" +
            "        \"longitude\": 0,\n" +
            "        \"screenlockpin\": 1234\n" +
            "    }";

    public static String getEndTrim(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() >= 8 ? phoneNumber.substring(phoneNumber.length() - 7) : phoneNumber;
    }

    //{
    public static final String Country = "{\n" +
            "  \"status\": 200,\n" +
            "  \"message\": \"success\",\n" +
            "  \"result\": [\n" +
            "    {\n" +
            "      \"id\": 13,\n" +
            "      \"countrycode\": \"AI\",\n" +
            "      \"countryname\": \"ANGUILLA\",\n" +
            "      \"areacode\": \"1264\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 170,\n" +
            "          \"statecode\": \"Blow\",\n" +
            "          \"statename\": \"Blowing Point\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 171,\n" +
            "          \"statecode\": \"Eas\",\n" +
            "          \"statename\": \"East End\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 172,\n" +
            "          \"statecode\": \"Geo\",\n" +
            "          \"statename\": \"George Hill\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 173,\n" +
            "          \"statecode\": \"Isl\",\n" +
            "          \"statename\": \"Island Harbour\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 174,\n" +
            "          \"statecode\": \"Nor\",\n" +
            "          \"statename\": \"North Hill\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 175,\n" +
            "          \"statecode\": \"Nor\",\n" +
            "          \"statename\": \"North Side\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 176,\n" +
            "          \"statecode\": \"San\",\n" +
            "          \"statename\": \"Sandy Ground\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 177,\n" +
            "          \"statecode\": \"San\",\n" +
            "          \"statename\": \"Sandy Hill\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 178,\n" +
            "          \"statecode\": \"Sou\",\n" +
            "          \"statename\": \"South Hill\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 179,\n" +
            "          \"statecode\": \"Sto\",\n" +
            "          \"statename\": \"Stoney Ground\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 180,\n" +
            "          \"statecode\": \"Far\",\n" +
            "          \"statename\": \"The Farrington\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 181,\n" +
            "          \"statecode\": \"Qua\",\n" +
            "          \"statename\": \"The Quarter\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 182,\n" +
            "          \"statecode\": \"Val\",\n" +
            "          \"statename\": \"The Valley\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 183,\n" +
            "          \"statecode\": \"Wes\",\n" +
            "          \"statename\": \"West End\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 9,\n" +
            "      \"countrycode\": \"AG\",\n" +
            "      \"countryname\": \"ANTIGUA\",\n" +
            "      \"areacode\": \"1268\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 130,\n" +
            "          \"statecode\": \"Bar\",\n" +
            "          \"statename\": \"Barbuda\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 131,\n" +
            "          \"statecode\": \"Pargeor\",\n" +
            "          \"statename\": \"Parish of Saint George\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 132,\n" +
            "          \"statecode\": \"Parjohn\",\n" +
            "          \"statename\": \"Parish of Saint John\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 133,\n" +
            "          \"statecode\": \"Parmary\",\n" +
            "          \"statename\": \"Parish of Saint Mary\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 134,\n" +
            "          \"statecode\": \"Parpaul\",\n" +
            "          \"statename\": \"Parish of Saint Paul\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 135,\n" +
            "          \"statecode\": \"Parpet\",\n" +
            "          \"statename\": \"Parish of Saint Peter\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 136,\n" +
            "          \"statecode\": \"Parphilip\",\n" +
            "          \"statename\": \"Parish of Saint Philip\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 137,\n" +
            "          \"statecode\": \"Paro\",\n" +
            "          \"statename\": \"Paronda\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 21,\n" +
            "      \"countrycode\": \"BB\",\n" +
            "      \"countryname\": \"BARBADOS\",\n" +
            "      \"areacode\": \"1246\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 219,\n" +
            "          \"statecode\": \"Chr\",\n" +
            "          \"statename\": \"Christ Church\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 220,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Andrew\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 221,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint George\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 222,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint James\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 223,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint John\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 224,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Joseph\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 225,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Lucy\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 226,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Michael\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 227,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Peter\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 228,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Philip\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 229,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Thomas\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 31,\n" +
            "      \"countrycode\": \"CA\",\n" +
            "      \"countryname\": \"CANADA\",\n" +
            "      \"areacode\": \"1\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 8,\n" +
            "      \"countrycode\": \"KY\",\n" +
            "      \"countryname\": \"CAYMAN ISLANDS\",\n" +
            "      \"areacode\": \"1345\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 123,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Croix Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 124,\n" +
            "          \"statecode\": \"Bod\",\n" +
            "          \"statename\": \"Bodden Town\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 125,\n" +
            "          \"statecode\": \"Eas\",\n" +
            "          \"statename\": \"East End\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 126,\n" +
            "          \"statecode\": \"Geo\",\n" +
            "          \"statename\": \"George Town\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 127,\n" +
            "          \"statecode\": \"Nor\",\n" +
            "          \"statename\": \"North Side\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 128,\n" +
            "          \"statecode\": \"Sis\",\n" +
            "          \"statename\": \"Sister Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 129,\n" +
            "          \"statecode\": \"West Ba\",\n" +
            "          \"statename\": \"West Bay\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 30,\n" +
            "      \"countrycode\": \"CH\",\n" +
            "      \"countryname\": \"China\",\n" +
            "      \"areacode\": \"86\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 17,\n" +
            "      \"countrycode\": \"DM\",\n" +
            "      \"countryname\": \"DOMINICA\",\n" +
            "      \"areacode\": \"1767\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 186,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Andrew\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 187,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint David\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 188,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint George\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 189,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint John\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 190,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Joseph\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 191,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Luke\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 192,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Mark\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 193,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Patrick\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 194,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Paul\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 195,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Peter\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 25,\n" +
            "      \"countrycode\": \"GF\",\n" +
            "      \"countryname\": \"FRENCH GUIANA\",\n" +
            "      \"areacode\": \"594\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 20,\n" +
            "      \"countrycode\": \"GD\",\n" +
            "      \"countryname\": \"GRENADA\",\n" +
            "      \"areacode\": \"1473\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 212,\n" +
            "          \"statecode\": \"Car\",\n" +
            "          \"statename\": \"Carriacou and Petite Martinique\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 213,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Andrew\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 214,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint David\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 215,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint George\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 216,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint John\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 217,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Mark\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 218,\n" +
            "          \"statecode\": \"Sai\",\n" +
            "          \"statename\": \"Saint Patrick\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 15,\n" +
            "      \"countrycode\": \"GP\",\n" +
            "      \"countryname\": \"GUADELOUPE\",\n" +
            "      \"areacode\": \"590\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 184,\n" +
            "          \"statecode\": \"Gua\",\n" +
            "          \"statename\": \"Guadeloupe\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"countrycode\": \"GT\",\n" +
            "      \"countryname\": \"GUATEMALA\",\n" +
            "      \"areacode\": \"502\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 53,\n" +
            "          \"statecode\": \"Alt\",\n" +
            "          \"statename\": \"Departameto de Alta Verapaz\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 54,\n" +
            "          \"statecode\": \"Baja\",\n" +
            "          \"statename\": \"Departameto de Baja Verapaz\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 55,\n" +
            "          \"statecode\": \"Chim\",\n" +
            "          \"statename\": \"Departameto de Chimaltenango\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 56,\n" +
            "          \"statecode\": \"Chiq\",\n" +
            "          \"statename\": \"Departameto de Chiquimula\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 57,\n" +
            "          \"statecode\": \"El\",\n" +
            "          \"statename\": \"Departameto de El Progreso\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 58,\n" +
            "          \"statecode\": \"Esc\",\n" +
            "          \"statename\": \"Departameto de Escuitla\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 59,\n" +
            "          \"statecode\": \"Gua\",\n" +
            "          \"statename\": \"Departameto de Guatemala\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 60,\n" +
            "          \"statecode\": \"Hue\",\n" +
            "          \"statename\": \"Departameto de Huehueteago\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 61,\n" +
            "          \"statecode\": \"Iza\",\n" +
            "          \"statename\": \"Departameto de Izabal\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 62,\n" +
            "          \"statecode\": \"Jal\",\n" +
            "          \"statename\": \"Departameto de Jalapa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 63,\n" +
            "          \"statecode\": \"Jut\",\n" +
            "          \"statename\": \"Departameto de Jutiapa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 64,\n" +
            "          \"statecode\": \"Que\",\n" +
            "          \"statename\": \"Departameto de Quetzalteago\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 65,\n" +
            "          \"statecode\": \"Ret\",\n" +
            "          \"statename\": \"Departameto de Retalhuleu\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 66,\n" +
            "          \"statecode\": \"Sac\",\n" +
            "          \"statename\": \"Departameto de Sacatepequez\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 67,\n" +
            "          \"statecode\": \"Sa\",\n" +
            "          \"statename\": \"Departameto de Sa Marcos\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 68,\n" +
            "          \"statecode\": \"Sat\",\n" +
            "          \"statename\": \"Departameto de Sata Rosa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 69,\n" +
            "          \"statecode\": \"Sol\",\n" +
            "          \"statename\": \"Departameto de Solola\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 70,\n" +
            "          \"statecode\": \"Suchi\",\n" +
            "          \"statename\": \"Departameto de Suchitepequez\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 71,\n" +
            "          \"statecode\": \"Tot\",\n" +
            "          \"statename\": \"Departameto de Totoicapa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 72,\n" +
            "          \"statecode\": \"Zac\",\n" +
            "          \"statename\": \"Departameto de Zacapa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 73,\n" +
            "          \"statecode\": \"Pet\",\n" +
            "          \"statename\": \"Departameto del Pete\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 74,\n" +
            "          \"statecode\": \"Del\",\n" +
            "          \"statename\": \"Departamento del Quiche\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 23,\n" +
            "      \"countrycode\": \"GY\",\n" +
            "      \"countryname\": \"GUYANA\",\n" +
            "      \"areacode\": \"592\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 245,\n" +
            "          \"statecode\": \"Bar\",\n" +
            "          \"statename\": \"Barima-Waini Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 246,\n" +
            "          \"statecode\": \"Cuy\",\n" +
            "          \"statename\": \"Cuyuni-Mazaruni Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 247,\n" +
            "          \"statecode\": \"Dem\",\n" +
            "          \"statename\": \"Demerara-Mahaica Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 248,\n" +
            "          \"statecode\": \"Eas\",\n" +
            "          \"statename\": \"East Berbice-Corentyne Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 249,\n" +
            "          \"statecode\": \"Ess\",\n" +
            "          \"statename\": \"Essequibo Islands-West Demerara Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 250,\n" +
            "          \"statecode\": \"Mah\",\n" +
            "          \"statename\": \"Mahaica-Berbice Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 251,\n" +
            "          \"statecode\": \"Pom\",\n" +
            "          \"statename\": \"Pomeroon-Supenaam Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 252,\n" +
            "          \"statecode\": \"Pot\",\n" +
            "          \"statename\": \"Potaro-Siparuni Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 253,\n" +
            "          \"statecode\": \"Upp\",\n" +
            "          \"statename\": \"Upper Demerara-Berbice Region\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 254,\n" +
            "          \"statecode\": \"Upp\",\n" +
            "          \"statename\": \"Upper Takutu-Upper Essequibo Region\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 6,\n" +
            "      \"countrycode\": \"HT\",\n" +
            "      \"countryname\": \"HAITI\",\n" +
            "      \"areacode\": \"509\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 110,\n" +
            "          \"statecode\": \"Centre\",\n" +
            "          \"statename\": \"Centre\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 111,\n" +
            "          \"statecode\": \"Art\",\n" +
            "          \"statename\": \"Departement de l'Artibonite\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 112,\n" +
            "          \"statecode\": \"Oue\",\n" +
            "          \"statename\": \"Departement de l'Ouest\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 113,\n" +
            "          \"statecode\": \"Nip\",\n" +
            "          \"statename\": \"Departement de Nippes\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 114,\n" +
            "          \"statecode\": \"Nordeas\",\n" +
            "          \"statename\": \"Departement du Nord-Est\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 115,\n" +
            "          \"statecode\": \"Gran\",\n" +
            "          \"statename\": \"Grandans\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 116,\n" +
            "          \"statecode\": \"Nor\",\n" +
            "          \"statename\": \"Nord\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 117,\n" +
            "          \"statecode\": \"Nord\",\n" +
            "          \"statename\": \"Nord-Ouest\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 118,\n" +
            "          \"statecode\": \"Sud\",\n" +
            "          \"statename\": \"Sud\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 119,\n" +
            "          \"statecode\": \"Sud-E\",\n" +
            "          \"statename\": \"Sud-Est\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 5,\n" +
            "      \"countrycode\": \"HN\",\n" +
            "      \"countryname\": \"HONDURAS\",\n" +
            "      \"areacode\": \"504\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 92,\n" +
            "          \"statecode\": \"Atlan\",\n" +
            "          \"statename\": \"Departamento de Atlantida\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 93,\n" +
            "          \"statecode\": \"Cho\",\n" +
            "          \"statename\": \"Departamento de Choluteca\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 94,\n" +
            "          \"statecode\": \"Col\",\n" +
            "          \"statename\": \"Departamento de Colon\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 95,\n" +
            "          \"statecode\": \"Com\",\n" +
            "          \"statename\": \"Departamento de Comayagua\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 96,\n" +
            "          \"statecode\": \"Cop\",\n" +
            "          \"statename\": \"Departamento de Copan\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 97,\n" +
            "          \"statecode\": \"Cop\",\n" +
            "          \"statename\": \"Departamento de Coptes\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 98,\n" +
            "          \"statecode\": \"Para\",\n" +
            "          \"statename\": \"Departamento de El Paraiso\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 99,\n" +
            "          \"statecode\": \"Fra\",\n" +
            "          \"statename\": \"Departamento de Francisco Morazan\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 100,\n" +
            "          \"statecode\": \"Grac\",\n" +
            "          \"statename\": \"Departamento de Gracias a Dios\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 101,\n" +
            "          \"statecode\": \"Inti\",\n" +
            "          \"statename\": \"Departamento de Intibuca\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 102,\n" +
            "          \"statecode\": \"Isl\",\n" +
            "          \"statename\": \"Departamento de Islas de la Bahia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 103,\n" +
            "          \"statecode\": \"La\",\n" +
            "          \"statename\": \"Departamento de La Paz\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 104,\n" +
            "          \"statecode\": \"Lem\",\n" +
            "          \"statename\": \"Departamento de Lempira\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 105,\n" +
            "          \"statecode\": \"Oco\",\n" +
            "          \"statename\": \"Departamento de Ocotepeque\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 106,\n" +
            "          \"statecode\": \"Ola\",\n" +
            "          \"statename\": \"Departamento de Olancho\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 107,\n" +
            "          \"statecode\": \"San\",\n" +
            "          \"statename\": \"Departamento de Santa Barbara\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 108,\n" +
            "          \"statecode\": \"Val\",\n" +
            "          \"statename\": \"Departamento de Valle\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 109,\n" +
            "          \"statecode\": \"Yor\",\n" +
            "          \"statename\": \"Departamento de Yoro\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 27,\n" +
            "      \"countrycode\": \"IND\",\n" +
            "      \"countryname\": \"INDIA\",\n" +
            "      \"areacode\": \"91\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 279,\n" +
            "          \"statecode\": \"HYD\",\n" +
            "          \"statename\": \"Hyderbad\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 16,\n" +
            "      \"countrycode\": \"MQ\",\n" +
            "      \"countryname\": \"MARTINIQUE\",\n" +
            "      \"areacode\": \"596\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 185,\n" +
            "          \"statecode\": \"Mar\",\n" +
            "          \"statename\": \"Martinique\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 28,\n" +
            "      \"countrycode\": \"MX\",\n" +
            "      \"countryname\": \"MEXICO\",\n" +
            "      \"areacode\": \"52\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 11,\n" +
            "      \"countrycode\": \"MS\",\n" +
            "      \"countryname\": \"MONTSERRAT\",\n" +
            "      \"areacode\": \"1644\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 152,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint Anthony\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 153,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint Georges\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 154,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint Peter\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 29,\n" +
            "      \"countrycode\": \"MA\",\n" +
            "      \"countryname\": \"Morocco\",\n" +
            "      \"areacode\": \"212\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 4,\n" +
            "      \"countrycode\": \"NI\",\n" +
            "      \"countryname\": \"NICARAGUA\",\n" +
            "      \"areacode\": \"505\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 75,\n" +
            "          \"statecode\": \"Cost\",\n" +
            "          \"statename\": \"Costa Caribe Sur\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 76,\n" +
            "          \"statecode\": \"Boac\",\n" +
            "          \"statename\": \"Departamento de Boaco\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 77,\n" +
            "          \"statecode\": \"Cara\",\n" +
            "          \"statename\": \"Departamento de Carazo\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 78,\n" +
            "          \"statecode\": \"Chin\",\n" +
            "          \"statename\": \"Departamento de Chinandega\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 79,\n" +
            "          \"statecode\": \"Chon\",\n" +
            "          \"statename\": \"Departamento de Chontales\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 80,\n" +
            "          \"statecode\": \"Est\",\n" +
            "          \"statename\": \"Departamento de Esteli\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 81,\n" +
            "          \"statecode\": \"Gran\",\n" +
            "          \"statename\": \"Departamento de Granada\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 82,\n" +
            "          \"statecode\": \"Jin\",\n" +
            "          \"statename\": \"Departamento de Jinotega\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 83,\n" +
            "          \"statecode\": \"Leo\",\n" +
            "          \"statename\": \"Departamento de Leon\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 84,\n" +
            "          \"statecode\": \"Mad\",\n" +
            "          \"statename\": \"Departamento de Madriz\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 85,\n" +
            "          \"statecode\": \"Mana\",\n" +
            "          \"statename\": \"Departamento de Managua\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 86,\n" +
            "          \"statecode\": \"Mas\",\n" +
            "          \"statename\": \"Departamento de Masaya\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 87,\n" +
            "          \"statecode\": \"Mata\",\n" +
            "          \"statename\": \"Departamento de Matagalpa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 88,\n" +
            "          \"statecode\": \"Nue\",\n" +
            "          \"statename\": \"Departamento de Nueva Segovia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 89,\n" +
            "          \"statecode\": \"Rio\",\n" +
            "          \"statename\": \"Departamento de Rio San Juan\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 90,\n" +
            "          \"statecode\": \"Riv\",\n" +
            "          \"statename\": \"Departamento de Rivas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 91,\n" +
            "          \"statecode\": \"Atlan\",\n" +
            "          \"statename\": \"North Atlantic Autonomous Region (RAAN)\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"countrycode\": \"PA\",\n" +
            "      \"countryname\": \"PANAMA\",\n" +
            "      \"areacode\": \"507\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 1,\n" +
            "          \"statecode\": \"PS\",\n" +
            "          \"statename\": \"PANAMA STATE\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 266,\n" +
            "          \"statecode\": \"Gun\",\n" +
            "          \"statename\": \"Guna Yala\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 267,\n" +
            "          \"statecode\": \"Ngo\",\n" +
            "          \"statename\": \"Ngoebe-Bugle\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 268,\n" +
            "          \"statecode\": \"1na\",\n" +
            "          \"statename\": \"1nama Oeste\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 269,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Bocas del Toro\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 270,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Chiriqui\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 271,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Cocle\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 272,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Colon\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 273,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Herrera\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 274,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Los Santos\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 275,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de 1nama\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 276,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia de Veraguas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 277,\n" +
            "          \"statecode\": \"Prov\",\n" +
            "          \"statename\": \"Provincia del Darien\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 278,\n" +
            "          \"statecode\": \"Emb\",\n" +
            "          \"statename\": \"Embera-Wounaan\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10,\n" +
            "      \"countrycode\": \"KN\",\n" +
            "      \"countryname\": \"SAINT KITTS AND NEVIS\",\n" +
            "      \"areacode\": \"1869\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 138,\n" +
            "          \"statecode\": \"Chr\",\n" +
            "          \"statename\": \"Christ Church Nichola Town\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 139,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Anne Sandy Point\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 140,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint George Basseterre\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 141,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint George Gingerland\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 142,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint James Windward\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 143,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint John Capesterre\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 144,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint John Figtree\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 145,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Mary Cayon\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 146,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Paul Capesterre\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 147,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Paul Charlestown\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 148,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Peter Basseterre\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 149,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Thomas Lowland\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 150,\n" +
            "          \"statecode\": \"Saint\",\n" +
            "          \"statename\": \"Saint Thomas Middle Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 151,\n" +
            "          \"statecode\": \"Tri\",\n" +
            "          \"statename\": \"Trinity Palmetto Point\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 18,\n" +
            "      \"countrycode\": \"LC\",\n" +
            "      \"countryname\": \"SAINT LUCIA\",\n" +
            "      \"areacode\": \"1758\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 196,\n" +
            "          \"statecode\": \"Ans\",\n" +
            "          \"statename\": \"Anse-la-Raye\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 197,\n" +
            "          \"statecode\": \"Can\",\n" +
            "          \"statename\": \"Canaries\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 198,\n" +
            "          \"statecode\": \"Cas\",\n" +
            "          \"statename\": \"Castries\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 199,\n" +
            "          \"statecode\": \"Cho\",\n" +
            "          \"statename\": \"Choiseul\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 200,\n" +
            "          \"statecode\": \"Den\",\n" +
            "          \"statename\": \"Dennery\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 201,\n" +
            "          \"statecode\": \"Gro\",\n" +
            "          \"statename\": \"Gros-Islet\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 202,\n" +
            "          \"statecode\": \"Lab\",\n" +
            "          \"statename\": \"Laborie\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 203,\n" +
            "          \"statecode\": \"Mic\",\n" +
            "          \"statename\": \"Micoud\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 204,\n" +
            "          \"statecode\": \"Sou\",\n" +
            "          \"statename\": \"Soufriere\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 205,\n" +
            "          \"statecode\": \"Vie\",\n" +
            "          \"statename\": \"Vieux-Fort\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 19,\n" +
            "      \"countrycode\": \"VC\",\n" +
            "      \"countryname\": \"SAINT VINCENT\",\n" +
            "      \"areacode\": \"1784\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 206,\n" +
            "          \"statecode\": \"Gre\",\n" +
            "          \"statename\": \"Grenadines\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 207,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Charlotte\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 208,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint Andrew\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 209,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint David\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 210,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint George\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 211,\n" +
            "          \"statecode\": \"Par\",\n" +
            "          \"statename\": \"Parish of Saint Patrick\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 12,\n" +
            "      \"countrycode\": \"MR\",\n" +
            "      \"countryname\": \"ST.MAARTEN\",\n" +
            "      \"areacode\": \"1721\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 155,\n" +
            "          \"statecode\": \"Adr\",\n" +
            "          \"statename\": \"Adrar\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 156,\n" +
            "          \"statecode\": \"Ass\",\n" +
            "          \"statename\": \"Assaba\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 157,\n" +
            "          \"statecode\": \"Bra\",\n" +
            "          \"statename\": \"Brakna\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 158,\n" +
            "          \"statecode\": \"Dak\",\n" +
            "          \"statename\": \"Dakhlet Nouadhibou\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 159,\n" +
            "          \"statecode\": \"Gor\",\n" +
            "          \"statename\": \"Gorgol\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 160,\n" +
            "          \"statecode\": \"Gui\",\n" +
            "          \"statename\": \"Guidimaka\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 161,\n" +
            "          \"statecode\": \"Hod\",\n" +
            "          \"statename\": \"Hodh ech Chargui\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 162,\n" +
            "          \"statecode\": \"Hodh\",\n" +
            "          \"statename\": \"Hodh El Gharbi\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 163,\n" +
            "          \"statecode\": \"Inch\",\n" +
            "          \"statename\": \"Inchiri\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 164,\n" +
            "          \"statecode\": \"Nou\",\n" +
            "          \"statename\": \"Nouakchott Nord\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 165,\n" +
            "          \"statecode\": \"Nou\",\n" +
            "          \"statename\": \"Nouakchott Ouest\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 166,\n" +
            "          \"statecode\": \"Nou\",\n" +
            "          \"statename\": \"Nouakchott Sud\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 167,\n" +
            "          \"statecode\": \"Tag\",\n" +
            "          \"statename\": \"Tagant\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 168,\n" +
            "          \"statecode\": \"Tir\",\n" +
            "          \"statename\": \"Tiris Zemmour\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 169,\n" +
            "          \"statecode\": \"Wil\",\n" +
            "          \"statename\": \"Wilaya du Trarza\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 24,\n" +
            "      \"countrycode\": \"SR\",\n" +
            "      \"countryname\": \"SURINAME\",\n" +
            "      \"areacode\": \"597\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 255,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Brokopondo\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 256,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Commewijne\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 257,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Coronie\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 258,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Marowijne\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 259,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Nickerie\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 260,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Para\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 261,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Paramaribo\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 262,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Saramacca\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 263,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Sipaliwini\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 264,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"Distrikt Wanica\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 22,\n" +
            "      \"countrycode\": \"TT\",\n" +
            "      \"countryname\": \"TRINIDAD AND TOBAGO\",\n" +
            "      \"areacode\": \"1868\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 230,\n" +
            "          \"statecode\": \"Ari\",\n" +
            "          \"statename\": \"Arima\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 231,\n" +
            "          \"statecode\": \"Cha\",\n" +
            "          \"statename\": \"Chaguanas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 232,\n" +
            "          \"statecode\": \"Cit\",\n" +
            "          \"statename\": \"City of Port of Spain\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 233,\n" +
            "          \"statecode\": \"Cou\",\n" +
            "          \"statename\": \"Couva-Tabaquite-Talparo\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 234,\n" +
            "          \"statecode\": \"Die\",\n" +
            "          \"statename\": \"Diego Martin\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 235,\n" +
            "          \"statecode\": \"May\",\n" +
            "          \"statename\": \"Mayaro\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 236,\n" +
            "          \"statecode\": \"Pen\",\n" +
            "          \"statename\": \"Penal/Debe\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 237,\n" +
            "          \"statecode\": \"Poi\",\n" +
            "          \"statename\": \"Point Fortin\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 238,\n" +
            "          \"statecode\": \"Pri\",\n" +
            "          \"statename\": \"Princes Town\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 239,\n" +
            "          \"statecode\": \"San\",\n" +
            "          \"statename\": \"San Fernando\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 240,\n" +
            "          \"statecode\": \"San\",\n" +
            "          \"statename\": \"San Juan/Laventille\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 241,\n" +
            "          \"statecode\": \"Sangr\",\n" +
            "          \"statename\": \"Sangre Grande\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 242,\n" +
            "          \"statecode\": \"Sip\",\n" +
            "          \"statename\": \"Siparia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 243,\n" +
            "          \"statecode\": \"Tob\",\n" +
            "          \"statename\": \"Tobago\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 244,\n" +
            "          \"statecode\": \"Tuna\",\n" +
            "          \"statename\": \"Tunapuna/Piarco\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 26,\n" +
            "      \"countrycode\": \"TC\",\n" +
            "      \"countryname\": \"TURKS AND CAICOS ISLANDS\",\n" +
            "      \"areacode\": \"1649\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"countrycode\": \"US\",\n" +
            "      \"countryname\": \"UNITED STATES\",\n" +
            "      \"areacode\": \"1\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 2,\n" +
            "          \"statecode\": \"Al\",\n" +
            "          \"statename\": \"Alabama\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 3,\n" +
            "          \"statecode\": \"Alas\",\n" +
            "          \"statename\": \"Alaska\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"statecode\": \"Alas\",\n" +
            "          \"statename\": \"Arizona\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 5,\n" +
            "          \"statecode\": \"Ark\",\n" +
            "          \"statename\": \"Arkansas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 6,\n" +
            "          \"statecode\": \"Cali\",\n" +
            "          \"statename\": \"California\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 7,\n" +
            "          \"statecode\": \"Colo\",\n" +
            "          \"statename\": \"Colorado\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 8,\n" +
            "          \"statecode\": \"Conn\",\n" +
            "          \"statename\": \"Connecticut\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 9,\n" +
            "          \"statecode\": \"Del\",\n" +
            "          \"statename\": \"Delaware\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 10,\n" +
            "          \"statecode\": \"Dis\",\n" +
            "          \"statename\": \"District of Columbia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 11,\n" +
            "          \"statecode\": \"Flo\",\n" +
            "          \"statename\": \"Florida\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 12,\n" +
            "          \"statecode\": \"Haw\",\n" +
            "          \"statename\": \"Hawaii\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13,\n" +
            "          \"statecode\": \"Id\",\n" +
            "          \"statename\": \"Idaho\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 14,\n" +
            "          \"statecode\": \"Ill\",\n" +
            "          \"statename\": \"Illinois\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 15,\n" +
            "          \"statecode\": \"Indi\",\n" +
            "          \"statename\": \"Indiana\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 16,\n" +
            "          \"statecode\": \"Iow\",\n" +
            "          \"statename\": \"Iowa\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 17,\n" +
            "          \"statecode\": \"Kan\",\n" +
            "          \"statename\": \"Kansas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 18,\n" +
            "          \"statecode\": \"Ken\",\n" +
            "          \"statename\": \"Kentucky\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 19,\n" +
            "          \"statecode\": \"Lou\",\n" +
            "          \"statename\": \"Louisiana\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 20,\n" +
            "          \"statecode\": \"Mai\",\n" +
            "          \"statename\": \"Maine\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 21,\n" +
            "          \"statecode\": \"Mar\",\n" +
            "          \"statename\": \"Maryland\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 22,\n" +
            "          \"statecode\": \"Mas\",\n" +
            "          \"statename\": \"Massachusetts\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 23,\n" +
            "          \"statecode\": \"Mic\",\n" +
            "          \"statename\": \"Michigan\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 24,\n" +
            "          \"statecode\": \"Min\",\n" +
            "          \"statename\": \"Minnesota\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 25,\n" +
            "          \"statecode\": \"Missi\",\n" +
            "          \"statename\": \"Mississippi\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 26,\n" +
            "          \"statecode\": \"Misso\",\n" +
            "          \"statename\": \"Missouri\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 27,\n" +
            "          \"statecode\": \"Mont\",\n" +
            "          \"statename\": \"Montana\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 28,\n" +
            "          \"statecode\": \"Neb\",\n" +
            "          \"statename\": \"Nebraska\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 29,\n" +
            "          \"statecode\": \"Nev\",\n" +
            "          \"statename\": \"Nevada\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 30,\n" +
            "          \"statecode\": \"Ham\",\n" +
            "          \"statename\": \"New Hampshire\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 31,\n" +
            "          \"statecode\": \"Jer\",\n" +
            "          \"statename\": \"New Jersey\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 32,\n" +
            "          \"statecode\": \"Mex\",\n" +
            "          \"statename\": \"New Mexico\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 33,\n" +
            "          \"statecode\": \"York\",\n" +
            "          \"statename\": \"New York\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 34,\n" +
            "          \"statecode\": \"Caro\",\n" +
            "          \"statename\": \"North Carolina\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 35,\n" +
            "          \"statecode\": \"Nor\",\n" +
            "          \"statename\": \"North Dakota\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 36,\n" +
            "          \"statecode\": \"Ohi\",\n" +
            "          \"statename\": \"Ohio\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 37,\n" +
            "          \"statecode\": \"Okl\",\n" +
            "          \"statename\": \"Oklahoma\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 38,\n" +
            "          \"statecode\": \"Ore\",\n" +
            "          \"statename\": \"Oregon\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 39,\n" +
            "          \"statecode\": \"Penn\",\n" +
            "          \"statename\": \"Pennsylvania\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 40,\n" +
            "          \"statecode\": \"Rho\",\n" +
            "          \"statename\": \"Rhode Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 41,\n" +
            "          \"statecode\": \"Sou\",\n" +
            "          \"statename\": \"South Carolina\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 42,\n" +
            "          \"statecode\": \"Dako\",\n" +
            "          \"statename\": \"South Dakota\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 43,\n" +
            "          \"statecode\": \"Ten\",\n" +
            "          \"statename\": \"Tennessee\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 44,\n" +
            "          \"statecode\": \"Tex\",\n" +
            "          \"statename\": \"Texas\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 45,\n" +
            "          \"statecode\": \"Uta\",\n" +
            "          \"statename\": \"Utah\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 46,\n" +
            "          \"statecode\": \"Ver\",\n" +
            "          \"statename\": \"Vermont\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 47,\n" +
            "          \"statecode\": \"Vir\",\n" +
            "          \"statename\": \"Virginia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 48,\n" +
            "          \"statecode\": \"Was\",\n" +
            "          \"statename\": \"Washington\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 49,\n" +
            "          \"statecode\": \"Wes\",\n" +
            "          \"statename\": \"West Virginia\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 50,\n" +
            "          \"statecode\": \"Wis\",\n" +
            "          \"statename\": \"Wisconsin\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 51,\n" +
            "          \"statecode\": \"Wyo\",\n" +
            "          \"statename\": \"Wyoming\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 52,\n" +
            "          \"statecode\": \"Geo\",\n" +
            "          \"statename\": \"Georgia\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 14,\n" +
            "      \"countrycode\": \"VG\",\n" +
            "      \"countryname\": \"VIRGIN ISLANDS, BRITISH\",\n" +
            "      \"areacode\": \"1284\",\n" +
            "      \"states\": [\n" +
            "        \n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 7,\n" +
            "      \"countrycode\": \"VI\",\n" +
            "      \"countryname\": \"VIRGIN ISLANDS, U.S.\",\n" +
            "      \"areacode\": \"1340\",\n" +
            "      \"states\": [\n" +
            "        {\n" +
            "          \"id\": 120,\n" +
            "          \"statecode\": \"Cro\",\n" +
            "          \"statename\": \"Saint Croix Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 121,\n" +
            "          \"statecode\": \"Joh\",\n" +
            "          \"statename\": \"Saint John Island\",\n" +
            "          \"status\": true\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 122,\n" +
            "          \"statecode\": \"Tho\",\n" +
            "          \"statename\": \"Saint Thomas Island\",\n" +
            "          \"status\": true\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    //}

    public static String getEmail() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.EMIAL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getSenderAreaCode() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.PHONECODE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getSenderMobileNumber() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return Long.parseLong(result.getString(AppoConstants.MOBILENUMBER));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPhoneCode() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.PHONECODE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWalletAccountNumber() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray customerAccounts = customerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject indexAccounts = customerAccounts.getJSONObject(0);
            String walletAccountNumber = indexAccounts.getString(AppoConstants.ACCOUNTNUMBER);
            return walletAccountNumber;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrencyId() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray customerAccounts = customerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject indexAccounts = customerAccounts.getJSONObject(0);
            String currencyid = indexAccounts.getString(AppoConstants.CURRENCYID);
            return currencyid;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrencyId(JSONObject object) {
        try {

            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray customerAccounts = customerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject indexAccounts = customerAccounts.getJSONObject(0);
            String currencyid = indexAccounts.getString(AppoConstants.CURRENCYID);
            return currencyid;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getTransactionPin() {
        //transactionpin
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            String mTransactionPin = result.getString(AppoConstants.TRANSACTIONPIN);
            return mTransactionPin;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static String getCurrantBalance() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray customerAccounts = customerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject indexAccounts = customerAccounts.getJSONObject(0);
            String currentBalance = indexAccounts.getString(AppoConstants.CURRENTBALANCE);
            return currentBalance;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
        //currentbalance
    }

    public static String getCustomerAccountId() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray customerAccounts = customerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject indexAccounts = customerAccounts.getJSONObject(0);
            String id = indexAccounts.getString(AppoConstants.ID);
            return id;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
        //currentbalance
    }


    public static String getUserDetails() {
        if (mUserDetails == null) {
            mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
            return mUserDetails;
        } else {
            return mUserDetails;
        }
    }

    public static void setUserDetailsNull() {
        mUserDetails = null;
    }

    public static String getSenderName() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFirstName() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            //Log.e(TAG, "getFirstName: "+object );
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.FIRSTNAME); //+ " " + result.getString(AppoConstants.LASTNAME);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLastName() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.LASTNAME);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getNumberWithCountryCode() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return result.getString(AppoConstants.PHONECODE) + result.getString(AppoConstants.MOBILENUMBER);
            // return result.getString(AppoConstants.MOBILENUMBER);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNumberWithCountryCodeSpace() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            return "+" + result.getString(AppoConstants.PHONECODE) + " " + result.getString(AppoConstants.MOBILENUMBER);
            // return result.getString(AppoConstants.MOBILENUMBER);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getUserId() {
        try {
            JSONObject index = new JSONObject(Helper.getUserDetails());
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            return Integer.parseInt(userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCustomerId() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            String customerID = customerDetails.getString(AppoConstants.ID);
            return customerID;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getDepositDateFormat(String mDate) {

        try {
            //depositdate: "Fri Sep 25 2020",
            SimpleDateFormat formatterOriginal = new SimpleDateFormat("yyyy-MM-dd");
            Date originalDate = formatterOriginal.parse(mDate);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd yyyy"); //this format changeable
            String required = dateFormatter.format(originalDate);
            return required;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean isLoginEnable(Context context) {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        return StringUtils.isEmpty(vaultValue);
    }

    public static String getCurrency(String param, List<CurrencyResult> result) {
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

    public static String getAccountNumber(String string) {
        /*char[] chars = string.toCharArray();
        String strTemp = "";
        int cout = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            cout = cout + 1;
            if (cout >= 4 && cout < 9) {
                strTemp = strTemp + "X";
            } else {
                String temp = String.valueOf(chars[i]);
                strTemp = strTemp + temp;
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(strTemp);
        builder = builder.reverse();
        return String.valueOf(builder);*/

        char[] chars = string.toCharArray();
        String strTemp = "";
        int cout = 0;

        for (int i = 0; i < chars.length; i++) {

            if (cout >= 4 && cout < chars.length - 4) {
                strTemp = strTemp + "X";
            } else {
                String temp = String.valueOf(chars[i]);
                strTemp = strTemp + temp;
            }
            cout = cout + 1;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(strTemp);
        //builder = builder.reverse();
        return String.valueOf(builder);


    }

    /*
     float roundedFloat = (float) ((float) Math.round(params * 100.0) / 100.0);
            //Log.e(TAG, "getTwoDecimal: ::: " + roundedFloat);
            return roundedFloat;
     */
    public static boolean isValidAmount(float parseFloat) {
        try {
            float twoDecimal = getTwoDecimal(parseFloat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCountryId(JSONObject response) {
        try {
            JSONObject object = new JSONObject(String.valueOf(response));
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject jsonCustomers = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            String countryId = jsonCustomers.getString(AppoConstants.COUNTRYID);
            //Log.e(TAG, "getCountryId: " + countryId);
            return countryId;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void openShareIntent(Context context, String shareText) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(intent, "Share Via:"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getUniqueFileName() {
        String fileName;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Helper.FILE_NAME_FORMAT);
        fileName = simpleDateFormat.format(new Date());
        return "image_" + fileName + ".jpg";
    }

    public static ArrayList<ShopModel> getShopItems(Context mCtx) {
        ArrayList<ShopModel> mListShop = new ArrayList<>();
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_resturant), R.drawable.restaurant_icon2));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_grocery), R.drawable.cat_grocery));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_pharmacy), R.drawable.cross1));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_mobile), R.drawable.cat_mobile));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_men), R.drawable.cat_men));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_women), R.drawable.cat_women));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_electronics), R.drawable.cat_electronics));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_tv_amp_appliances), R.drawable.cat_tvac));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_laptops), R.mipmap.cat_pc));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_home_amp_kithcen), R.mipmap.cat_kitcheb));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_health_amp_fitness), R.mipmap.cat_health));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_kids_toys), R.mipmap.cat_kids));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_beauty_amp_grooming), R.mipmap.cat_beauty));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_bags_amp_luggage), R.mipmap.cat_bags));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_gift_vouchers), R.mipmap.cat_giftvoucher));

        return mListShop;


    }

    public static GiftProductList.Product getAppopayGift() {
        GiftProductList.Product product = new GiftProductList.Product();
        //GiftProductList.Amount amount = new GiftProductList.Amount();
        product.setCarrier("Appopay");
        List<GiftProductList.Amount> amoutList = new ArrayList<>();
        product.setProductName("Appopay");
        product.setAmounts(amoutList);

        return product;


    }

    /*public static String getLocationCityName(double lat, double lon) {
        JSONObject result = getLocationFormGoogle(lat + "," + lon);
        return getCityAddress(result);
    }*/

    /*protected static JSONObject getLocationFormGoogle(String placesName) {

        String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + placesName; //+ "&ka&sensor=false"
        HttpGet httpGet;
        httpGet = new HttpGet(apiRequest);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject;
    }*/

   /* protected static void getLocationFormGoogle(String placesName) {

        String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + placesName; //+ "&ka&sensor=false"
        AndroidNetworking.get(apiRequest)
                .setTag("add")
                .setOkHttpClient(AppoPayApplication.getOkHttpClient(10))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e(TAG, "onResponse: "+response.toString() );
                        getCityAddress(response);

                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.e(TAG, "onError: "+anError.getErrorDetail() );
                    }
                });

    }*/

    protected static String getCityAddress(JSONObject result) {
        if (result.has("results")) {
            try {
                JSONArray array = result.getJSONArray("results");
                if (array.length() > 0) {
                    JSONObject place = array.getJSONObject(0);
                    JSONArray components = place.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject component = components.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            if (types.getString(j).equals("locality")) {
                                return component.getString("long_name");
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * public static String getSenderName() {
     * try {
     * JSONObject object = new JSONObject(Helper.getUserDetails());
     * JSONObject result = object.getJSONObject(AppoConstants.RESULT);
     * return result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME);
     * <p>
     * } catch (JSONException e) {
     * e.printStackTrace();
     * }
     * return null;
     * }
     *
     * @return
     */


    public static String getAddress() {

        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            return customerDetails.getString(AppoConstants.ADDRESS);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String getCityName() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            if (customerDetails.getString(AppoConstants.CITYNAME).equalsIgnoreCase("null")) {
                return "";
            } else {
                return customerDetails.getString(AppoConstants.CITYNAME);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStateId() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            return customerDetails.getString(AppoConstants.STATEID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCountryId() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            return customerDetails.getString(AppoConstants.COUNTRYID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static int[] getGiftCardLogos() {

        return new int[]{
                R.drawable.appopay_gift_card,
                R.mipmap.gift1,
                R.mipmap.gift2,
                R.mipmap.gift3,
                R.mipmap.gift4,
                R.mipmap.gift5,
                R.mipmap.gift6,
                R.mipmap.gift7,
                R.mipmap.gift8,
                R.mipmap.gift9,
                R.mipmap.gift10,
                R.mipmap.gift11,
                R.mipmap.gift12,
                R.mipmap.gift13,
                R.mipmap.gift14,
                R.mipmap.gift15,
                R.mipmap.gift16,
                R.mipmap.gift17,
                R.mipmap.gift18,
                R.mipmap.gift19,
                R.mipmap.gift20,
                R.mipmap.gift21,
                R.mipmap.gift22,
                R.mipmap.gift23,
                R.mipmap.gift24,
                R.mipmap.gift25,
                R.mipmap.gift26,
                R.mipmap.gift27,
                R.mipmap.gift28,
                R.mipmap.gift29,
                R.mipmap.gift30,
                R.mipmap.gift31,
                R.mipmap.gift32,
                R.mipmap.gift33,
                R.mipmap.gift34,
                R.mipmap.gift35,
                R.mipmap.gift36,
                R.mipmap.gift37,
                R.mipmap.gift38,
                R.mipmap.gift39,
                R.mipmap.gift40,
                R.mipmap.gift41,
                R.mipmap.gift42,
                R.mipmap.gift43,
                R.mipmap.gift44,
                R.mipmap.gift45,
                R.mipmap.gift46,
                R.mipmap.gift47,
                R.mipmap.gift48,
                R.mipmap.gift49,
                R.mipmap.gift50,
                R.mipmap.gift51,
                R.mipmap.gift52,
                R.mipmap.gift53,
                R.mipmap.gift54,
                R.mipmap.gift55,
        };

    }

    public static int[] getGiftTypeLogo() {
        return new int[]{
                R.drawable.appopay_gift_card,
                R.mipmap.ic_gifticon,
                R.drawable.gift_card111};
    }

    public static String getAppendAccessToken(String bearer_, String accessToken) {
        return bearer_ + accessToken;

    }


    public static String getDob() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            return customerDetails.getString(AppoConstants.DOB);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String getMerchantWalletAccount(JSONObject object) {
        try {
            //JSONObject object = new JSONObject(jsonObject);
            //Log.e(TAG, "getMerchantWalletAccount: "+object.toString() );
            //JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject result = object;
            JSONObject merchantDetails = result.getJSONObject(AppoConstants.MERCHANTDETAILS);
            JSONArray merchantAccounts = merchantDetails.getJSONArray(AppoConstants.MERCHNATACCOUNTS);
            JSONObject indexAccounts = merchantAccounts.getJSONObject(0);
            String accountnumber = indexAccounts.getString(AppoConstants.ACCOUNTNUMBER);
            return accountnumber;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSenderAvatar() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            String avatar = "";
            if (result.has(AppoConstants.AVATAR)) {

                avatar = result.getString(AppoConstants.AVATAR);
            }

            return avatar;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getReceiverAvatar(JSONObject object) {
        try {
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            String avatar = "";
            if (result.has(AppoConstants.AVATAR)) {

                avatar = result.getString(AppoConstants.AVATAR);
            }

            return avatar;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrencySymble() {
        try {
            JSONObject object = new JSONObject(Helper.getUserDetails());
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            if (customerDetails.getString(AppoConstants.CURRENCYSYMBOL).equalsIgnoreCase("null")) {
                return "";
            } else {
                return customerDetails.getString(AppoConstants.CURRENCYSYMBOL);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrencySymble(JSONObject object) {
        try {
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            if (customerDetails.getString(AppoConstants.CURRENCYSYMBOL).equalsIgnoreCase("null")) {
                return "";
            } else {
                return customerDetails.getString(AppoConstants.CURRENCYSYMBOL);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateOfBirth(String dob) {
        //milliseconds
        //Log.e(TAG, "getDateOfBirth: " + dob);
        long milliSec = Long.parseLong(dob);

        // Creating date format
        //DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(milliSec);

        // Formatting Date according to the
        // given format
        //System.out.println(simple.format(result));
        //Log.e(TAG, "getTimeDateOther: " + simple.format(result));

        return simple.format(result);
    }

    public static String beneficiaryFirstName(String mRecName) {
        String[] s = mRecName.split(" ");
        return s[0];
    }

    public static String beneficiaryLastName(String mRecName) {
        String[] s = mRecName.split(" ");
        return s[s.length - 1];
    }

    public static ArrayList<ArrayList<ShopModel>> getSubShopItems(Context mCtx) {
        ArrayList<ArrayList<ShopModel>> ret = new ArrayList<>();
        ArrayList<ShopModel> restaurant = new ArrayList() {{
//            add(new ShopModel(mCtx.getString(R.string.sub_food_a1), R.drawable.a3));
//            add(new ShopModel(mCtx.getString(R.string.sub_food_a2), R.drawable.a3));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a3), R.drawable.a3));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a4), R.drawable.a4));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a5), R.drawable.a5));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a6), R.drawable.a6));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a7), R.drawable.a7));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a8), R.drawable.a8));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a9), R.drawable.a9));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a10), R.drawable.a10));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a11), R.drawable.a11));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a12), R.drawable.a12));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a13), R.drawable.a13));
            add(new ShopModel(mCtx.getString(R.string.sub_food_a14), R.drawable.a14));
        }};
        ret.add(restaurant);
        ArrayList<ShopModel> grocery = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a1), R.drawable.grocery_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a2), R.drawable.grocery_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a3), R.drawable.grocery_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a4), R.drawable.grocery_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a5), R.drawable.grocery_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a6), R.drawable.grocery_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a7), R.drawable.grocery_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a8), R.drawable.grocery_a8));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a9), R.drawable.grocery_a9));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a10), R.drawable.grocery_a10));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a11), R.drawable.grocery_a11));
            add(new ShopModel(mCtx.getString(R.string.sub_grocery_a12), R.drawable.grocery_a12));
        }};
        ret.add(grocery);
        ArrayList<ShopModel> pharmacy = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a1), R.drawable.pharmacy_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a2), R.drawable.pharmacy_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a3), R.drawable.pharmacy_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a4), R.drawable.pharmacy_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a5), R.drawable.pharmacy_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a6), R.drawable.pharmacy_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a7), R.drawable.pharmacy_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_pharmacy_a8), R.drawable.pharmacy_a8));
        }};
        ret.add(pharmacy);
        ArrayList<ShopModel> mobile = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a1), R.drawable.sub_phone_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a2), R.drawable.sub_phone_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a3), R.drawable.sub_phone_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a4), R.drawable.sub_phone_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a5), R.drawable.sub_phone_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a6), R.drawable.sub_phone_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a7), R.drawable.sub_phone_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a8), R.drawable.sub_phone_a8));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a9), R.drawable.sub_phone_a9));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a10), R.drawable.sub_phone_a10));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a11), R.drawable.sub_phone_a11));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a12), R.drawable.sub_phone_a12));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a13), R.drawable.sub_phone_a13));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a14), R.drawable.sub_phone_a14));
            add(new ShopModel(mCtx.getString(R.string.sub_phone_a15), R.drawable.sub_phone_a15));
        }};
        ret.add(mobile);
        ArrayList<ShopModel> men = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_men_a1), R.drawable.men_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a2), R.drawable.men_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a3), R.drawable.men_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a4), R.drawable.men_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a5), R.drawable.men_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a6), R.drawable.men_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a7), R.drawable.men_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a8), R.drawable.men_a8));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a9), R.drawable.men_a9));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a10), R.drawable.men_a10));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a11), R.drawable.men_a11));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a12), R.drawable.men_a12));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a13), R.drawable.men_a13));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a14), R.drawable.men_a14));
            add(new ShopModel(mCtx.getString(R.string.sub_men_a15), R.drawable.men_a15));
        }};
        ret.add(men);
        ArrayList<ShopModel> women = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_women), R.drawable.cat_women));
        }};
        ret.add(women);
        ArrayList<ShopModel> electronics = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_electronics), R.drawable.cat_electronics));
        }};
        ret.add(electronics);
        ArrayList<ShopModel> tv_appliances = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_app_a1), R.drawable.app_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a2), R.drawable.app_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a3), R.drawable.app_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a4), R.drawable.app_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a5), R.drawable.app_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a6), R.drawable.app_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a7), R.drawable.app_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a8), R.drawable.app_a8));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a9), R.drawable.app_a9));
            add(new ShopModel(mCtx.getString(R.string.sub_app_a10), R.drawable.app_a10));
        }};
        ret.add(tv_appliances);
        ArrayList<ShopModel> laptops = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a1), R.drawable.laptops_a1));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a2), R.drawable.laptops_a2));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a3), R.drawable.laptops_a3));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a4), R.drawable.laptops_a4));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a5), R.drawable.laptops_a5));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a6), R.drawable.laptops_a6));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a7), R.drawable.laptops_a7));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a8), R.drawable.laptops_a8));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a9), R.drawable.laptops_a9));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a10), R.drawable.laptops_a10));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a11), R.drawable.laptops_a11));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a12), R.drawable.laptops_a12));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a13), R.drawable.laptops_a13));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a14), R.drawable.laptops_a14));
            add(new ShopModel(mCtx.getString(R.string.sub_laptops_a15), R.drawable.laptops_a15));
        }};
        ret.add(laptops);
        ArrayList<ShopModel> home_kithcen = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_home_amp_kithcen), R.mipmap.cat_kitcheb));
        }};
        ret.add(home_kithcen);
        ArrayList<ShopModel> health_fitness = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_health_amp_fitness), R.mipmap.cat_health));
        }};
        ret.add(health_fitness);
        ArrayList<ShopModel> kids_toys = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_kids_toys), R.mipmap.cat_kids));
        }};
        ret.add(kids_toys);
        ArrayList<ShopModel> beauty_grooming = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_beauty_amp_grooming), R.mipmap.cat_beauty));
        }};
        ret.add(beauty_grooming);
        ArrayList<ShopModel> bags_luggage = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_bags_amp_luggage), R.mipmap.cat_bags));
        }};
        ret.add(bags_luggage);
        ArrayList<ShopModel> gift_vouchers = new ArrayList() {{
            add(new ShopModel(mCtx.getString(R.string.info_item_gift_vouchers), R.mipmap.cat_giftvoucher));
        }};
        ret.add(gift_vouchers);
        return ret;
    }

    public static ArrayList<CustomArea> getCountryList() {
        ArrayList<CustomArea> mArrayList = new ArrayList<>();
        mArrayList.add(new CustomArea("507", "Panama", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpENTU4Rjg0Mzk2RjExMUUwOEFCNjgxRjczMTZGRjEwQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpENTU4Rjg0Mjk2RjExMUUwOEFCNjgxRjczMTZGRjEwQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3QkYwNEVEODE2NkRFMDExOEI5OUY3NUM5RDg4RUFBOCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PhTCH/YAAATfSURBVHja7FtNbBtFGH273vV/HDfFqSCAIOBKaRsEouqhyqmHImgrBOJGUVEVceDQqgeIhNQKRC6RuHCsBOqtSFWlHEpyIBJFEIRQIRKtk6iCVK2auG7q1GkcO157f5jZrFvXiR1vdjO24/2k0W52Z+fnzZv3fTOecJqmoZWNR4ubA4ADgAOAA4BjrWxcCRNEkjzGdbsyQyWpQJJkXFXBeEE7HSJB0TzrFt0/cYJpfUoyia7R0U5yu0SBKAJAR74zl8vB6/UybVDgyBF2dHe5ED97lt52GiyQnmJAPTgpx+OrNzQk57gn142sPF+174x3fDgMI/QPGX2GUKIBnnoAoMny2sbqw2UCjBpB0etaLd9T1Dmh7rJUKJjkMfcEJNN0k9c8sgxALq/g39sp9O5+xh4GbKkCKmvAswxAMpXF2B+3sS+6kwwOZx4AswywMt3WqcsyANMzDzFzM4n4gwy6OoObmwLVgCuney15K+XJ560zgI64qqoQBV4va2LqPvK5Av6KJeDf/wJtBfIFBX6viLaAuzEYYOiGZgcAs4k0Lv44hXky4oJHgFwgBXpEDI/9h5FfbiEvydhL9OCjd/c2DgCldVkF4PWeXXixK4zvL/2DiRsJUoJLD6g1VYOS5/DeW1EcO/SqzpCajI4KK5Mke0SwI+TBZ/0H8Pk3v2Lm1gLxqKSzBRlHj/bg/cO7zQXnDAHgCACaXV4gnZFwJ75EQFXQtkNEmmjDTeIOTVszMoDab3/HsfhgGZ9+vB99b3bhu0s38POfd3CPaMOzkUDt89IuAIrKXyVI0uwCQFZUJJIZDH1xiGhCp/5s4JMDeOWlHcQtJusDAEsG8ATs48f2wC0+LXQfHI4iu2JO1R8DUDqClUazUhhc7vcrxA62eAEdACJ67goi7/eJ9mnAeh0uX+yY+ZYsh20PhbciPN2yuuwIhGzfo2KoASrP2+cGbV0Om6WyxZC4oQCQ5+bYBUKCUB0A3xvfkrierOsLSzS2ZTMvi1tijIzue8Lnq8QArSwxAIDxz/MNpwGNBQB9qRWvbBpG9xaYaQARwfL6Wn4K8I0wBcwmVZaRvXp1U99uAIDGPG2mA8riIlJDQ1BSKbsBaA4GZH4aQ2Z8HNL0tGUAmkID1IUFqNkV8H6f3qrs6AhUEkEuDw9DiEZXyyH+nQ8EwLe3W/UCGlMvUAsAhUQCD8+dQ+7aNfChEAEjC1ckgqXLl5G+cgVaJgNfXx86vvwKYii0/URQ7OlB5MIFBD88DpnOe0WBRl0aWUipKytoP30akfPnIXS/vCH1m3IK6D6c0Ltj8GvI9+JYHhkB53brW1zh/n60nzpVU1lNL4LKo0fITU7q90J3N1muqMhdv67vKbSEG8yO/w55dg47BwfxHJn74TNnkJuYgBSbtMsLNPBagOSTZ+9i1w8X4T14UH8UHhiAuK9Xd4fia701s626F1DRcF6gaG0nT+qbGqXf+N95W/+JvZZy1svTfKvB9fK7XDUD0LReYNsuhupta6eAJhNKBZk1YDOnSqzsPVSaAlT6JMhpIL/AdARisRjT+oJBfXAlo8+Pj8rSAJoe79hD0vOo05lBBkZPh86SNEXSXfp3kQEUkeIxWZrBs00BkAwQ5o1757A0HGtx45z/G2xxcwBwAHAAcABwAGhl+1+AAQBgwlfM237vzwAAAABJRU5ErkJggg=="));
        mArrayList.add(new CustomArea("57", "Colombia", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDowNjE0RTYzQTg2NkQxMUUwQTYzMjlBNTA4NEEyRDFCOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowNjE0RTYzOTg2NkQxMUUwQTYzMjlBNTA4NEEyRDFCOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBOEU4NzU5M0EyODVFMDExOUY2MEM4QkNDODU1NkNDQyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PnWSxdIAAAKzSURBVHja7JvNbtNAEMdnHacmQiAVRE7tC3DmxoULD8SJM0+AeBskzjwAF86g9uQKJaKlij+6w2y9Dk5tZ9cOlGR2Jlp5kziO57f/mZ11HIWIELJFELgJAAEgAASAWMimGkqYUkvslqsyNLWCWma3OrZvGKcfr368TlX0jPWIo76AB08/zan704CoAZiRn2P5BZLj99S9Yer+BFaLN6YztyrINhQAOCFRXBKmpWHVx7AROXUf70TU3f1g4LGUz1gO+D77njqGWx+Nr5XPEDdyQFJ1zeiXngCar4HjxIc4BgNh+EJZKzup81zcPkBhAeg9zttjF3BF67NtAJhTK/YYwC6WuwDUO40BsMvI3CcAcChgNAAYGf/okTu6ShdX/hilgDoEcgeAQxjtrvP1AfBPFHAwOQDr+mDECA+dsv6HZT4AVrTJDkzivpEw9VVAzhOAnwLyAwWgemaNpvkoABmHwO06wFUIGec3KsFtU57aMle75v5tx3IdFxy1QU/tgF6FUHMW6DoRNcBh3LFuUI5CCEd81hkCmSXFMQScAHQ1DbZWTdukh56jqDquH/iOnk+C861VtE8IlPeoADVCyrsAcIWA/kaN6w+mPjngr0htrxNBP4DZq7cAyZxSwNJcPuXn+9ETivCUOu/WLwX/y9BmCJibJZqNnfrbfsXdWRKZ5oG2XxICogBRQNgKiAP3fxPAy/wMHsIVrMormi34FUIzXMKvfAGf+wB8WHyER1EMhb5hKYCjaAKXuoQXfQACjACZBYJXQNy3GOa6IEZXHfDnwdF5qQQlBzhCoNphGk2CiP8mAFP2Zen1NXxnLvlZtcmsz+sL7ua+uVNqz6md2Occzdwdek7tK7Uz87xWgCGS2r7ZIWEKILMQUtuXm6VBLHBT8r/BwE0ACAABIAAEQMj2W4ABABW/RdTzEPY1AAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("506", "Costa Rica", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1MTlERkIzNzcxQTkxMUUyODhBNUIzM0Y2MDdGQTVBMyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1MTlERkIzNjcxQTkxMUUyODhBNUIzM0Y2MDdGQTVBMyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEREEwMkNGREVCOTZFMDExOEU1N0RFMEQxMkRFQTQxMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        mArrayList.add(new CustomArea("52", "Mexico", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDOTVFMkUzNTkwQjIxMUUwQkUzRkI2OEY0Q0ZDNERDMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDOTVFMkUzNDkwQjIxMUUwQkUzRkI2OEY0Q0ZDNERDMCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3OUJCMjhDMzhCOTBFMDExOENDOENDOEMyQzk0QjU0MCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        mArrayList.add(new CustomArea("1809", "Dominican Republic", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoxQ0JBNEQ5N0FEOUQxMUUyOTQ0NkFDNzVBQzczN0MyQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxQ0JBNEQ5NkFEOUQxMUUyOTQ0NkFDNzVBQzczN0MyQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEREEwMkNGREVCOTZFMDExOEU1N0RFMEQxMkRFQTQxMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pu2V1DUAAASKSURBVHja7FtNbxtFGH52vbGdko8mJQlNGiUVqoJahFqgcIAeqIRUcewFwYkDohVCIHHj3iPixC+AA4dWCpd+SIgPCahAqEJIbSVkmlBi3CTQuCQO8czuzjDj7Lr2rtebmIzXsue1Rjv2zs6888zzfszu2uCco5fFRI+LBkADoAHQAGjpZTFqmNAnSsY7diszmCi2KMQ7Mss7ISc99PzrF1YPTYwo1eDSR283Pb/4xrtKx7cLy5j74uK4qK5LIHwA5MqP5+7ewzuvnQZLMDscPnNaHd1NE3c+uCCr4x4LSB0DZGW9VEZxYxNRGMjU2TCMurqfTvu/B9vtRmi+oAyA1P5hqRm8uco5w6rxARmptOO6lcJZBADiY8AI/bbtUKIn3Oi6hu0cp9rXTq+JGiN0vZiXt7IZ389ZgR5AXQbHYWAsGTPgjuurUnes99u8CQjR3025sIEGVhBBWyggS2IA2LbCvp0QRPUAVBjQGgDS3PfCd24rqQgA6oQoEjABjrLtggol9pIBvCbh2DUAQVSbOVa/bUQbTpxQf1ZQUckAGsOA5lb4f1eJ7n348yIVt2kMAOIkpZIBMgqwZBIBBT6gOmVqxwGAigmQFgBoNe6HclWq0AkSCh7LACIYIJxFUgxQGgUamFcIgLKYPBFKJAYAoer67iPb26FmTpAk7ANaNYHa9Dyyb2LHO8GynTADWowCfKcMiAOAiFhpUxkG3boQshvU/WtacYpMmEBcv1F61M6joZ4W3UEUkE5QUIWL3VAjRRopETXh2l1iI8VzC4swUyYen5mpYYD9cDUDCQdHIKPiTVbfCJ/nZpgBfndjohzb2tr6OpvNqg3zIsn6/PK3KG0VMXJgDGm42HiwgoHBCZx5+UWkUiml45fLZfT3978kqrdE+avtt742SwSL95dxY+F3QUkXr6wXMPbTN8gX7uDfTbftPsdqu5cXjsg4Usb175bASkuYLf2NxXtrWDu1H9T8xyNjFwPw8Q+f4Mfcz3jzubMYH7CRowz8+ADmf/0Qo6PzOPfCW90NwPRjs1guLuH8tRv48s8VXN83iYNzh/FgEsizjWRNoLJjUnxD9OzGDK788RmuToziKWMIt4cOYN74HsVcDq8+8aTy8WP3AqoVyAyP4ORXBfw2uSZMYBbPZglKK7dwKn8QR997JlkAmMj+VINgHZnC+59eg3M3j7WZUaQz+3ByYRXm1KNgg1mphLKxZT7CAv3XAfDL9AkMptKgzKne6d1rOS5WGwN9SB07/NDfz01tjyaATz+t0Ammh8Rmp9h8M+QfeZsomLSEAOCJA6ASIIbYu8LV0osMaMt2Nw4AlQD5qxttAvUf7QM0AN3mBHmME9QM6IA8oL0E6MAooE0gURPohDCYIAM6wwS6PAx2mlhhknBkTHW3pmMflvQ9opB+LBIAeYbc5y7yCp/OSrFv3mzeoJRXu+Rm5e044s25+mBEvjc3LcpRUQ5537tR5NuhEuHboizJ7z4DJCKrXl02yHQpAMQDYdWr65eloaXHxdD/G+xx0QBoADQAGgANQC/LfwIMAEzZwS/B41VsAAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("1246", "Barbados", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo2N0NCNzc3ODg1QTcxMUUwQUE4REVFNjBERTgyQkQ3QSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo2N0NCNzc3Nzg1QTcxMUUwQUE4REVFNjBERTgyQkQ3QSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBN0U4NzU5M0EyODVFMDExOUY2MEM4QkNDODU1NkNDQyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        mArrayList.add(new CustomArea("592", "Guyana", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoxODY0MTAwMDkwOEYxMUUwOTJFNEJFQTI4RjBCNkM4MyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxODY0MEZGRjkwOEYxMUUwOTJFNEJFQTI4RjBCNkM4MyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3MUJCMjhDMzhCOTBFMDExOENDOENDOEMyQzk0QjU0MCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        mArrayList.add(new CustomArea("1758", "Saint Lucia", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo5QzZCQkVCQzk2RjYxMUUwOUQ4RTlDQzIwMUY2NzhGRiIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5QzZCQkVCQjk2RjYxMUUwOUQ4RTlDQzIwMUY2NzhGRiIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3QkYwNEVEODE2NkRFMDExOEI5OUY3NUM5RDg4RUFBOCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        mArrayList.add(new CustomArea("505", "Nicaragua", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBOTc4QjMxQzk2RTcxMUUwOTUyQUQwMTQyQThERUVFQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBOTc4QjMxQjk2RTcxMUUwOTUyQUQwMTQyQThERUVFQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3QkYwNEVEODE2NkRFMDExOEI5OUY3NUM5RDg4RUFBOCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PoFtOmYAAASxSURBVHja7FvNj9tEFH9jO8km2W23hYYVYg9UFWqhCAQC8XWAG+LQA3DhgDhwRRzK38CFAxcOvVQcEFKl9tQKIT5UPiRo0xaVA5RPIfpFF2VpNrvbOLEdz/B7iU03TuIkqPZubY/01mPP2J73m997897EK5RSlOaiUcpLBkAGQAZABkBW0lzEBibkIAXvmFRmSIgDsbyjNLwGVnrbEwdP1Crbi4me8dpqi6rvHqigusZA+ADwzFcu1Zr05oEHyeXoMIEBoq4JeutwlasVjwVWHwO4stayqXHDgf7DEWBshOiv+9G0fz3Yb1QZ9axx9w17fth9ftuO2bx/aZunMxkbfECBe7quoo4rRxKAcwcReJOfT4iQkQ+7b1Q//1mT3jPqHcFz1s2brYLv54y+B7BnkAAAIkOTJDXl9Unbh/VVA15bTfWOm+esW7DVCFLF7khyIDKBWSLrFlTLGOgElBgpKacDQNDW95v2EJ2MoA3ZjgsGuFMDMI3DmrTNb+8DeoK+o/qwbsH9jwEfwCgxVcIA8B3UbRPteeO1J/MBMIHOOBO43fyD8nyACvcBjJLlumTBBNSUJjDp+r2ZhXUbawJtrJUWe0uZvFUg505gApYj4SySCQDrNtYEbNDE/h8msFXSWhXKgEET8O/bBXmgaZpfzszMxDBMHmkHselVVPA+rQyZjSWaaLfbVC6VnkP1AmS5jwEavJgWtSeTy1B8BXq2cayDg8BeroOffwCLxzGIndFugQX0M2JdjNzrmILzJIqPYbJnqe40SOsImi8yAcuknBUSOrJUY6GfLVECEpvynWukzFMkZp7szbIw6GT1OOSjHu1zd+MSgGh9R9T5O7ZJiQcAZSMVu9Kbeb277UDX/vqBvjl7mb46c5GWli54OxZoKz2Dvpc5eU0QAEz9zlXMcMW7YNLbp36lrxv30beN3fTOaQag5YEwjz/sIFcTBAA7OW3+v9d99uNpOnSuSRd1QX8Kjd4706Qvfq7eXCkYKOXEMrR4nCA8vpD/9DYl7Ov04fmj9Py+FXKFTjndobql0QfnKvTsnodIy+2Es2ySyO2IHwAOEiLJ8oxFTGy5G2oe/vQ4fXK2RYt3agi5NeQPeSY8fdxYpffvOEGvv/AazGAXKTE3mAvfktRcbQIAYjts+hK57i9kmXl6Ze/LZOiiL8d3FhSZzQZJ+3ewpQkQ7oofACllhCAg4mt/T2+89CJO8iM6OSTNkyQ0zH43FJe3fF+AdRzLgEgA0HeD1kuk1j4nKj6CFWEhsFIsI06oQvkSqeJTXkS8SSYQ2W5P8WmwAIHO+hFSuYcR+u7HJGP5c37Dy3HM7yWV3xMJ9bcGAFwKj0KgpGv2cgGtAMXvBUOYEcVIlR8LwMKrx0jMYRkyb0Q+kE1JmctzpNbrYXGAGlFPUlHjANgoSVQ+DID06Z8xIAMgM4GUI5D6z+QCvwwp729yGaAyH5D5gDAAupsCSN1LyTR4KUf6AG6xrFUkCvWlZHs9nT8Q634j2EXD35fizfpFyP2Qe7zzJBb+OpR/kPwJcoXPfQYwIjWvzh0KCQXA8kCoefXsY2nKSsqLyP5vMOUlAyADIAMgAyADIM3lXwEGADU0g6O+Sj/bAAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("54", "Argentina", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo5OTIwMTcwMTg1QTIxMUUwODMzOUU2N0UxQTIwQUI5OSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5OTIwMTcwMDg1QTIxMUUwODMzOUU2N0UxQTIwQUI5OSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBN0U4NzU5M0EyODVFMDExOUY2MEM4QkNDODU1NkNDQyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PpcPVjEAAAS3SURBVHja7FvdbhtFGD0z++e4iV1XiVN+AhVIkVpuqlbccMcFbwAvwAPxCFxwzTtUAnGDhLiBUiIBSpsAdQhp6sQ/+zPDGWfd2K6ztml2CeMd6ZNnd9a7c8535ptvxmuhtcYyF4klLyUBJQElASUBZVnmIkaU4NGC9NNWZShaROunn8pNGwzo2qdfftdqVD2rPX7UifD5x/ebrD43RAwJMJ5vPml38cndLShLk0NJvX/29Y6pNlMV9McUIAi8EyZo92NcxIHJnIUYrw+z6eH5yeuw4L1mfW/a/bO+N2yrBS7EWV9rKWa4IzEgMG0x3Z/wG5kq0HPUpx3Pe695FbjI8/QZNn2ueDlKwHmUMAQofWWHwajiFo6AU0C5kwTGvHusry4BeIV+GVw6k4CBTDg/JGRrQZpfxTNFFYNtso/uJL2xUukQ0JfoNM2EQyzcNmwfT1xmX3vRNQbbpIReGgIGfMQLMwkQ/0aK+pJ0rRe/Ju1vouYZAtoQcLkKuDJDwMQAPSMIRokemI0EGFyZCjCtxvuxpQow2JCtAI3QxIBEWUlAOIgBM4JgyAAYqv8fAcPIrzOCpMGlL1gOb9DeO2p3HlxbqSBP6IIJg8nItO4NHq/ZKSElz/uQ/Mxzm96APe320Firfsjqj7SDMQVUXAHPEfm6SnW4KA8JtAUdPybZG3CEQ8W9xrYInn8z18cbjBmJUL4lSXpIoj3o5Fdq9YQeOaXndxGHVargd5IQkJAArt+AKKhPhRGQRC0k8S4l/4wHOyThN9of1KWRfwAl34V0b1MVO1DiFhxv0x4CtGZ6nYRci+8T9GOo8Cue7NLbEoozjuO4BH7Icz0CPyVRDS5W6zxfsYWAiOP7gMGvQ6DfUw2nBNdnnSrQjN+ixjT1bYjkByRinW1PqZQ6YAsBSsW0YwL8iQe/0MPHSMJH6PYk6jWBg9Yubtw4QdRz4a9uk6BVOHK7mG2yYjYxfEo9RpzUENP7cRhi9ZrCBidfKRW23hI8H5EkSXIOOTQ2C9uPmFgM6VzmYa3ajHWc6mIz1zfgVQSiaJPn99HvGhKYe4gm/CBkDHiH9b9JWjefvmhdPAFC1ohyC467Ry/f5hT3AGHSRBCsI/AcPD8BfP+YwO9yHbIC6dyj3SyeAGVSxZxIkM4a5b3OCgnwGdyib9DnUFAMgp4fQ3r3OBW+zzH5Br1vnu8M+nP5WaiarYB8VFCHdu9zfFcZ/RMguMVA+DM8z6TejBHidV5DgtwPSEYzXbf/R0Mgn3xcEux1evkOYoJF/JCer9MjCcd9jQRsQzgrcN3rUztqAQHn8cALGBMIWkfPIFwmPYJ5glzD2XY9cn1+JgEfffEtqut19Ns9RuiilsN/FrYWqdRX0PnrOGtHSI+bbWUKrpe2xcfNOgaQvS0+EgNsfH9wGq6lHwJL/47QFAXAYgWgDIIzgiAGy1BbBaD0HL8OL7UCzuixWAIoZ4HZs4BZqnpV30qwg2X4BbOA2SXot48Ocfx032qPS3+w09xPMb/4Aca8N7dFu0N7Mz22sZi3Q/doD2lPzPFQAYaRVlrfe7Ewt6/0UxJaab18WRplWfIiyv8NLnkpCSgJKAkoCSgJWObyjwADAAmwyNCd+At2AAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("595", "Paraguay", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1N0YyOEU1Rjk2RjIxMUUwQUJGOEI5ODY5RUExRkI3NiIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1N0YyOEU1RTk2RjIxMUUwQUJGOEI5ODY5RUExRkI3NiIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3QkYwNEVEODE2NkRFMDExOEI5OUY3NUM5RDg4RUFBOCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PitpTg8AAAS+SURBVHja7FtdbNtUFP5sJ7Hz26TJMvoHY2J0WlUegA4xNDHYJgQMJFRp4nnihQeENIH4kXiYeChISDwhwRMST0MIygMw0JBAUIHYC5M2WFc6IrGu6k+6pU2T1Hbsy7mZg5Kw/HRd3M7xiY7i2Nc393z+znfPdRyBMYZONhEdbi4ALgAuAC4ArnWyCRVM8JLL1rtTmWGS6+Sq9W56rAM86MiVQ08tSImEo6+4kU6j7/tvkrS5woEoA8CvfDJ3KYXeF44RLgacWCAKkoTZN97im0mLBWoVAxgYjGwW5vIy6iLA9wtC9Xa5bXl/bbt6Vq+vZufdqP9G51nHxGgUPEYeqxUzPBUaIPOGzCiCFYutAVC5rxaAVs6r167cV6vntAgKj83qXy7rnKe6A3IePHfT3KI8FnDT+Vm6sNW7PKhFoGhcZ8BWBWAjRrHVIvA/BjBNB9P1dQMg0Ithaysnj60xAzi1VI1myPUzgK0nN9ejDbV0b6VtvTY8NsYaA8B0jZDSGgOwkTzcRN3gsTUH4CZT4Haw6ynAmqUA1QfNGLCRaW4zjcfWCAAuYiY1Mku54jwGCBQbQ0sMUOHIWrgZA0oawFHStNsPgMqSul6WNgPgrtQkFEWxX5wq1uXttvDaGuD316sE7TGNGJbOrlJtzkqCK3q90OnKRMMBRIJBW8diOwDLq6tIL2UQOH8GytRPMLQ5EicF0vbdyOw7guVkEn3bEhBFe+7J2HrnZ41ycHHuKrq++wTBUx/AiPdA3DsKc+9BBLJ/ITz+GoR/UriymLZtTLYCMH9tBeGpH+D741ucfeZFfLFrGFMD+3AquQOf7T8K/e4hBL8eQzGdQTafd1YKmJTjYkGFb2Icsw8+gZMrc2Bn+nHy/KeIxLahe2QR8T2HcHD6N8iTP1MqPIdwIOAcBuRIfbMzKXhnLuL0fBaZBQm/TPyK1599F0HtY8yfjeFCfglFhCGmzkHy+pyVAlzURJEI5zchhxgkSYdmbMfEuW7MpuPwBWk5TdWnyIoQZI9tZYhYXQextrlCU523vx85aQCP/Z0HC63i8NNhTK2M4c77jiDT+zuGzSBy45MoRAeh64W2jWVTAOCFjqh4kR05BuWjz/FKJoTYIPDyiSE8MprEqw88ifvHPoSWHEZh5FEkwiFbAKgSQZNWgDdqdKusJxrDpQMPw/PmccTffg/PD94D70PTOLB0FfLpH6Ht3IH8Oyfgj4QgE2PMW7wkF6hcru3TU48B7TCJdGAgkcDlo6OQhofg+/IrCBenIXp80I+/BPXw44jQ8Z54vG1jaMiAdgPATZFl7OrrRaYrgvnd9xIoAjRdR6wriiStQ/yKv63f3xCAgkoKLBWJJu2VYK4HQSWEnT0hGAYpP1GzRE8a3BqNgbVtJhJKMdYF4I797wNyEtAzjrwhAl83oC40qgRZjTvuriCa/i7QYfG7DOj4R2RcBrgMcGeBmuOmg+M3W5oFOgsB90lRVwRrU4DRYkEKOjNaHlsdEeTJoaKYA7RrDud86WarasX8309y/Lm5AfI95P3WZycafzp0hvxP8sv8c5kBHJHyOpE3kB0KgGqBsGBtuw9Lw7UON8H936BbCboAuAC4ALgAuAB0rP0rwACktVAyytBbMwAAAABJRU5ErkJggg=="));
        mArrayList.add(new CustomArea("55", "Brazil", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo5QTdFQ0Q2Mjg2NUUxMUUwQUI2MEZBQkQyNkY3OTAxNCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5QTdFQ0Q2MTg2NUUxMUUwQUI2MEZBQkQyNkY3OTAxNCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBOEU4NzU5M0EyODVFMDExOUY2MEM4QkNDODU1NkNDQyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3QzE2MzA2QjNCMjA2ODExOTEwOTkzNkE0OUQ5OUE5OSIvPiA8L3JkZjpEZXNjcmlwdGlv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"));
        return mArrayList;
        /*ArrayList<CustomArea> mArrayList = new ArrayList<>();
        mArrayList.add(new CustomArea("507", "Panama", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAJlSURBVHja7JfBSxRhFMB/Mzs6aOqiZloaQWwsISmhXgKDoC7dIgns3B/hpU71T3QJT96Cbh6isyRCZLBoUR4sTMuldHZ3Zr7vex1WWcud3Z3R1osPHjPDN/P4vfe99743lohwkmJzwnIKYAEpwN27NlM04DtAl4hsJ7WyMT2d0HWL83NzPQ6QPoobbZOTib4LcjmAtLMX/kjZ2i7Q19MeHcfNzareAbBf4pZVuQfsdBrCEMCtm4QvX3+suS6+f1hLpbIefP53vQyAU83oj3yBt8vfWP2yzfvVPMWS4tKFLm5cH6S/9+9omGIxfvxbWjC1AM52t3P3Zoa1r+/wvIAdL+De7SvVI5AAQBwHlIoG2Jf177s8ejDCqzefoo15XnwAQIKgNoAxwv07GcaGBxga6KDoK9rcw6+bJAAi4Pu1AWzbYmx4AICrl3sjjZlCIX4LMAbqRaBhbzyvUnb1XS+/qxTSSA40ZDNJFYQh2HYF4OKt5+wWAkJlYtvaXVo6wklklQGUViht0KZ5w8n+IGQDhMogUs78uFqz3y9/aAxAK4MARiS21pKfTx5HV48xlRwIlQZs5Bi2IFhZoTA/T2lhgdLiIhvTD3FHR+iYmqIlk6kOoLQm5aQ4jhRozWZpzWbZyuUg8AGhe2bm8Cmq9cEt0CDlfYmrkZX2eY1zL2ZR6+v1t8CIabiXNDZsaXqfPcWdGMcZGkR8H8t1owHkuAFSKdyJcQDc0dH6ERAjWJZF55nWpvWBgwAahJ3fvxIZyufzR2lE2gL6gWtAX5PH8i1g2dobSjvrDaf/QXxgxzr9OT1pgD8DAEyHnVA/7bwEAAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("57", "Colombia", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAGESURBVHja7Jc9TsNAEIW/sU2iSIFISIiGK3AGjoC4BRUH4g7chI6CQ4BA+Sfen0dhQ5xAJBomTaZZr7XFNzNv7LcmiX1GwZ7jAGBACfTb1TMSsKqAkzS+fkPOxbCCcvRwWgEjZHB05Zz/M8CoasqfgBo0BtTpDp19t2v65Xn7DLvP2QhUA/Sr5k0GLUEfTul/AAGAquGMGEtg6QRwtAkAETQHLZwASrQBoAiaNhBuUW8DzBwrINBqswXS1FGECfKGBkLbgtWOUdui/zFq9vfMsVaAcUuEefatzP/vQA1WdjUQWgEGvzG0kzXAxc0t80UgJh9zUpXG8bAH3DUAMSQkyNkHQIURQ15XICUhIDvZMwEhpjVAiBEokFMFkEixU4EYE2VVkt38qRFi5zvw+HrPoNdDS5+fkQ0GLELN5ZcnTDlhZrjlb0bOeW1KswSOAHQAGj/QAhTDoRvD18hXQMqC98nE1RK2ek8GnAOXwJmzLX8Bnqy9Exy3q2esgKkdLqf7BvgcAIbCtO8QpVy1AAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("506", "Costa Rica", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA9dpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wUmlnaHRzPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvcmlnaHRzLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcFJpZ2h0czpNYXJrZWQ9IkZhbHNlIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDpkYWE3ZTgxNC0wMTVjLTExZGMtOGNlOC1jOWE5MjcxMjRjMmMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MkIxRjRDQTI3MUFBMTFFMjk0MTQ4MkU3NDQxMzNFNzAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MkIxRjRDQTE3MUFBMTFFMjk0MTQ4MkU3NDQxMzNFNzAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBXaW5kb3dzIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NEQ0RUQ0NTkzNzlERTAxMTk3Njc4REM4MEEzNTc1OEIiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDpkYWE3ZTgxNC0wMTVjLTExZGMtOGNlOC1jOWE5MjcxMjRjMmMiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4n4unsAAAC70lEQVR42uxXTWsTQRh+NptNto0xRelHSmuoH7VBC55a8GCh2JMGwYOiqBe9iD9A8OBNBW9exHoQexDEQ0GwKHgoHoq0UoVKaVps2opaS+1Hvnd3dnec2U12Q3sQa91c+pI3szO7k/fJvO/zzKxAKUU1zYcq2w4AgbnIPFhqvTSDuepnX7tPXh1YFUVvF8MnCHjdf2kPBxDxiQJ6u9s8BTD5ZYk3EQ4gqOsGFJUgnVXAWSmUk8ONWh+ra7VCxRi73sjiyrnle+U5pZ9DXViGqum8G/RbyTAoiqpuuRdWZME1YjqADxNCktVggCRJHX57qaqnhhYA0zSrC2Cq9yykaAPMgrLpgWBbDHLHfijJFNS5he2hYK0MI51zARRXUyB0HaaiOLWswkRjvBs1Vy5jbvE7Dh7rgn73DlaWZhGopAg2UMa5ruxTBMKtLgDDACWaC8Bf08w0WWZ0Ua1Bkl9D/aE4oo/u4/ncR4yr0+hpiSIx8BgkcR5KJgcxFHK5Jgh/lr18wXnWJDqoTipqQNdh5vIMlT2oZn9ib891JFensTD/Es0nupGaeIYfRy6g7ngP5l/0M+1u3br+ayxOSXltACywUSgyACUdSCvQVhYxODaKhZVhXJyZxOD4GN78WsaZYhOM9TxLWWbrACQ/xPAuFwAhbF8wdE4H2KwIIDfyCb2Jc7i5TnHrvQ+RxjiuNXQh/e4hy2oY9B+YQ1ms8p91ihCq6uSF2/ToEI7ea8KTG6cxnB7CqWgf6O2nSKY+sGphG+fa32lHMBJzV4CymtE1VwnftnQmfYYJqpFNE2vi7Qge2Ad19iuKUzPbcwYISDBFEX3fJmwlbB95xWXRUwEivOBjMTsFOmOBKIreAyjXAAfg9QrwmA6AeOIBZDmEgkI8CV4rS1CVggvAphTdfLr4b0YZ1Y0KACwwV9NQrXdpKB8B/NbplJrIZzPw1iwABteBRuadzOs9RrDM/LNQeicIl1ovjW+9WWHn5bTaAH4LMAB7cz4QWUbMcAAAAABJRU5ErkJggg=="));
        mArrayList.add(new CustomArea("52", "Mexico", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAANrSURBVHja7JfNbxtFGMZ/s17HjvCHWlJckqpKVRqStqkEUkulKodKCKkcAAkOIHHpgTP8AVz4J7hx4gC3coELXwIkgloEgqatK9qmYBHU2M2HnfV+zrwcdl1v3NQ2InIufaVXo9ndmXn2eZ/3nRklIuylWeyxPQaggAyQS9pRmgZ8Gyi9cemDtYw1mIxPX3l/qJlX3nx7CO4tpj75eL8NlC2lWDh0MiEEQFIEpfvD2fjCuYHfBDduApRtIKfF4OuITd/5v2vH3K42dviR7kRWuYQEPkDOBtDG4EU+fhTsSnDF9/u/93wINQA2QGQ0Xhjg6gCk+/Md8NL7bBAAz011HiZBxmwkCrsAtBicyMMN/V1hwLTd/h/YGUwUbWegFbg4offfFwt8rLHc9meOM3hgEHQBhCbCCT3aQzLgRgGNv+5wd/EL7l37gaeeOcWZ198jX94XU9xu9weNIGkAHQa8HhEKgkI9aDt27e51lj/7iJVbl1G25sZijfv1Gq+++yFWPo9xEgAioFS37chBG8TbAYCvg0Qv8aIIKLVdfs3NOuPFMqVnn2Nl6Wu28oashps//0h18RLHz7/VBfCQOmMgKowgLcLIaJzAJTRRX+rWvAahH/J79Rcqy7fY9FqIzvHChYvcvn6F+laD1dYq0h6ggTCATKYHQOgSGt133NU/vqdYUzTbdWanjjB7aoFq9Sdu//o5M2deo3xwhsvVbzi+sdEfQDZLplhMiVBrtBZMOuF7KyJQHt+Pe1SQK3/y5T/38A+fZl9+AjdwkI0x/vYbHKwcRoz0L6Nao5M0tOJKGGKUwWAwkjg9fTEcO/Q87WaNmblZtqZeorD0Fb8tfUtm8ixr0wVCPOanT4MxicuOrowgYQpAqA0CGJG+/kS+xMm5F/G8GpXid/D0MU6cOM997jA1cYCz8xfIZfMpAI9wESRMZ4GOkIwVh2CAVQqTvHzuHVaayzhzDuiI7JNFpieOdsVuzIBaLURRei/QGtu24tQb0iZLR6D06AX6H4PUAw3YAMYYENi1E/IQ80iScVYMQKMUIzOlFDoJkwUgenupHQGCmPVOCARBKUVhbHx3jtqFwuBdNAmTDWgRobWxOXDQ+vr6UADWW80hdBKXJAVUgHngwIiP5XXgqkruBMWkHaX5QEs9vpzuNYB/BwA7w/0Mp44rhwAAAABJRU5ErkJggg=="));
        mArrayList.add(new CustomArea("1809", "Dominican Republic", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA9dpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wUmlnaHRzPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvcmlnaHRzLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcFJpZ2h0czpNYXJrZWQ9IkZhbHNlIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDpkYWE3ZTgxNC0wMTVjLTExZGMtOGNlOC1jOWE5MjcxMjRjMmMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MDkwNTFDRUJBRDlCMTFFMjhEMTk5RTM5OTFFRkYyNDEiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MDkwNTFDRUFBRDlCMTFFMjhEMTk5RTM5OTFFRkYyNDEiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBXaW5kb3dzIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NEQ0RUQ0NTkzNzlERTAxMTk3Njc4REM4MEEzNTc1OEIiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDpkYWE3ZTgxNC0wMTVjLTExZGMtOGNlOC1jOWE5MjcxMjRjMmMiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5LInc5AAACyElEQVR42uxXPW/TQBh+znZw2zSuVKmiAqlQKVIBqWOFGMpQUDuxsbEgIcQMv4MfwMDIxl7BwIQElRgqBENBdIABtY1aNR+2c7kv7oyT2GlAvlRKl77K6/PZ99rP+7wfOROlFM5SHJyxnAMgWl2tfjqOU4RW6ulDcPfx8yPXsSPjzYunufmP+4/sPNfvq75+OWsAzDiEYO3m9VO5U7l9y2p9e+e7GWYMAJ9LgTbtoN6MYKqSdINjRCW/ZJqM6clg+bL9g4x7qXF3jZln1rtBANFm5tT3kmBwhVgDiCkr7MEgAHnCVmXSLL+WUArF/65PADDBEcUUUZsOMRg+l1LmGYiiwuCl50F0On0AnHO0IoowpiMzoKK4uK3rQjGeASAEmmGEMKI98shQy/6NkwyEFvWnICjNhECjqbfiNAQFaRwAwFvFQ+BoW0EzIWDcMBDrSmCDPPczmJDcdQMgimOE+tbc1CS4ZvAEdbnyyQDQjCvW6WXUUqPR2LGt+93dPbz/vI15doyl5RUs3qha944gCK55w+gskEZ4++sjaoc1XAkPsYk9PKg+QeBNWIMYCUDMKb4df8LqfhnbchavyCY2WvcwHVwdDYDQMSGEFDbynRLWj+Yhfm5h7sIEHjqLWJhesHKkW8YJgK8rG0CH9mqziNz58A7h6jqmSi5I+TIurT0r7rXnYNIvZRsRg2P6uyy+PZMVD5OVhSTBjUol89mu/r0BUNIBZ5lWzJlASVsoCwoH6VbCIo9c3QdENgckh+t42gs1MgAbW8MOEyzfij0NQJ0CgFI2lSSTxO93Qv0wn+juhtEBwMI2aY4yA8DQJ/VlKx8GAdiEwDib5kwPgDKwdE8n/9lW5Np75oXmvDzl2/VS1Qdg/hpQD1tWD/h9sJ+bh83GSDtj48xFrcta58a8La9p/ULSb4JKOo5TzOajSc4/Ts8awB8BBgCC2XbTrj3arwAAAABJRU5ErkJggg=="));
        mArrayList.add(new CustomArea("1246", "Barbados", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAM5SURBVHja7Jexj9xEFMZ/s/buXnLJbXIoQqkiRaBFUQgpqEkU0QQpfwA1IilQaCiAAoT4DwBFSp0C6FKkuIKKgooqICKEApGAE5BIl7vz+rz2zLxHYe+ubZIdN9w1mWZszbxvPn/zvplno6ocZOtxwO0ZAQNEwLDq97N5II+Btdev3NyKorAYd77Y6IR85frlsPTGsHH77fUYGPV6PS5deKESBEBrAi3ezeHXOhG4dOHFKqaNt8D86d5fAKMYGDov5LlneydDVcsppgpWXUD5h9VTneB/bfzPw6QKrYgbA9WzAsdHh8hzDzCMAcQJWVYwndrlnyZ5JwVCONnUYp0HIAawXsgyS5YFCGjWiUAIZ9CPKOoEnPVMUku6F1JgrxOBEE4URbiiTkCEJJmSpvl8n8yTBJC08f5oq9zXE+vN2TOcZd4v8jqBwrObTEn3iqWBf2wmbO3AKy+Vlr1xq1zok3eHANz9WVgfQTJZTkBEyQpXywEnJJOcrJ08Wkt0A8fXMt75aI9rb/a5fHHA5mYKGMR7Nr4tuPllwVefrS4+pBVfJ2DrW2CdsLs7Ja9YzYO0eVivrqS8f9XzzXcZb1xc4ezYAopB+P5uzgdXY1ZXUiYzBdr7KCWetb7pAmc9iXVYK8tNICmvnoUbtzLEecSXiomz/PJrxofXDqECabp8K4u+I46jmgJWmKQ51vqAC7bpR3Dq5JT7DzK8LQncf9Dn1MmCfpSDwOPt5Xbt9yNGays1BZzDecVLoDjRUqHxafj6zpTfftcq6z3j0735eAjHeG2eA9YJqoKIBBxejp8bG364J/z5dwny3LGIc+NoPh7CUTU4W3OBdw5VRUIKVAucP2M4f2bAp5/nYODj64PGeAhHVbG2YUMP2kOl7RttRzY8unpYKrdI43JS0fIy06fcrAq+SvgqBzxRHCGNAH2qArP23luD1tyyF22TbeEZg/X1LfCeKFbCFXK3CroLjvd+UROKCOaJp///VQeaeaL2ZoyN2UcCZkEgXhCAo0eGgULuSKcFgjiA6oKAByVJdoJBj7eTTgS6YM0qYwM8D7wMnNjnsvwR8KOp/gmOVv1+thxIzLOf04Mm8O8ApMbMX0VAnjYAAAAASUVORK5CYII="));
        mArrayList.add(new CustomArea("592", "Guyana", "data:image/png;base64,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"));
        mArrayList.add(new CustomArea("1758", "Saint Lucia", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAUCAIAAAAVyRqTAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo3NTFGNjkzODE3ODYxMUUyQTcxNDlDNEFCRkNENzc2NiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo3NTFGNjkzOTE3ODYxMUUyQTcxNDlDNEFCRkNENzc2NiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjc1MUY2OTM2MTc4NjExRTJBNzE0OUM0QUJGQ0Q3NzY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjc1MUY2OTM3MTc4NjExRTJBNzE0OUM0QUJGQ0Q3NzY2Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+RRKY5AAAAg9JREFUeNpi5A5cyEAbwMRAM0Cs0UwsTN++/fr29SeQQaQWFmIUMTIw/Pj0Q1FaAMh+9OQDBw/b//9UcvV/JsbfD95OzHGcV+r+5+Hb/yC7qGT092+/xMx12d9d5nh3Ud7e6PuXH9QxmpGR8d/zT+lh1stndk5oLcsIsfj38gtQkApG/waGq4ioFvfrAwf279hzXJHpMbOc7K+//yg1Gui2n68/+/pbMzw9cv/1zw+/GP48OhAZaPnr5SeC7mYiGIEMX/6FmQiuWbKclZGBh5Vh0bwlvjocDH9Y/hEym4DR37/+VHUw+vnw4todu06s5jq/jXfXkeMvLp7QcTH+/vkH+UYzASPwxedQe60tK9bFOTIYBQupuPDn+DHsXLs+xErt/6svTEyMZBr9698/Jmmpb/efmkuvWbiE/f+t3/+v/548l8NZY/O3e0/YFeR//vlLjtGgCHzzUc/QMdLkUkH4MwYmgb9f///98Z/ht0BG4Ksw4+Omxk6/Xn7EE+A4M/pfYJr7IV5ifdHMtpThFdCeVyzi4Nz9h4FLk8FYtLz0t9SRzTJ//+IMFpxG//nLyCbF9+3Vyh3b2f5+c2L4j5yQmZg573/+tJJTJuL3r2/sTNgLFEY85TUT858vn5kZfggxsGBo/sPEwPGOl/f3379swCRKcsn37y8LFxcDA9d7nIH2lxWXuYOjKiADAAQYACDo1+pXfbuqAAAAAElFTkSuQmCC"));
        mArrayList.add(new CustomArea("505", "Nicaragua", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAIxSURBVHja7JdLaxRBEMd/1T27mxBjYlATBEUQjSAr3rwJOfod/CqCp/hBchNyUE85eMtBQU8SiFdFwVfYPHazM9td5WEfGYcVdgN2LqlLDU1N1b/r3WJmnCU5zpjOAQjggcaAp6QI5Blw8fGzN3veSVrXi/Dq6dpSBix4EdaaK0kB7HxuASxkQCOokYfIfrtgWJUycEi1SkX4S2ZcFVf/rcotztUpggI0MgBVpZsHukVMcvtuEQixbysD6EWjk0eO8zQAat5RBDsBEFVp55F2Ig947whRTwCEaBx2CtrdkKb2zch7OuoDq2a2O70aA20PNM6AZNMDEbmbneYGFltghwQFxFHjO7hl8BcStGIrIHxBsuu83H7H6+23ULsF8SvY9CGc3gNhD/FL/Gj/4vn7I7zAo4e/udy4BHEPsqv/2QOujvk665sb+N4WvWKL9c0N8DPgZk81jFZVdVdk0lmgaOcDO9+WiDoHApk74t61fWTmPkhtskia4ZzrJ6GZMTkAh2vcpnnjE9QX+tUQcpCbExsf2hzlgKri3BTR8IvgHmB6jAD4O1MZH9ocAVh58gJ1nqKnWCk2VuLjYkdJhopc9aysp1FzZMRyJ4x459HSyLIxSittaOz3v86q4IoQyrMg4hHSLchCHAwjBxCjkXIfEkBNTwCYRSQhAhFQLeWAGYgI87O1ZCDKZRgx5eCglXYn7gOIAiwDTeBK4rX8J/BRBm+C+QFPSTlwKOeP07MG8GcAv2TyFzMG8/AAAAAASUVORK5CYII="));
        mArrayList.add(new CustomArea("54", "Argentina", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAHpSURBVHja7JfPahRBEMZ/1dMzGyW6IIh4EgL+w38Q2Gfw4DPkCXLzNXwMfQffwbAnwehBETwKuiRLNtMzVeVhRzNZPLgj6b2kLn2Y7p6vvq7v62pxdzYZgQ3HJQABCmDUjTlDgToC11++/fAjBMme+avnD29EYCwCu7fHWQF8/XkCMI7AyIDGjHlq+a1K6QhZVakI5+b8TcWra1fnbVeRZAYwigBmTt0qqbUs2SdVtEMUAdScWo1a8wCIKjTdvyJA684iGaeNZdK+oNZjwNw5UWWhms19UgdAgPvu/nHQRu7LHRgmYRF5EIel4GjzBUuHgBKqxxTlziAggwC06RM6f4PpN0AI9RTf3iNWdzMA8BZLB7TpEE2fASiqOdJModwBKS6aAQc/xfSIWMyWpq7HYIvltyEMuDsi/3h+UhLKCbE6wDTgXhDLO4RqAhLXqF8fCACIW0+BfSxNcTeK0YQ4erSmgPo+YEYIYS0hx61neHUPBESurE29Wc8JX7x+B7H8Y48XHWURQJueFatSFpFcDaq407bau4zcKGVIDQ/vRtT0rCf0XHfAuRrwMwBmtpYK/psAEcy0L8Oli18t8/Wl3ruOFTeOZrPMh+AAKsAt4AlwMzOC78B76d4E17oxZ9TAsVw+TjcN4NcAEDjiEwBWF/0AAAAASUVORK5CYII="));
        mArrayList.add(new CustomArea("595", "Paraguay", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAIHSURBVHja7JdPa5NBEMZ/8777JlipkaCI6CGC0IIUPRU8VKHi3Us/hVe/heLXEEFvngS9eBKhHnpoDkKhCEUTU5PQvP92dzwkTWPwklfYXDqXuewyzzyzz8ysqCrLtIgl2zkAAWKgPvEhzQG5AS4dPdnpEQWOHwnX37xqGqCBRFx4uBU0frG/D9AwQF2dR/Mc3x/AqSxFxn5epiJ/n/mXjOfvzp2LGg3IS4C6GVfDommGZlmQ7DXLUFsCMAHg8GmKT9Mw/CcJvpgBoNaioxEaCIAaA9bOAnDocIienIQBAGhRTPvAmqq2qwpZp3Ws0IRE1k3VDI5//MR+/Qh5gdx9QLPVqtTJKrXifrdL9vYFR2XB4coK6buXHB98q8SCqZI97c98ccLr3S4+H7Fzp8X23ifKW7dJQjDgncf/HiC1iLIeU3YG+KFDqpZgkaVEgGhji3vfDU+LDs+aCZu7h7j7jxai8zSmAGvOuXYULUbGr16P7P0HNM2oPd7mys0bC9HpvSeO43UB1sqybBtjgsrQWkuSJGMZXt58jhKRFy5II6rXYmLRMxVY54hNjA+1n4pQzg4jZz1xvNhj/N9e7Jw7U4FXNx3hgQjAq5+VoUcCIhARvPcz09CPUa1erAUDof7sETpQhoP+MrZyJ8A1YAO4Gjh4B9iTyZ9gdeJDWg4M5fxzumwAfwYAvejoYHyrhakAAAAASUVORK5CYII="));
        mArrayList.add(new CustomArea("55", "Brazil", "data:image/png;base64,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"));
        return mArrayList;*/

    }

    public static String getHashCode() {
        String smsHash;
        if (BuildConfig.DEBUG) {
            smsHash = "fOZuIutuAAQ";
        } else {
            smsHash = "Y8Eb8n2uulT";
        }
        //smsHash = "fOZuIutuAAQ";
        return smsHash;
    }
/**
*
C:\Users\User\AppData\Local\Android\Sdk\platform-tools> adb install -r -d "D:\Hash_Sms\ClientDemoOne.apk"
Performing Streamed Install
Success
* * */
    boolean verifyInstallerId(Context context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }
}



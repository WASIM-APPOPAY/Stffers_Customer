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

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class Helper {
    private static final String TAG = "Helper";
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
        return null;
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
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_grocery), R.mipmap.cat_grocery));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_resturant), R.drawable.restaurant_icon2));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_pharmacy), R.drawable.cross1));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_mobile), R.mipmap.cat_mobile));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_men), R.mipmap.cat_men));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_women), R.mipmap.cat_women));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_electronics), R.mipmap.cat_electronics));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_tv_amp_appliances), R.mipmap.cat_tvac));
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
            String avatar = result.getString(AppoConstants.AVATAR);
            return avatar;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getReceiverAvatar(JSONObject object) {
        try {
            JSONObject result = object.getJSONObject(AppoConstants.RESULT);
            String avatar = result.getString(AppoConstants.AVATAR);
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

    public static String getDateOfBirth(String dob) {
        //milliseconds
        Log.e(TAG, "getDateOfBirth: "+dob);
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
        Log.e(TAG, "getTimeDateOther: " + simple.format(result));

        return simple.format(result);
    }
}



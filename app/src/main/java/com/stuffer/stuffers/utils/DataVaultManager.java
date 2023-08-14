package com.stuffer.stuffers.utils;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.activity.contact.InviteChat;
import com.sybase.persistence.DataVaultException;
import com.sybase.persistence.PrivateDataVault;

import java.util.ArrayList;


/**
 * @author
 */

public class DataVaultManager {


    public static final String ISFROMSPLASH = "isfromsplash";
    public static final String KEY_COUNT_NOTIFICATION = "key_count_notification";
    public static final String KEY_USER_LANGUAGE = "key_user_language";
    private static final String KEY_LIST_GIFT_CARD = "key_list_gift_card";
    private static String DVPassWord = "appopay";
    private static String APP_VAULTNAME = "APPOPAY";
    public static String COMMON_VAULTNAME = "com.cooptavanza5477.appopay";

    public static final String KEY_NAME = "name";
    public static final String KEY_CCODE = "ccode";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_ACCESSTOKEN = "accessToken";
    public static final String KEY_BASE_64 = "base_64";
    public static final String KEY_USER_LOGO = "logo";
    public static final String KEY_WALLET_ID = "wallet_id";
    public static final String KEY_UNIQUE_NUMBER = "uniquenumber";
    public static final String KEY_USER_DETIALS = "key_user_details";
    public static final String KEY_FIREBASE_TOKEN = "key_firebase_token";
    public static final String My_CHAT_USER_CACHE = "my_chat_user_cache";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_ID1 = "userid1";
    public static final String KEY_IDPATH = "idpath";
    public static final String TANDC="tandc";

    private static PrivateDataVault vault;
    private static DataVaultManager dataVaultManager;

    private DataVaultManager() {
    }

    public static DataVaultManager getInstance(Context context) {

        if (dataVaultManager == null) {
            dataVaultManager = new DataVaultManager();
        }

        PrivateDataVault.init(context);
        if (!PrivateDataVault.vaultExists(APP_VAULTNAME)) {
            vault = PrivateDataVault.createVault(APP_VAULTNAME, DVPassWord, "salt");
        } else {
            vault = PrivateDataVault.getVault(APP_VAULTNAME);
            checkVaultStatus();
        }
        return dataVaultManager;
    }

    public void saveName(String name) {
        checkVaultStatus();
        vault.setString(KEY_NAME, name);
        vault.lock();
    }

    public void saveUserMobile(String mobile) {
        checkVaultStatus();
        vault.setString(KEY_MOBILE, mobile);
        vault.lock();
    }

    public void saveUserEmail(String email) {
        checkVaultStatus();
        vault.setString(KEY_EMAIL, email);
        vault.lock();
    }

    public void saveUserPassword(String password) {
        checkVaultStatus();
        vault.setString(KEY_PASSWORD, password);
        vault.lock();
    }

    public void saveUserId(String userId) {
        checkVaultStatus();
        vault.setString(KEY_USER_ID, userId);
        vault.lock();
    }

    public void saveUserId1(String userId) {
        checkVaultStatus();
        vault.setString(KEY_USER_ID1, userId);
        vault.lock();
    }


    public void saveWalletId(String wallet_id) {
        checkVaultStatus();
        vault.setString(KEY_WALLET_ID, wallet_id);
        vault.lock();
    }

    public void saveUserAccessToken(String accessToken) {
        checkVaultStatus();
        vault.setString(KEY_ACCESSTOKEN, accessToken);
        vault.lock();
    }

    public void saveUserLogo(String userLogo) {
        checkVaultStatus();
        vault.setString(KEY_USER_LOGO, userLogo);
        vault.lock();
    }


    public String getVaultValue(String vaultName) {
        checkVaultStatus();

        String credential = "";
        try {
            credential = vault.getString(vaultName);
            vault.lock();

        } catch (DataVaultException e) {
            e.printStackTrace();
            return null;
        }
        return credential;
    }

    public void deleteVault() {
        try {
            if (PrivateDataVault.vaultExists(APP_VAULTNAME)) {
                PrivateDataVault.deleteVault(APP_VAULTNAME);
            }
        } catch (DataVaultException e1) {
            e1.printStackTrace();
        }
    }

    private static void checkVaultStatus() {
        if (vault.isLocked()) {
            vault.unlock(DVPassWord, "salt");
        }
    }

    public void saveUniqueNumber(String uniquenumber) {
        checkVaultStatus();
        vault.setString(KEY_UNIQUE_NUMBER, uniquenumber);
        vault.lock();
    }


    public void saveLanguage(String language) {
        checkVaultStatus();
        vault.setString(KEY_USER_LANGUAGE, language);
        vault.lock();
    }


    public void saveUserDetails(String jsonUserDetails) {
        checkVaultStatus();
        vault.setString(KEY_USER_DETIALS, jsonUserDetails);
        vault.lock();
    }

    public void saveFirebaseToken(String newToken) {
        checkVaultStatus();
        vault.setString(KEY_FIREBASE_TOKEN, newToken);
        vault.lock();
    }

    public void saveComingFromSplash(String isfromsplash) {
        checkVaultStatus();
        vault.setString(ISFROMSPLASH, isfromsplash);
        vault.lock();
    }

    public void saveNotificationCount(String countNotification) {
        checkVaultStatus();
        vault.setString(KEY_COUNT_NOTIFICATION, countNotification);
        vault.lock();
    }

    public void setMyChatUsers(ArrayList<InviteChat> myUsers) {
        Gson gson = new Gson();
        checkVaultStatus();
        vault.setString(My_CHAT_USER_CACHE, gson.toJson(myUsers, new TypeToken<ArrayList<InviteChat>>() {
        }.getType()));
        vault.lock();
    }

    public void saveGiftCardList(String listCard) {
        checkVaultStatus();
        vault.setString(KEY_LIST_GIFT_CARD, listCard);
        vault.lock();
    }

    public void saveCardToken(String token) {
        checkVaultStatus();
        vault.setString(KEY_TOKEN, token);
        vault.lock();
    }

    public void saveIdImagePath(String idpath) {
        checkVaultStatus();
        vault.setString(KEY_IDPATH, idpath);
        vault.lock();
    }


    public void saveTerm(String check) {
        checkVaultStatus();
        vault.setString(TANDC, check);
        vault.lock();
    }

    public void saveCCODE(String selectedCountryNameCode) {
        checkVaultStatus();
        vault.setString(KEY_CCODE, selectedCountryNameCode);
        vault.lock();
    }
}
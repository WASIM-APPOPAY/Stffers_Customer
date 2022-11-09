package com.stuffer.stuffers.commonChat.chatUtils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.ChatMore;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatModel.UserChat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ChatHelper {
    public static boolean DISABLE_SPLASH_HANDLER = false;
    public static String CURRENT_CHAT_ID;
    private static final String USER = "USER";
    private static final String SELLER = "SELLER";
    public static final String GROUP_PREFIX = "group";
    public static final String CONTACTS_MY_CACHE = "contactsmycache";
    public static final String BROADCAST_MY_USERS = "com.stuffer.stuffers.MY_USERS";
    public static final String BROADCAST_MY_CONTACTS = "com.stuffer.stuffers.MY_CONTACTS";
    public static final String BROADCAST_USER_ME = "com.stuffer.stuffers.USER_ME";
    //public static final String BROADCAST_LOGOUT = "com.opuslabs.yoohoo.services.LOGOUT";
    private static final String TAG = "Helper";
    public static final String USER_MY_CACHE = "usersmycache";



    private SharedPreferenceHelper sharedPreferenceHelper;
    private Gson gson;
    public static boolean CHAT_CAB = false;
    public ChatHelper(Context context) {
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        gson = new Gson();
    }

    public static void closeKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getTime(Long milliseconds) {
        return new SimpleDateFormat("kk:mm", Locale.getDefault()).format(new Date(milliseconds));
    }
    public static boolean isPhonePermissionGranted(Context context) {
        boolean phonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        return phonePermission;
    }

    public static String getEndTrim(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() >= 8 ? phoneNumber.substring(phoneNumber.length() - 7) : phoneNumber;
    }

    public static CharSequence timeDiff(Long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        return DateUtils.getRelativeTimeSpanString(new Date(milliseconds).getTime(), calendar.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static String getChatChild(String userId, String myId) {
        //example: userId="9" and myId="5" -->> chat child = "5-9"
        String[] temp = {userId, myId};
        Arrays.sort(temp);
        return temp[0] + "-" + temp[1];
    }

    public static void openShareIntent(Context context, @Nullable View itemview, String shareText) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (itemview != null) {
                try {
                    Uri imageUri = getImageUri(context, itemview, "postBitmap.jpeg");
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (IOException e) {
                    intent.setType("text/plain");
                    e.printStackTrace();
                }
            } else {
                intent.setType("text/plain");
            }
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(intent, "Share Via:"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, R.string.error_detail, Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri getImageUri(Context context, View view, String fileName) throws IOException {
        Bitmap bitmap = loadBitmapFromView(view);
        File pictureFile = new File(context.getExternalCacheDir(), fileName);
        FileOutputStream fos = new FileOutputStream(pictureFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        fos.close();
        return Uri.parse("file://" + pictureFile.getAbsolutePath());
    }
    private static Bitmap loadBitmapFromView(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            v.setDrawingCacheEnabled(true);
            return v.getDrawingCache();
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static void presentToast(Context context, String string, boolean lengthLong) {
        Toast toast = Toast.makeText(context, string, lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static String getFileBase(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + context.getString(R.string.app_name);
    }

    public static String timeFormater(float time) {
        Long secs = (long) (time / 1000);
        Long mins = (long) ((time / 1000) / 60);
        Long hrs = (long) (((time / 1000) / 60) / 60); /* Convert the seconds to String * and format to ensure it has * a leading zero when required */
        secs = secs % 60;
        String seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        } /* Convert the minutes to String and format the String */
        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        } /* Convert the hours to String and format the String */
        String hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

        return hours + ":" + minutes + ":" + seconds;
//        String milliseconds = String.valueOf((long) time);
//        if (milliseconds.length() == 2) {
//            milliseconds = "0" + milliseconds;
//        }
//        if (milliseconds.length() <= 1) {
//            milliseconds = "00";
//        }
//        milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2); /* Setting the timer text to the elapsed time */
    }

    public static File getFile(Context context, Message message, boolean isMine) {
        return new File(getFileBase(context) + "/" + AttachmentTypes.getTypeName(message.getAttachmentType()) + (isMine ? "/.sent/" : "")
                , message.getAttachment().getName());
    }

    public static File getFile(Context context, Message message, String meId) {
        return new File(getFileBase(context) + "/" + AttachmentTypes.getTypeName(message.getAttachmentType()) + (message.getSenderId().equals(meId) ? "/.sent/" : "")
                , message.getAttachment().getName());
    }

    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static String getMimeType(Context context, String url) {
        String mimeType;
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static void loadUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
// create an intent builder

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
// Begin customizing
// set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        intentBuilder.addDefaultShareMenuItem();
        intentBuilder.enableUrlBarHiding();
// build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();
// launch the url
        customTabsIntent.launchUrl(context, uri);
    }

    /*public static ArrayList<ShopModel> getShopItems(Context mCtx) {
        ArrayList<ShopModel> mListShop = new ArrayList<>();
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_resturant), R.drawable.restaurant_icon2));
        mListShop.add(new ShopModel(mCtx.getString(R.string.info_item_grocery), R.mipmap.cat_grocery));
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
*/
    public void setLastRead(String chatChild, String id) {
        sharedPreferenceHelper.setStringPreference((chatChild + "_lastread"), id);
        sharedPreferenceHelper.setStringPreference("refreshunreadindicator", chatChild);
    }

    public String getChatChildToRefreshUnreadIndicatorFor() {
        return sharedPreferenceHelper.getStringPreference("refreshunreadindicator");
    }

    public void clearRefreshUnreadIndicatorFor() {
        sharedPreferenceHelper.clearPreference("refreshunreadindicator");
    }

    public void setMessages(String chatChild, ArrayList<Message> dataList) {
        sharedPreferenceHelper.setStringPreference(chatChild, gson.toJson(dataList, new TypeToken<ArrayList<Message>>() {
        }.getType()));
    }

    public void setMessagesDeleted(String chatChild, ArrayList<String> dataList) {
        sharedPreferenceHelper.setStringPreference((chatChild + "_deleted"), gson.toJson(dataList, new TypeToken<ArrayList<String>>() {
        }.getType()));
    }




    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }



    public void saveChats(String key, ArrayList<Chat> chats) {
        sharedPreferenceHelper.setStringPreference(key, gson.toJson(chats, new TypeToken<ArrayList<Chat>>() {
        }.getType()));
    }
    public User getLoggedInUser() {
        String savedUserPref = sharedPreferenceHelper.getStringPreference(USER);
//        savedUserPref = "{\"chatChild\":\"919830450542-919836683269\",\"chatImage\":\"\",\"chatName\":\"Md Wasim\",\"dateTimeStamp\":\"1654012356070\",\"lastMessage\":\"hi\",\"lastMessageId\":\"-N3PoPMaE0GN8XQWgHSU\",\"latest\":false,\"selected\":false,\"timeDiff\":\"43 minutes ago\",\"id\":\"12345670102\"}";
        if (savedUserPref != null)
            return gson.fromJson(savedUserPref, new TypeToken<User>() {
            }.getType());
        return null;
    }

    public void setLoggedInUser(User user) {
        sharedPreferenceHelper.setStringPreference(USER, gson.toJson(user, new TypeToken<User>() {
        }.getType()));
    }



    public static final String REF_USERS = "users";
    public static final String REF_CHAT = "chats";
    public static final String REF_GROUP = "groups";
    public static final String REF_INBOX = "inbox";

    public static ArrayList<UserChat> getList() {
        ArrayList<UserChat> mList = new ArrayList<>();
        mList.add(new UserChat("Don Pitts", "12:05", "2"));
        mList.add(new UserChat("Hitesh", "06:15", "3"));
        mList.add(new UserChat("Bikash", "08:25", "7"));
        mList.add(new UserChat("Venu", "09:33", "1"));
        mList.add(new UserChat("Andrew", "10:11", "4"));
        mList.add(new UserChat("Pratap", "09:54", "3"));
        mList.add(new UserChat("Surekha", "07:45", "1"));
        mList.add(new UserChat("Raju", "11:23", "2"));
        return mList;

    }

    public static ArrayList<ChatMore> getMoreItems() {
        ArrayList<ChatMore> mListMore = new ArrayList<>();
        mListMore.add(new ChatMore("Voice Input",R.drawable.icon_voice_input));
        mListMore.add(new ChatMore("Voice Call",R.drawable.icon_voice_call));
        mListMore.add(new ChatMore("Video Call",R.drawable.icon_video_call));
        mListMore.add(new ChatMore("Token",R.drawable.icon_token));

        mListMore.add(new ChatMore("Link Your Bank", R.drawable.icon_link_your_bank));
        mListMore.add(new ChatMore("ADD Fund",R.drawable.icon_add_fund));

        mListMore.add(new ChatMore("Account List",R.drawable.icon_account_list));
        mListMore.add(new ChatMore("Location",R.drawable.icon_location));
        return mListMore;

    }

    public ArrayList<Chat> getChats(String key) {
        ArrayList<Chat> toReturn;
        String savedInPrefs = sharedPreferenceHelper.getStringPreference(key);
        if (savedInPrefs != null) {
            toReturn = gson.fromJson(savedInPrefs, new TypeToken<ArrayList<Chat>>() {
            }.getType());
        } else {
            toReturn = new ArrayList<>();
        }
        return toReturn;
    }

    public String getLastRead(String chatChild) {
        return sharedPreferenceHelper.getStringPreference((chatChild + "_lastread"));
    }

    public HashMap<String, Contact> getMyContacts() {
        String savedContactsPref = sharedPreferenceHelper.getStringPreference(CONTACTS_MY_CACHE);
        if (savedContactsPref != null) {
            return gson.fromJson(savedContactsPref, new TypeToken<HashMap<String, Contact>>() {
            }.getType());
        } else {
            return new HashMap<String, Contact>();
        }
    }

    public void setMyContacts(HashMap<String, Contact> myContacts) {
        sharedPreferenceHelper.setStringPreference(CONTACTS_MY_CACHE, gson.toJson(myContacts, new TypeToken<HashMap<String, Contact>>() {
        }.getType()));
    }

    public String getMyPlayerId() {
        return null;
    }

    public void setMyPlayerId(String userId) {
        sharedPreferenceHelper.setStringPreference("myplayerid", userId);
    }

    public ArrayList<Message> getMessages(String chatChild) {
        ArrayList<Message> toReturn;
        String savedInPrefs = sharedPreferenceHelper.getStringPreference(chatChild);
        if (savedInPrefs != null) {
            toReturn = gson.fromJson(savedInPrefs, new TypeToken<ArrayList<Message>>() {
            }.getType());
        } else {
            toReturn = new ArrayList<>();
        }
        return toReturn;
    }
    public ArrayList<String> getMessagesDeleted(String chatChild) {
        ArrayList<String> toReturn;
        String savedInPrefs = sharedPreferenceHelper.getStringPreference(chatChild + "_deleted");
        if (savedInPrefs != null) {
            toReturn = gson.fromJson(savedInPrefs, new TypeToken<ArrayList<String>>() {
            }.getType());
        } else {
            toReturn = new ArrayList<>();
        }
        return toReturn;
    }

    public boolean isLoggedIn() {
        return sharedPreferenceHelper.getStringPreference(USER) != null;
    }


    public void setMyUsers(ArrayList<User> myUsers) {
        sharedPreferenceHelper.setStringPreference(USER_MY_CACHE, gson.toJson(myUsers, new TypeToken<ArrayList<User>>() {
        }.getType()));
    }

    public ArrayList<User> getMyUsers() {
        String savedUserPref = sharedPreferenceHelper.getStringPreference(USER_MY_CACHE);
        if (savedUserPref != null) {
            return gson.fromJson(savedUserPref, new TypeToken<ArrayList<User>>() {
            }.getType());
        } else {
            return new ArrayList<User>();
        }
    }
}

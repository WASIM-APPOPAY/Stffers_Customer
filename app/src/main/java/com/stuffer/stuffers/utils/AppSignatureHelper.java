package com.stuffer.stuffers.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created on : May 21, 2019
 * Author     : AndroidWave
 */
//https://prishanmaduka.medium.com/generate-hash-keys-properly-for-android-sms-retriever-api-c26b9be42ddc
    //.\a.sh --package "com.stuffer.stuffers" --keystore E:\StufferJks\Tempjks.jks
    //SHA-256 output in hex: 5e17f6fa1d1fab22040d93db9b6d3ceab02fa9e70011c58ad8af1f9330801428
    //First 8 bytes encoded by base64: Xhf2+h0fqyI
    //SMS Retriever hash code:  Xhf2+h0fqyI

public class AppSignatureHelper extends ContextWrapper {
    private static final String TAG = "AppSignatureHelper";
    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;
    public AppSignatureHelper(Context context) {
        super(context);
    }
    /**
     * Get all the app signatures for the current package
     */
    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();
        try {
            // Get all package signatures for the current package
            String packageName = getPackageName();
            PackageManager packageManager = getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;
            // For each signature create a compatible hash//3z8jiQN9JSV
                                                          //3z8jiQN9JSV
            for (Signature signature : signatures) {
                String hash = hash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to find package to obtain hash.", e);
        }
        return appCodes;
    }
    private static String hash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
            byte[] hashSignature = messageDigest.digest();
            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);
            Log.d(TAG, String.format("pkg: %s – hash: %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e);
        }
        return null;
    }
}



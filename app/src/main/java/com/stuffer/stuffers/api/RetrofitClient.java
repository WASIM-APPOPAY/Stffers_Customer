package com.stuffer.stuffers.api;


import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitNotification = null;
    private static Retrofit retrofitShopItems = null;
    private static Retrofit retrofitNode = null;
    private static Retrofit retrofitUnion = null;
    private static Retrofit retrofitUnionCard = null;
    protected static Retrofit retrofitOpen = null;
    private static Retrofit retrofitOCR = null;


    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static OkHttpClient getClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            //interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(interceptor);
            if (Build.VERSION.SDK_INT < 29) {
                okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory());
            } else {
                okHttpClientBuilder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
            }
            //okHttpClientBuilder.hostnameVerifier((hostname, session) -> true);
            okHttpClientBuilder.connectTimeout(8000, TimeUnit.MILLISECONDS);
            okHttpClientBuilder.readTimeout(25000, TimeUnit.MILLISECONDS);
            return okHttpClientBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public static OkHttpClient getClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(interceptor);
        okHttpClientBuilder.connectTimeout(8000, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(25000, TimeUnit.MILLISECONDS);
        //okHttpClientBuilder.sslSocketFactory(CustomSSLSocketFactory.create(AppoPayApplication.getInstance(), R.raw.appopay_com));
        return okHttpClientBuilder.build();
    }*/

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientUnionPayCard(String baseUrl) {
        if (retrofitUnionCard == null) {
            retrofitUnionCard = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitUnionCard;
    }

    public static Retrofit getClientOcr(String baseUrl) {
        if (retrofitOCR == null) {
            retrofitOCR = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitOCR;
    }

    public static Retrofit getClientNode(String baseUrl) {
        if (retrofitNode == null) {
            retrofitNode = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitNode;
    }

    public static Retrofit getClientShopItems(String baseUrl) {
        if (retrofitShopItems == null) {
            retrofitShopItems = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClientShopItems())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitShopItems;
    }

    public static Retrofit getClientUnionPay(String baseUrl) {
        if (retrofitUnion == null) {
            retrofitUnion = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitUnion;
    }

    public static OkHttpClient getClientShopItems() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(interceptor);
        okHttpClientBuilder.connectTimeout(3000, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(15000, TimeUnit.MILLISECONDS);
        //  okHttpClientBuilder.sslSocketFactory(CustomSSLSocketFactory.create(AppoPayApplication.getInstance(), R.raw.appopay_com));
        return okHttpClientBuilder.build();
    }

    public static Retrofit getClientForNotification(String baseUrl) {
        if (retrofitNotification == null) {
            retrofitNotification = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitNotification;
    }




}
